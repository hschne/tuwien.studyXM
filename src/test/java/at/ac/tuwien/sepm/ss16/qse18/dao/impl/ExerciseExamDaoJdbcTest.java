package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoBaseTest;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExerciseExamQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.*;
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
    private Subject s;
    private Exam exam;

    @Before public void setUp() throws Exception {
        super.setUp();
        this.examDaoJdbc =
            new ExerciseExamDaoJdbc(this.mockConnectionH2, this.mockExerciseExamQuestionDao);

        ArrayList<Question> al = new ArrayList<Question>() {
        };
        Question question = new Question();
        question.setQuestion("TestQuestion");
        question.setQuestionId(1);
        question.setType(QuestionType.valueOf(1));
        al.add(question);


        testExerciseExam = new ExerciseExam();
        testExerciseExam.setExamid(1);
        testExerciseExam.setExam(1);
        testExerciseExam.setCreated(new Timestamp(798));
        testExerciseExam.setPassed(false);
        testExerciseExam.setAuthor("Test1");
        testExerciseExam.setSubjectID(1);
        testExerciseExam.setExamQuestions(al);
        testExerciseExam.setExamTime(1);

        s = new Subject();
        s.setSubjectId(1);
        s.setName("Subject1");
        s.setEcts(4.3f);
        s.setSemester("SS16");
        s.setAuthor("testAuthor");

        exam = new Exam();
        exam.setCreated(new Timestamp(1111));
        exam.setExamid(1);
        exam.setDueDate(new Timestamp(2222));
        exam.setName("TestExam");
        exam.setSubject(1);
    }

    // Testing create(exercise)
    //----------------------------------------------------------------------------------------------
    @Test public void test_createWith_validParameters_should_persist() throws Exception {
        when(mockConnectionH2.getConnection().prepareStatement(anyString(), anyInt()))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        ExerciseExam exerciseExam;

        exerciseExam = this.examDaoJdbc
            .create(this.testExerciseExam, this.testExerciseExam.getExamQuestions());
        verify(mockPreparedStatement).executeUpdate();
        verify(mockExerciseExamQuestionDao)
            .create(this.testExerciseExam, this.testExerciseExam.getExamQuestions().get(0));
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

        temp = this.examDaoJdbc
            .create(this.testExerciseExam, this.testExerciseExam.getExamQuestions());
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

        when(this.mockConnectionH2.getConnection()).thenThrow(SQLException.class);

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
        assertTrue("Found exerciseExam shouldn't been passsd yet",
            exerciseExam.getPassed() == false);
        assertEquals("Found exerciseExam should have author test1", exerciseExam.getAuthor(),
            "Test1");
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
        when(this.mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        this.examDaoJdbc.getExam(1);
        PowerMockito.verifyStatic();
        this.mockConnectionH2.getConnection();

    }

    @Test public void test_getExamWithTooBigInt_should_fail() throws Exception {
        ExerciseExam e = this.examDaoJdbc.getExam(Integer.MAX_VALUE);
        assertTrue("There should be no Subject with ID 2147483647 (= max int value)", e == null);
    }

    @Test(expected = DaoException.class)
    public void test_getExamWithInvalidExerciseExamID_should_fail() throws Exception {
        this.examDaoJdbc.getExam(-1);
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
        assertTrue("Exam1 should have Timestamp 10",
            exerciseExam1.getCreated().equals(new Timestamp(10)));
        assertTrue("Exam1 should have been passsd already", exerciseExam1.getPassed() == true);
        assertEquals("Exam1 should have author Author1", exerciseExam1.getAuthor(), "Author1");
        assertEquals("Exam1 should have subject ID 2", exerciseExam1.getSubjectID(), 2);

        assertEquals("Exam2 should have the ID 2", exerciseExam2.getExamid(), 2);
        assertTrue("Exam2 should have Timestamp 20",
            exerciseExam2.getCreated().equals(new Timestamp(20)));
        assertTrue("Exam2 shouldn't been passsd yet", exerciseExam2.getPassed() == false);
        assertEquals("Exam2 should have author Author2", exerciseExam2.getAuthor(), "Author2");
        assertEquals("Exam2 should have subject ID 2", exerciseExam2.getSubjectID(), 2);
    }

    @Test(expected = DaoException.class)
    public void test_getExamsWithoutDatabaseConnection_should_fail() throws Exception {
        when(this.mockConnectionH2.getConnection()).thenThrow(SQLException.class);
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

    //Testing update(int, long)
    //----------------------------------------------------------------------------------------------

    @Test public void test_updateWithValidParametersShould_persist() throws Exception {
        this.examDaoJdbc
            .update(this.testExerciseExam.getExamid(), this.testExerciseExam.getExamTime());
        verify(this.mockPreparedStatement).executeUpdate();
    }

    @Test(expected = DaoException.class) public void test_updateWithInvalidExamID_should_fail()
        throws Exception {
        this.examDaoJdbc.update(0, 3);
    }

    @Test(expected = DaoException.class) public void test_updateWithInvalidExamTime_should_fail()
        throws Exception {
        this.examDaoJdbc.update(1, -3);
    }

    @Test(expected = DaoException.class)
    public void test_updateWithoutDatabaseConnection_should_fail() throws Exception {
        when(this.mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        this.examDaoJdbc
            .update(this.testExerciseExam.getExamid(), this.testExerciseExam.getExamTime());
        PowerMockito.verifyStatic();
        this.mockConnectionH2.getConnection();
    }

    //----------------------------------------------------------------------------------------------

    //Testing getAllExamsOfSubject(Subject)
    //----------------------------------------------------------------------------------------------

    @Test public void test_getAllExamsOfSubjectWith3ExerciseExams_should_persist()
        throws Exception {
        s.setSubjectId(1);
        s.setName("Subject1");
        List<ExerciseExam> exerciseExamList = new ArrayList<>();

        ExerciseExam e2 = dummyExerciseExam(2, "testAuthor", 1);
        ExerciseExam e3 = dummyExerciseExam(3, "testAuthor", 1);

        when(this.mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true)
            .thenReturn(false);

        when(this.mockResultSet.getInt("exam")).thenReturn(1).thenReturn(1).thenReturn(1);
        when(this.mockResultSet.getInt("examid")).thenReturn(testExerciseExam.getExamid())
            .thenReturn(e2.getExamid()).thenReturn(e3.getExamid());
        when(this.mockResultSet.getTimestamp(anyString())).thenReturn(testExerciseExam.getCreated())
            .thenReturn(e2.getCreated()).thenReturn(e3.getCreated());
        when(this.mockResultSet.getString(anyString())).thenReturn(testExerciseExam.getAuthor())
            .thenReturn(e2.getAuthor()).thenReturn(e3.getAuthor());
        when(this.mockResultSet.getBoolean(anyString())).thenReturn(testExerciseExam.getPassed())
            .thenReturn(e2.getPassed()).thenReturn(e3.getPassed());
        when(this.mockResultSet.getInt("subject")).thenReturn(testExerciseExam.getSubjectID())
            .thenReturn(e2.getSubjectID()).thenReturn(e3.getSubjectID());
        when(this.mockResultSet.getLong(anyString())).thenReturn(testExerciseExam.getExamTime())
            .thenReturn(e2.getExamTime()).thenReturn(e3.getExamTime());


        exerciseExamList = this.examDaoJdbc.getAllExamsOfSubject(this.s);

        exerciseExamList.get(0).setExamQuestions(testExerciseExam.getExamQuestions());
        exerciseExamList.get(1).setExamQuestions(testExerciseExam.getExamQuestions());
        exerciseExamList.get(2).setExamQuestions(testExerciseExam.getExamQuestions());

        System.out.println(exerciseExamList.get(0).equals(this.testExerciseExam));

        assertTrue("List should contain 3 exercise exams", exerciseExamList.size() == 3);
        assertTrue("First Elements ID should be the 1", this.testExerciseExam.getExamid() == 1);
        assertTrue("Second Elements ID should be the 2", e2.getExamid() == 2);
        assertTrue("Third Elements ID should be the 3", e3.getExamid() == 3);
    }

    @Test(expected = DaoException.class)
    public void test_getAllExamsOfSubjectWithInvalidSubjectName_should_fail() throws Exception {
        s.setSubjectId(1);
        s.setName("       ");

        this.examDaoJdbc.getAllExamsOfSubject(s);
    }

    @Test(expected = DaoException.class)
    public void test_getAllExamsOfSubjectWithInvalidSubjectID_should_fail() throws Exception {
        s.setSubjectId(-1);
        this.examDaoJdbc.getAllExamsOfSubject(this.s);
    }

    @Test(expected = DaoException.class)
    public void test_getAllExamsOfSubjectWithoutDatabaseConnection_should_fail() throws Exception {
        s.setSubjectId(1);
        s.setName("Subject1");
        when(this.mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        this.examDaoJdbc.getAllExamsOfSubject(s);
        PowerMockito.verifyStatic();
        this.mockConnectionH2.getConnection();
    }
    //----------------------------------------------------------------------------------------------

    //Testing getExerciseExamsFrom(Exam)
    //----------------------------------------------------------------------------------------------

    @Test public void getExerciseExamsFromWith3ExerciseExams_should_persist() throws Exception {
        exam.setName("testExam");
        List<ExerciseExam> exerciseExamList = new ArrayList<>();

        ExerciseExam e2 = dummyExerciseExam(2, "testAuthor", 1);
        ExerciseExam e3 = dummyExerciseExam(3, "testAuthor", 1);

        when(this.mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true)
            .thenReturn(false);

        when(this.mockResultSet.getInt("exam")).thenReturn(1).thenReturn(1).thenReturn(1);
        when(this.mockResultSet.getInt("examid")).thenReturn(testExerciseExam.getExamid())
            .thenReturn(e2.getExamid()).thenReturn(e3.getExamid());
        when(this.mockResultSet.getTimestamp(anyString())).thenReturn(testExerciseExam.getCreated())
            .thenReturn(e2.getCreated()).thenReturn(e3.getCreated());
        when(this.mockResultSet.getString(anyString())).thenReturn(testExerciseExam.getAuthor())
            .thenReturn(e2.getAuthor()).thenReturn(e3.getAuthor());
        when(this.mockResultSet.getBoolean(anyString())).thenReturn(testExerciseExam.getPassed())
            .thenReturn(e2.getPassed()).thenReturn(e3.getPassed());
        when(this.mockResultSet.getInt("subject")).thenReturn(testExerciseExam.getSubjectID())
            .thenReturn(e2.getSubjectID()).thenReturn(e3.getSubjectID());
        when(this.mockResultSet.getLong(anyString())).thenReturn(testExerciseExam.getExamTime())
            .thenReturn(e2.getExamTime()).thenReturn(e3.getExamTime());


        exerciseExamList = this.examDaoJdbc.getExerciseExamsFrom(this.exam);

        exerciseExamList.get(0).setExamQuestions(testExerciseExam.getExamQuestions());
        exerciseExamList.get(1).setExamQuestions(testExerciseExam.getExamQuestions());
        exerciseExamList.get(2).setExamQuestions(testExerciseExam.getExamQuestions());


        assertTrue("List should contain 3 exercise exams", exerciseExamList.size() == 3);
        assertTrue("First Elements ID should be the 1", this.testExerciseExam.getExamid() == 1);
        assertTrue("Second Elements ID should be the 2", e2.getExamid() == 2);
        assertTrue("Third Elements ID should be the 3", e3.getExamid() == 3);
    }

    @Test(expected = DaoException.class)
    public void test_getAllExerciseExamsFromtWithInvalidExamName_should_fail() throws Exception {
        exam.setName("     ");
        this.examDaoJdbc.getExerciseExamsFrom(this.exam);

    }

    @Test(expected = DaoException.class)
    public void test_getAllExerciseExamsFromWithoutDatabaseConnection_should_fail()
        throws Exception {
        exam.setName("testExam");
        when(this.mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        this.examDaoJdbc.getExerciseExamsFrom(this.exam);
        PowerMockito.verifyStatic();
        this.mockConnectionH2.getConnection();
    }


    //----------------------------------------------------------------------------------------------

    @After public void tearDown() throws Exception {
        //nothing to tear down
    }

    private ExerciseExam dummyExerciseExam(int examID, String author, int subjectID) {
        ExerciseExam exerciseExam = new ExerciseExam();
        exerciseExam.setExamid(examID);
        exerciseExam.setCreated(new Timestamp(11111));
        exerciseExam.setPassed(false);
        exerciseExam.setAuthor(author);
        exerciseExam.setSubjectID(subjectID);
        exerciseExam.setExam(1);
        exerciseExam.setExamQuestions(this.testExerciseExam.getExamQuestions());
        exerciseExam.setExamTime(30);

        return exerciseExam;
    }


}
