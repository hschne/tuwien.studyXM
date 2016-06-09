package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.*;
import at.ac.tuwien.sepm.ss16.qse18.domain.*;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
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
 * Class ExerciseExamServiceImplTest
 * Tests for the service layer in ExerciseExamServiceImpl. In order to be isolated while testing, this
 * test class uses mocks primarily to bypass the database connection procedure.
 *
 * @author Zhang Haixiang
 */
@RunWith(MockitoJUnitRunner.class) public class ExerciseExamServiceImplTest {
    @Mock private ExerciseExamDaoJdbc mockExerciseExamDaoJdbc;
    @Mock private ExerciseExamQuestionDaoJdbc mockExerciseExamQuestionDaoJdbc;
    @Mock private SubjectQuestionDaoJdbc mockSubjectQuestionDaoJdbc;
    @Mock private QuestionDaoJdbc mockQuestionDaoJdbc;
    @Mock private SubjectTopicDaoJdbc mockSubjectTopicDaoJdbc;
    @Mock private QuestionTopicDaoJdbc mockQuestionTopicDaoJdbc;
    @Mock private SubjectDaoJdbc mockSubjectDaoJdbc;
    @Mock private ConnectionH2 mockConnectionH2;
    @Mock private Connection mockConnection;
    @Mock private Statement mockStatement;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet mockResultSet;
    @Mock private ExerciseExamServiceImpl mockExam;
    private ExerciseExamServiceImpl exerciseExamService;
    private ExerciseExam exerciseExam;
    private Topic topic;


    @Before public void setUp() throws Exception {
        when(mockConnectionH2.getConnection()).thenReturn(mockConnection);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        this.exerciseExamService =
            new ExerciseExamServiceImpl(this.mockExerciseExamDaoJdbc, this.mockSubjectQuestionDaoJdbc,
                this.mockExerciseExamQuestionDaoJdbc, this.mockQuestionDaoJdbc, this.mockSubjectTopicDaoJdbc,
                this.mockQuestionTopicDaoJdbc, this.mockSubjectDaoJdbc);

        ArrayList<Question> al = new ArrayList<Question>() {
        };
        Question question = new Question();
        question.setQuestion("TestQuestion");
        question.setQuestionId(1);
        question.setType(QuestionType.valueOf(1));
        al.add(question);

        this.exerciseExam = createDummyExam(1, "auhtor");
        exerciseExam.setExamQuestions(al);
        exerciseExam.setExamTime(1);

        this.topic = new Topic();
        topic.setTopic("Topic1");
        topic.setTopicId(1);
    }

    //Testing getExam(int)
    //----------------------------------------------------------------------------------------------
    @Test public void testIf_getExam_callsRightMethodInDao() throws Exception {
        this.exerciseExamService.getExam(1);
        verify(this.mockExerciseExamDaoJdbc).getExam(1);
    }
    //----------------------------------------------------------------------------------------------

    //Testing getExams()
    //----------------------------------------------------------------------------------------------
    @Test public void testIf_getExams_callsRightMethodInDao() throws Exception {
        this.exerciseExamService.getExams();
        verify(this.mockExerciseExamDaoJdbc).getExams();
    }
    //----------------------------------------------------------------------------------------------

    //Testing createExam(ExerciseExam, int, int)
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

        when(this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exerciseExam, 1))
            .thenReturn(questionIDList);
        when(this.mockExerciseExamQuestionDaoJdbc.getAllQuestionBooleans(questionIDList))
            .thenReturn(questionBooleans);
        when(this.mockQuestionDaoJdbc.getQuestion(anyInt())).thenReturn(q1).thenReturn(q2);

        this.exerciseExamService.createExam(this.exerciseExam, topic, 1000);
        verify(this.mockExerciseExamDaoJdbc).create(this.exerciseExam, this.exerciseExam.getExamQuestions());
    }

    @Test(expected = ServiceException.class)
    public void test_createExam_invalidAuthorThrowsException() throws Exception {
        ExerciseExam fail = createDummyExam(2, "");

        this.exerciseExamService.createExam(fail, topic, 1000);
    }

    @Test(expected = ServiceException.class) public void test_createExam_ExamIDThrowsException()
        throws Exception {
        ExerciseExam fail = createDummyExam(-2, "Author2");
        this.exerciseExamService.createExam(fail, topic, 1000);
    }
    //----------------------------------------------------------------------------------------------

    //Testing deleteExam(ExerciseExam)
    //----------------------------------------------------------------------------------------------
    @Test public void testIf_deleteExam_callsRightMethodInDao() throws Exception {
        this.exerciseExamService.deleteExam(this.exerciseExam);
        verify(this.mockExerciseExamDaoJdbc).delete(this.exerciseExam);
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

        when(this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exerciseExam, 1))
            .thenReturn(questionIDList);
        when(this.mockExerciseExamQuestionDaoJdbc.getAllQuestionBooleans(questionIDList))
            .thenReturn(questionBooleans);
        when(this.mockQuestionDaoJdbc.getQuestion(anyInt())).thenReturn(q1).thenReturn(q2);


        this.exerciseExamService.getRightQuestions(this.exerciseExam, 1, 1000);
        verify(this.mockSubjectQuestionDaoJdbc).getAllQuestionsOfSubject(this.exerciseExam, 1);
        this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exerciseExam, 1);
        verify(this.mockExerciseExamQuestionDaoJdbc).getAllQuestionBooleans(questionIDList);
        this.mockExerciseExamQuestionDaoJdbc.getAllQuestionBooleans(questionIDList);
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

        when(this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exerciseExam, 1))
            .thenReturn(questionIDList);
        when(this.mockExerciseExamQuestionDaoJdbc.getAllQuestionBooleans(questionIDList))
            .thenReturn(questionBooleans);
        when(this.mockQuestionDaoJdbc.getQuestion(anyInt())).thenReturn(q1).thenReturn(q2)
            .thenReturn(q3);

        test = this.exerciseExamService.getRightQuestions(this.exerciseExam, 1, 1500);
        this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exerciseExam, 1);
        this.mockExerciseExamQuestionDaoJdbc.getAllQuestionBooleans(questionIDList);
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

        when(this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exerciseExam, 1))
            .thenReturn(questionIDList);
        when(this.mockExerciseExamQuestionDaoJdbc.getAllQuestionBooleans(questionIDList))
            .thenReturn(questionBooleans);
        when(this.mockQuestionDaoJdbc.getQuestion(anyInt())).thenReturn(q1).thenReturn(q2)
            .thenReturn(q3);

        test = this.exerciseExamService.getRightQuestions(this.exerciseExam, 1, 1000);
        this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exerciseExam, 1);
        this.mockExerciseExamQuestionDaoJdbc.getAllQuestionBooleans(questionIDList);
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

        when(this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exerciseExam, 1))
            .thenReturn(questionIDList);
        when(this.mockExerciseExamQuestionDaoJdbc.getAllQuestionBooleans(questionIDList))
            .thenReturn(questionBooleans);
        when(this.mockQuestionDaoJdbc.getQuestion(anyInt())).thenReturn(q1).thenReturn(q2)
            .thenReturn(q3).thenReturn(q4);

        test = this.exerciseExamService.getRightQuestions(this.exerciseExam, 1, 1000);
        this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exerciseExam, 1);
        this.mockExerciseExamQuestionDaoJdbc.getAllQuestionBooleans(questionIDList);
        this.mockQuestionDaoJdbc.getQuestion(questionIDList.get(0));
        this.mockQuestionDaoJdbc.getQuestion(questionIDList.get(1));
        this.mockQuestionDaoJdbc.getQuestion(questionIDList.get(2));
        this.mockQuestionDaoJdbc.getQuestion(questionIDList.get(3));

        assertTrue("List should only contain 2 Elements", test.size() == 2);
        assertTrue("First Element should be in the List", test.get(0).equals(q1) || test.get(0).equals(q2));
        assertTrue("Second Question should be in the List", test.get(1).equals(q2)|| test.get(1).equals(q1));

    }

    @Test(expected = ServiceException.class)
    public void test_getRightQuestionsWithEmptyExamQuestionList_should_fail() throws Exception {
        this.exerciseExamService.getRightQuestions(this.exerciseExam, 1, 2);
    }

    @Test(expected = ServiceException.class)
    public void test_getRightQuestionsWithTooSmallExamTime_should_fail() throws Exception{
        List<Integer> questionIDList = new ArrayList<>();
        Question q1 = createDummyQuestion(1, "question1");
        questionIDList.add(1);

        List<Question> questions = new ArrayList<>();
        questions.add(q1);

        List<Question> test;

        when(this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exerciseExam, 1))
            .thenReturn(questionIDList);
        when(this.mockQuestionDaoJdbc.getQuestion(anyInt())).thenReturn(q1);

        this.exerciseExamService.getRightQuestions(this.exerciseExam, 1, 700);
        this.mockSubjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.exerciseExam, 1);
        this.mockQuestionDaoJdbc.getQuestion(questionIDList.get(0));
    }

    @Test(expected = ServiceException.class)
    public void test_getRightQuestionsWithoutDatabaseConnection_should_fail() throws Exception {
        when(this.mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        this.exerciseExamService.getRightQuestions(this.exerciseExam, 1, 1500);
        PowerMockito.verifyStatic();
        this.mockConnectionH2.getConnection();
    }
    //----------------------------------------------------------------------------------------------

    //Testing getAllQuestionsOfExam(int)
    //----------------------------------------------------------------------------------------------
    @Test public void test_getAllQuestionsCallsRightMethodInDao() throws Exception {
        this.exerciseExamService.getAllQuestionsOfExam(1);
        verify(this.mockExerciseExamQuestionDaoJdbc).getAllQuestionsOfExam(1);
    }

    @Test(expected = ServiceException.class)
    public void test_getAllQuestionsWithInvalidExamID_should_fail() throws Exception {
        this.exerciseExamService.getAllQuestionsOfExam(-1);
    }

    @Test(expected = ServiceException.class)
    public void test_getAllQuestionsWithoutDatabaseConnection_should_fail() throws Exception {
        when(mockExerciseExamQuestionDaoJdbc.getAllQuestionsOfExam(anyInt())).thenThrow(DaoException.class);

        exerciseExamService.getAllQuestionsOfExam(exerciseExam.getExamid());

        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }
    //----------------------------------------------------------------------------------------------

    //Testing gradeExam(Exam)
    //----------------------------------------------------------------------------------------------
    @Test public void test_gradeExamCallsRightMethodInDao_should_persist()throws Exception{
        List<Integer> questionID = new ArrayList<>();
        questionID.add(1);
        questionID.add(2);

        Map<Integer, Boolean> questionBooleans = new HashMap<>();
        questionBooleans.put(1, true);
        questionBooleans.put(2, false);

        when(this.mockExerciseExamQuestionDaoJdbc.getAllQuestionsOfExam(this.exerciseExam.getExamid())).thenReturn(questionID);
        when(this.mockExerciseExamQuestionDaoJdbc.getAllQuestionBooleans(questionID)).thenReturn(questionBooleans);

        this.exerciseExamService.gradeExam(this.exerciseExam);
        verify(mockExerciseExamQuestionDaoJdbc).getAllQuestionsOfExam(this.exerciseExam.getExamid());
        verify(mockExerciseExamQuestionDaoJdbc).getAllQuestionBooleans(questionID);
    }

    @Test public void test_gradeExamWith3CorrectAnd2IncorrectQuestions_should_persist() throws Exception{
        String[] test;
        List<Integer> questionID = new ArrayList<>();
        questionID.add(1);
        questionID.add(2);
        questionID.add(3);
        questionID.add(4);
        questionID.add(5);

        Map<Integer, Boolean> questionBooleans = new HashMap<>();
        questionBooleans.put(1, true);
        questionBooleans.put(2, true);
        questionBooleans.put(3, true);
        questionBooleans.put(4, false);
        questionBooleans.put(5, false);

        when(this.mockExerciseExamQuestionDaoJdbc.getAllQuestionsOfExam(this.exerciseExam.getExamid())).thenReturn(questionID);
        when(this.mockExerciseExamQuestionDaoJdbc.getAllQuestionBooleans(questionID)).thenReturn(questionBooleans);

        test = this.exerciseExamService.gradeExam(this.exerciseExam);

        assertTrue("3 should be correct", Double.parseDouble(test[0]) == 3);
        assertTrue("2 should be incorrect", Double.parseDouble(test[1]) == 2);
        assertTrue("Grade should be D", test[2].equals("D"));
    }

    @Test(expected = ServiceException.class)
    public void test_gradeExamWithNull_should_fail() throws Exception{
        this.exerciseExamService.gradeExam(null);
    }

    @Test(expected = ServiceException.class)
    public void test_gradeExamWithInvalidExamID_should_fail()throws Exception{
        this.exerciseExam.setExamid(0);
        this.exerciseExamService.gradeExam(this.exerciseExam);
    }

    //----------------------------------------------------------------------------------------------

    //Testing topicGrade(Exam)
    //----------------------------------------------------------------------------------------------
    @Test public void test_gradeExamCallsRightMethodsInDao_should_persist()throws Exception{
        List<Topic> topicList = new ArrayList<>();
        topicList.add(new Topic(1, "topic1"));
        topicList.add(new Topic(2, "topic2"));

        List<Question> questionList1 = new ArrayList<>();
        questionList1.add(new Question(1, "question1", QuestionType.valueOf(1), 6));

        List<Question> questionList2 = new ArrayList<>();
        questionList2.add(new Question(2, "question2", QuestionType.valueOf(2), 5));

        Subject subject = createDummySubject("subject1", 1);

        Map<Integer, Boolean> questionBooleans = new HashMap<>();
        questionBooleans.put(1, false);
        questionBooleans.put(2, true);

        Map<Integer, Boolean> questionBooleans2 = new HashMap<>();
        questionBooleans.put(1, false);
        questionBooleans.put(2, true);

        when(this.mockSubjectDaoJdbc.getSubject(this.exerciseExam.getSubjectID())).thenReturn(subject);
        when(this.mockSubjectTopicDaoJdbc.getTopicToSubject(subject)).thenReturn(topicList);
        when(this.mockQuestionTopicDaoJdbc.getQuestionToTopic(topicList.get(0))).thenReturn(questionList1);
        when(this.mockQuestionTopicDaoJdbc.getQuestionToTopic(topicList.get(1))).thenReturn(questionList2);
        when(this.mockExerciseExamQuestionDaoJdbc.getAllQuestionBooleans(anyList())).thenReturn(questionBooleans).
            thenReturn(questionBooleans2);

        this.exerciseExamService.topicGrade(this.exerciseExam);
        verify(mockSubjectDaoJdbc).getSubject(this.exerciseExam.getSubjectID());
        verify(mockSubjectTopicDaoJdbc).getTopicToSubject(subject);
        verify(mockQuestionTopicDaoJdbc).getQuestionToTopic(topicList.get(0));
        verify(mockQuestionTopicDaoJdbc).getQuestionToTopic(topicList.get(1));
        verify(mockExerciseExamQuestionDaoJdbc, times(2)).getAllQuestionBooleans(anyList());
    }

    @Test public void test_gradeExamWith3Topics_should_persist() throws Exception{
        Map<Topic, String[]> test = new HashMap<>();
        List<Topic> topicList = new ArrayList<>();
        topicList.add(new Topic(1, "topic1"));
        topicList.add(new Topic(2, "topic2"));
        topicList.add(new Topic(3, "topic3"));

        List<Question> questionList1 = new ArrayList<>();
        questionList1.add(new Question(1, "question1", QuestionType.valueOf(1), 6));

        List<Question> questionList2 = new ArrayList<>();
        questionList2.add(new Question(2, "question2", QuestionType.valueOf(2), 5));

        List<Question> questionList3 = new ArrayList<>();
        questionList3.add(new Question(3, "question3", QuestionType.valueOf(1), 4));

        Subject subject = createDummySubject("subject1", 1);


        Map<Integer, Boolean> questionBooleans = new HashMap<>();
        questionBooleans.put(1, false);
        questionBooleans.put(2, true);

        Map<Integer, Boolean> questionBooleans2 = new HashMap<>();
        questionBooleans.put(3, false);
        questionBooleans.put(4, false);


        Map<Integer, Boolean> questionBooleans3 = new HashMap<>();
        questionBooleans.put(5, true);
        questionBooleans.put(6, true);


        when(this.mockSubjectDaoJdbc.getSubject(this.exerciseExam.getSubjectID())).thenReturn(subject);
        when(this.mockSubjectTopicDaoJdbc.getTopicToSubject(subject)).thenReturn(topicList);
        when(this.mockQuestionTopicDaoJdbc.getQuestionToTopic(topicList.get(0))).thenReturn(questionList1);
        when(this.mockQuestionTopicDaoJdbc.getQuestionToTopic(topicList.get(1))).thenReturn(questionList2);
        when(this.mockQuestionTopicDaoJdbc.getQuestionToTopic(topicList.get(2))).thenReturn(questionList3);
        when(this.mockExerciseExamQuestionDaoJdbc.getAllQuestionBooleans(anyList())).thenReturn(questionBooleans).
            thenReturn(questionBooleans2).thenReturn(questionBooleans3);

        test = this.exerciseExamService.topicGrade(this.exerciseExam);
        assertTrue("Map should have size 3", test.size() == 3);
        assertTrue("Map should contain topic1", test.containsKey(topicList.get(0)));
        assertTrue("Map should contain topic2", test.containsKey(topicList.get(1)));
        assertTrue("Map should contain topic3", test.containsKey(topicList.get(2)));

    }

    @Test(expected = ServiceException.class) public void test_topicGradeWithNull_should_fail()throws Exception{
        this.exerciseExamService.topicGrade(null);
    }

    //----------------------------------------------------------------------------------------------

    @After public void tearDown() throws Exception {
        //nothing to tear down
    }

    private ExerciseExam createDummyExam(int examID, String author) {
        ExerciseExam exerciseExam = new ExerciseExam();
        exerciseExam.setExamid(1);
        exerciseExam.setCreated(new Timestamp(10));
        exerciseExam.setPassed(false);
        exerciseExam.setAuthor("author1");
        exerciseExam.setSubjectID(1);

        return exerciseExam;
    }

    private Question createDummyQuestion(int questionID, String question) {
        Question q = new Question();
        q.setQuestionId(questionID);
        q.setQuestion(question);
        q.setType(QuestionType.valueOf(1));
        q.setQuestionTime(500);

        return q;
    }

    private Subject createDummySubject(String name, int subjectID) {
        Subject subject = new Subject();
        subject.setName(name);
        subject.setEcts(6);
        subject.setAuthor("Author");
        subject.setSemester("SS16");
        subject.setSubjectId(subjectID);
        subject.setTimeSpent(800);
        return subject;
    }

}
