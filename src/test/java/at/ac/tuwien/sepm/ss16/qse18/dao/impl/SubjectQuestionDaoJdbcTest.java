package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoBaseTest;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Class SubjectQuestonDaoJdbcTest
 * Tests for the JDBC implementation in SubjectQuestionDaoJdbc. In order to be isolated while testing, this
 * test class uses mocks primarily to bypass the database connection procedure.
 *
 * @author Zhang Haixiang
 */
public class SubjectQuestionDaoJdbcTest extends DaoBaseTest {

    private SubjectQuestionDaoJdbc subjectQuestionDaoJdbc;
    private Exam testExam;


    @Before public void setUp() throws Exception {
        super.setUp();

        ArrayList<Question> al = new ArrayList<Question>() {
        };
        Question question = new Question();
        question.setQuestion("TestQuestion");
        question.setQuestionId(1);
        question.setType(QuestionType.valueOf(1));
        al.add(question);

        testExam = new Exam();
        testExam.setExamid(1);
        testExam.setCreated(new Timestamp(798));
        testExam.setPassed(false);
        testExam.setAuthor("Test1");
        testExam.setSubjectID(1);
        testExam.setExamQuestions(al);
        testExam.setExamTime(1);

        this.subjectQuestionDaoJdbc = new SubjectQuestionDaoJdbc(this.mockConnectionH2);
    }

    //Testing getAllQuestionsOfSubject(Exam, int)
    //----------------------------------------------------------------------------------------------
    @Test public void test_getAllQuestionsOfSubjectWith2Elements_should_persist() throws Exception {
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt(anyString())).thenReturn(1).thenReturn(2);

        List<Integer> questionIDList = new ArrayList<>();

        questionIDList = this.subjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.testExam, 1);
        assertTrue("List should have size 2", questionIDList.size() == 2);
        assertTrue("First Element should be 1", questionIDList.get(0) == 1);
        assertTrue("Second Element should be 2", questionIDList.get(1) == 2);
    }

    @Test(expected = DaoException.class)
    public void test_getAllQuestionsOfSubjectWithInvalidTopicID_should_fail() throws Exception {
        this.subjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.testExam, -4);
    }

    @Test(expected = DaoException.class)
    public void test_getAllQuestionsOfSubjectWithoutDatabaseConnectioin_should_fail()
        throws Exception {
        when(this.mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        this.subjectQuestionDaoJdbc.getAllQuestionsOfSubject(this.testExam, 1);
        PowerMockito.verifyStatic();
        this.mockConnectionH2.getConnection();
    }
    //----------------------------------------------------------------------------------------------

    @After public void tearDown() throws Exception {

    }

}
