package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoBaseTest;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.sql.SQLException;
import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummyQuestion;
import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummyTopic;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Class QuestionTopicDaoJdbcTest
 * Tests for the JDBC implementation in QuestonTopicDaoJdbc. In order to be isolated while testing, this
 * test class uses mocks primarily to bypass the database connection procedure.
 *
 * @author Philipp Ganiu, Bicer Cem, Hans-Jörg Schrödl
 */
public class QuestionTopicDaoJdbcTest extends DaoBaseTest {
    private QuestionTopicDao dao;

    @Before public void setUp() throws Exception {
        super.setUp();
        dao = new QuestionTopicDaoJdbc(mockConnectionH2);
    }


    @Test(expected = DaoException.class)
    public void test_getQuestionToTopic_withNoDataBaseConnection_Fail() throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        dao.getQuestionToTopic(new Topic());
        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }

    @Test(expected = DaoException.class) public void test_getQuestionToTopic_withNull_Fail()
        throws Exception {
        dao.getQuestionToTopic(null);
    }

    @Test public void test_getQuestionToTopic_withValidTopic() throws Exception {
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt(1)).thenReturn(1);
        when(mockResultSet.getString(2)).thenReturn("TestQuestion");
        when(mockResultSet.getInt(3)).thenReturn(2);
        when(mockResultSet.getLong(4)).thenReturn(5000L);


        List<Question> questions = dao.getQuestionToTopic(createDummyTopic());
        assertTrue("There should be one question in the list", questions.size() == 1);
        assertTrue("QuestionID should be 1", questions.get(0).getQuestionId() == 1);
        assertTrue("Question should be of type 2", questions.get(0).getType().getValue() == 2);
        assertTrue("Question content should be TestQuestion",
            questions.get(0).getQuestion().equals("TestQuestion"));
    }

    @Test(expected = DaoException.class) public void test_getTopicsFromQuestion_withNull_Fail()
        throws Exception {
        dao.getTopicsFromQuestion(null);
    }

    @Test(expected = DaoException.class)
    public void test_getTopicsFromQuestion_withNoDatabaseConnection_Fail() throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);

        dao.getTopicsFromQuestion(new Question());

        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }

    @Test public void test_getTopicsFromQuestion_withValidQuestion() throws Exception {
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt(anyInt())).thenReturn(1).thenReturn(2);
        when(mockResultSet.getString(anyInt())).thenReturn("TestTopic1").thenReturn("TestTopic2");

        List<Topic> topics = dao.getTopicsFromQuestion(new Question());
        assertEquals("There should be 2 topics in the list", topics.size(), 2);
        assertEquals("The first topic's topicId should be 1", topics.get(0).getTopicId(), 1);
        assertEquals("The second topic's topicId should be 2", topics.get(1).getTopicId(), 2);
        assertEquals("The first topic's name should be \"TestTopic1\"", topics.get(0).getTopic(),
            "TestTopic1");
        assertEquals("The second topic's name should be \"TestTopic2\"", topics.get(1).getTopic(),
            "TestTopic2");
    }

    @Test public void test_removeTopic_withValidTopic() throws Exception {
        Topic topic = createDummyTopic();

        dao.removeTopic(topic);

        verify(mockPreparedStatement).setInt(1, topic.getTopicId());
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test(expected = DaoException.class) public void test_removeTopic_errorInDatabase() throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(new SQLException());
        Topic topic = createDummyTopic();

        dao.removeTopic(topic);
    }

    @Test public void test_removeQuestion_withValidQuestion() throws Exception{
        Question question = createDummyQuestion();

        dao.removeQuestion(question);

        verify(mockPreparedStatement).setInt(1, question.getQuestionId());
        verify(mockPreparedStatement).executeUpdate();
    }


    @Test(expected = DaoException.class) public void test_removeQuestion_errorInDatabase() throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(new SQLException());
        Question question = createDummyQuestion();

        dao.removeQuestion(question);
    }

    @Test public void test_createQuestionInTopic_addValidQuestionToTopic() throws Exception{
        Question question = createDummyQuestion();
        Topic topic = createDummyTopic();

        dao.createQuestionInTopic(question, topic);

        verify(mockPreparedStatement).setInt(1, question.getQuestionId());
        verify(mockPreparedStatement).setInt(2, topic.getTopicId());
        verify(mockPreparedStatement).executeUpdate();
    }



}
