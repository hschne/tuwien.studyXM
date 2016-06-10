package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoBaseTest;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExerciseExamQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Class ExerciseExamDaoJdbcTest
 * Tests for the JDBC implementation in ExerciseExamDaoJdbc. In order to be isolated while testing, this
 * test class uses mocks primarily to bypass the database connection procedure.
 *
 * @author Zhang Haixiang
 */
public class ExerciseExamDaoJdbcTest extends DaoBaseTest {

    private ExerciseExamDaoJdbc examDaoJdbc;
    @Mock private ExerciseExamQuestionDao mockExerciseExamQuestionDao;

    private ExerciseExam testExerciseExam;

    @Before public void setUp() throws Exception {
        super.setUp();
        this.examDaoJdbc = new ExerciseExamDaoJdbc(this.mockConnectionH2, this.mockExerciseExamQuestionDao);

        ArrayList<Question> al = new ArrayList<Question>() {
        };
        Question question = new Question();
        question.setQuestion("TestQuestion");
        question.setQuestionId(1);
        question.setType(QuestionType.valueOf(1));
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

    // Testing create(exercise)
    //----------------------------------------------------------------------------------------------
    @Test public void test_createWith_validParameters_should_persist() throws Exception {
        when(mockConnectionH2.getConnection().prepareStatement(anyString(), anyInt()))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        ExerciseExam exerciseExam;

        exerciseExam = this.examDaoJdbc.create(this.testExerciseExam, this.testExerciseExam.getExamQuestions());
        verify(mockPreparedStatement).executeUpdate();
        verify(mockExerciseExamQuestionDao).create(this.testExerciseExam, this.testExerciseExam.getExamQuestions().get(0));
        assertSame("exercise Objects should be the same", exerciseExam, this.testExerciseExam);
    }

    @Test(expected = DaoException.class)
    public void test_createWithoutDatabaseConnection_should_fail() throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        this.examDaoJdbc.create(this.testExerciseExam, this.testExerciseExam.getExamQuestions());

        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }

    @Test(expected = DaoException.class) public void test_createWith_Null_shoud_fail()
        throws Exception {
        this.examDaoJdbc.create(null, null);
    }

    @Test(expected = DaoException.class)
    public void test_createExamWithAlreadyExistingID_should_fail() throws Exception {
        when(mockConnectionH2.getConnection().prepareStatement(anyString(), anyInt()))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        ExerciseExam temp;

        temp = this.examDaoJdbc.create(this.testExerciseExam, this.testExerciseExam.getExamQuestions());
        verify(mockPreparedStatement).executeUpdate();
        assertSame("exercise Objects should be the same", this.testExerciseExam, temp);

        when(mockPreparedStatement.executeUpdate()).thenThrow(DaoException.class);
        this.examDaoJdbc.create(temp, temp.getExamQuestions());
    }
    //----------------------------------------------------------------------------------------------

    // Testing delete(exercise)
    //----------------------------------------------------------------------------------------------
    @Test public void test_deleteWithValidExam_should_persist() throws Exception {
        ExerciseExam exerciseExam;

        exerciseExam = this.examDaoJdbc.delete(this.testExerciseExam);

        verify(this.mockPreparedStatement).executeUpdate();
        verify(this.mockExerciseExamQuestionDao).delete(this.testExerciseExam.getExamid());
        assertSame("exercise Objects should be the same", exerciseExam, testExerciseExam);
    }

    @Test(expected = DaoException.class) public void test_deleteWithNull_should_fail()
        throws Exception {
        this.examDaoJdbc.delete(null);
    }

    @Test(expected = DaoException.class) public void test_deleteWithNoExistingExam_should_fail()
        throws Exception {

        when(this.mockPreparedStatement.executeUpdate()).thenThrow(DaoException.class);
        this.examDaoJdbc.delete(this.testExerciseExam);
    }

    @Test(expected = DaoException.class)
    public void test_delteWithoutDatabaseConnection_should_fail() throws Exception {

        when(this.mockConnectionH2.getConnection()).thenThrow(DaoException.class);

        this.examDaoJdbc.delete(this.testExerciseExam);

        PowerMockito.verifyStatic();
        this.mockConnectionH2.getConnection();
    }
    //----------------------------------------------------------------------------------------------

    // Testing getExam(int)
    //----------------------------------------------------------------------------------------------
    @Test public void test_getExamWithValidExamID_should_persist() throws Exception {
        when(this.mockResultSet.next()).thenReturn(true);
        when(this.mockResultSet.getInt(anyString())).thenReturn(1).thenReturn(1);
        when(this.mockResultSet.getTimestamp(anyString())).thenReturn(new Timestamp(798));
        when(this.mockResultSet.getBoolean(anyString())).thenReturn(false);
        when(this.mockResultSet.getString(anyString())).thenReturn("Test1");


        ExerciseExam exerciseExam = this.examDaoJdbc.getExam(1);

        assertTrue("There should be exactly one element found", exerciseExam != null);
        assertEquals("Found exerciseExam should have the ID 1", exerciseExam.getExamid(), 1);
        assertTrue("Found exerciseExam should have Timestamp 798",
            exerciseExam.getCreated().equals(new Timestamp(798)));
        assertTrue("Found exerciseExam shouldn't been passsd yet", exerciseExam.getPassed() == false);
        assertEquals("Found exerciseExam should have author test1", exerciseExam.getAuthor(), "Test1");
        assertEquals("Found exerciseExam should have subject ID 1", exerciseExam.getSubjectID(), 1);

    }

    @Test public void test_getExamWithValidExamID2_should_persist() throws Exception {
        when(this.mockResultSet.next()).thenReturn(true);
        when(this.mockResultSet.getInt(anyString())).thenReturn(1).thenReturn(2).thenReturn(2)
            .thenReturn(3);
        when(this.mockResultSet.getTimestamp(anyString())).thenReturn(new Timestamp(123))
            .thenReturn(new Timestamp(80));
        when(this.mockResultSet.getBoolean(anyString())).thenReturn(false).thenReturn(true);
        when(this.mockResultSet.getString(anyString())).thenReturn("Test2").thenReturn("Test3");

        ExerciseExam exerciseExam = this.examDaoJdbc.getExam(1);
        ExerciseExam exerciseExam2 = this.examDaoJdbc.getExam(2);

        assertFalse("These two exams should not be the same", exerciseExam == exerciseExam2);
        assertFalse("These two exams should not have the same ID's",
            exerciseExam.getExamid() == exerciseExam2.getExamid());
        assertFalse("These two exams should not have the same Timestamps",
            exerciseExam.getCreated().equals(exerciseExam2.getCreated()));
        assertFalse("These two exams should not have the same passed status",
            exerciseExam.getPassed() == exerciseExam2.getPassed());
        assertNotEquals("These two exams should not have the same Author", exerciseExam.getAuthor(),
            exerciseExam2.getAuthor());

        assertFalse("These two exams should not have the same subject ID",
            exerciseExam.getSubjectID() == exerciseExam2.getSubjectID());
    }

    @Test(expected = DaoException.class)
    public void test_getExamWithoutDatabaseConnection_should_fail() throws Exception {
        when(this.mockConnectionH2.getConnection()).thenThrow(DaoException.class);
        this.examDaoJdbc.getExam(1);
        PowerMockito.verifyStatic();
        this.mockConnectionH2.getConnection();

    }

    @Test public void test_getExamWithTooBigInt_should_fail() throws Exception {
        ExerciseExam e = this.examDaoJdbc.getExam(Integer.MAX_VALUE);
        assertTrue("There should be no Subject with ID 2147483647 (= max int value)", e == null);
    }

    //----------------------------------------------------------------------------------------------

    //Testing getExams()
    //----------------------------------------------------------------------------------------------
    @Test public void test_getExamsWith10ElementsInTheDatabase_should_persist() throws Exception {
        when(mockResultSet.next()).thenAnswer(new Answer<Boolean>() {
            private int count = 0;

            @Override public Boolean answer(InvocationOnMock invocationOnMock) throws Throwable {
                if (count < 10) {
                    count++;
                    return true;
                } else
                    return false;
            }
        });

        List<ExerciseExam> exerciseExamList = this.examDaoJdbc.getExams();
        assertSame("The List should contain 10 Exams", exerciseExamList.size(), 10);
    }

    @Test public void test_getExamWith2ElementsInTheDatabase_should_persist() throws Exception {
        when(this.mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(this.mockResultSet.getInt(anyString())).thenReturn(1).thenReturn(1).thenReturn(2)
            .thenReturn(2);
        when(this.mockResultSet.getTimestamp(anyString())).thenReturn(new Timestamp(10))
            .thenReturn(new Timestamp(20));
        when(this.mockResultSet.getBoolean(anyString())).thenReturn(true).thenReturn(false);
        when(this.mockResultSet.getString(anyString())).thenReturn("Author1").thenReturn("Author2");

        List<ExerciseExam> exerciseExamList = this.examDaoJdbc.getExams();

        assertTrue("List contains 2 Exams", exerciseExamList.size() == 2);
        ExerciseExam exerciseExam1 = exerciseExamList.get(0);
        ExerciseExam exerciseExam2 = exerciseExamList.get(1);


        assertFalse("Both elements should diffeer", exerciseExam1 == exerciseExam2);


        assertEquals("Exam1 should have the ID 1", exerciseExam1.getExamid(), 1);
        assertTrue("Exam1 should have Timestamp 10", exerciseExam1.getCreated().equals(new Timestamp(10)));
        assertTrue("Exam1 should have been passsd already", exerciseExam1.getPassed() == true);
        assertEquals("Exam1 should have author Author1", exerciseExam1.getAuthor(), "Author1");
        assertEquals("Exam1 should have subject ID 2", exerciseExam1.getSubjectID(), 2);

        assertEquals("Exam2 should have the ID 2", exerciseExam2.getExamid(), 2);
        assertTrue("Exam2 should have Timestamp 20", exerciseExam2.getCreated().equals(new Timestamp(20)));
        assertTrue("Exam2 shouldn't been passsd yet", exerciseExam2.getPassed() == false);
        assertEquals("Exam2 should have author Author2", exerciseExam2.getAuthor(), "Author2");
        assertEquals("Exam2 should have subject ID 2", exerciseExam2.getSubjectID(), 2);
    }

    @Test(expected = DaoException.class)
    public void test_getExamsWithoutDatabaseConnection_should_fail() throws Exception {
        when(this.mockConnectionH2.getConnection()).thenThrow(DaoException.class);
        this.examDaoJdbc.getExams();
        PowerMockito.verifyStatic();
        this.mockConnectionH2.getConnection();
    }

    @Test public void test_getExamWithoutAnyElementsInTheDatabase_should_fail() throws Exception {
        when(this.mockResultSet.next()).thenReturn(false);

        List<ExerciseExam> exerciseExamList = this.examDaoJdbc.getExams();
        assertTrue("List should be emtpy", exerciseExamList.isEmpty());
    }

    //----------------------------------------------------------------------------------------------

    @After public void tearDown() throws Exception {
        //nothing to tear down
    }

}
