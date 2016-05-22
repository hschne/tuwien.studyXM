package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;

/**
 * Implementors provide functionality to remove relation entries between resource and topic
 *
 * @author Hans-Joerg Schroedl
 */
public interface ResourceTopicDao {
    
    void addResourceTopic(Topic topic, Resource resource) throws Exception;

    void removeResourceTopic(Topic topic) throws DaoException;
}
