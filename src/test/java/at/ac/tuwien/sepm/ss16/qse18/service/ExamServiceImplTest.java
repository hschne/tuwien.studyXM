package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ExamDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ExamQuestionDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.QuestionDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.SubjectQuestionDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.ExamServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * @author Zhang Haixiang
 */
@RunWith(MockitoJUnitRunner.class) public class ExamServiceImplTest {
    @Mock private ExamDaoJdbc mockExamDaoJdbc;
    private ExamServiceImpl examService;
    private Exam exam;
    private Topic topic;


    @Before public void setUp() throws Exception {
        this.examService = new ExamServiceImpl(this.mockExamDaoJdbc,
            new SubjectQuestionDaoJdbc(new ConnectionH2()), new ExamQuestionDaoJdbc(new ConnectionH2()),
            new QuestionDaoJdbc(new ConnectionH2()));

        ArrayList<Question> al = new ArrayList<Question>(){};
        Question question = new Question();
        question.setQuestion("TestQuestion");
        question.setQuestionid(1);
        question.setType(1);
        al.add(question);

        this.exam = createDummyExam(1, "auhtor");
        exam.setExamQuestions(al);

        this.topic = new Topic();
        topic.setTopic("Topic1");
        topic.setTopicId(1);
    }

    //Testing getSubject(int)
    @Test public void testIf_getExam_callsRightMethodInDao() throws Exception {
        this.examService.getExam(1);
        verify(this.mockExamDaoJdbc).getExam(1);
    }

    @Test public void testIf_getExams_callsRightMethodInDao() throws Exception {
        this.examService.getExams();
        verify(this.mockExamDaoJdbc).getExams();
    }

    @Test public void testIf_createExam_callsRightMethodInDao() throws Exception {
        this.examService.createExam(this.exam, topic, 1000);
        verify(this.mockExamDaoJdbc).create(this.exam, this.exam.getExamQuestions());
    }

    @Test(expected = ServiceException.class)
    public void test_createExam_invalidAuthorThrowsException() throws Exception {
        Exam fail = createDummyExam(2, "");

        this.examService.createExam(fail, topic, 1000);
    }

    @Test(expected = ServiceException.class)
    public void test_createExam_ExamIDThrowsException() throws Exception {
        Exam fail = createDummyExam(-2, "Author2");
        this.examService.createExam(fail, topic, 1000);
    }


    @Test public void testIf_deleteExam_callsRightMethodInDao() throws Exception {
        this.examService.deleteExam(this.exam);
        verify(this.mockExamDaoJdbc).delete(this.exam);
    }

    @After public void tearDown() throws Exception {
        //nothing to tear down
    }

    private Exam createDummyExam(int examID, String author) {
        Exam exam = new Exam();
        exam.setExamid(1);
        exam.setCreated(new Timestamp(10));
        exam.setPassed(false);
        exam.setAuthor("author1");
        exam.setSubjectID(1);

        return exam;
    }

    //Testing getRightQuestions(exam, int, int)
    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------

}
