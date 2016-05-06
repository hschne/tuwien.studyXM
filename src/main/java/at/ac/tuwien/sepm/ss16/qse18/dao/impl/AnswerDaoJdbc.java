package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.AnswerDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.DataBaseConnection;
import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Vector;

/**
 * Created by Felix on 06.05.2016.
 */
public class AnswerDaoJdbc implements AnswerDao {
    private DataBaseConnection con = new ConnectionH2();
    private Logger logger = LogManager.getLogger(AnswerDaoJdbc.class);
    private final String GET_SINGLE_ANSWER = "SELECT * FROM ENTITY_ANSWER WHERE ANSWERID=?";
    private final String GET_ALL_ANSWERS = "SELECT * FROM ENTITY_ANSWER";
    private final String UPDATE_ANSWER = "UPDATE ENTITY_ANSWER SET TYPE=?, ANSWER=?, IS_CORRECT=?" +
                                            " WHERE ANSWERID=?";
    private final String CREATE_ANSWER = "INSERT INTO ENTITY_ANSWER " +
                                            "(ANSWERID, TYPE, ANSWER, IS_CORRECT) " +
                                            "VALUES (?, ?, ?, ?)";
    private final String DELETE_ANSWER = "DELETE FROM ENTITY_ANSWER WHERE ANSWERID=?";

    public AnswerDaoJdbc() {

    }

    @Override public Answer getAnswer(int answerId) throws DaoException {
        logger.info("Trying to fetch answer from database by id " + answerId);
        try {
            PreparedStatement ps = con.getConnection().prepareStatement(GET_SINGLE_ANSWER);
            ps.setString(1, Integer.toString(answerId));
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                Answer a = new Answer(result.getInt(1),
                                        QuestionType.valueOf(result.getInt(2)),
                                        result.getString(3),
                                        result.getBoolean(4));
                logger.info("Found answer: " + a.toString());
                return a;
            } else {
                logger.warn("Could not find any answer with the given id " + answerId);
                return null;
            }
        } catch(Exception e) {
            throw new DaoException("Could not fetch answer with id " + answerId);
        }
    }

    @Override public List<Answer> getAnswer() {
        logger.info("Trying to fetch all answers from database");
        List<Answer> answerList = new Vector<>();
        try {

        } catch(Exception e) {

        }

        return null;
    }

    @Override public Answer createAnswer(Answer a) {
        return null;
    }

    @Override public Answer updateAnswer(Answer a) {
        return null;
    }

    @Override public Answer deleteAnswer(Answer a) {
        return null;
    }
}
