package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.AnswerDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.DataBaseConnection;
import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Vector;

/**
 * Created by Felix on 06.05.2016.
 */
@Service
public class AnswerDaoJdbc implements AnswerDao {
    private DataBaseConnection con;
    private Logger logger = LogManager.getLogger(AnswerDaoJdbc.class);
    private final String GET_SINGLE_ANSWER = "SELECT * FROM ENTITY_ANSWER WHERE ANSWERID=?";
    private final String GET_ALL_ANSWERS = "SELECT * FROM ENTITY_ANSWER";
    private final String UPDATE_ANSWER = "UPDATE ENTITY_ANSWER SET TYPE=?," +
            " ANSWER=?, IS_CORRECT=?, " + "QUESTION=? WHERE ANSWERID=?";
    private final String CREATE_ANSWER = "INSERT INTO ENTITY_ANSWER " +
            "(TYPE, ANSWER, IS_CORRECT, QUESTION) " + "VALUES (?, ?, ?, ?)";
    private final String DELETE_ANSWER = "DELETE FROM ENTITY_ANSWER WHERE ANSWERID=?";

    @Autowired
    public AnswerDaoJdbc(DataBaseConnection db) throws DaoException {
        if(db == null) {
            throw new DaoException("Database Connection must not be null");
        }

        this.con = db;
    }

    @Override public Answer getAnswer(int answerId) throws DaoException {
        logger.info("Trying to fetch answer from database by id " + answerId);
        if(answerId < 0) {
            logger.warn("Answers with id smaller than 0 are not in the database");
            return null;
        }

        try {
            PreparedStatement ps = con.getConnection().prepareStatement(GET_SINGLE_ANSWER);
            ps.setInt(1, answerId);
            ResultSet result = ps.executeQuery();
            if (result.next()) {
                Answer a = new Answer(result.getInt(1),
                                        QuestionType.valueOf(result.getInt(2)),
                                        result.getString(3),
                                        result.getBoolean(4),
                                        this.getCorrespondingQuestion(result.getInt(5)));
                logger.info("Found answer: " + a.toString());
                return a;
            } else {
                logger.warn("Could not find any answer with the given id " + answerId);
                return null;
            }
        } catch(Exception e) {
            logger.debug(e.getMessage());
            throw new DaoException("Could not fetch answer with id " + answerId);
        }
    }

    @Override public List<Answer> getAnswer() throws DaoException {
        logger.info("Trying to fetch all answers from database");
        List<Answer> answerList = new Vector<>();
        try {
            PreparedStatement ps = con.getConnection().prepareStatement(GET_ALL_ANSWERS);
            ResultSet result = ps.executeQuery();
            while(result.next()) {
                Answer a = new Answer(result.getInt(1),
                    QuestionType.valueOf(result.getInt(2)),
                    result.getString(3),
                    result.getBoolean(4),
                    this.getCorrespondingQuestion(result.getInt(5)));
                logger.info("Found answer: " + a.toString());
                answerList.add(a);
            }
        } catch(Exception e) {
            logger.debug(e.getMessage());
            throw new DaoException("Could not fetch answers");
        }
        return answerList;
    }

    @Override public Answer createAnswer(Answer a) throws DaoException {
        logger.info("Trying to save answer persistently");
        if(a == null) {
            throw new DaoException("Answer must not be null");
        }

        if(a.getAnswerId() > 0) {
            throw new DaoException("Answer ID already in use");
        }

        if(a.getQuestion() == null || a.getQuestion().getQuestionId() < 0) {
            throw new DaoException("Can not create answer without corresponding question");
        }

        try {
            PreparedStatement ps = con.getConnection().prepareStatement(CREATE_ANSWER);
            ps.setInt(1, a.getType().getValue());
            ps.setString(2, a.getAnswer());
            ps.setBoolean(3, a.isCorrect());
            ps.setInt(4, a.getQuestion().getQuestionId());
            ps.executeUpdate();
            ResultSet key = ps.getGeneratedKeys();
            if(key.next())
                a.setAnswerId(key.getInt(1));
            logger.debug("Inserted Answer " + a.toString());
        } catch(Exception e) {
            logger.debug(e.getMessage());
            throw new DaoException("Could not save answer");
        }

        return a;
    }

    @Override public Answer updateAnswer(Answer a) throws DaoException {
        logger.info("Trying to modify answer");
        if(a == null) {
            throw new DaoException("Answer must not be null");
        }

        if(a.getAnswerId() < 0) {
            logger.info("Answer not yet in Database, now creating entry");
            return this.createAnswer(a);
        }

        try {
            PreparedStatement ps = con.getConnection().prepareStatement(UPDATE_ANSWER);
            ps.setInt(1, a.getType().getValue());
            ps.setString(2, a.getAnswer());
            ps.setBoolean(3, a.isCorrect());
            ps.setInt(4, a.getQuestion().getQuestionId());
            ps.setInt(5, a.getAnswerId());
            ps.executeUpdate();

            return this.getAnswer(a.getAnswerId());
        } catch(Exception e) {
            logger.debug(e.getMessage());
            throw new DaoException("Could not modify answer");
        }
    }

    @Override public Answer deleteAnswer(Answer a) throws DaoException {
        logger.info("Removing answer from database");
        if(a == null) {
            throw new DaoException("Answer must not be null");
        }

        if(a.getAnswerId() < 0) {
            logger.info("Answer not in database, nothing to do");
            return a;
        }

        try {
            PreparedStatement ps = con.getConnection().prepareStatement(DELETE_ANSWER);
            ps.setInt(1, a.getAnswerId());
            ps.executeUpdate();
            a.setAnswerId(-1);
            return a;
        } catch(Exception e) {
            logger.debug(e.getMessage());
            throw new DaoException("Could not delete answer");
        }
    }

    private Question getCorrespondingQuestion(int questionId) throws DaoException {
        return new QuestionDaoJdbc(this.con).getQuestion(questionId);
    }
}