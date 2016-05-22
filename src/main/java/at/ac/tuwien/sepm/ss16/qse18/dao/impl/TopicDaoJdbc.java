package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.*;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementor of TopicDao
 *
 * @author Philipp Ganiu, Hans-Jörg Schrödl
 */
@Service public class TopicDaoJdbc implements TopicDao {
    private static final Logger logger = LogManager.getLogger(TopicDaoJdbc.class);
    private static final String GETTOPIC_SQL = "SELECT * FROM ENTITY_TOPIC WHERE TOPICID = ?;";
    private static final String GETOPICS_SQL = "SELECT * FROM ENTITY_TOPIC;";
    private static final String CREATE_SQL = "INSERT INTO ENTITY_TOPIC (TOPIC) VALUES(?);";
    private static final String DELETE_SQL = "DELETE FROM ENTITY_TOPIC WHERE TOPICID = ?;";
    private static final String UPDATE_SQL = "UPDATE ENTITY_TOPIC SET TOPIC = ? WHERE TOPICID = ?;";
    private ConnectionH2 database;
    private SubjectTopicDao subjectTopicDaoJdbc;
    private QuestionTopicDaoJdbc questionTopicDao;
    private ResourceTopicDaoJdbc resourceTopicDao;


    @Autowired public TopicDaoJdbc(ConnectionH2 database) {
        this.database = database;
    }

    @Autowired public void setSubjectTopicDaoJdbc(SubjectTopicDaoJdbc subjectTopicDaoJdbc) {
        this.subjectTopicDaoJdbc = subjectTopicDaoJdbc;
    }

    @Autowired public void setQuestionTopicDao(QuestionTopicDaoJdbc questionTopicDao) {
        this.questionTopicDao = questionTopicDao;
    }

    @Autowired public void setTopicResourceDao(ResourceTopicDaoJdbc resourceTopicDao){
        this.resourceTopicDao = resourceTopicDao;
    }


    @Override public Topic getTopic(int topicId) throws DaoException {
        logger.debug("Entering getTopic with parameters {}", topicId);

        Topic topic = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = database.getConnection().prepareStatement(GETTOPIC_SQL);
            pstmt.setInt(1,topicId);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                topic = new Topic(topicId,rs.getString("TOPIC"));
            }
        } catch(SQLException e){
            logger.error("Could not get topic with id (" + topicId + ")", e);
            throw new DaoException("Could not get topic with id (" + topicId + ")");
        } finally {
            closeStatementAndResultSet(pstmt,rs);
        }
        return topic;
    }

    @Override public List<Topic> getTopics() throws DaoException {
        logger.debug("Entering getTopics()");

        List<Topic> topics = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = database.getConnection().createStatement();
            rs = stmt.executeQuery(GETOPICS_SQL);

            while(rs.next()){
                Topic topic = new Topic(rs.getInt("TOPICID"), rs.getString("TOPIC"));
                topics.add(topic);
            }
        } catch(SQLException e){
            logger.error("Could not get all Topics: " + e);
            throw new DaoException("Could not get all Topics");
        } finally {
            closeStatementAndResultSet(stmt, rs);
        }
        return topics;
    }

    @Override public Topic createTopic(Topic topic, Subject subject) throws DaoException {
        logger.debug("Entering createTopic with parameters {}", topic);
        if (topic == null) {
            throw new DaoException("Topic must not be null");
        }
        if (subject == null) {
            throw new DaoException("Subject must not be null");
        }


        Topic createdTopic = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKey = null;

        try {
            pstmt = database.getConnection().prepareStatement(CREATE_SQL,Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, topic.getTopic());
            pstmt.executeUpdate();

            generatedKey = pstmt.getGeneratedKeys();
            if(generatedKey.next()) {
                createdTopic = new Topic(generatedKey.getInt(1), topic.getTopic());
            }
            subjectTopicDaoJdbc.createSubjectTopic(subject, createdTopic);
        } catch (SQLException e) {
            logger.error("Could not create " + topic, e);
            throw new DaoException("Could not create " + topic);
        } finally {
            closeStatementAndResultSet(pstmt, generatedKey);
        }
        return createdTopic;
    }

    @Override public boolean deleteTopic(Topic topic) throws DaoException {
        logger.debug("Entering deleteTopic with parameters {}", topic);
        if (topic == null) {
            throw new DaoException("Topic must not be null");
        }
        try {
            subjectTopicDaoJdbc.deleteSubjectTopic(topic);
        } catch (DaoException e) {
            logger.error(e);
            throw new DaoException(e.getMessage());
        }
        questionTopicDao.removeQuestionFromTopic(topic);
        resourceTopicDao.removeResourceTopic(topic);
        PreparedStatement pstmt = null;
        try {
            pstmt = database.getConnection().prepareStatement(DELETE_SQL);
            pstmt.setInt(1, topic.getTopicId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("Could not delete " + topic, e);
            throw new DaoException("Could not delete" + topic);
        } finally {
            closeStatementAndResultSet(pstmt, null);
        }
        return true;
    }

    @Override public Topic updateTopic(Topic topic) throws DaoException {
        logger.debug("Entering updateTopic with parameters {}", topic);
        if (topic == null) {
            throw new DaoException("Topic must not be null");
        }

        Topic updatedTopic = null;
        PreparedStatement pstmt = null;

        try {
            pstmt = database.getConnection().prepareStatement(UPDATE_SQL);
            pstmt.setString(1, topic.getTopic());
            pstmt.setInt(2, topic.getTopicId());
            pstmt.executeUpdate();
            updatedTopic = topic;
        } catch (SQLException e) {
            logger.error("Could not update " + topic, e);
            throw new DaoException("Could not update " + topic);
        }
        return updatedTopic;
    }

    public static void closeStatementAndResultSet(Statement statement, ResultSet resultSet)
        throws DaoException {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.error("Could not close Statement ", e);
                throw new DaoException("Could not close Statement " + e.getMessage());
            }
        }
        if(resultSet != null) {
            try {
                resultSet.close();
            } catch(SQLException e) {
                logger.error("Could not close ResultSet ", e);
                throw new DaoException("Could not close Resultset " + e.getMessage());
            }
        }
    }
}
