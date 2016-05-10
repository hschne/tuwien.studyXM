package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Felix on 10.05.2016.
 */
public class QuestionServiceImpl implements QuestionService {
    private QuestionDao qdao;
    private Logger logger = LogManager.getLogger(QuestionServiceImpl.class);

    @Autowired public QuestionServiceImpl(QuestionDao qd) {
        this.qdao = qd;
    }

    @Override public Question getQuestion(int questionId) throws ServiceException {
        try {
            return this.qdao.getQuestion(questionId);
        } catch(DaoException e) {
            logger.error("Could not fetch question: " + e.getMessage());
            throw new ServiceException("Could not fetch question");
        }
    }

    @Override public List<Question> getQuestion() throws ServiceException {
        try {
            return this.qdao.getQuestions();
        } catch(DaoException e) {
            logger.error("Could not fetch a list of all questions: " + e.getMessage());
            throw new ServiceException("Could not fetch list of all questions");
        }
    }

    @Override public Question createQuestion(Question q) throws ServiceException {
        try {
            return this.qdao.createQuestion(q);
        } catch(DaoException e) {
            logger.error("Could not save question persistently: " + e.getMessage());
            throw new ServiceException("Could not save question persistently");
        }
    }

    @Override public Question updateQuestion(Question q) throws ServiceException {
        try {
            return this.qdao.updateQuestion(q);
        } catch(DaoException e) {
            logger.error("Could not update question: " + e.getMessage());
            throw new ServiceException("Could not update question");
        }
    }

    @Override public Question deleteQuestion(Question q) throws ServiceException {
        try {
            return this.qdao.deleteQuestion(q);
        } catch(DaoException e) {
            logger.error("Could not delete question: " + e.getMessage());
            throw new ServiceException("Could not delete question");
        }
    }

    @Override public boolean setCorrespondingAnswers(Question q, List<Answer> al)
        throws ServiceException {
        //TODO
        return false;
    }

    @Override public List<Answer> getCorrespondingAnswers(Question q) throws ServiceException {
        //TODO
        return null;
    }

    @Override public boolean setCorrespondingTopic(Question q, Topic t) throws ServiceException {
        //TODO
        return false;
    }

    @Override public List<Topic> getCorrespondingTopic(Question q) throws ServiceException {
        //TODO
        return null;
    }

    @Override public boolean setCorrespondingResource(Question q, Resource r)
        throws ServiceException {
        //TODO
        return false;
    }

    @Override public List<Resource> getCorrespondingResources(Question q) throws ServiceException {
        //TODO
        return null;
    }
}
