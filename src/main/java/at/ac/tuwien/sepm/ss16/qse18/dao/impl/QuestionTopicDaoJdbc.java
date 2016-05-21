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
 * @author Philipp Ganiu, Bicer Cem
 */
@Service public class QuestionTopicDaoJdbc implements QuestionTopicDao {

    private static final Logger logger = LogManager.getLogger();
    private ConnectionH2 database;
    private static final String QUESTIONTOTOPIC_SQL = "SELECT Q.QUESTIONID,Q.QUESTION,Q.TYPE,Q.QUESTION_TIME "
        + "FROM ENTITY_QUESTION Q NATURAL JOIN REL_QUESTION_TOPIC R WHERE R.TOPICID = ?;";

    @Autowired public QuestionTopicDaoJdbc(ConnectionH2 database) {
        this.database = database;
    }

    @Override public List<Integer> getQuestionIdsFromTopicId(int topicId) throws DaoException {
        logger.debug("Entering getQuestionIdsFromTopicId(int)");

        return getItems('t', "SELECT questionId FROM rel_question_topic WHERE topicId = ?",
            topicId);
    }

    @Override public List<Integer> getTopicIdsFromQuestionId(int questionId) throws DaoException {
        logger.debug("Entering getTopicIdsFromQuestionId(int)");

        return getItems('q', "SELECT topicId FROM rel_question_topic WHERE questionId = ?",
            questionId);
    }

    private List<Integer> getItems(char typeOfId, String sqlStatement, int id) throws DaoException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Integer> res = null;

        try {
            ps = database.getConnection().prepareStatement(sqlStatement);
            ps.setInt(1, id);

            rs = ps.executeQuery();
            res = new LinkedList<>();

            while (rs.next()) {
                res.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            if (typeOfId == 'q') {
                logger.error("Could not get all topicIds from question with id (" + id + ")" + e
                    .getMessage());
                throw new DaoException(
                    "Could not get all topicIds from question with id (" + id + ")" + e
                        .getMessage());
            } else if (typeOfId == 't') {
                logger.error(
                    "Could not get all questions from topic with id (" + id + ")" + e.getMessage());
                throw new DaoException(
                    "Could not get all questions from topic with id (" + id + ")" + e.getMessage());
            }
        } finally {
            closeStatementsAndResultSets(new Statement[] {ps}, new ResultSet[] {rs});
        }

        return res;
    }

    @Override
    public List<Question> getQuestionToTopic(Topic topic) throws DaoException{
        logger.debug("Entering getQuesitonToTopic with parameters {}",topic);
        if(topic == null){
            logger.error("Topic cannot be null");
            throw new DaoException("Topic cannot be null");
        }
        List<Question> questions = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            pstmt = database.getConnection().prepareStatement(QUESTIONTOTOPIC_SQL);
            pstmt.setInt(1,topic.getTopicId());
            rs = pstmt.executeQuery();

            while (rs.next()){
               Question q = new Question(rs.getInt(1),rs.getString(2),
                   QuestionType.valueOf(rs.getInt(3)),rs.getLong(4));
                questions.add(q);
            }
        }
        catch (SQLException e){
            logger.error("Couldn't get all Quesitons to this Topic " + topic,e);
            throw new DaoException("Couldn't get all Questions to this Topic" + topic);
        }
        finally {
            if(pstmt != null){
                try{
                    pstmt.close();
                }
                catch (SQLException e){
                    logger.error("Couldn't close Statement");
                    throw new DaoException("Couldn't close Statement");
                }
            }
            if(rs != null){
                try{
                    rs.close();
                }
                catch (SQLException e){
                    logger.error("Couldn't close ResultSet");
                    throw new DaoException("Couldn't close ResultSet");
                }
            }
        }
        return questions;
    }
}
