package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Philipp Ganiu
 */
@RunWith(PowerMockRunner.class) @PrepareForTest(ConnectionH2.class)
@PowerMockIgnore("javax.management.*") public class SubjectTopicDaoJdbcTest {
    private SubjectTopicDao dao;
    @Mock
    private ConnectionH2 mockDatabase;
    @Mock
    private Connection mockConnection;
    @Mock
    private Statement mockStatement;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    @Before public void setUp() throws Exception {
        when(mockDatabase.getConnection()).thenReturn(mockConnection);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        dao = new SubjectTopicDaoJdbc(mockDatabase);
    }


    @Test(expected = DaoException.class)
    public void test_createSubjectTopic_withNoDataBaseConnection_Fail() throws Exception{
        when(mockDatabase.getConnection()).thenThrow(SQLException.class);
        dao.createSubjectTopic(new Subject(),new Topic());
        PowerMockito.verifyStatic();
        mockDatabase.getConnection();
    }


    @Test (expected = DaoException.class)
    public void test_createSubjectTopic_withNull_Fail() throws Exception{
        dao.createSubjectTopic(null,null);
    }

    @Test
    public void test_createSubjectTopic_withValidTopic() throws Exception{
        dao.createSubjectTopic(new Subject(),new Topic());
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test (expected = DaoException.class)
    public void test_deleteSubectTopic_withNull_Fail() throws Exception{
        dao.deleteSubjectTopic(null);
    }

    @Test (expected = DaoException.class)
    public void test_deleteSubjectTopic_withNotExistingTopic_Fail() throws Exception{
        when(mockPreparedStatement.executeUpdate()).thenThrow(SQLException.class);
        dao.deleteSubjectTopic(new Topic());
        PowerMockito.verifyStatic();
        mockDatabase.getConnection();
    }

    @Test (expected = DaoException.class)
    public void test_deleteSubjectTopic_withNoDataBaseConnection_Fail() throws Exception{
        when(mockDatabase.getConnection()).thenThrow(SQLException.class);
        dao.deleteSubjectTopic(new Topic(1,"TestTopic"));
        PowerMockito.verifyStatic();
        mockDatabase.getConnection();
    }

    @Test
    public void test_deleteSubjectTopic_withValidTopic() throws Exception{
        dao.deleteSubjectTopic(new Topic(1,"TestTopic"));
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test (expected = DaoException.class)
    public void test_getTopicToSubject_withNoDataBaseConnection_Fail() throws Exception{
        when(mockDatabase.getConnection()).thenThrow(SQLException.class);
        dao.getTopicToSubject(new Subject());
        PowerMockito.verifyStatic();
        mockDatabase.getConnection();
    }

    @Test
    public void test_getTopicToSubject_withEmptyDatabase() throws Exception{
        when(mockResultSet.next()).thenReturn(false);
        List<Topic> topics = dao.getTopicToSubject(new Subject());
        assertTrue("Topics should be empty",topics.isEmpty());
    }

    @Test
    public void test_getTopicToSubject_withHundredElementsInDatabase() throws Exception {
        when(mockResultSet.next()).thenAnswer(new Answer<Boolean>() {
            private int count = 0;

            @Override
            public Boolean answer(InvocationOnMock invocationOnMock) throws Throwable {
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








    @After public void tearDown() throws Exception {

    }

}
