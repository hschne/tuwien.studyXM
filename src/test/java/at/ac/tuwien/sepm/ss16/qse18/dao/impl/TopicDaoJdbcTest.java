package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoBaseTest;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Class TopicDaoJdbcTest
 * Tests for the JDBC implementation in TopicDaoJdbc. In order to be isolated while testing, this
 * test class uses mocks primarily to bypass the database connection procedure.
 *
 * @author Philipp Ganiu
 */
public class TopicDaoJdbcTest extends DaoBaseTest {
    private TopicDaoJdbc dao;

    @Mock private SubjectTopicDaoJdbc mockSubjectTopicDao;
    @Mock private QuestionTopicDaoJdbc mockTopicQuestionDao;

    @Before public void setUp() throws Exception {
        super.setUp();
        dao = new TopicDaoJdbc(mockConnectionH2);
        dao.setSubjectTopicDaoJdbc(mockSubjectTopicDao);
        dao.setQuestionTopicDao(mockTopicQuestionDao);
    }

    //Testing getTopic(int)
    //----------------------------------------------------------------------------------------------
    @Test(expected = DaoException.class) public void test_getTopic_withNoDataBaseConnection_Fail()
        throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        dao.getTopic(1);
        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }

    @Test public void test_getTopic_withValidId() throws Exception {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString(anyString())).thenReturn("TestTopic");

        Topic t = dao.getTopic(1);
        Topic t2 = dao.getTopic(2);

        assertEquals("Found topic should be 'TestTopic'", t.getTopic(), "TestTopic");
        assertTrue("Method should return topicID that is given in input parameter",
            t.getTopicId() == 1);
        assertTrue("Method should return topicID that is given in input parameter",
            t2.getTopicId() == 2);
        assertTrue("Both Topics have the same name", t.getTopic().equals(t2.getTopic()));
    }
    //----------------------------------------------------------------------------------------------

    //Testing getTopics
    //----------------------------------------------------------------------------------------------
    @Test(expected = DaoException.class) public void test_getTopics_withNoDataBaseConnection_Fail()
        throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        dao.getTopics();
        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }

    @Test public void test_getTopics_withEmptyDatabase() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        List<Topic> topics = dao.getTopics();
        assertTrue("Topics should be empty", topics.isEmpty());
    }

    @Test public void test_getTopics_withHundredElementsInDatabase() throws Exception {
        when(mockResultSet.next()).thenAnswer(new Answer<Boolean>() {
            private int count = 0;

            @Override public Boolean answer(InvocationOnMock invocationOnMock) throws Throwable {
                if (count < 100) {
                    count++;
                    return true;
                } else
                    return false;
            }
        });

        List<Topic> topics = dao.getTopics();
        assertEquals("The size of the resulting list should be 100", topics.size(), 100);
    }
    //----------------------------------------------------------------------------------------------

    //Testing createTopic(Topic)
    //----------------------------------------------------------------------------------------------
    @Test(expected = DaoException.class)
    public void test_createTopic_withNoDataBaseConnection_Fail() throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        dao.createTopic(new Topic(), new Subject());
        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }

    @Test(expected = DaoException.class) public void test_createTopic_withAlreadyExistingId_Fail()
        throws Exception {
        when(mockConnection.prepareStatement(anyString(), anyInt()))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(SQLException.class);
        dao.createTopic(new Topic(1, "TestTopic"), new Subject());
        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }

    @Test(expected = DaoException.class) public void test_createTopic_withNull_Fail()
        throws Exception {
        dao.createTopic(null, new Subject());
    }

    @Test public void test_createTopic_withValidTopic() throws Exception {
        when(mockConnection.prepareStatement(anyString(), anyInt()))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(anyInt())).thenReturn(1);
        when(mockResultSet.getString(anyString())).thenReturn("TestTopic");

        Topic t = dao.createTopic(new Topic(1, "TestTopic"), new Subject());
        assertTrue("Created topic should be returned with name 'TestTopic'",
            t.getTopic().equals("TestTopic"));
        verify(mockPreparedStatement, times(1)).executeUpdate();


    }
    //----------------------------------------------------------------------------------------------

    //Testing deleteTopic(Topic)
    //----------------------------------------------------------------------------------------------
    @Test(expected = DaoException.class) public void test_deleteTopic_withNull_Fail()
        throws Exception {
        dao.deleteTopic(null);
    }

    @Test(expected = DaoException.class) public void test_deleteTopic_withNotExistingTopic_Fail()
        throws Exception {
        when(mockPreparedStatement.executeUpdate()).thenThrow(SQLException.class);
        dao.deleteTopic(new Topic());
        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }

    @Test(expected = DaoException.class)
    public void test_deleteTopic_withNoDataBaseConnection_Fail() throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        dao.deleteTopic(new Topic(1, "TestTopic"));
        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }

    @Test public void test_deleteTopic_withValidTopic() throws Exception {
        dao.deleteTopic(new Topic(1, "TestTopic"));
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }
    //----------------------------------------------------------------------------------------------

    //Testing updateTopic(Topic)
    //----------------------------------------------------------------------------------------------
    @Test(expected = DaoException.class) public void test_updateTopic_withNull_Fail()
        throws Exception {
        dao.updateTopic(null);
    }

    @Test(expected = DaoException.class)
    public void test_updateTopic_withNoDataBaseConnection_Fail() throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        dao.updateTopic(new Topic(1, "TestTopic"));
        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }

    @Test public void test_updateTopic_withValidTopic() throws Exception {
        dao.updateTopic(new Topic(1, "TestTopic"));
        verify(mockPreparedStatement).executeUpdate();
    }
    //----------------------------------------------------------------------------------------------

    @After public void tearDown() throws Exception {
        // nothing to tear down
    }

}
