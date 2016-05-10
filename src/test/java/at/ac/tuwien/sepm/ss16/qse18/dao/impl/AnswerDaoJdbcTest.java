package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

<<<<<<< 5370
/**
 * Created by Felix on 08.05.2016.
 */
public class AnswerDaoJdbcTest {
=======
import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.DataBaseConnection;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Felix on 08.05.2016.
 */
@RunWith(PowerMockRunner.class) @PrepareForTest(ConnectionH2.class)
@PowerMockIgnore("javax.management.*") public class AnswerDaoJdbcTest {

    private AnswerDaoJdbc adao;
    @Mock
    private DataBaseConnection mockDatabase;
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private Statement mockStatement;
    @Mock
    private ResultSet mockResultSet;
    @Mock
    private Answer mockAnswer;



    @Before
    public void setUp() throws Exception {
        when(mockDatabase.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        adao = new AnswerDaoJdbc(mockDatabase);
    }

    @Test (expected = DaoException.class)
    public void test_getSingleAnswer_noDatabaseConnection() throws Exception {
        when(mockDatabase.getConnection()).thenThrow(SQLException.class);

        adao.getAnswer(1);
        PowerMockito.verifyStatic();
        mockDatabase.getConnection();
    }

    @Test
    public void test_getSingleAnswer_notInDatabase() throws Exception {
        assertTrue("Object with id smaller than 0 should return null", adao.getAnswer(-1) == null);
    }

    @Test
    public void test_getSingleAnswer_inDatabase() throws Exception{
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);
        when(mockResultSet.getInt(2)).thenReturn(2);
        when(mockResultSet.getString(3)).thenReturn("TestAnswer");
        when(mockResultSet.getBoolean(4)).thenReturn(true);

        //TODO WHY?
        //Answer a = adao.getAnswer(1);
        assertTrue("One Answer should be found", mockAnswer != null);
        assertTrue("Answer should be consistent to the database entry", false);
    }

    @Test


    @After
    public void tearDown() {
        // Nothing to tear down
    }

>>>>>>> local
}
