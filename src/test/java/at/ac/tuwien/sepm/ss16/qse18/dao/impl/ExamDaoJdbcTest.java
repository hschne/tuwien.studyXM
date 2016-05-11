package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
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
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Zhang Haixiang
 */
    @RunWith(PowerMockRunner.class) @PrepareForTest(ConnectionH2.class) @PowerMockIgnore("javax.management.*")
    public class ExamDaoJdbcTest {

    private ExamDaoJdbc examDaoJdbc;
    @Mock private ConnectionH2 mockConnectionH2;
    @Mock private Connection mockConnection;
    @Mock private Statement mockStatement;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet mockResultSet;
    private Exam test;

    @Before public void setUp() throws Exception {
        when(mockConnectionH2.getConnection()).thenReturn(mockConnection).thenReturn(mockConnection);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement).thenReturn(mockPreparedStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet).thenReturn(mockResultSet);
        when(mockConnection.createStatement()).thenReturn(mockStatement).thenReturn(mockStatement);

        this.examDaoJdbc = new ExamDaoJdbc(mockConnectionH2);

        ArrayList<Question> al = new ArrayList<Question>(){};
        Question question = new Question();
        question.setQuestion("TestQuestion");
        question.setQuestionid(1);
        question.setType(1);
        al.add(question);


        test = new Exam();
        test.setExamid(1);
        test.setCreated(new Timestamp(798));
        test.setPassed(false);
        test.setAuthor("Test1");
        test.setExamQuestions(al);
    }

    @Test public void test_createWith_validParameters_should_persist() throws Exception{
        Exam exam;

        exam = this.examDaoJdbc.create(this.test);

        verify(mockPreparedStatement, times(2)).executeUpdate();
        assertSame("Exam Objects should be the same", exam, this.test);
    }

    @Test(expected = DaoException.class) public void test_createWithoutDatabaseConnection_should_fail() throws Exception{
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        this.examDaoJdbc.create(this.test);

        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }

    @Test(expected = DaoException.class) public void test_createWith_Null_shoud_fail() throws Exception{
        this.examDaoJdbc.create(null);
    }

    @Test(expected = DaoException.class) public void test_createExamWithAlreadyExistingID_should_fail()
        throws Exception{
        Exam temp;

        temp = this.examDaoJdbc.create(this.test);
        verify(mockPreparedStatement, times(2)).executeUpdate();
        assertSame("Exam Objects should be the same", this.test, temp);

        when(mockPreparedStatement.executeUpdate()).thenThrow(DaoException.class);
        this.examDaoJdbc.create(temp);
    }

    @After public void tearDown() throws Exception {

    }

}
