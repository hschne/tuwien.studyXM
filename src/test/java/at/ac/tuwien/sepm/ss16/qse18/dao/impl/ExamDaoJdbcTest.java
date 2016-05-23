package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoBaseTest;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExamQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
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
 * Class ExamDaoJdbcTest
 * Tests for the JDBC implementation in ExamDaoJdbc. In order to be isolated while testing, this
 * test class uses mocks primarily to bypass the database connection procedure.
 *
 * @author Zhang Haixiang
 */
public class ExamDaoJdbcTest extends DaoBaseTest {

    private ExamDaoJdbc examDaoJdbc;
    @Mock private ExamQuestionDao mockExamQuestionDao;

    private Exam testExam;

    @Before public void setUp() throws Exception {
        super.setUp();
        this.examDaoJdbc = new ExamDaoJdbc(this.mockConnectionH2, this.mockExamQuestionDao);

        ArrayList<Question> al = new ArrayList<Question>() {
        };
        Question question = new Question();
        question.setQuestion("TestQuestion");
        question.setQuestionId(1);
        question.setType(QuestionType.valueOf(1));
        al.add(question);


        testExam = new Exam();
        testExam.setExamid(1);
        testExam.setCreated(new Timestamp(798));
        testExam.setPassed(false);
        testExam.setAuthor("Test1");
        testExam.setSubjectID(1);
        testExam.setExamQuestions(al);
    }

    // Testing create(Exam)
    //----------------------------------------------------------------------------------------------
    @Test public void test_createWith_validParameters_should_persist() throws Exception {
        when(mockConnectionH2.getConnection().prepareStatement(anyString(), anyInt()))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        Exam exam;

        exam = this.examDaoJdbc.create(this.testExam, this.testExam.getExamQuestions());
        verify(mockPreparedStatement).executeUpdate();
        verify(mockExamQuestionDao).create(this.testExam, this.testExam.getExamQuestions().get(0));
        assertSame("Exam Objects should be the same", exam, this.testExam);
    }

    @Test(expected = DaoException.class)
    public void test_createWithoutDatabaseConnection_should_fail() throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        this.examDaoJdbc.create(this.testExam, this.testExam.getExamQuestions());

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
        Exam temp;

        temp = this.examDaoJdbc.create(this.testExam, this.testExam.getExamQuestions());
        verify(mockPreparedStatement).executeUpdate();
        assertSame("Exam Objects should be the same", this.testExam, temp);

        when(mockPreparedStatement.executeUpdate()).thenThrow(DaoException.class);
        this.examDaoJdbc.create(temp, temp.getExamQuestions());
    }
    //----------------------------------------------------------------------------------------------

    // Testing delete(Exam)
    //----------------------------------------------------------------------------------------------
    @Test public void test_deleteWithValidExam_should_persist() throws Exception {
        Exam exam;

        exam = this.examDaoJdbc.delete(this.testExam);

        verify(this.mockPreparedStatement).executeUpdate();
        verify(this.mockExamQuestionDao).delete(this.testExam.getExamid());
        assertSame("Exam Objects should be the same", exam, testExam);
    }

    @Test(expected = DaoException.class) public void test_deleteWithNull_should_fail()
        throws Exception {
        this.examDaoJdbc.delete(null);
    }

    @Test(expected = DaoException.class) public void test_deleteWithNoExistingExam_should_fail()
        throws Exception {

        when(this.mockPreparedStatement.executeUpdate()).thenThrow(DaoException.class);
        this.examDaoJdbc.delete(this.testExam);
    }

    @Test(expected = DaoException.class)
    public void test_delteWithoutDatabaseConnection_should_fail() throws Exception {

        when(this.mockConnectionH2.getConnection()).thenThrow(DaoException.class);

        this.examDaoJdbc.delete(this.testExam);

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


        Exam exam = this.examDaoJdbc.getExam(1);

        assertTrue("There should be exactly one element found", exam != null);
        assertEquals("Found exam should have the ID 1", exam.getExamid(), 1);
        assertTrue("Found exam should have Timestamp 798",
            exam.getCreated().equals(new Timestamp(798)));
        assertTrue("Found exam shouldn't been passsd yet", exam.getPassed() == false);
        assertEquals("Found exam should have author test1", exam.getAuthor(), "Test1");
        assertEquals("Found exam should have subject ID 1", exam.getSubjectID(), 1);

    }

    @Test public void test_getExamWithValidExamID2_should_persist() throws Exception {
        when(this.mockResultSet.next()).thenReturn(true);
        when(this.mockResultSet.getInt(anyString())).thenReturn(1).thenReturn(2).thenReturn(2)
            .thenReturn(3);
        when(this.mockResultSet.getTimestamp(anyString())).thenReturn(new Timestamp(123))
            .thenReturn(new Timestamp(80));
        when(this.mockResultSet.getBoolean(anyString())).thenReturn(false).thenReturn(true);
        when(this.mockResultSet.getString(anyString())).thenReturn("Test2").thenReturn("Test3");

        Exam exam = this.examDaoJdbc.getExam(1);
        Exam exam2 = this.examDaoJdbc.getExam(2);

        assertFalse("These two exams should not be the same", exam == exam2);
        assertFalse("These two exams should not have the same ID's",
            exam.getExamid() == exam2.getExamid());
        assertFalse("These two exams should not have the same Timestamps",
            exam.getCreated().equals(exam2.getCreated()));
        assertFalse("These two exams should not have the same passed status",
            exam.getPassed() == exam2.getPassed());
        assertNotEquals("These two exams should not have the same Author", exam.getAuthor(),
            exam2.getAuthor());

        assertFalse("These two exams should not have the same subject ID",
            exam.getSubjectID() == exam2.getSubjectID());
    }

    @Test(expected = DaoException.class)
    public void test_getExamWithoutDatabaseConnection_should_fail() throws Exception {
        when(this.mockConnectionH2.getConnection()).thenThrow(DaoException.class);
        this.examDaoJdbc.getExam(1);
        PowerMockito.verifyStatic();
        this.mockConnectionH2.getConnection();

    }

    @Test public void test_getExamWithTooBigInt_should_fail() throws Exception {
        Exam e = this.examDaoJdbc.getExam(Integer.MAX_VALUE);
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

        List<Exam> examList = this.examDaoJdbc.getExams();
        assertSame("The List should contain 10 Exams", examList.size(), 10);
    }

    @Test public void test_getExamWith2ElementsInTheDatabase_should_persist() throws Exception {
        when(this.mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(this.mockResultSet.getInt(anyString())).thenReturn(1).thenReturn(1).thenReturn(2)
            .thenReturn(2);
        when(this.mockResultSet.getTimestamp(anyString())).thenReturn(new Timestamp(10))
            .thenReturn(new Timestamp(20));
        when(this.mockResultSet.getBoolean(anyString())).thenReturn(true).thenReturn(false);
        when(this.mockResultSet.getString(anyString())).thenReturn("Author1").thenReturn("Author2");

        List<Exam> examList = this.examDaoJdbc.getExams();

        assertTrue("List contains 2 Exams", examList.size() == 2);
        Exam exam1 = examList.get(0);
        Exam exam2 = examList.get(1);


        assertFalse("Both elements should diffeer", exam1 == exam2);


        assertEquals("Exam1 should have the ID 1", exam1.getExamid(), 1);
        assertTrue("Exam1 should have Timestamp 10", exam1.getCreated().equals(new Timestamp(10)));
        assertTrue("Exam1 should have been passsd already", exam1.getPassed() == true);
        assertEquals("Exam1 should have author Author1", exam1.getAuthor(), "Author1");
        assertEquals("Exam1 should have subject ID 1", exam1.getSubjectID(), 1);

        assertEquals("Exam2 should have the ID 2", exam2.getExamid(), 2);
        assertTrue("Exam2 should have Timestamp 20", exam2.getCreated().equals(new Timestamp(20)));
        assertTrue("Exam2 shouldn't been passsd yet", exam2.getPassed() == false);
        assertEquals("Exam2 should have author Author2", exam2.getAuthor(), "Author2");
        assertEquals("Exam2 should have subject ID 2", exam2.getSubjectID(), 2);
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

        List<Exam> examList = this.examDaoJdbc.getExams();
        assertTrue("List should be emtpy", examList.isEmpty());
    }

    //----------------------------------------------------------------------------------------------

    @After public void tearDown() throws Exception {
        //nothing to tear down
    }

}
