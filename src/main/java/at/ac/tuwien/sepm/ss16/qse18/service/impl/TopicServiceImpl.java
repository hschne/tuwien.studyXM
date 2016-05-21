package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.*;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.TopicDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.TopicDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.TopicService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Philipp Ganiu, Bicer Cem
 */
@Service public class TopicServiceImpl implements TopicService {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private TopicDao topicDao;
    private QuestionDao qDao;
    private SubjectDao sdao;
    private SubjectTopicDao stDao;
    private TopicDaoJdbc tDao;
    private QuestionTopicDao tqDao;

    @Autowired public TopicServiceImpl(SubjectTopicDao stDao, SubjectDao sdao, TopicDaoJdbc tDao,
        QuestionTopicDao tqDao) {
        this.stDao = stDao;
        this.sdao = sdao;
        this.tDao = tDao;
        this.tqDao = tqDao;
    }

    public TopicServiceImpl(TopicDaoJdbc topicDao) {
        this.topicDao = topicDao;
    }

    @Override public Topic getTopic(int topicid) throws ServiceException {
        try {
            return topicDao.getTopic(topicid);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public List<Topic> getTopics() throws ServiceException {
        try {
            return topicDao.getTopics();
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public Topic createTopic(Topic topic, Subject subject) throws ServiceException {
        if (!verifyTopic(topic)) {
            throw new ServiceException("Topic is not valid");
        }
        try {
            return topicDao.createTopic(topic, subject);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public boolean deleteTopic(Topic topic) throws ServiceException {
        if (!verifyTopic(topic)) {
            throw new ServiceException("Topic is not valid");
        }
        try {
            return topicDao.deleteTopic(topic);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public Topic updateTopic(Topic topic) throws ServiceException {
        if (!verifyTopic(topic)) {
            throw new ServiceException("Topic is not valid");
        }
        try {
            return topicDao.updateTopic(topic);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e.getMessage());
        }
    }

    public boolean verifyTopic(Topic t) {
        if (t == null) {
            return false;
        }
        return !t.getTopic().trim().isEmpty() && t.getTopic().length() <= 200;
    }

    @Override public List<Topic> getTopicsFromSubject(Subject subject) throws ServiceException {
        logger.debug("Entering getTopicsFromSubject()");

        return getTopicsFromId('s', subject.getSubjectId());
    }

    @Override public List<Topic> getTopicsFromQuestion(Question question) throws ServiceException {
        logger.debug("Entering getTopicsFromSubject()");

        return getTopicsFromId('q', question.getQuestionId());
    }

    private List<Topic> getTopicsFromId(char typeOfId, int id) throws ServiceException {
        List<Topic> res = null;

        try {
            if (typeOfId == 'q') {
                res = tqDao.getTopicsFromQuestion(qDao.getQuestion(id));
            } else if (typeOfId == 's') {
                res = stDao.getTopicToSubject(sdao.getSubject(id));
            } else {
                // add some types if you need
                res = new LinkedList<>();
            }
        } catch (DaoException e) {
            if (typeOfId == 'q') {
                logger.error(
                    "Could not get topics from given questionId (" + id + "): " + e.getMessage());
                throw new ServiceException(
                    "Could not get topics from given questionId (" + id + ")");
            } else if (typeOfId == 's') {
                logger.error(
                    "Could not get topics from given subjectId (" + id + "): " + e.getMessage());
                throw new ServiceException(
                    "Could not get topics from given subjectId (" + id + ")");
            }
        }
        return res;
    }
}
