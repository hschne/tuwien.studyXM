package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.DataBaseConnection;
import at.ac.tuwien.sepm.ss16.qse18.dao.ResourceTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class ResourceToicDaoJdbc
 * Implementation for H2 for {@link ResourceTopicDao}
 * This class has access to the h2 database that is defined in the ConnectionH2 class.
 *
 * @author Hans-Joerg Schroedl
 */
@Repository public class ResourceTopicDaoJdbc implements ResourceTopicDao {

    private DataBaseConnection database;

    @Autowired public ResourceTopicDaoJdbc(DataBaseConnection database) {
        this.database = database;
    }

    @Override public void addResourceTopic(Topic topic, Resource resource) throws DaoException {
        throw new DaoException("Not implemented yet");
    }

    @Override public void removeResourceTopic(Topic topic) throws DaoException {
        assertNotNull(topic);
        PreparedStatement pstmt;
        try {
            pstmt = database.getConnection()
                .prepareStatement("DELETE FROM REL_RESOURCE_TOPIC WHERE TOPICID =?;");
            pstmt.setInt(1, topic.getTopicId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    private void assertNotNull(Topic topic) throws DaoException {
        if (topic == null) {
            throw new DaoException("Topic must not be null");
        }
    }
}

