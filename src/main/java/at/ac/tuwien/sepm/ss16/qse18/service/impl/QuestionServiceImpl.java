package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.AnswerDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Felix on 10.05.2016.
 */
@Service public class QuestionServiceImpl implements QuestionService {
    private QuestionDao qdao;
    private AnswerDao adao;
    private QuestionTopicDao tqDao;
    private Logger logger = LogManager.getLogger(QuestionServiceImpl.class);

    @Autowired public QuestionServiceImpl(QuestionDao qd, AnswerDao ad, QuestionTopicDao tqDao) {
        this.qdao = qd;
        this.adao = ad;
        this.tqDao = tqDao;
    }

    @Override public Question getQuestion(int questionId) throws ServiceException {
        try {
            return this.qdao.getQuestion(questionId);
        } catch(DaoException e) {
            logger.error("Could not fetch question: ", e);
            throw new ServiceException("Could not fetch question");
        }
    }

    @Override public List<Question> getQuestion() throws ServiceException {
        try {
            return this.qdao.getQuestions();
        } catch(DaoException e) {
            logger.error("Could not fetch a list of all question: ", e);
            throw new ServiceException("Could not fetch list of all question");
        }
    }

    @Override public Question createQuestion(Question q,Topic t) throws ServiceException {
        try {
            return this.qdao.createQuestion(q,t);
        } catch (DaoException e) {
            logger.error("Could not save question persistently: ", e);
            throw new ServiceException("Could not save question persistently");
        }
    }

    @Override public Question updateQuestion(Question q,Topic t) throws ServiceException {
        try {
            return this.qdao.updateQuestion(q,t);
        } catch(DaoException e) {
            logger.error("Could not update question: ", e);
            throw new ServiceException("Could not update question");
        }
    }

    @Override public Question deleteQuestion(Question q) throws ServiceException {
        try {
            return this.qdao.deleteQuestion(q);
        } catch(DaoException e) {
            logger.error("Could not delete question: ", e);
            throw new ServiceException("Could not delete question");
        }
    }

    @Override public boolean setCorrespondingAnswers(Question q, List<Answer> al)
        throws ServiceException {
        try {
            for (Answer a : al) {
                a.setQuestion(q);
                adao.updateAnswer(a);
            }
            return true;
        } catch(DaoException e) {
            logger.error("Could not set corresponding answers: ", e);
        }
        return false;
    }

    @Override public List<Answer> getCorrespondingAnswers(Question q) throws ServiceException {
        try {
            List<Answer> al = adao.getAnswer();
            for (Answer a : al) {
                if (!a.getQuestion().equals(q)) {
                    al.remove(a);
                }
            }
            return al;
        } catch(DaoException e) {
            logger.error("Could not get corresponding answers: ", e);
            throw new ServiceException("Could not get corresponding answers");
        }
    }

    @Override public boolean setCorrespondingTopic(Question q, Topic t) throws ServiceException {
        //TODO
        return false;
    }

    @Override public List<Topic> getCorrespondingTopic(Question q) throws ServiceException {
        //TODO
        return new ArrayList<>();
    }

    @Override public boolean setCorrespondingResource(Question q, Resource r)
        throws ServiceException {
        //TODO
        return false;
    }

    @Override public List<Resource> getCorrespondingResources(Question q) throws ServiceException {
        //TODO
        return new ArrayList<>();
    }

    @Override public List<Question> getQuestionsFromTopic(Topic topic) throws ServiceException {
        logger.debug("Entering getQuestionsFromTopic");

        List<Question> res;

        try {
            res = tqDao.getQuestionToTopic(topic);
        } catch (DaoException e) {
            logger.error("Could not get questions from topic [" + topic + "]", e);
            throw new ServiceException("Could not get questions from topic [" + topic + "]");
        }

        return res;
    }
}
