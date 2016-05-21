package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectTopicDao;
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
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.dao.StatementResultsetCloser.closeStatementsAndResultSets;

/**
 * @author Philipp Ganiu, Bicer Cem
 */
@Service public class SubjectTopicDaoJdbc implements SubjectTopicDao {

    private static final Logger logger = LogManager.getLogger();
    private ConnectionH2 database;
    private static final String CREATE_SQL = "INSERT INTO REL_SUBJECT_TOPIC VALUES(?,?);";
    private static final String DELETE_SQL = "DELETE FROM REL_SUBJECT_TOPIC WHERE TOPICID =?;";
    private static final String TOPICTOSUBJECT_SQL = "SELECT T.TOPICID,T.TOPIC "
        + "FROM ENTITY_TOPIC T NATURAL JOIN REL_SUBJECT_TOPIC R WHERE R.SUBJECTID = ?;";

    @Autowired public SubjectTopicDaoJdbc(ConnectionH2 database) {
        this.database = database;
    }

    @Override
    public void createSubjectTopic(Subject subject, Topic topic) throws DaoException {
        logger.debug("Entering createSubjectTopic with parameters {}",subject,topic);
        PreparedStatement pstmt = null;
        if(subject == null || topic == null){
            throw new DaoException("Subject or topic should not be null");
        }
        try{
            pstmt = database.getConnection().prepareStatement(CREATE_SQL);
            pstmt.setInt(1,subject.getSubjectId());
            pstmt.setInt(2,topic.getTopicId());
            pstmt.executeUpdate();
        }

        catch (SQLException e){
            logger.error("Could not insert into rel_subject_topic " + subject + topic,e);
            throw new DaoException("Could not insert into rel_subject_topic " + subject + topic);
        }
        finally {
            TopicDaoJdbc.closeStatementAndResultSet(pstmt,null);
        }
    }

    @Override
    public void deleteSubjectTopic(Topic topic) throws DaoException {
        logger.debug("Entering deleteSubjectTopic with parameters {}",topic);
        PreparedStatement pstmt = null;

        if(topic == null){
            throw new DaoException("Topic cannot be null");
        }

        try{
            pstmt = database.getConnection().prepareStatement(DELETE_SQL);
            pstmt.setInt(1,topic.getTopicId());
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            logger.error("Could not delete in rel_subject_topic topic " + topic);
            throw new DaoException("Could not delete in rel_subject_topic topic " + topic);
        }
        finally {
            TopicDaoJdbc.closeStatementAndResultSet(pstmt,null);
        }

    }

    @Override public List<Topic> getTopicToSubject(Subject subject) throws DaoException {
        logger.debug("Entering getTopicToSubject with parameters {}",subject);
        if(subject == null){
            logger.error("Subject cannot be null");
            throw new DaoException("Subject cannot be null");
        }
        List<Topic> topics = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            pstmt = database.getConnection().prepareStatement(TOPICTOSUBJECT_SQL);
            pstmt.setInt(1,subject.getSubjectId());
            rs = pstmt.executeQuery();

            while (rs.next()){
                Topic t = new Topic(rs.getInt(1),rs.getString(2));
                topics.add(t);
            }
        }
        catch (SQLException e){
            logger.error("Couldn't get all Topics to this Subject " + subject,e);
            throw new DaoException("Couldn't get all Topics to this Subject" + subject);
        }
        finally {
            TopicDaoJdbc.closeStatementAndResultSet(pstmt,rs);
        }
        return topics;
    }

    @Override public List<Integer> getTopicIdsFromSubjectId(int subjectId) throws DaoException {
        logger.debug("Entering getTopicIdsFromSubjectId(" + subjectId + ")");

        return getItems('s',
            "SELECT topicid FROM rel_subject_topic WHERE subjectid = ? ORDER BY topicid ASC",
            subjectId);
    }

    @Override public List<Integer> getSubjectIdsFromTopicId(int topicId) throws DaoException {
        logger.debug("Entering getSubjectIdsFromTopicId(" + topicId + ")");

        return getItems('t',
            "SELECT subjectid FROM rel_subject_topic WHERE topicid = ? ORDER BY subjectid ASC",
            topicId);
    }

    private List<Integer> getItems(char typeOfId, String sqlStatement, int id) throws DaoException {
        List<Integer> res = null;
        PreparedStatement ps = null;
        ResultSet resultIds = null;

        try {
            ps = database.getConnection().prepareStatement(sqlStatement);
            ps.setInt(1, id);

            resultIds = ps.executeQuery();

            res = new LinkedList<>();

            while (resultIds.next()) {
                res.add(resultIds.getInt(1));
            }

        } catch (SQLException e) {
            if (typeOfId == 's') {
                logger.error("Could not get topicIds from subjectId (" + id + "): " + e);
                throw new DaoException("Could not get topicIds from subjectId(" + id + ")");
            } else if (typeOfId == 't') {
                logger.error("Could not get subjectId from topicId (" + id + "): " + e);
                throw new DaoException("Could not get subjectId from topicId(" + id + ")");
            }
        } finally {
            closeStatementsAndResultSets(new Statement[] {ps}, new ResultSet[] {resultIds});
        }
        return res;
    }
}
