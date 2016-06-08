package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.DataBaseConnection;
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

import static at.ac.tuwien.sepm.ss16.qse18.dao.StatementResultsetCloser.closeStatementsAndResultSets;

/**
 * Class SubjectTopicDaoJdbc
 * concrete implementation of Interface SubjectTopicDao
 * This class has access to the h2 database that is defined in the ConnectionH2 class.
 *
 * @author Philipp Ganiu, Bicer Cem
 */
@Service public class SubjectTopicDaoJdbc implements SubjectTopicDao {

    private static final Logger logger = LogManager.getLogger();
    private DataBaseConnection database;
    private static final String CREATE_SQL = "INSERT INTO REL_SUBJECT_TOPIC VALUES(?,?);";
    private static final String DELETE_SQL = "DELETE FROM REL_SUBJECT_TOPIC WHERE TOPICID =?;";
    private static final String TOPICTOSUBJECT_SQL = "SELECT T.TOPICID,T.TOPIC "
        + "FROM ENTITY_TOPIC T NATURAL JOIN REL_SUBJECT_TOPIC R WHERE R.SUBJECTID = ?;";

    @Autowired public SubjectTopicDaoJdbc(DataBaseConnection database) {
        this.database = database;
    }

    @Override public void createSubjectTopic(Subject subject, Topic topic) throws DaoException {
        logger.debug("Entering createSubjectTopic with parameters {}", subject, topic);
        PreparedStatement pstmt = null;
        if(subject == null || topic == null) {
            logger.warn("Subject or topic should not be null");
            throw new DaoException("Subject or topic should not be null");
        }
        try {
            pstmt = database.getConnection().prepareStatement(CREATE_SQL);
            pstmt.setInt(1, subject.getSubjectId());
            pstmt.setInt(2, topic.getTopicId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Could not insert into rel_subject_topic " + subject + topic, e);
            throw new DaoException("Could not insert into rel_subject_topic " + subject + topic);
        } finally {
            TopicDaoJdbc.closeStatementAndResultSet(pstmt, null);
        }
    }

    @Override public void deleteSubjectTopic(Topic topic) throws DaoException {
        logger.debug("Entering deleteSubjectTopic with parameters {}", topic);
        PreparedStatement pstmt = null;

        if(topic == null) {
            throw new DaoException("Topic cannot be null");
        }

        try {
            pstmt = database.getConnection().prepareStatement(DELETE_SQL);
            pstmt.setInt(1, topic.getTopicId());
            pstmt.executeUpdate();
        } catch(SQLException e) {
            logger.error("Could not delete in rel_subject_topic topic " + topic, e);
            throw new DaoException("Could not delete in rel_subject_topic topic " + topic);
        } finally {
            TopicDaoJdbc.closeStatementAndResultSet(pstmt, null);
        }

    }

    @Override public List<Topic> getTopicToSubject(Subject subject) throws DaoException {
        logger.debug("Entering getTopicToSubject with parameters {}", subject);
        if(subject == null) {
            logger.error("Subject cannot be null");
            throw new DaoException("Subject cannot be null");
        }
        List<Topic> topics = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = database.getConnection().prepareStatement(TOPICTOSUBJECT_SQL);
            pstmt.setInt(1, subject.getSubjectId());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Topic t = new Topic(rs.getInt(1), rs.getString(2));
                topics.add(t);
            }
        } catch(SQLException e) {
            logger.error("Couldn't get all Topics to this Subject " + subject, e);
            throw new DaoException("Couldn't get all Topics to this Subject" + subject);
        } finally {
            TopicDaoJdbc.closeStatementAndResultSet(pstmt, rs);
        }
        return topics;
    }

    @Override public List<Subject> getSubjectsFromTopic(Topic topic) throws DaoException {
        logger.debug("Entering getSubjectsFromTopic with parameter [" + topic + "]");
        List<Subject> res = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = database.getConnection().prepareStatement(
                "SELECT s.* FROM rel_subject_topic NATURAL JOIN entity_subject s "
                    + "WHERE topicid = ? ORDER BY subjectid ASC");
            ps.setInt(1, topic.getTopicId());

            rs = ps.executeQuery();

            res = new LinkedList<>();

            while (rs.next()) {
                Subject tmp = new Subject();
                tmp.setSubjectId(rs.getInt(1));
                tmp.setName(rs.getString(2));
                tmp.setEcts(rs.getFloat(3));
                tmp.setSemester(rs.getString(4));
                tmp.setTimeSpent(rs.getInt(5));
                tmp.setAuthor(rs.getString(6));

                res.add(tmp);
            }

        } catch(SQLException e) {
            logger.error("Could not get subjects from topic [" + topic + "]: " + e);
            throw new DaoException("Could not get subjects from topic [" + topic + "]");
        } finally {
            closeStatementsAndResultSets(new Statement[] {ps}, new ResultSet[] {rs});
        }
        return res;
    }
}
