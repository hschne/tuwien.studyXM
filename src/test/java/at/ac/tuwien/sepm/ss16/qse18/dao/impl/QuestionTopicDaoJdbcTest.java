package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Philipp Ganiu
 */
@RunWith(PowerMockRunner.class) @PrepareForTest(ConnectionH2.class)
@PowerMockIgnore("javax.management.*") public class QuestionTopicDaoJdbcTest {
    private QuestionTopicDao dao;
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
        dao = new QuestionTopicDaoJdbc(mockDatabase);
    }

    @Test(expected = DaoException.class)
    public void test_getQuestionToTopic_withNoDataBaseConnection_Fail() throws Exception{
        when(mockDatabase.getConnection()).thenThrow(SQLException.class);
        dao.getQuestionToTopic(new Topic());
        PowerMockito.verifyStatic();
        mockDatabase.getConnection();
    }

    @Test (expected = DaoException.class)
    public void test_getQuestionToTopic_withNull_Fail() throws Exception{
        dao.getQuestionToTopic(null);
    }

    @Test
    public void test_getQuestionToTopic_withValidTopid() throws Exception {
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt(1)).thenReturn(1);
        when(mockResultSet.getString(2)).thenReturn("TestQuestion");
        when(mockResultSet.getInt(3)).thenReturn(2);
        when(mockResultSet.getLong(4)).thenReturn(5000L);


        List<Question> questions = dao.getQuestionToTopic(new Topic());
        assertTrue("There should be one quesiton in the list", questions.size() == 1);
        assertTrue("QuestionID should be 1", questions.get(0).getQuestionId() == 1);
        assertTrue("Question should be of type 2", questions.get(0).getType().getValue() == 2);
        assertTrue("Question content should be TestQuestion",
            questions.get(0).getQuestion().equals("TestQuestion"));
    }


    @After public void tearDown() throws Exception {

    }

}
