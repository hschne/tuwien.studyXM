package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoBaseTest;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Class SubjectTopicDaoJdbcTest
 * Tests for the JDBC implementation in SubjectTopicDaoJdbc. In order to be isolated while testing, this
 * test class uses mocks primarily to bypass the database connection procedure.
 *
 * @author Philipp Ganiu
 */
public class SubjectTopicDaoJdbcTest extends DaoBaseTest {
    private SubjectTopicDao dao;


    @Before public void setUp() throws Exception {
        super.setUp();
        dao = new SubjectTopicDaoJdbc(mockConnectionH2);
    }


    //Testing createSubjectTopic(Subject, Topic)
    //----------------------------------------------------------------------------------------------
    @Test(expected = DaoException.class)
    public void test_createSubjectTopic_withNoDataBaseConnection_Fail() throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        dao.createSubjectTopic(new Subject(), new Topic());
        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }


    @Test(expected = DaoException.class) public void test_createSubjectTopic_withNull_Fail()
        throws Exception {
        dao.createSubjectTopic(null, null);
    }

    @Test public void test_createSubjectTopic_withValidTopic() throws Exception {
        dao.createSubjectTopic(new Subject(), new Topic());
        verify(mockPreparedStatement).executeUpdate();
    }
    //----------------------------------------------------------------------------------------------

    //Testing deleteSubjectTopic(Subject)
    //----------------------------------------------------------------------------------------------
    @Test(expected = DaoException.class) public void test_deleteSubectTopic_withNull_Fail()
        throws Exception {
        dao.deleteSubjectTopic(null);
    }

    @Test(expected = DaoException.class)
    public void test_deleteSubjectTopic_withNotExistingTopic_Fail() throws Exception {
        when(mockPreparedStatement.executeUpdate()).thenThrow(SQLException.class);
        dao.deleteSubjectTopic(new Topic());
        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }

    @Test(expected = DaoException.class)
    public void test_deleteSubjectTopic_withNoDataBaseConnection_Fail() throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        dao.deleteSubjectTopic(new Topic(1, "TestTopic"));
        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }

    @Test public void test_deleteSubjectTopic_withValidTopic() throws Exception {
        dao.deleteSubjectTopic(new Topic(1, "TestTopic"));
        verify(mockPreparedStatement).executeUpdate();
    }
    //----------------------------------------------------------------------------------------------


    //Testing getTopicToSubject(Subject)
    //----------------------------------------------------------------------------------------------
    @Test(expected = DaoException.class)
    public void test_getTopicToSubject_withNoDataBaseConnection_Fail() throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        dao.getTopicToSubject(new Subject());
        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }

    @Test public void test_getTopicToSubject_withEmptyDatabase() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        List<Topic> topics = dao.getTopicToSubject(new Subject());
        assertTrue("Topics should be empty", topics.isEmpty());
    }

    @Test public void test_getTopicToSubject_withHundredElementsInDatabase() throws Exception {
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

        List<Topic> topics = dao.getTopicToSubject(new Subject());
        assertEquals("The size of the resulting list should be 100", topics.size(), 100);
    }
    //----------------------------------------------------------------------------------------------

    @After public void tearDown() throws Exception {

    }

}
