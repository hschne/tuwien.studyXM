package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectTopicDao;
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

@Component public class TopicServiceImpl implements TopicService {

    private static final Logger logger = LogManager.getLogger();

    private SubjectTopicDao stDao;
    private TopicDaoJdbc tDao;

    @Autowired public TopicServiceImpl(SubjectTopicDao stDao, TopicDaoJdbc tDao) {
        this.stDao = stDao;
        this.tDao = tDao;
    }

    @Override public List<Topic> getTopicsFromSubject(int subjectid) throws ServiceException {
        logger.debug("Entering getTopicsFromSubject()");

        List<Topic> res = null;

        try {
            List<Integer> idList = stDao.getTopicIdsFromSubjectId(subjectid);

            res = new LinkedList<>();

            for (Integer i : idList) {
                res.add(tDao.getTopic(i));
            }
        } catch (DaoException e) {
            logger.error(
                "Could not get topics from given subjectId (" + subjectid + "): " + e.getMessage());
        }
        return res;
    }
}
