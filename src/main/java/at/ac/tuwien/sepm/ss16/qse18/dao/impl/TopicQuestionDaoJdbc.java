package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.StatementResultsetCloser;
import at.ac.tuwien.sepm.ss16.qse18.dao.TopicQuestionDao;
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

import static at.ac.tuwien.sepm.ss16.qse18.dao.StatementResultsetCloser.closeStatementsAndResultSets;

@Service public class TopicQuestionDaoJdbc implements TopicQuestionDao {

    private static final Logger logger = LogManager.getLogger();
    private ConnectionH2 database;

    @Autowired public TopicQuestionDaoJdbc(ConnectionH2 database) {
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
}
