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
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Zhang Haixiang
 */
@RunWith(PowerMockRunner.class) @PrepareForTest(ConnectionH2.class) @PowerMockIgnore("javax.management.*")
public class SubjectQuestionDaoJdbcTest {
    @Mock private ConnectionH2 mockConnectionH2;
    @Mock private Connection mockConnection;
    @Mock private Statement mockStatement;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet mockResultSet;
    private SubjectQuestionDaoJdbc subjectQuestionDaoJdbc;
    private Exam testExam;


    @Before public void setUp() throws Exception {
        when(mockConnectionH2.getConnection()).thenReturn(mockConnection);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        ArrayList<Question> al = new ArrayList<Question>(){};
        Question question = new Question();
        question.setQuestion("TestQuestion");
        question.setQuestionid(1);
        question.setType(1);
        al.add(question);

        testExam = new Exam();
        testExam.setExamid(1);
        testExam.setCreated(new Timestamp(798));
        testExam.setPassed(false);
        testExam.setAuthor("Test1");
        testExam.setSubjectID(1);
        testExam.setExamQuestions(al);

        this.subjectQuestionDaoJdbc = new SubjectQuestionDaoJdbc(this.mockConnectionH2);
    }

    @Test public void test_getAllQuestionsOfSubjectWith2Elements_should_persist() throws Exception{
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt(anyString())).thenReturn(1).thenReturn(2);

        List<Integer> questionIDList = new ArrayList<>();

        questionIDList = this.subjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.testExam, 1);
        assertTrue("List should have size 2", questionIDList.size() == 2);
        assertTrue("First Element should be 1", questionIDList.get(0) == 1);
        assertTrue("Second Element should be 2", questionIDList.get(1) == 2);
    }

    @Test(expected = DaoException.class) public void test_getAllQuestionsOfSubjectWithInvalidTopicID_should_fail()
        throws Exception{
        this.subjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.testExam, -4);
    }

    @Test(expected = DaoException.class)public void
    test_getAllQuestionsOfSubjectWithoutDatabaseConnectioin_should_fail() throws Exception{
        when(this.mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        this.subjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.testExam, 1);
        PowerMockito.verifyStatic();
        this.mockConnectionH2.getConnection();
    }

    @After public void tearDown() throws Exception {

    }

}
