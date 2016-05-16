package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import org.junit.runner.RunWith;
import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.DataBaseConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Felix Almer
 */
@RunWith(PowerMockRunner.class) @PrepareForTest(ConnectionH2.class)
@PowerMockIgnore("javax.management.*") public class QuestionDaoJdbcTest {

    private QuestionDaoJdbc qdao;
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

    @Before
    public void setUp() throws Exception {
        when(mockDatabase.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        qdao = new QuestionDaoJdbc(mockDatabase);
    }

    @Test (expected = DaoException.class)
    public void test_getQuestion_noDatabaseConnection() throws Exception {
        when(mockDatabase.getConnection()).thenThrow(SQLException.class);
        qdao.getQuestion(1);
        PowerMockito.verifyStatic();
        mockDatabase.getConnection();
    }

    @Test
    public void test_getAnswer_notInDatabase() throws Exception {
        assertTrue("Object with id smaller than 0 should return null",
            qdao.getQuestion(-1) == null);
        when(mockResultSet.next()).thenReturn(false);
        assertTrue("Object with this id is not in database", qdao.getQuestion(1) == null);
    }

    @Test
    public void test_getQuestion_withValidId() throws Exception {
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

    @Test
    public void test_getAllQuestions_emptyDatabase() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        assertTrue("Database should return empty list", qdao.getQuestions().isEmpty());
    }

    @Test
    public void test_getAllQuestions_fiveElementsInDatabase() throws Exception {
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true)
            .thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt(1)).thenReturn(1).thenReturn(2).thenReturn(3)
            .thenReturn(4).thenReturn(5);
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
/*
    @Test (expected = DaoException.class)
    public void test_createQuestion_noDatabaseConnection_fail() throws Exception {
        when(mockPreparedStatement.executeUpdate()).thenThrow(SQLException.class);
        qdao.createQuestion(new Question(-1, "", QuestionType.MULTIPLECHOICE));
        PowerMockito.verifyStatic();
        mockPreparedStatement.executeUpdate();
    }

    @Test (expected = DaoException.class)
    public void test_createQuestion_withAlreadyExistingId_fail() throws Exception {
        qdao.createQuestion(new Question(1, "", QuestionType.MULTIPLECHOICE));
    }

    @Test (expected = DaoException.class)
    public void test_createAnswer_null_fail() throws Exception {
        qdao.createQuestion(null);
    }

    @Test
    public void test_createQuestion_validQuestion() throws Exception {
        when(mockDatabase.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString(), anyInt()))
            .thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);
        Question input = new Question("TestQuestion", QuestionType.MULTIPLECHOICE);
        Question q = qdao.createQuestion(input);
        assertTrue("Question should have received primary key", q.getQuestionId() == 1);
    }

    @Test (expected = DaoException.class)
    public void test_updateQuestion_noDatabaseConnection_fail() throws Exception {
        when(mockDatabase.getConnection()).thenThrow(SQLException.class);
        qdao.updateQuestion(new Question(1, "", QuestionType.MULTIPLECHOICE));
    }

    @Test (expected = DaoException.class)
    public void test_updateQuestion_invalidId_fail() throws Exception {
        qdao.updateQuestion(new Question(-1, "", QuestionType.MULTIPLECHOICE));
    }

    @Test (expected = DaoException.class)
    public void test_updateQuestion_null_fail() throws Exception {
        qdao.updateQuestion(null);
    }

    @Test (expected = DaoException.class)
    public void test_deleteQuestion_noDatabaseConnection_fail() throws Exception {
        when(mockDatabase.getConnection()).thenThrow(SQLException.class);
        qdao.deleteQuestion(new Question(1, "", QuestionType.MULTIPLECHOICE));
    }

    @Test
    public void test_deleteQuestion_valid() throws Exception {
        qdao.deleteQuestion(new Question(1, "", QuestionType.MULTIPLECHOICE));
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test (expected = DaoException.class)
    public void test_deleteQuestion_invalid() throws Exception {
        qdao.deleteQuestion(new Question("", QuestionType.MULTIPLECHOICE));
    }

    @After
    public void tearDown() {
        // Nothing to tear down
    }
*/
}
