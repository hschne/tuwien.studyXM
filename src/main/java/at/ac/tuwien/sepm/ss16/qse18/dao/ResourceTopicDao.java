package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;

/**
 * Interface ResourceTopicDao
 * Implementors provide functionality to remove relation entries between resource and topic
 *
 * @author Hans-Joerg Schroedl
 */
public interface ResourceTopicDao {

    /**
     * addResourceTopic
     * the given topic and resource are saved into the database persistently
     * @param topic the topic which should be saved
     * @param resource the resource which should be saved
     * @throws DaoException
     *
     * */
    void addResourceTopic(Topic topic, Resource resource) throws DaoException;

    /**
     * removeResourceTopic
     * removes the given topic and all resources related to it from the database
     * @param topic the topic which should be deleted
     * @throws DaoException
     * */
    void removeResourceTopic(Topic topic) throws DaoException;
}
