package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.*;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.TopicDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.TopicService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * Class TopicServiceImpl
 * concrete implementatioin of TopicService
 *
 * @author Philipp Ganiu, Bicer Cem
 */
@Service public class TopicServiceImpl implements TopicService {
    private static final Logger logger = LogManager.getLogger();
    private TopicDao topicDao;
    private QuestionDao qDao;
    private SubjectDao sdao;
    private SubjectTopicDao stDao;
    private QuestionTopicDao qtDao;

    @Autowired public TopicServiceImpl(SubjectTopicDao stDao, SubjectDao sdao, TopicDao tDao,
        QuestionDao qDao, QuestionTopicDao qtDao) {
        this.stDao = stDao;
        this.sdao = sdao;
        this.topicDao = tDao;
        this.qtDao = qtDao;
        this.qDao = qDao;
    }

    public TopicServiceImpl(TopicDaoJdbc topicDao) {
        this.topicDao = topicDao;
    }

    @Override public Topic getTopic(int topicid) throws ServiceException {
        try {
            return topicDao.getTopic(topicid);
        } catch(DaoException e) {
            logger.error("Could not get topic", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public List<Topic> getTopics() throws ServiceException {
        try {
            return topicDao.getTopics();
        } catch(DaoException e) {
            logger.error("Could not get a list of all topics", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public Topic createTopic(Topic topic, Subject subject) throws ServiceException {
        try{
            DtoValidator.validate(topic);
            DtoValidator.validate(subject);
        }
        catch (DtoValidatorException e){
            logger.error(e);
            throw new ServiceException(e.getMessage());
        }

        try {
            return topicDao.createTopic(topic, subject);
        } catch(DaoException e) {
            logger.error("Could not create topic", e);
            throw new ServiceException(e.getMessage(),e);
        }
    }

    @Override public boolean deleteTopic(Topic topic) throws ServiceException {
        try{
            DtoValidator.validate(topic);
        }
        catch (DtoValidatorException e){
            logger.error(e);
            throw new ServiceException(e.getMessage(),e);
        }

        try {
            return topicDao.deleteTopic(topic);
        } catch (DaoException e) {
            logger.error("Could not delete topic", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override public Topic updateTopic(Topic topic) throws ServiceException {
        try{
            DtoValidator.validate(topic);
        }
        catch (DtoValidatorException e){
            logger.error(e);
            throw new ServiceException(e.getMessage(),e);
        }


        try {
            return topicDao.updateTopic(topic);
        } catch(DaoException e) {
            logger.error("Could not update topic", e);
            throw new ServiceException(e.getMessage());
        }
    }


    @Override public List<Topic> getTopicsFromSubject(Subject subject) throws ServiceException {
        logger.debug("Entering getTopicsFromSubject()");

        if(subject == null) {
            logger.error("Subject must not be null in getTopicsFromSubject()");
            throw new ServiceException("Subject is null in getTopicsFromSubject()");
        }
        try{
            DtoValidator.validate(subject);
        }
        catch (DtoValidatorException e){
            logger.error(e.getMessage(),e);
            throw new ServiceException(e.getMessage(),e);
        }

        return getTopicsFromId('s', subject.getSubjectId());
    }

    @Override public List<Topic> getTopicsFromQuestion(Question question) throws ServiceException {
        logger.debug("Entering getTopicsFromSubject()");

        if (question == null) {
            logger.error("Question must not be null in getTopicsFromQuestion()");
            throw new ServiceException("Question is null in getTopicsFromQuestion()");
        }

        try{
            DtoValidator.validate(question);
        }
        catch (DtoValidatorException e){
            logger.error(e.getMessage(),e);
            throw new ServiceException(e.getMessage(),e);
        }


        return getTopicsFromId('q', question.getQuestionId());
    }

    /**
     * getTopicsFromId
     * get the topics to a certain subject or from a certain question and saves it in a list
     * @param typeOfId defines which topic to get, whether from to a subject or from a question
     * @param id either the id of a question or a subject
     * @return returns a list of topics related to the given id
     * @throws ServiceException
     *
     * */
    private List<Topic> getTopicsFromId(char typeOfId, int id) throws ServiceException {
        List<Topic> res = null;

        try {
            if (typeOfId == 'q') {
                res = qtDao.getTopicsFromQuestion(qDao.getQuestion(id));
            } else if (typeOfId == 's') {
                res = stDao.getTopicToSubject(sdao.getSubject(id));
            } else {
                // add some types if you need
                res = new LinkedList<>();
            }
        } catch (DaoException e) {
            if (typeOfId == 'q') {
                logger.error(
                    "Could not get topics from given questionId (" + id + "): ", e);
                throw new ServiceException(
                    "Could not get topics from given questionId (" + id + ")");
            } else if (typeOfId == 's') {
                logger.error(
                    "Could not get topics from given subjectId (" + id + "): ", e);
                throw new ServiceException(
                    "Could not get topics from given subjectId (" + id + ")");
            }
        }
        return res;
    }
}
