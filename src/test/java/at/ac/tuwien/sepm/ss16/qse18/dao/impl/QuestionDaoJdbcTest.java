package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoBaseTest;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.powermock.api.mockito.PowerMockito;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Class QuestionDaoJdbcTest
 * Tests for the JDBC implementation in QuestionDaoJdbc. In order to be isolated while testing, this
 * test class uses mocks primarily to bypass the database connection procedure.
 *
 * @author Felix Almer
 */
public class QuestionDaoJdbcTest extends DaoBaseTest {

    private QuestionDaoJdbc qdao;
    @Mock private QuestionTopicDao questionTopicDao;
    private Question testQuestion;

    @Before public void setUp() throws Exception {
        super.setUp();

        testQuestion = DummyEntityFactory.createDummyQuestion();

        qdao = new QuestionDaoJdbc(mockConnectionH2);
        qdao.setQuestionTopicDao(questionTopicDao);
    }

    //Testing getQuestion(int)
    //----------------------------------------------------------------------------------------------
    @Test(expected = DaoException.class) public void test_getQuestion_noDatabaseConnection()
        throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        qdao.getQuestion(1);
        verify(mockConnectionH2).getConnection();
    }

    @Test public void test_getQuestion_withInvalidQuestion() throws Exception {
        assertTrue("Object with id smaller than 0 should return null",
            qdao.getQuestion(-1) == null);
        when(mockResultSet.next()).thenReturn(false);
        assertTrue("Object with this id is not in database", qdao.getQuestion(1) == null);
    }

    @Test public void test_getQuestion_noEntryInDatabase_Fail() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        assertEquals("If there is no entry with the given ID, the method should return null",
            qdao.getQuestion(1), null);

    }

    @Test public void test_getQuestion_withValidId() throws Exception {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1).thenReturn(2);
        when(mockResultSet.getString(2)).thenReturn("TestQuestion");
        when(mockResultSet.getInt(3)).thenReturn(2);

        Question a = qdao.getQuestion(1);
        Question b = qdao.getQuestion(2);
        assertTrue("Question should be retrieved", a != null);
        assertTrue("QuestionID should be 1", a.getQuestionId() == 1);
        assertTrue("Question should be of type 2", a.getType().getValue() == 2);
        assertTrue("Question content should be TestQuestion",
            a.getQuestion().equals("TestQuestion"));
        assertFalse("Question with different ids should not be equal", a.equals(b));
    }
    //----------------------------------------------------------------------------------------------

    //Testing getQuestions
    //----------------------------------------------------------------------------------------------
    @Test(expected = DaoException.class) public void test_getQuestions_withNoDatabase_Fail()
        throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        qdao.getQuestions();
        verify(mockConnectionH2).getConnection();
    }

    @Test public void test_getQuestions_emptyDatabase() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        assertTrue("Database should return empty list", qdao.getQuestions().isEmpty());
    }

    @Test public void test_getQuestions_fiveElementsInDatabase() throws Exception {
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true)
            .thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt(1)).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4)
            .thenReturn(5);
        when(mockResultSet.getString(2)).thenReturn("TestQuestion");
        when(mockResultSet.getInt(3)).thenReturn(2);

        List<Question> questions = qdao.getQuestions();
        Question first = questions.get(0);
        Question second = questions.get(1);
        Question third = questions.get(2);
        Question fourth = questions.get(3);
        Question fifth = questions.get(4);
        assertTrue("Size of the List should be 5", questions.size() == 5);
        assertFalse("All answers should have a different id",
            first.equals(second.equals(third.equals(fourth.equals(fifth)))));
    }
    //----------------------------------------------------------------------------------------------

    //Testing createQuestion(Question)
    //----------------------------------------------------------------------------------------------
    @Test(expected = DaoException.class) public void test_createQuestion_noDatabaseConnection_fail()
        throws Exception {
        when(mockPreparedStatement.executeUpdate()).thenThrow(SQLException.class);
        qdao.createQuestion(new Question(-1, "", QuestionType.MULTIPLECHOICE, 0L), new Topic());
        PowerMockito.verifyStatic();
        mockPreparedStatement.executeUpdate();
    }

    @Test(expected = DaoException.class)
    public void test_createQuestion_withAlreadyExistingId_fail() throws Exception {
        qdao.createQuestion(new Question(1, "", QuestionType.MULTIPLECHOICE, 0L), new Topic());
    }

    @Test(expected = DaoException.class) public void test_createAnswer_null_fail()
        throws Exception {
        qdao.createQuestion(null, null);
    }

    @Test public void test_createQuestion_validQuestion() throws Exception {

        when(mockConnectionH2.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString(), anyInt()))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);
        Question input = new Question("TestQuestion", QuestionType.MULTIPLECHOICE, 0L);
        Question q = qdao.createQuestion(input, new Topic(2, "abc"));
        assertTrue("Question should have received primary key", q.getQuestionId() == 1);
    }
    //----------------------------------------------------------------------------------------------

    //Testing updateQuestion(Question)
    //----------------------------------------------------------------------------------------------
    @Test(expected = DaoException.class) public void test_updateQuestion_noDatabaseConnection_fail()
        throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        qdao.updateQuestion(new Question(1, "", QuestionType.MULTIPLECHOICE, 0L), new Topic());
    }

    @Test(expected = DaoException.class) public void test_updateQuestion_invalidId_fail()
        throws Exception {
        qdao.updateQuestion(new Question(-1, "", QuestionType.MULTIPLECHOICE, 0L), new Topic());
    }

    @Test(expected = DaoException.class) public void test_updateQuestion_null_fail()
        throws Exception {
        qdao.updateQuestion(null, null);
    }
    //----------------------------------------------------------------------------------------------

    //Testing deleteQuestion(Question)
    //----------------------------------------------------------------------------------------------
    @Test(expected = DaoException.class) public void test_deleteQuestion_noDatabaseConnection_fail()
        throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        qdao.deleteQuestion(new Question(1, "", QuestionType.MULTIPLECHOICE, 0L));
    }

    @Test public void test_deleteQuestion_valid() throws Exception {
        qdao.deleteQuestion(new Question(1, "", QuestionType.MULTIPLECHOICE, 0L));
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test(expected = DaoException.class) public void test_deleteQuestion_invalid()
        throws Exception {
        qdao.deleteQuestion(new Question("", QuestionType.MULTIPLECHOICE, 0L));
    }
    //----------------------------------------------------------------------------------------------

    // Testing getRelatedAnswers(Question)
    //----------------------------------------------------------------------------------------------
    @Test(expected = DaoException.class)
    public void test_getRelatedAnswers_withInvalidQuestion_Fail() throws Exception {
        qdao.getRelatedAnswers(new Question());
    }

    @Test(expected = DaoException.class)
    public void test_getRelatedAnswers_withNoDatabaseConnection_Fail() throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);

        qdao.getRelatedAnswers(testQuestion);

        verify(mockConnectionH2.getConnection());
    }

    @Test public void test_getRelatedAnswer_withValidQuestion() throws Exception {
        Answer testAnswer = DummyEntityFactory.createDummyAnswer();

        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true)
            .thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt(anyInt())).thenReturn(testAnswer.getAnswerId())
            .thenReturn(testAnswer.getType().getValue()).thenReturn(testQuestion.getQuestionId())
            .thenReturn(testQuestion.getQuestionId()).thenReturn(testQuestion.getType().getValue())
            .thenReturn(2).thenReturn(2).thenReturn(testQuestion.getQuestionId())
            .thenReturn(testQuestion.getQuestionId()).thenReturn(testQuestion.getType().getValue());
        when(mockResultSet.getString(anyInt())).thenReturn(testAnswer.getAnswer())
            .thenReturn(testQuestion.getQuestion()).thenReturn("Answer2")
            .thenReturn(testQuestion.getQuestion());
        when(mockResultSet.getBoolean(anyInt())).thenReturn(true).thenReturn(true);
        when(mockResultSet.getLong(anyInt())).thenReturn(testQuestion.getQuestionTime());

        List<Answer> answers = qdao.getRelatedAnswers(testQuestion);

        assertEquals("There must be 2 answers for this question", 2, answers.size());
        assertEquals("The first answer should be the testanswer", answers.get(0).getAnswer(),
            testAnswer.getAnswer());
        assertEquals("The second answer should have the ID 2", answers.get(1).getAnswerId(), 2);
    }
    //----------------------------------------------------------------------------------------------

    @After public void tearDown() {
        // Nothing to tear down
    }
}
