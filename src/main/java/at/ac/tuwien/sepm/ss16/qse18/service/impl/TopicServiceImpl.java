package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.TopicDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.TopicService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Bicer Cem
 */
@Component public class TopicServiceImpl implements TopicService {

    private static final Logger logger = LogManager.getLogger();

    private SubjectTopicDao stDao;
    private TopicDaoJdbc tDao;
    private QuestionTopicDao tqDao;

    @Autowired
    public TopicServiceImpl(SubjectTopicDao stDao, TopicDaoJdbc tDao, QuestionTopicDao tqDao) {
        this.stDao = stDao;
        this.tDao = tDao;
        this.tqDao = tqDao;
    }

    @Override public List<Topic> getTopicsFromSubject(int subjectid) throws ServiceException {
        logger.debug("Entering getTopicsFromSubject()");

        return getTopicsFromId('s', subjectid);
    }

    @Override public List<Topic> getTopicsFromQuestion(int questionid) throws ServiceException {
        logger.debug("Entering getTopicsFromSubject()");

        return getTopicsFromId('q', questionid);
    }

    private List<Topic> getTopicsFromId(char typeOfId, int id) throws ServiceException {
        List<Topic> res = null;

        try {

            List<Integer> idList = null;

            if (typeOfId == 'q') {
                idList = tqDao.getTopicIdsFromQuestionId(id);
            } else if (typeOfId == 's') {
                idList = stDao.getTopicIdsFromSubjectId(id);
            } else {
                // add some types if you need
                idList = new LinkedList<>();
            }

            res = new LinkedList<>();

            for (Integer i : idList) {
                res.add(tDao.getTopic(i));
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
