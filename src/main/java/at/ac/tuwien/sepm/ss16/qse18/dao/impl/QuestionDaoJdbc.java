package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.DataBaseConnection;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Ganiu, Felix Almer
 */
@Service
public class QuestionDaoJdbc implements QuestionDao {
    private DataBaseConnection con;
    private Logger logger = LogManager.getLogger(QuestionDaoJdbc.class);

    private final String GET_SINGLE_QUESTION = "SELECT * FROM ENTITY_QUESTION WHERE QUESTIONID=?";
    private final String GET_ALL_QUESTIONS = "SELECT * FROM ENTITY_QUESTION";
    private final String UPDATE_QUESTION = "UPDATE ENTITY_QUESTION SET TYPE=?, QUESTION=?, "
        + "QUESTION_TIME WHERE QUESTIONID=?";
    private final String CREATE_QUESTION = "INSERT INTO ENTITY_QUESTION " +
        "(TYPE, ANSWER, IS_CORRECT, QUESTION_TIME) " +
        "VALUES (?, ?, ?, ?)";
    private final String DELETE_QUESTION = "DELETE FROM ENTITY_QUESTION WHERE QUESTIONID=?";

    @Autowired QuestionDaoJdbc(DataBaseConnection database){
        this.con = database;
    }

    @Override public Question getQuestion(int id) throws DaoException{
        logger.info("Trying to fetch single question from the database");
        if(id < 0) {
            logger.warn("Question not in the database");
            return null;
        }

        try {
            PreparedStatement ps = con.getConnection().prepareStatement(GET_SINGLE_QUESTION);
            ps.setString(1, Integer.toString(id));
            ResultSet result = ps.executeQuery();
            if(result.next()) {
                Question q = new Question(result.getInt(1),
                    result.getString(2),
                    QuestionType.valueOf(result.getInt(3)),
                    result.getLong(4));
                logger.debug("Found entry of question: " + q.toString());
                return q;
            } else {
                logger.info("Could not find an entry of question with the given id " + id);
                return null;
            }
        } catch(Exception e) {
            logger.error("Could not fetch question: " + e.getMessage());
            throw new DaoException("Could not fetch question");
        }
    }

    @Override public List<Question> getQuestions() throws DaoException {
        logger.debug("entering method getQuestions()");
        List<Question> questions = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            ps = con.getConnection().prepareStatement(GET_ALL_QUESTIONS);
            rs = ps.executeQuery();

            while (rs.next()){
                Question q = new Question(rs.getInt("QUESTIONID"),
                    rs.getString("QUESTION"), QuestionType.valueOf(rs.getInt("TYPE")),
                    rs.getLong("QUESTION_TIME"));
                logger.debug("Found question: " + q.toString());
                questions.add(q);
            }
        }
        catch (SQLException e){
            logger.error("SQL Exception getQuestions()",e);
            throw new DaoException("SQL Exception while getting all Questions");
        }
        finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("ResultSet couldn't close properly",e);
                    throw new DaoException("ResultSet couldn't close properly");
                }
            }
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    logger.error("Statement couldn't close properly",e);
                    throw new DaoException("Statement couldn't close properly");
                }
            }
        }
        return questions;
    }

    @Override public Question createQuestion(Question question) throws DaoException {
        logger.info("Now saving question in database");
        if(question == null) {
            logger.error("Given question must not be null");
            throw new DaoException("Question must not be null");
        }

        if(question.getQuestionId() > -1) {
            logger.info("Question already in database, aborting");
            throw new DaoException("Question already in database");
        }

        try {
            PreparedStatement ps = con.getConnection().prepareStatement(CREATE_QUESTION,
                Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, question.getType().getValue());
            ps.setString(2, question.getQuestion());
            ps.setLong(3, question.getQuestionTime());
            ps.executeUpdate();
            ResultSet generatedKey = ps.getGeneratedKeys();
            if(generatedKey.next()) {
                question.setQuestionId(generatedKey.getInt(1));
                return question;
            } else {
                logger.error("Primary Key for question could not be created");
                throw new DaoException("Question could not be inserted into database");
            }
        } catch(Exception e) {
            logger.error("Could not execute query " + e.getMessage());
            throw new DaoException("Could not save question in database " + e.getMessage());
        }
    }

    @Override public Question deleteQuestion(Question question) throws DaoException {
        logger.info("Now deleting quetion entry from database");
        if(question == null) {
            logger.error("Given question must not be null");
            throw new DaoException("Question must not be null");
        }

        if(question.getQuestionId() < 0) {
            logger.info("Question not in database, aborting");
            throw new DaoException("Question not in database");
        }

        try {
            PreparedStatement ps = con.getConnection().prepareStatement(DELETE_QUESTION);
            ps.setInt(1, question.getQuestionId());
            ps.executeUpdate();
            return question;
        } catch(Exception e) {
            logger.error("Could not delete question from database " + e.getMessage());
            throw new DaoException("Could not delete question");
        }
    }

    @Override public Question updateQuestion(Question question) throws DaoException {
        logger.info("Now updating question in database");
        if(question == null) {
            logger.error("Given question must not be null");
            throw new DaoException("Question must not be null");
        }

        if(question.getQuestionId() < 0) {
            logger.info("Question not in database, creating new entry for the given instance");
            return this.createQuestion(question);
        }

        try {
            PreparedStatement ps = con.getConnection().prepareStatement(UPDATE_QUESTION);
            ps.setInt(1, question.getType().getValue());
            ps.setString(2, question.getQuestion());
            ps.setLong(3, question.getQuestionTime());
            ps.executeUpdate();
            return question;
        } catch(Exception e) {
            logger.error("Could not update question " + e.getMessage());
            throw new DaoException("Could not update question");
        }
    }

    @Override public boolean relateQuestionWithAnswers(Question q, List<Answer> al)
        throws DaoException {
        return false;
    }

    @Override public List<Answer> getRelatedAnswers(Question q) throws DaoException {
        return null;
    }
}
