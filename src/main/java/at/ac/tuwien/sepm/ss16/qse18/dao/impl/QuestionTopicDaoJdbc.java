package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
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
 * Class QuestionTopicDaoJdbc
 * concrete implementation of Interface QuestionTopicDao
 * This class has access to the h2 database that is defined in the ConnectionH2 class.
 *
 * @author Philipp Ganiu, Bicer Cem
 */
@Service public class QuestionTopicDaoJdbc implements QuestionTopicDao {

    private static final Logger logger = LogManager.getLogger();
    private ConnectionH2 database;
    private static final String QUESTIONTOTOPIC_SQL =
        "SELECT Q.QUESTIONID,Q.QUESTION,Q.TYPE,Q.QUESTION_TIME "
            + "FROM ENTITY_QUESTION Q NATURAL JOIN REL_QUESTION_TOPIC R WHERE R.TOPICID = ?;";
    private static final String CREATE_SQL = "INSERT INTO REL_QUESTION_TOPIC VALUES(?,?);";

    @Autowired public QuestionTopicDaoJdbc(ConnectionH2 database) {
        this.database = database;
    }

    @Override public List<Topic> getTopicsFromQuestion(Question question) throws DaoException {
        logger.debug("Entering getTopicsFromQuestion with parameter [{}]", question);

        if (question == null) {
            logger.error("Question must not be null");
            throw new DaoException("Question is null in getTopicsFromQuestion()");
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Topic> res = null;

        try {
            ps = database.getConnection().prepareStatement(
                "SELECT t.* FROM rel_question_topic NATURAL JOIN entity_topic t WHERE questionId = ?");
            ps.setInt(1, question.getQuestionId());
            rs = ps.executeQuery();

            res = new LinkedList<>();

            while (rs.next()) {
                Topic tmp = new Topic();
                tmp.setTopicId(rs.getInt(1));
                tmp.setTopic(rs.getString(2));

                res.add(tmp);
            }
        } catch (SQLException e) {
            logger.error("Could not get all topics from question {}", question, e);
            throw new DaoException("Could not get all topics from question " + question);

        } finally {
            closeStatementsAndResultSets(new Statement[] {ps}, new ResultSet[] {rs});
        }

        return res;
    }

    @Override public List<Question> getQuestionToTopic(Topic topic) throws DaoException {
        logger.debug("Entering getQuestionToTopic with parameters {}", topic);
        if (topic == null) {
            logger.error("Topic cannot be null");
            throw new DaoException("Topic cannot be null");
        }
        List<Question> questions = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = database.getConnection().prepareStatement(QUESTIONTOTOPIC_SQL);
            pstmt.setInt(1, topic.getTopicId());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Question q =
                    new Question(rs.getInt(1), rs.getString(2), QuestionType.valueOf(rs.getInt(3)),
                        rs.getLong(4));
                questions.add(q);
            }
        } catch (SQLException e) {
            logger.error("Couldn't get all Quesitons to this Topic " + topic, e);
            throw new DaoException("Couldn't get all Questions to this Topic" + topic);
        } finally {
            closeStatementsAndResultSets(new Statement[] {pstmt}, new ResultSet[] {rs});
        }
        return questions;
    }

    @Override public void removeQuestionFromTopic(Topic topic) throws DaoException {
        PreparedStatement pstmt;
        try {
            pstmt = database.getConnection()
                .prepareStatement("DELETE FROM REL_QUESTION_TOPIC WHERE TOPICID =?;");
            pstmt.setInt(1, topic.getTopicId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override public void createQuestionTopic(Question question, Topic topic) throws DaoException {
        logger.debug("Entering createQuestionTopic with parameters {}", question, topic);
        if (question == null || topic == null) {
            logger.error("Question or topic should not be null");
            throw new DaoException("Question or topic should not be null");
        }

        PreparedStatement pstmt = null;

        try {
            pstmt = database.getConnection().prepareStatement(CREATE_SQL);
            pstmt.setInt(1, question.getQuestionId());
            pstmt.setInt(2, topic.getTopicId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Could not insert into rel_question_topic " + question + topic, e);
            throw new DaoException("Could not insert into rel_question_topic " + question + topic);
        } finally {
            closeStatementsAndResultSets(new Statement[] {pstmt}, null);
        }

    }

}
