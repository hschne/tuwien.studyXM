package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ResourceTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Implementation for H2 for {@link ResourceTopicDao}
 *
 * @author Hans-Joerg Schroedl
 */
@Service public class ResourceTopicDaoJdbc implements ResourceTopicDao {

    private ConnectionH2 database;

    @Autowired public ResourceTopicDaoJdbc(ConnectionH2 database) {
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

