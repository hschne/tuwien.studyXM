package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ExamDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ExerciseExamDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.QuestionTopicDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ExerciseExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummyExam;
import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummyExerciseExam;
import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummyQuestion;
import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummySubject;
import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummyTopic;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

/**
 * This is the test class for the statistic service layer.
 * @author Julian Strohmayer
 */
@RunWith(MockitoJUnitRunner.class)
public class StatisticServiceImplTest {

    @Mock
    private ExamService examServiceMock;
    @Mock
    private ExerciseExamService exerciseExamServiceMock;
    @Mock
    private ExamDaoJdbc examDaoJdbcMock;
    @Mock
    private ExerciseExamDaoJdbc exerciseExamDaoJdbcMock;
    @Mock
    private SubjectService subjectServiceMock;
    @Mock
    private SubjectTopicDao subjectTopicDaoMock;
    @Mock
    private QuestionTopicDaoJdbc questionTopicDaoJdbcMock;

    private StatisticServiceImpl statisticService;

    @Before
    public void setUp() {
        statisticService =
                new StatisticServiceImpl(examServiceMock, exerciseExamServiceMock, examDaoJdbcMock,
                        exerciseExamDaoJdbcMock, questionTopicDaoJdbcMock, subjectServiceMock,
                        subjectTopicDaoMock);
    }

    /**
     * This test calls the getAnsweredQuestionsForTopic with null. A ServiceException is expected.
     */
    @Test(expected = ServiceException.class)
    public void test_getAnsweredQuestionsForTopic_shouldFail() throws Exception {
        statisticService.getAnsweredQuestionsForTopic(null);
    }

    /**
     * This test calls the getAnsweredQuestionsForTopic method with a valid topic. The topic has 3
     * questions which are already answered in an exercise exam. The expected return value is
     * double[]{1,3,3}
     */
    @Test
    public void test_getAnsweredQuestionsForTopic_shouldReturnCorrectResult() throws Exception {

        Question q1 = createDummyQuestion();
        q1.setQuestionId(1);
        Question q2 = createDummyQuestion();
        q2.setQuestionId(2);
        Question q3 = createDummyQuestion();
        q3.setQuestionId(3);
        List<Question> topicQuestions = new ArrayList<>();
        topicQuestions.add(q1);
        topicQuestions.add(q2);
        topicQuestions.add(q3);
        when(questionTopicDaoJdbcMock.getQuestionToTopic(any())).thenReturn(topicQuestions);

        Exam exam = createDummyExam();
        List<Exam> examList = new ArrayList<>();
        examList.add(exam);
        when(examDaoJdbcMock.getExams()).thenReturn(examList);

        ExerciseExam exerciseExam1 = createDummyExerciseExam();
        exerciseExam1.setExamid(1);
        List<Integer> exerciseExamList = new ArrayList<>();
        exerciseExamList.add(1);
        when(examServiceMock.getAllExerciseExamsOfExam(any())).thenReturn(exerciseExamList);
        when(exerciseExamDaoJdbcMock.getExam(1)).thenReturn(exerciseExam1);

        List<Integer> answeredQuestionsOfExam = new ArrayList<>();
        answeredQuestionsOfExam.add(1);
        answeredQuestionsOfExam.add(2);
        answeredQuestionsOfExam.add(3);
        when(exerciseExamServiceMock.getAnsweredQuestionsOfExam(1)).
                thenReturn(answeredQuestionsOfExam);

        double[] result = statisticService.getAnsweredQuestionsForTopic(createDummyTopic());
        assertEquals(1, result[0], 0);
        assertEquals(3, result[1], 0);
        assertEquals(3, result[2], 0);
    }

    /**
     * This test calls getHint with null, a ServiceException is expected
     */
    @Test(expected = ServiceException.class)
    public void test_getHintWithNull_shouldFail() throws Exception {
        statisticService.getHint(null);
    }

    /**
     * This test calls the initializeHints method, followed by the getHint method, to check if the
     * hints are correctly initialized.
     */
    @Test
    public void test_initializeHints_shouldReturnCorrectHints() throws Exception {

        List<Subject> subjectList = new ArrayList<>();
        Subject subjectUnderTime = createDummySubject();
        subjectUnderTime.setSubjectId(1);
        subjectUnderTime.setEcts(5);
        subjectUnderTime.setTimeSpent(0);
        subjectList.add(subjectUnderTime);

        Subject subjectOverTime = createDummySubject();
        subjectOverTime.setSubjectId(2);
        subjectOverTime.setEcts(0);
        subjectOverTime.setTimeSpent(5);
        subjectList.add(subjectOverTime);
        when(subjectServiceMock.getSubjects()).thenReturn(subjectList);

        statisticService.initializeHints();
        assertEquals("Hint - you are doing fine!", statisticService.getHint(subjectUnderTime));
        assertEquals("Hint - try to manage your time more efficient!",
                statisticService.getHint(subjectOverTime));
    }

    /**
     * This test calls gradeAllExamsForSubject method with null, a ServiceException is expected
     */
    @Test(expected = ServiceException.class)
    public void test_gradeAllExamsForSubject_shouldFail() throws Exception {
        statisticService.gradeAllExamsForSubject(null);
    }

    /**
     * This test calls gradeAllExamsForSubject method with a subject with no topics. The expected
     * return value is double[]{-1,0}
     */
    @Test
    public void test_gradeAllExamsForSubjectWithNoTopic_shouldReturnCorrectResult() throws Exception {
        when(subjectTopicDaoMock.getTopicToSubject(any())).thenReturn(new ArrayList<Topic>());
        List<Subject> subjects = new ArrayList<>();
        subjects.add(createDummySubject());
        when(subjectServiceMock.getSubjects()).thenReturn(subjects);

        statisticService.initializeHints();
        double[] result = statisticService.gradeAllExamsForSubject(createDummySubject());
        assertEquals(-1, result[0], 0);
        assertEquals(0, result[1], 0);
    }

    /**
     * This test calls gradeAllExamsForSubject method with a valid subject. The subjects has two
     * exercise exams. One is graded with an "B" = 2.0 One is graded with an "F" = 5.0 And the
     * combined value is 7.0 Which means the overall grade for this subject is 7/2 = 3.5
     */
    @Test
    public void test_gradeAllExamsForSubjectWithTopics_shouldReturnCorrectResult() throws Exception {

        List<Topic> topics = new ArrayList<>();
        topics.add(createDummyTopic());
        when(subjectTopicDaoMock.getTopicToSubject(any())).thenReturn(topics);

        List<Exam> exams = new ArrayList<>();
        Exam exam = createDummyExam();
        exam.setExamid(1);
        exam.setSubject(1);
        exams.add(exam);
        when(examDaoJdbcMock.getExams()).thenReturn(exams);

        List<Subject> subjects = new ArrayList<>();
        Subject subject = createDummySubject();
        subject.setSubjectId(1);
        subjects.add(subject);
        when(subjectServiceMock.getSubjects()).thenReturn(subjects);

        List<Integer> exerciseExams = new ArrayList<>();
        exerciseExams.add(1);
        exerciseExams.add(2);
        when(examServiceMock.getAllExerciseExamsOfExam(any())).thenReturn(exerciseExams);

        ExerciseExam ex1 = createDummyExerciseExam();
        ex1.setExamid(1);
        ExerciseExam ex2 = createDummyExerciseExam();
        ex2.setExamid(2);
        when(exerciseExamDaoJdbcMock.getExam(1)).thenReturn(ex1);
        when(exerciseExamDaoJdbcMock.getExam(2)).thenReturn(ex2);

        when(exerciseExamServiceMock.gradeExam(ex1)).thenReturn(new String[]{"10", "10", "B"});
        when(exerciseExamServiceMock.gradeExam(ex2)).thenReturn(new String[]{"0", "10", "F"});

        statisticService.initializeHints();
        double[] result = statisticService.gradeAllExamsForSubject(createDummySubject());
        assertEquals(3.5, result[0], 0);
        assertEquals(2.0, result[1], 0);
    }

    /**
     * This test calls the method checkKnowItAllAchievement with null. A ServiceException is
     * expected.
     */
    @Test(expected = ServiceException.class)
    public void test_checkKnowItAllAchievement_ShouldFail() throws Exception {
        statisticService.checkKnowItAllAchievement(null);
    }

    /**
     * This test calls the method checkKnowItAllAchievement with a valid subject with no topics. The
     * expected return value is false
     */
    @Test
    public void test_checkKnowItAllAchievementWithNoTopics_ShouldReturnFalse() throws Exception {
        when(subjectTopicDaoMock.getTopicToSubject(any())).thenReturn(new ArrayList<Topic>());
        assertEquals(false, statisticService.checkKnowItAllAchievement(createDummySubject()));
    }


    /**
     * This test calls the method checkKnowItAllAchievement with a valid subject with topics. The
     * topic has 3 questions, of which 2 are answered and one is not. The expected return value is
     * false.
     */
    @Test
    public void test_checkKnowItAllAchievementNotAllAnswered_ShouldReturnFalse() throws Exception {

        List<Topic> topics = new ArrayList<>();
        topics.add(createDummyTopic());
        when(subjectTopicDaoMock.getTopicToSubject(any())).thenReturn(topics);

        Question q1 = createDummyQuestion();
        q1.setQuestionId(1);
        Question q2 = createDummyQuestion();
        q2.setQuestionId(2);
        Question q3 = createDummyQuestion();
        q3.setQuestionId(3);
        List<Question> topicQuestions = new ArrayList<>();
        topicQuestions.add(q1);
        topicQuestions.add(q2);
        topicQuestions.add(q3);
        when(questionTopicDaoJdbcMock.getQuestionToTopic(any())).thenReturn(topicQuestions);

        Exam exam = createDummyExam();
        List<Exam> examList = new ArrayList<>();
        examList.add(exam);
        when(examDaoJdbcMock.getExams()).thenReturn(examList);

        ExerciseExam exerciseExam1 = createDummyExerciseExam();
        exerciseExam1.setExamid(1);
        List<Integer> exerciseExamList = new ArrayList<>();
        exerciseExamList.add(1);
        when(examServiceMock.getAllExerciseExamsOfExam(any())).thenReturn(exerciseExamList);
        when(exerciseExamDaoJdbcMock.getExam(1)).thenReturn(exerciseExam1);

        List<Integer> answeredQuestionsOfExam = new ArrayList<>();
        answeredQuestionsOfExam.add(1);
        answeredQuestionsOfExam.add(2);
        when(exerciseExamServiceMock.getAnsweredQuestionsOfExam(1)).
                thenReturn(answeredQuestionsOfExam);

        assertEquals(false, statisticService.checkKnowItAllAchievement(createDummySubject()));
    }

    /**
     * This test calls the method checkKnowItAllAchievement with a valid subject with topics. The
     * topic has 3 questions, of which 3 are answered. The expected return value is
     * true.
     */
    @Test
    public void test_checkKnowItAllAchievementWithAllAnswered_ShouldReturntrue() throws Exception {

        List<Topic> topics = new ArrayList<>();
        topics.add(createDummyTopic());
        when(subjectTopicDaoMock.getTopicToSubject(any())).thenReturn(topics);

        Question q1 = createDummyQuestion();
        q1.setQuestionId(1);
        Question q2 = createDummyQuestion();
        q2.setQuestionId(2);
        Question q3 = createDummyQuestion();
        q3.setQuestionId(3);
        List<Question> topicQuestions = new ArrayList<>();
        topicQuestions.add(q1);
        topicQuestions.add(q2);
        topicQuestions.add(q3);
        when(questionTopicDaoJdbcMock.getQuestionToTopic(any())).thenReturn(topicQuestions);

        Exam exam = createDummyExam();
        List<Exam> examList = new ArrayList<>();
        examList.add(exam);
        when(examDaoJdbcMock.getExams()).thenReturn(examList);

        ExerciseExam exerciseExam1 = createDummyExerciseExam();
        exerciseExam1.setExamid(1);
        List<Integer> exerciseExamList = new ArrayList<>();
        exerciseExamList.add(1);
        when(examServiceMock.getAllExerciseExamsOfExam(any())).thenReturn(exerciseExamList);
        when(exerciseExamDaoJdbcMock.getExam(1)).thenReturn(exerciseExam1);

        List<Integer> answeredQuestionsOfExam = new ArrayList<>();
        answeredQuestionsOfExam.add(1);
        answeredQuestionsOfExam.add(2);
        answeredQuestionsOfExam.add(3);
        when(exerciseExamServiceMock.getAnsweredQuestionsOfExam(1)).
                thenReturn(answeredQuestionsOfExam);

        assertEquals(true, statisticService.checkKnowItAllAchievement(createDummySubject()));
    }


}