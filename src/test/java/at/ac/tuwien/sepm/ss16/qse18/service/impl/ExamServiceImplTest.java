package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ExamDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ExamQuestionDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.QuestionDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.SubjectQuestionDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.ExamServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Class ExamServiceImplTest
 * Tests for the service layer in ExamServiceImpl. In order to be isolated while testing, this
 * test class uses mocks primarily to bypass the database connection procedure.
 *
 * @author Zhang Haixiang
 */
@RunWith(MockitoJUnitRunner.class) public class ExamServiceImplTest {
    @Mock private ExamDaoJdbc mockExamDaoJdbc;
    @Mock private ExamQuestionDaoJdbc mockExamQuestionDaoJdbc;
    @Mock private SubjectQuestionDaoJdbc mockSubjectQuestionDaoJdbc;
    @Mock private QuestionDaoJdbc mockQuestionDaoJdbc;
    @Mock private ConnectionH2 mockConnectionH2;
    @Mock private Connection mockConnection;
    @Mock private Statement mockStatement;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet mockResultSet;
    @Mock private ExamServiceImpl mockExam;
    private ExamServiceImpl examService;
    private Exam exam;
    private Topic topic;


    @Before public void setUp() throws Exception {
        when(mockConnectionH2.getConnection()).thenReturn(mockConnection);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        this.examService =
            new ExamServiceImpl(this.mockExamDaoJdbc, this.mockSubjectQuestionDaoJdbc,
                this.mockExamQuestionDaoJdbc, this.mockQuestionDaoJdbc);

        ArrayList<Question> al = new ArrayList<Question>() {
        };
        Question question = new Question();
        question.setQuestion("TestQuestion");
        question.setQuestionId(1);
        question.setType(QuestionType.valueOf(1));
        al.add(question);

        this.exam = createDummyExam(1, "auhtor");
        exam.setExamQuestions(al);

        this.topic = new Topic();
        topic.setTopic("Topic1");
        topic.setTopicId(1);
    }

    //Testing getExam(int)
    //----------------------------------------------------------------------------------------------
    @Test public void testIf_getExam_callsRightMethodInDao() throws Exception {
        this.examService.getExam(1);
        verify(this.mockExamDaoJdbc).getExam(1);
    }
    //----------------------------------------------------------------------------------------------

    //Testing getExams()
    //----------------------------------------------------------------------------------------------
    @Test public void testIf_getExams_callsRightMethodInDao() throws Exception {
        this.examService.getExams();
        verify(this.mockExamDaoJdbc).getExams();
    }
    //----------------------------------------------------------------------------------------------

    //Testing createExam(Exam, int, int)
    //----------------------------------------------------------------------------------------------
    @Test public void testIf_createExam_callsRightMethodInDao() throws Exception {
        List<Integer> questionIDList = new ArrayList<>();
        Question q1 = createDummyQuestion(1, "question1");
        Question q2 = createDummyQuestion(2, "question2");
        Map<Integer, Boolean> questionBooleans = new HashMap<>();
        questionBooleans.put(1, false);
        questionBooleans.put(2, false);

        questionIDList.add(1);
        questionIDList.add(2);

        when(this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exam, 1))
            .thenReturn(questionIDList);
        when(this.mockExamQuestionDaoJdbc.getAllQuestionBooleans(questionIDList))
            .thenReturn(questionBooleans);
        when(this.mockQuestionDaoJdbc.getQuestion(anyInt())).thenReturn(q1).thenReturn(q2);

        this.examService.createExam(this.exam, topic, 1000);
        verify(this.mockExamDaoJdbc).create(this.exam, this.exam.getExamQuestions());
    }

    @Test(expected = ServiceException.class)
    public void test_createExam_invalidAuthorThrowsException() throws Exception {
        Exam fail = createDummyExam(2, "");

        this.examService.createExam(fail, topic, 1000);
    }

    @Test(expected = ServiceException.class) public void test_createExam_ExamIDThrowsException()
        throws Exception {
        Exam fail = createDummyExam(-2, "Author2");
        this.examService.createExam(fail, topic, 1000);
    }
    //----------------------------------------------------------------------------------------------

    //Testing deleteExam(Exam)
    //----------------------------------------------------------------------------------------------
    @Test public void testIf_deleteExam_callsRightMethodInDao() throws Exception {
        this.examService.deleteExam(this.exam);
        verify(this.mockExamDaoJdbc).delete(this.exam);
    }
    //----------------------------------------------------------------------------------------------

    //Testing getRightQuestions(exam, int, int)
    //----------------------------------------------------------------------------------------------

    @Test public void test_getRightQuestionsCallsRightMethodsInTheDaos() throws Exception {
        List<Integer> questionIDList = new ArrayList<>();
        Question q1 = createDummyQuestion(1, "question1");
        Question q2 = createDummyQuestion(2, "question2");
        Map<Integer, Boolean> questionBooleans = new HashMap<>();
        questionBooleans.put(1, false);
        questionBooleans.put(2, false);

        questionIDList.add(1);
        questionIDList.add(2);

        when(this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exam, 1))
            .thenReturn(questionIDList);
        when(this.mockExamQuestionDaoJdbc.getAllQuestionBooleans(questionIDList))
            .thenReturn(questionBooleans);
        when(this.mockQuestionDaoJdbc.getQuestion(anyInt())).thenReturn(q1).thenReturn(q2);


        this.examService.getRightQuestions(this.exam, 1, 1000);
        verify(this.mockSubjectQuestionDaoJdbc).getAllQuestionsOfSubject(this.exam, 1);
        this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exam, 1);
        verify(this.mockExamQuestionDaoJdbc).getAllQuestionBooleans(questionIDList);
        this.mockExamQuestionDaoJdbc.getAllQuestionBooleans(questionIDList);
        verify(this.mockQuestionDaoJdbc).getQuestion(questionIDList.get(0));
        verify(this.mockQuestionDaoJdbc).getQuestion(questionIDList.get(1));
        this.mockQuestionDaoJdbc.getQuestion(questionIDList.get(0));
        this.mockQuestionDaoJdbc.getQuestion(questionIDList.get(1));
    }

    @Test public void test_getRightQuestionsWith3Questions_should_persist() throws Exception {
        Question q1 = createDummyQuestion(1, "Question1");
        Question q2 = createDummyQuestion(2, "Question2");
        Question q3 = createDummyQuestion(3, "Question3");
        List<Integer> questionIDList = new ArrayList<>();
        questionIDList.add(q1.getQuestionId());
        questionIDList.add(q2.getQuestionId());
        questionIDList.add(q3.getQuestionId());
        Map<Integer, Boolean> questionBooleans = new HashMap<>();
        questionBooleans.put(1, false);
        questionBooleans.put(2, false);
        questionBooleans.put(3, false);
        List<Question> questions = new ArrayList<>();
        questions.add(q1);
        questions.add(q2);
        questions.add(q3);
        List<Question> test;

        when(this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exam, 1))
            .thenReturn(questionIDList);
        when(this.mockExamQuestionDaoJdbc.getAllQuestionBooleans(questionIDList))
            .thenReturn(questionBooleans);
        when(this.mockQuestionDaoJdbc.getQuestion(anyInt())).thenReturn(q1).thenReturn(q2)
            .thenReturn(q3);

        test = this.examService.getRightQuestions(this.exam, 1, 1500);
        this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exam, 1);
        this.mockExamQuestionDaoJdbc.getAllQuestionBooleans(questionIDList);
        this.mockQuestionDaoJdbc.getQuestion(questionIDList.get(0));
        this.mockQuestionDaoJdbc.getQuestion(questionIDList.get(1));
        this.mockQuestionDaoJdbc.getQuestion(questionIDList.get(2));

        assertSame("Should be the same size", test.size(), questions.size());
        assertTrue("Questions should have the same values",
            test.get(0).equals(questions.get(0)) || test.get(0).equals(questions.get(1)) || test
                .get(0).equals(questions.get(2)));
        assertTrue("Questions should have the same values",
            test.get(1).equals(questions.get(0)) || test.get(1).equals(questions.get(1)) || test
                .get(1).equals(questions.get(2)));
        assertTrue("Questions should have the same values",
            test.get(2).equals(questions.get(0)) || test.get(2).equals(questions.get(1)) || test
                .get(2).equals(questions.get(2)));


    }

    @Test public void test_getRightQuesionsShouldOnlyHaveOneQuestion_should_persist()
        throws Exception {
        List<Integer> questionIDList = new ArrayList<>();
        Question q1 = createDummyQuestion(1, "question1");
        Question q2 = createDummyQuestion(2, "question2");
        Question q3 = createDummyQuestion(3, "question3");
        q3.setQuestionTime(1000);

        Map<Integer, Boolean> questionBooleans = new HashMap<>();
        questionBooleans.put(1, false);
        questionBooleans.put(2, false);

        questionIDList.add(1);
        questionIDList.add(2);
        questionIDList.add(3);

        List<Question> questions = new ArrayList<>();
        questions.add(q1);
        questions.add(q2);
        questions.add(q3);
        List<Question> test;

        when(this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exam, 1))
            .thenReturn(questionIDList);
        when(this.mockExamQuestionDaoJdbc.getAllQuestionBooleans(questionIDList))
            .thenReturn(questionBooleans);
        when(this.mockQuestionDaoJdbc.getQuestion(anyInt())).thenReturn(q1).thenReturn(q2)
            .thenReturn(q3);

        test = this.examService.getRightQuestions(this.exam, 1, 1000);
        this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exam, 1);
        this.mockExamQuestionDaoJdbc.getAllQuestionBooleans(questionIDList);
        this.mockQuestionDaoJdbc.getQuestion(questionIDList.get(0));
        this.mockQuestionDaoJdbc.getQuestion(questionIDList.get(1));
        this.mockQuestionDaoJdbc.getQuestion(questionIDList.get(2));

        assertTrue("Should only contain one Element", test.size() == 1);
    }

    @Test public void test_getRightQuestionsOnlyTheFirst2OutOf4QuestionsShouldBeInTheList()throws Exception{
        List<Integer> questionIDList = new ArrayList<>();
        Question q1 = createDummyQuestion(1, "question1");
        Question q2 = createDummyQuestion(2, "question2");
        Question q3 = createDummyQuestion(3, "question3");
        Question q4 = createDummyQuestion(4, "question4");

        Map<Integer, Boolean> questionBooleans = new HashMap<>();
        questionBooleans.put(3, false);
        questionBooleans.put(4, true);

        questionIDList.add(1);
        questionIDList.add(2);
        questionIDList.add(3);
        questionIDList.add(4);

        List<Question> questions = new ArrayList<>();
        questions.add(q1);
        questions.add(q2);
        questions.add(q3);
        questions.add(q4);

        List<Question> test;

        when(this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exam, 1))
            .thenReturn(questionIDList);
        when(this.mockExamQuestionDaoJdbc.getAllQuestionBooleans(questionIDList))
            .thenReturn(questionBooleans);
        when(this.mockQuestionDaoJdbc.getQuestion(anyInt())).thenReturn(q1).thenReturn(q2)
            .thenReturn(q3).thenReturn(q4);

        test = this.examService.getRightQuestions(this.exam, 1, 1000);
        this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exam, 1);
        this.mockExamQuestionDaoJdbc.getAllQuestionBooleans(questionIDList);
        this.mockQuestionDaoJdbc.getQuestion(questionIDList.get(0));
        this.mockQuestionDaoJdbc.getQuestion(questionIDList.get(1));
        this.mockQuestionDaoJdbc.getQuestion(questionIDList.get(2));
        this.mockQuestionDaoJdbc.getQuestion(questionIDList.get(3));

        assertTrue("List should only contain 2 Elements", test.size() == 2);
        System.out.println(test.get(0).getQuestion());
        System.out.println(test.get(1).getQuestion());
        assertTrue("First Element should be in the List", test.get(0).equals(q1) || test.get(0).equals(q2));
        assertTrue("Second Question should be in the List", test.get(1).equals(q2)|| test.get(1).equals(q1));

    }

    @Test(expected = ServiceException.class)
    public void test_getRightQuestionsWithEmptyExamQuestionList_should_fail() throws Exception {
        this.examService.getRightQuestions(this.exam, 1, 2);
    }

    @Test(expected = ServiceException.class)
    public void test_getRightQuestionsWithTooSmallExamTime_should_fail() throws Exception{
        List<Integer> questionIDList = new ArrayList<>();
        Question q1 = createDummyQuestion(1, "question1");
        questionIDList.add(1);

        List<Question> questions = new ArrayList<>();
        questions.add(q1);

        List<Question> test;

        when(this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exam, 1))
            .thenReturn(questionIDList);
        when(this.mockQuestionDaoJdbc.getQuestion(anyInt())).thenReturn(q1);

        this.examService.getRightQuestions(this.exam, 1, 700);
        this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exam, 1);
        this.mockQuestionDaoJdbc.getQuestion(questionIDList.get(0));
    }

    @Test(expected = ServiceException.class)
    public void test_getRightQuestionsWithoutDatabaseConnection_should_fail() throws Exception {
        when(this.mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        this.examService.getRightQuestions(this.exam, 1, 1500);
        PowerMockito.verifyStatic();
        this.mockConnectionH2.getConnection();
    }
    //----------------------------------------------------------------------------------------------

    //Testing getAllQuestionsOfExam(int)
    //----------------------------------------------------------------------------------------------
    @Test public void test_getAllQuestionsCallsRightMethodInDao() throws Exception {
        this.examService.getAllQuestionsOfExam(1);
        verify(this.mockExamQuestionDaoJdbc).getAllQuestionsOfExam(1);
    }

    @Test(expected = ServiceException.class)
    public void test_getAllQuestionsWithInvalidExamID_should_fail() throws Exception {
        this.examService.getAllQuestionsOfExam(-1);
    }

    @Test(expected = ServiceException.class)
    public void test_getAllQuestionsWithoutDatabaseConnection_should_fail() throws Exception {
        when(mockExamQuestionDaoJdbc.getAllQuestionsOfExam(anyInt())).thenThrow(DaoException.class);

        examService.getAllQuestionsOfExam(exam.getExamid());

        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }
    //----------------------------------------------------------------------------------------------

    @After public void tearDown() throws Exception {
        //nothing to tear down
    }

    private Exam createDummyExam(int examID, String author) {
        Exam exam = new Exam();
        exam.setExamid(1);
        exam.setCreated(new Timestamp(10));
        exam.setPassed(false);
        exam.setAuthor("author1");
        exam.setSubjectID(1);

        return exam;
    }

    private Question createDummyQuestion(int questionID, String question) {
        Question q = new Question();
        q.setQuestionId(questionID);
        q.setQuestion(question);
        q.setType(QuestionType.valueOf(1));
        q.setQuestionTime(500);

        return q;
    }

}
