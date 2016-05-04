package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SubjectDaoJdbcTest {

    private SubjectDaoJdbc sdao;
    @Mock private ConnectionH2 mockDatabase;
    @Mock private Connection mockConnection;
    @Mock private Statement mockStatement;
    @Mock private ResultSet mockResultSet;

    @Before public void setUp() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockDatabase.getConnection()).thenReturn(mockConnection);
        sdao = new SubjectDaoJdbc(mockDatabase);
    }

    @Test(expected = DaoException.class) public void testGetsubjectWithNegativeIdFail() throws Exception {
        when(mockDatabase.getConnection()).thenThrow(SQLException.class);
        sdao.getSubject(-1);
    }

    @After public void tearDown() throws Exception {

    }
}
