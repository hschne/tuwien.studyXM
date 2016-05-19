package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.TopicDao;
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
 * @author Philipp Ganiu
 */
@Service
public class TopicDaoJdbc implements TopicDao {
    private static final Logger logger = LogManager.getLogger(TopicDaoJdbc.class);
    private ConnectionH2 database;
    private static final String GETTOPIC_SQL = "SELECT * FROM ENTITY_TOPIC WHERE TOPICID = ?;";
    private static final String GETOPICS_SQL = "SELECT * FROM ENTITY_TOPIC;";
    private static final String CREATE_SQL = "INSERT INTO ENTITY_TOPIC (TOPIC) VALUES(?);";
    private static final String DELETE_SQL = "DELETE FROM ENTITY_TOPIC WHERE TOPICID = ?;";
    private static final String UPDATE_SQL = "UPDATE ENTITY_TOPIC SET TOPIC = ? WHERE TOPICID = ?;";

    @Autowired
    public TopicDaoJdbc(ConnectionH2 database) {
        this.database = database;
    }

    @Override
    public Topic getTopic(int topicid) throws DaoException {
        logger.debug("Entering getTopic with parameters {}",topicid);

        Topic topic = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            pstmt = database.getConnection().prepareStatement(GETTOPIC_SQL);
            pstmt.setInt(1,topicid);
            rs = pstmt.executeQuery();

            if(rs.next()){
                topic = new Topic(topicid,rs.getString("TOPIC"));
            }
        }
        catch (SQLException e){
            logger.error("Could not get topic with id (" + topicid + ")",e);
            throw new DaoException("Could not get topic with id (" + topicid + ")");
        }
        finally {
            closeStatementAndResultSet(pstmt,rs);
        }
        return topic;
    }

    @Override
    public List<Topic> getTopics() throws DaoException {
        logger.debug("Entering getTopics()");

        List<Topic> topics = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = database.getConnection().createStatement();
            rs = stmt.executeQuery(GETOPICS_SQL);

            while(rs.next()){
                Topic topic = new Topic(rs.getInt("TOPICID"),rs.getString("TOPIC"));
                topics.add(topic);
            }
        }
        catch (SQLException e){
            logger.error("Could not get all Topics: " + e);
            throw new DaoException("Could not get all Topics");
        }
        finally {
            closeStatementAndResultSet(stmt,rs);
        }
        return topics;
    }

    @Override
    public Topic createTopic(Topic topic) throws DaoException {
        logger.debug("Entering createTopic with parameters {}",topic);
        if(topic == null){
            throw new DaoException("Topic must not be null");
        }

        Topic createdTopic = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKey = null;

        try{
            pstmt = database.getConnection().prepareStatement(CREATE_SQL,Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1,topic.getTopic());
            pstmt.executeUpdate();

            generatedKey = pstmt.getGeneratedKeys();
            if(generatedKey.next()){
                createdTopic = new Topic(generatedKey.getInt(1),topic.getTopic());
            }
        }
        catch (SQLException e){
            logger.error("Could not create " + topic,e);
            throw new DaoException("Could not create " + topic);
        }
        finally {
            closeStatementAndResultSet(pstmt,generatedKey);
        }
        return createdTopic;
    }

    @Override
    public Topic deleteTopic(Topic topic) throws DaoException {
        logger.debug("Entering deleteTopic with parameters {}",topic);
        if(topic == null){
            throw new DaoException("Topic must not be null");
        }

        Topic deletedTopic = null;
        PreparedStatement pstmt = null;

        try{
            pstmt = database.getConnection().prepareStatement(DELETE_SQL);
            pstmt.setInt(1,topic.getTopicId());
            pstmt.executeUpdate();
            deletedTopic = topic;
        }
        catch (SQLException e){
            logger.error("Could not delete " + topic,e);
            throw new DaoException("Could not delete" + topic);
        }
        finally {
            closeStatementAndResultSet(pstmt,null);
        }
        return deletedTopic;
    }

    @Override
    public Topic updateTopic(Topic topic) throws DaoException {
        logger.debug("Entering updateTopic with parameters {}",topic);
        if(topic == null){
            throw new DaoException("Topic must not be null");
        }

        Topic updatedTopic = null;
        PreparedStatement pstmt = null;

        try{
            pstmt = database.getConnection().prepareStatement(UPDATE_SQL);
            pstmt.setString(1,topic.getTopic());
            pstmt.setInt(2,topic.getTopicId());
            pstmt.executeUpdate();
            updatedTopic = topic;
        }
        catch (SQLException e){
            logger.error("Could not update " + topic);
            throw new DaoException("Could not update " + topic);
        }
        return updatedTopic;
    }

    private void closeStatementAndResultSet(Statement statement, ResultSet resultSet) throws DaoException {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                logger.error("Could not close Statement " + e.getMessage());
                throw new DaoException("Could not close Statement " + e.getMessage());
            }
        }
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                logger.error("Could not close ResultSet " + e.getMessage());
                throw new DaoException("Could not close Resultset " + e.getMessage());
            }
        }
    }
}
