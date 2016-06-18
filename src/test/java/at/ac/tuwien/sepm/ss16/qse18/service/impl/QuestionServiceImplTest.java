package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.AnswerDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

/**
 * Created by Felix on 18.06.2016.
 */
@RunWith(MockitoJUnitRunner.class) public class QuestionServiceImplTest {
    private QuestionService questionService;
    @Mock private QuestionDao mockQuestionDao;
    @Mock private AnswerDao mockAnswerDao;
    @Mock private QuestionTopicDao mockQuestionTopicDao;

    @Before public void setUp() {
        questionService = new QuestionServiceImpl(mockQuestionDao,
            mockAnswerDao, mockQuestionTopicDao);
    }

    @Test (expected = ServiceException.class) public void test_getQuestion_daoException()
        throws Exception {
        when(mockQuestionDao.getQuestion(anyInt())).thenThrow(DaoException.class);
        questionService.getQuestion(1);
    }

    @Test public void test_getQuestion_success() throws Exception {
        when(mockQuestionDao.getQuestion(anyInt())).thenReturn(new Question());
        assertTrue(questionService.getQuestion(1) != null);
    }

    @Test (expected = ServiceException.class) public void test_getQuestions_daoException()
        throws Exception {
        when(mockQuestionDao.getQuestions()).thenThrow(DaoException.class);
        questionService.getQuestion();
    }

    @Test public void test_getQuestions_success() throws Exception {
        when(mockQuestionDao.getQuestions()).thenReturn(new ArrayList<>());
        assertTrue(questionService.getQuestion().size() == 0);
    }

    @Test (expected = ServiceException.class) public void test_createQuestion_daoException()
        throws Exception {
        when(mockQuestionDao.createQuestion(anyObject(), anyObject())).thenThrow(DaoException.class);
        questionService.createQuestion(null, null);
    }

    @Test public void test_createQuestion_success() throws Exception {
        when(mockQuestionDao.createQuestion(anyObject(), anyObject())).thenReturn(new Question());
        assertTrue(questionService.createQuestion(null, null) != null);
    }

    @Test (expected = ServiceException.class) public void test_updateQuestion_daoException()
        throws Exception {
        when(mockQuestionDao.updateQuestion(anyObject(), anyObject())).thenThrow(DaoException.class);
        questionService.updateQuestion(null, null);
    }

    @Test public void test_updateQuestion_success() throws Exception {
        when(mockQuestionDao.updateQuestion(anyObject(), anyObject())).thenReturn(new Question());
        assertTrue(questionService.updateQuestion(null, null) != null);
    }

    @Test (expected = ServiceException.class) public void test_deleteQuestion_daoException()
        throws Exception {
        when(mockQuestionDao.deleteQuestion(anyObject())).thenThrow(DaoException.class);
        questionService.deleteQuestion(null);
    }

    @Test public void test_deleteQuestion_success() throws Exception {
        when(mockQuestionDao.deleteQuestion(anyObject())).thenReturn(new Question());
        assertTrue(questionService.deleteQuestion(null) != null);
    }

    @Test public void test_setCorrespondingAnswers_daoException()
        throws Exception {
        List<Answer> al = new ArrayList<>();
        al.add(new Answer());
        when(mockAnswerDao.updateAnswer(anyObject())).thenThrow(DaoException.class);
        assertFalse(questionService.setCorrespondingAnswers(new Question(), al));
    }

    @Test public void test_setCorrespondingAnswers_success() throws Exception {
        List<Answer> al = new ArrayList<>();
        al.add(new Answer());
        assertTrue(questionService.setCorrespondingAnswers(new Question(), al));
    }

    @Test (expected = ServiceException.class) public void test_getCorrespondingAnswers_daoException()
        throws Exception {
        when(mockQuestionDao.getRelatedAnswers(anyObject())).thenThrow(DaoException.class);
        questionService.getCorrespondingAnswers(null);
    }

    @Test public void test_getCorrespondingAnswers_success() throws Exception {
        when(mockQuestionDao.getRelatedAnswers(anyObject())).thenReturn(new ArrayList<>());
        assertTrue(questionService.getCorrespondingAnswers(null) != null);
    }

    @Test (expected = ServiceException.class) public void test_getQuestionsFromTopic_daoException()
        throws Exception {
        when(mockQuestionTopicDao.getQuestionToTopic(anyObject())).thenThrow(DaoException.class);
        questionService.getQuestionsFromTopic(null);
    }

    @Test public void test_getQuestionsFromTopic_success() throws Exception {
        when(mockQuestionTopicDao.getQuestionToTopic(anyObject())).thenReturn(new ArrayList<>());
        assertTrue(questionService.getQuestionsFromTopic(null) != null);
    }
}
