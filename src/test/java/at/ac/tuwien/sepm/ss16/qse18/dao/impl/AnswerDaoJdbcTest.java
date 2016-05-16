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
 * @author Felix Almer & Philipp Ganiu
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

    @Before
    public void setUp() throws Exception {
        when(mockDatabase.getConnection()).thenReturn(mockConnection);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        adao = new AnswerDaoJdbc(mockDatabase);
    }

    @Test (expected = DaoException.class)
    public void test_getAnswer_noDatabaseConnection() throws Exception {
        when(mockDatabase.getConnection()).thenThrow(SQLException.class);
        adao.getAnswer(1);
        PowerMockito.verifyStatic();
        mockDatabase.getConnection();
    }

    @Test
    public void test_getAnswer_notInDatabase() throws Exception {
        assertTrue("Object with id smaller than 0 should return null", adao.getAnswer(-1) == null);
        when(mockResultSet.next()).thenReturn(false);
        assertTrue("Object with this id is not in database", adao.getAnswer(1) == null);
    }

    @Test
    public void test_getAnswer_withValidId() throws Exception {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1).thenReturn(2);
        when(mockResultSet.getInt(2)).thenReturn(2);
        when(mockResultSet.getString(3)).thenReturn("TestAnswer");
        when(mockResultSet.getBoolean(4)).thenReturn(true);

        Answer a = adao.getAnswer(1);
        Answer a2 = adao.getAnswer(2);
        assertTrue("One Answer should be found", a != null);
        assertEquals("AnswerId should be 1", a.getAnswerId(), 1);
        assertEquals("Answer should by of type 2", a.getType().getValue(), 2);
        assertTrue("Answer should be 'TestAnswer'", a.getAnswer().equals("TestAnswer"));
        assertTrue("Answer should be correct", a.isCorrect());
        assertFalse("Answers with different Ids should be different objects", a.equals(a2));
    }

    @Test
    public void test_getAnswers_withEmptyDatabase() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        assertTrue("Database should be empty",adao.getAnswer().isEmpty());
    }

    @Test(expected = DaoException.class)
    public void test_getAnswers_withNoDatabaseConnection_Fail() throws Exception {
        when(mockDatabase.getConnection()).thenThrow(SQLException.class);
        adao.getAnswer();
        PowerMockito.verifyStatic();
        mockDatabase.getConnection();
    }
    @Test
    public void test_getAnswers_FiveElementsInDatabase() throws Exception {
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt(1)).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(4).thenReturn(5);
        when(mockResultSet.getInt(2)).thenReturn(2);
        when(mockResultSet.getString(3)).thenReturn("TestAnswer");
        when(mockResultSet.getBoolean(4)).thenReturn(true);
        when(mockResultSet.getInt(5)).thenReturn(-1);

        List<Answer> answers = adao.getAnswer();
        Answer first = answers.get(0);
        Answer second = answers.get(1);
        Answer third = answers.get(2);
        Answer fourth = answers.get(3);
        Answer fifth = answers.get(4);
        assertTrue("Size of the List should be 5", answers.size() == 5);
        assertFalse("All answers should have a different id", first.equals(second.equals(third.equals(fourth.equals(fifth)))));
    }

    @Test(expected = DaoException.class)
    public void test_createAnswer_withNoDatabaseConnection_Fail() throws Exception {
        when(mockDatabase.getConnection()).thenThrow(SQLException.class);
        adao.createAnswer(new Answer());
        PowerMockito.verifyStatic();
        mockDatabase.getConnection();
    }
    @Test(expected = DaoException.class)
    public void test_createAnswer_withAlreadyExistingId_Fail() throws Exception {
        when(mockPreparedStatement.executeUpdate()).thenThrow(SQLException.class);
        Question q = new Question(1, "", QuestionType.MULTIPLECHOICE, 0L);
        Answer a = new Answer(1, QuestionType.MULTIPLECHOICE,"Testanswer", true, q);
        adao.createAnswer(a);
        PowerMockito.verifyStatic();
        mockDatabase.getConnection();
    }

    @Test(expected = DaoException.class)
    public void test_createAnswer_withNull_Fail() throws Exception {
        adao.createAnswer(null);
    }

    @Test
    public void test_createAnswer_withValidAnswer() throws Exception {
        Question q = new Question(1, "", QuestionType.MULTIPLECHOICE, 0L);
        Answer a = new Answer(-1, QuestionType.MULTIPLECHOICE,"Testanswer", true, q);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        adao.createAnswer(a);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test(expected =  DaoException.class)
    public void test_updateAnswer_withNull_Fail() throws Exception {
        adao.updateAnswer(null);
    }

    @Test(expected = DaoException.class)
    public void test_updateAnswer_withNoDatabaseConnection_Fail() throws Exception {
        when(mockDatabase.getConnection()).thenThrow(SQLException.class);
        adao.updateAnswer(new Answer());
        PowerMockito.verifyStatic();
        mockDatabase.getConnection();
    }


    @Test(expected = DaoException.class)
    public void test_deleteAnswer_withNoDatabaseConnection_Fail() throws Exception {
        when(mockDatabase.getConnection()).thenThrow(SQLException.class);
        adao.deleteAnswer(new Answer());
        PowerMockito.verifyStatic();
        mockDatabase.getConnection();
    }

    @Test
    public void test_deleteAnswer_withValidAnswer() throws Exception {
        Answer a = new Answer(1,QuestionType.MULTIPLECHOICE,"Testanswer", true, new Question());
        adao.deleteAnswer(a);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void test_deleteAnswer_withInvalidAnswer() throws Exception {
        Answer a = new Answer(QuestionType.MULTIPLECHOICE,"Testanswer", true);
        adao.deleteAnswer(a);
        verify(mockPreparedStatement,never()).executeUpdate();
    }

    @After
    public void tearDown() {
        // Nothing to tear down
    }
}
