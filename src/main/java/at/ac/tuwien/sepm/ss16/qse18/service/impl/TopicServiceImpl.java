package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.TopicDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.TopicDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.TopicService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Philipp Ganiu
 */
public class TopicServiceImpl implements TopicService {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private TopicDao topicDao;

    @Autowired
    public TopicServiceImpl(TopicDaoJdbc topicDao) {
        this.topicDao = topicDao;
    }

    @Override
    public Topic getTopic(int topicid) throws ServiceException {
        try{
            return topicDao.getTopic(topicid);
        }
        catch (DaoException e){
            logger.error(e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public List<Topic> getTopics() throws ServiceException {
        try{
            return topicDao.getTopics();
        }
        catch (DaoException e){
            logger.error(e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Topic createTopic(Topic topic) throws ServiceException {
        //TODO verify topic
        try{
            return topicDao.createTopic(topic);
        }
        catch (DaoException e){
            logger.error(e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public boolean deleteTopic(Topic topic) throws ServiceException {
        //TODO verify topic
        try{
            return topicDao.deleteTopic(topic);
        }
        catch (DaoException e){
            logger.error(e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Topic updateTopic(Topic topic) throws ServiceException {
        //TODO verify topic
        try{
            return topicDao.updateTopic(topic);
        }
        catch (DaoException e){
            logger.error(e);
            throw new ServiceException(e.getMessage());
        }
    }
}
