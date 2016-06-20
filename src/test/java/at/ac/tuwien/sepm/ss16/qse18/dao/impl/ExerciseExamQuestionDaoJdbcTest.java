package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoBaseTest;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExerciseExamQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.domain.Tag;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Class ExerciseExamQuestionDaoJdbcTest
 * Tests for the JDBC implementation in ExerciseExamQuestionDaoJdbc. In order to be isolated while testing, this
 * test class uses mocks primarily to bypass the database connection procedure.
 *
 * @author Zhang Haixiang
 */
public class ExerciseExamQuestionDaoJdbcTest extends DaoBaseTest {
    private ExerciseExamQuestionDaoJdbc examQuestionDaoJdbc;
    @Mock private ExerciseExamQuestionDao mockExerciseExamQuestionDao;

    private ExerciseExam testExerciseExam;

    @Before public void setUp() throws Exception {
        super.setUp();
        this.examQuestionDaoJdbc = new ExerciseExamQuestionDaoJdbc(mockConnectionH2);

        ArrayList<Question> al = new ArrayList<Question>() {
        };
        Question question = new Question();
        question.setQuestion("TestQuestion");
        question.setQuestionId(1);
        question.setType(QuestionType.valueOf(1));
        question.setQuestionTime(1);
        al.add(question);


        testExerciseExam = new ExerciseExam();
        testExerciseExam.setExamid(1);
        testExerciseExam.setCreated(new Timestamp(798));
        testExerciseExam.setPassed(false);
        testExerciseExam.setAuthor("Test1");
        testExerciseExam.setSubjectID(1);
        testExerciseExam.setExamQuestions(al);
        testExerciseExam.setExamTime(1);
    }

    //Testing create(exercise, Question)
    //----------------------------------------------------------------------------------------------
    @Test public void test_createWith_validParameters_should_persist() throws Exception {
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        this.examQuestionDaoJdbc
            .create(this.testExerciseExam, this.testExerciseExam.getExamQuestions().get(0));
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test(expected = DaoException.class)
    public void test_createWithoutDatabaseConnection_should_fail() throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        this.examQuestionDaoJdbc
            .create(this.testExerciseExam, this.testExerciseExam.getExamQuestions().get(0));

        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }

    @Test(expected = DaoException.class) public void test_createWith_Null_shoud_fail()
        throws Exception {
        this.examQuestionDaoJdbc.create(null, null);
    }

    @Test(expected = DaoException.class)
    public void test_createExamWithAlreadyExistingID_should_fail() throws Exception {

        this.examQuestionDaoJdbc
            .create(this.testExerciseExam, this.testExerciseExam.getExamQuestions().get(0));
        verify(mockPreparedStatement).executeUpdate();

        when(mockPreparedStatement.executeUpdate()).thenThrow(DaoException.class);
        this.examQuestionDaoJdbc
            .create(this.testExerciseExam, this.testExerciseExam.getExamQuestions().get(0));
    }

    @Test(expected = DaoException.class)
    public void test_createExamWithInvalidQuestion_should_fail() throws Exception {
        Question q1 = new Question(2, "      ", QuestionType.valueOf(1), 3, Tag.HARD);
        this.examQuestionDaoJdbc.create(this.testExerciseExam, q1);
        verify(mockPreparedStatement).executeUpdate();
    }
    //----------------------------------------------------------------------------------------------

    //Testing delete(int)
    //----------------------------------------------------------------------------------------------
    @Test public void test_deleteWithValidExam_should_persist() throws Exception {
        this.examQuestionDaoJdbc.delete(this.testExerciseExam.getExamid());
        verify(this.mockPreparedStatement).executeUpdate();
    }

    @Test(expected = DaoException.class) public void test_deleteWithInvalidExamID_should_fail()
        throws Exception {
        this.examQuestionDaoJdbc.delete(-1);
    }

    @Test(expected = DaoException.class) public void test_deleteWithNoExistingExam_should_fail()
        throws Exception {

        when(this.mockPreparedStatement.executeUpdate()).thenThrow(DaoException.class);
        this.examQuestionDaoJdbc.delete(this.testExerciseExam.getExamid());
    }

    @Test(expected = DaoException.class)
    public void test_delteWithoutDatabaseConnection_should_fail() throws Exception {

        when(this.mockConnectionH2.getConnection()).thenThrow(SQLException.class);

        this.examQuestionDaoJdbc.delete(this.testExerciseExam.getExamid());

        PowerMockito.verifyStatic();
        this.mockConnectionH2.getConnection();
    }

    //Testing getAllQuestionBooleans(List<Integer>)
    //----------------------------------------------------------------------------------------------
    @Test public void test_getAllQuestionBooleansWith3ElementsInDatabase_should_persist()
        throws Exception {
        List<Integer> questionIDList = new ArrayList<>();
        Map<Integer, Boolean> questionBooleans = new HashMap<>();
        questionIDList.add(1);
        questionIDList.add(2);
        questionIDList.add(3);

        when(this.mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true)
            .thenReturn(false);
        when(this.mockResultSet.getInt(anyString())).thenReturn(1).thenReturn(2).thenReturn(3);
        when(this.mockResultSet.getBoolean(anyString())).thenReturn(true).thenReturn(true)
            .thenReturn(false);

        questionBooleans = this.examQuestionDaoJdbc.getAllQuestionBooleans(questionIDList);

        assertTrue("One Element should have ID 1", questionBooleans.containsKey(1));
        assertTrue("One Element should have ID 2", questionBooleans.containsKey(2));
        assertTrue("One Element should have ID 3", questionBooleans.containsKey(3));
        assertTrue("Value of Element with ID 1 should be true", questionBooleans.get(1));
        assertTrue("Value of Element with ID 2 should be true", questionBooleans.get(2));
        assertTrue("Value of Element with ID 3 should be false", !questionBooleans.get(3));

    }

    @Test(expected = DaoException.class)
    public void test_getAllQuestionBooleansWithNegativeQuestionID_should_fail() throws Exception {
        List<Integer> questionIDList = new ArrayList<>();
        questionIDList.add(-1);

        this.examQuestionDaoJdbc.getAllQuestionBooleans(questionIDList);
    }

    @Test(expected = DaoException.class)
    public void test_getAllQuestionBooleansWithoutDatabaseConnection_should_fail()
        throws Exception {
        List<Integer> questionIDList = new ArrayList<>();
        questionIDList.add(1);
        when(this.mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        this.examQuestionDaoJdbc.getAllQuestionBooleans(questionIDList);
        PowerMockito.verifyStatic();
        this.mockConnectionH2.getConnection();
    }
    //----------------------------------------------------------------------------------------------

    //Testing getAllQuestionsOfExam(int)
    //----------------------------------------------------------------------------------------------
    @Test public void test_getAllQuestionsOfExamWith3QuestionsInExam_should_persist()
        throws Exception {
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true)
            .thenReturn(false);
        when(mockResultSet.getInt(anyString())).thenReturn(10).thenReturn(20).thenReturn(30);

        List<Integer> questionIDList = new ArrayList<>();

        questionIDList = this.examQuestionDaoJdbc.getAllQuestionsOfExam(1);

        assertTrue("Elements should be diffrent",
            !questionIDList.get(0).equals(questionIDList.get(1)));
        assertTrue("Elements should be diffrent",
            !questionIDList.get(0).equals(questionIDList.get(2)));
        assertTrue("Elements should be diffrent",
            !questionIDList.get(1).equals(questionIDList.get(2)));
        assertTrue("First Element should be 10", questionIDList.get(0) == 10);
        assertTrue("Second Element should be 20", questionIDList.get(1) == 20);
        assertTrue("Third Element should be 30", questionIDList.get(2) == 30);
    }

    @Test(expected = DaoException.class)
    public void test_getAllQuestionsOfExamWithInvalidExamID_should_fail() throws Exception {
        this.examQuestionDaoJdbc.getAllQuestionsOfExam(-10);
    }

    @Test(expected = DaoException.class)
    public void test_getAllQuestionsOfExamWithoutDatabaseConnection_should_fail() throws Exception {
        when(this.mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        this.examQuestionDaoJdbc.getAllQuestionsOfExam(1);
        PowerMockito.verifyStatic();
        this.mockConnectionH2.getConnection();
    }
    //----------------------------------------------------------------------------------------------

    //Testing getQuestionBooleansOfExam(int, List<Integer>)
    //----------------------------------------------------------------------------------------------
    @Test public void test_geQuestionBooleansOfExamWith3ElementsInDatabase_should_persist()
        throws Exception {
        List<Integer> questionList = new ArrayList<>();
        Map<Integer, Boolean> questionBooleans = new HashMap<>();

        questionList.add(1);
        questionList.add(2);
        questionList.add(3);

        when(this.mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true)
            .thenReturn(false);
        when(this.mockResultSet.getInt(anyString())).thenReturn(1).thenReturn(2).thenReturn(3);
        when(this.mockResultSet.getBoolean(anyString())).thenReturn(true).thenReturn(true)
            .thenReturn(false);

        questionBooleans = this.examQuestionDaoJdbc
            .getQuestionBooleansOfExam(this.testExerciseExam.getExamid(), questionList);

        assertTrue("One Element should have ID 1", questionBooleans.containsKey(1));
        assertTrue("One Element should have ID 2", questionBooleans.containsKey(2));
        assertTrue("One Element should have ID 3", questionBooleans.containsKey(3));
        assertTrue("Value of Element with ID 1 should be true", questionBooleans.get(1));
        assertTrue("Value of Element with ID 2 should be true", questionBooleans.get(2));
        assertTrue("Value of Element with ID 3 should be false", !questionBooleans.get(3));

    }

    @Test(expected = DaoException.class)
    public void test_getQuestionBooleansOfExamWithInvalidExamID_should_fail() throws Exception {
        List<Integer> questionList = new ArrayList<>();
        questionList.add(1);
        this.examQuestionDaoJdbc.getQuestionBooleansOfExam(0, questionList);
    }

    @Test(expected = DaoException.class)
    public void test_getQuestionBooleansOfExamWithInvalidQuestionID_should_fail() throws Exception {
        List<Integer> questionList = new ArrayList<>();
        questionList.add(-1);
        this.examQuestionDaoJdbc
            .getQuestionBooleansOfExam(this.testExerciseExam.getExamid(), questionList);
    }

    @Test(expected = DaoException.class)
    public void test_getQuestionBooleansOfExamWithoutDatabaseConnection_should_fail()
        throws Exception {
        List<Integer> questionList = new ArrayList<>();
        questionList.add(1);
        when(this.mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        this.examQuestionDaoJdbc
            .getQuestionBooleansOfExam(this.testExerciseExam.getExamid(), questionList);
        PowerMockito.verifyStatic();
        this.mockConnectionH2.getConnection();
    }

    //----------------------------------------------------------------------------------------------

    //Testing update(int, int, boolean, boolean)
    //----------------------------------------------------------------------------------------------

    @Test public void test_updateWithValidParameters_should_persist() throws Exception {
        this.examQuestionDaoJdbc.update(this.testExerciseExam.getExamid(), 1, false, true);
        verify(this.mockPreparedStatement).executeUpdate();
    }

    @Test(expected = DaoException.class)
    public void test_updateWithInvalidExerciseExamID_should_fail() throws Exception {
        this.examQuestionDaoJdbc.update(0, 1, false, true);
    }

    @Test(expected = DaoException.class)
    public void test_updateWithoutDatabaseConnection_should_fail() throws Exception {
        when(this.mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        this.examQuestionDaoJdbc.update(this.testExerciseExam.getExamid(), 1, false, false);
        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }

    @Test(expected = DaoException.class) public void test_updateWithInvalidQuestionID_should_fail()
        throws Exception {
        this.examQuestionDaoJdbc.update(1, -1, false, true);
    }
    //----------------------------------------------------------------------------------------------

    //Testing getAnsweredQuestionsPerExam(int)
    //----------------------------------------------------------------------------------------------

    @Test public void test_getAnsweredQuestionsPerExamWith3Questions_should_persist()
        throws Exception {
        List<Integer> questionIDList = new ArrayList<>();

        when(this.mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true)
            .thenReturn(false);
        when(this.mockResultSet.getInt(anyString())).thenReturn(1).thenReturn(2).thenReturn(3);

        questionIDList =
            this.examQuestionDaoJdbc.getAnsweredQuestionsPerExam(this.testExerciseExam.getExamid());

        assertTrue("List should contain 3 Question ID's", questionIDList.size() == 3);
        assertTrue("First Question ID should be 1", questionIDList.get(0) == 1);
        assertTrue("Second Question ID should be 2", questionIDList.get(1) == 2);
        assertTrue("Third Question ID should be 3", questionIDList.get(2) == 3);
    }

    @Test(expected = DaoException.class)
    public void test_getAnsweredQuestionsPerExamWithInvalidExerciseExamID_should_fail()
        throws Exception {
        this.examQuestionDaoJdbc.getAnsweredQuestionsPerExam(0);
    }

    @Test(expected = DaoException.class)
    public void test_getAnsweredQuestionsPerExamWithoutDatabaseConnection_should_fail()
        throws Exception {
        when(this.mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        this.examQuestionDaoJdbc.getAnsweredQuestionsPerExam(1);
        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }

    //Testing getNotAnsweredQuestionsPerExam(int)
    //----------------------------------------------------------------------------------------------
    @Test public void test_getNotAnsweredQuestionsPerExamWith3Questions_should_persist()
        throws Exception {
        List<Integer> questionIDList = new ArrayList<>();

        when(this.mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true)
            .thenReturn(false);
        when(this.mockResultSet.getInt(anyString())).thenReturn(1).thenReturn(2).thenReturn(3);

        questionIDList = this.examQuestionDaoJdbc
            .getNotAnsweredQuestionsPerExam(this.testExerciseExam.getExamid());

        assertTrue("List should contain 3 Question ID's", questionIDList.size() == 3);
        assertTrue("First Question ID should be 1", questionIDList.get(0) == 1);
        assertTrue("Second Question ID should be 2", questionIDList.get(1) == 2);
        assertTrue("Third Question ID should be 3", questionIDList.get(2) == 3);
    }

    @Test(expected = DaoException.class)
    public void test_getNotAnsweredQuestionsPerExamWithInvalidExerciseExamID_should_fail()
        throws Exception {
        this.examQuestionDaoJdbc.getNotAnsweredQuestionsPerExam(0);
    }

    @Test(expected = DaoException.class)
    public void test_getNotAnsweredQuestionsPerExamWithoutDatabaseConnection_should_fail()
        throws Exception {
        when(this.mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        this.examQuestionDaoJdbc.getNotAnsweredQuestionsPerExam(1);
        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }

    //----------------------------------------------------------------------------------------------

    @After public void tearDown() throws Exception {

    }

}
