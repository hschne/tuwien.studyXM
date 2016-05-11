package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Zhang Haixiang
 */
    @RunWith(MockitoJUnitRunner.class) public class ExamDaoJdbcTest {
    private ExamDaoJdbc examDaoJdbc;
    @Mock private ConnectionH2 mockConnectionH2;
    @Mock private Connection mockConnection;
    @Mock private Statement mockStatement;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet mockResultSet;

    @Before public void setUp() throws Exception {
        when(mockConnectionH2.getConnection()).thenReturn(mockConnection);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        examDaoJdbc = new ExamDaoJdbc(mockConnectionH2);
    }

    @Test public void sljfljsfshould() throws Exception{
        Exam exam = new Exam();
        exam.setExamid(1);
        exam.setCreated(new Timestamp(798));
        exam.setPassed(false);
        exam.setAuthor("Test1");

        examDaoJdbc.create(exam);

        verify(mockPreparedStatement).executeUpdate();
    }

    @After public void tearDown() throws Exception {

    }

}
