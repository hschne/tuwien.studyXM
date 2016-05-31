package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.DataBaseConnection;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static at.ac.tuwien.sepm.ss16.qse18.dao.StatementResultsetCloser.closeStatementsAndResultSets;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Class QuestionDaoJdbc
 * concrete implementation of Interface QuestionDao
 * This class has access to the h2 database that is defined in the ConnectionH2 class.
 *
 * @author Philipp Ganiu, Felix Almer
 */
@Service
public class QuestionDaoJdbc implements QuestionDao {
    private DataBaseConnection con;
    private static final Logger logger = LogManager.getLogger(QuestionDaoJdbc.class);
    private QuestionTopicDao questionTopicDao;

    private static final String GET_SINGLE_QUESTION = "SELECT * FROM ENTITY_QUESTION WHERE QUESTIONID=?";
    private static final String GET_ALL_QUESTIONS = "SELECT * FROM ENTITY_QUESTION";
    private static final String UPDATE_QUESTION = "UPDATE ENTITY_QUESTION SET TYPE=?, QUESTION=?, "
        + "QUESTION_TIME WHERE QUESTIONID=?";
    private static final String CREATE_QUESTION = "INSERT INTO ENTITY_QUESTION " +
            "(TYPE, QUESTION, QUESTION_TIME) " + "VALUES (?, ?, ?)";
    private static final String DELETE_QUESTION = "DELETE FROM ENTITY_QUESTION WHERE QUESTIONID=?";

    @Autowired public QuestionDaoJdbc(DataBaseConnection database){
        this.con = database;
    }

    @Autowired public void setQuestionTopicDao(QuestionTopicDao questionTopicDao){
        this.questionTopicDao = questionTopicDao;
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
            logger.error("Could not fetch question", e);
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
            closeStatementsAndResultSets(new Statement[]{ps}, new ResultSet[]{rs});
        }
        return questions;
    }

    @Override public Question createQuestion(Question question,Topic topic) throws DaoException {
        logger.info("Now saving question in database");
        isQuestionNull(question);

        if(question.getQuestionId() > -1) {
            logger.info("Question already in database, aborting");
            throw new DaoException("Question already in database");
        }

        PreparedStatement ps = null;
        ResultSet generatedKey = null;

        try {
            ps = con.getConnection().prepareStatement(CREATE_QUESTION,
                Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, question.getType().getValue());
            ps.setString(2, question.getQuestion());
            ps.setLong(3, question.getQuestionTime());
            ps.executeUpdate();
            generatedKey = ps.getGeneratedKeys();
            if(generatedKey.next()) {
                question.setQuestionId(generatedKey.getInt(1));
                questionTopicDao.createQuestionTopic(question,topic);
                return question;
            } else {
                logger.error("Primary Key for question could not be created");
                throw new DaoException("Question could not be inserted into database");
            }
        } catch(Exception e) {
            logger.error("Could not execute query", e);
            throw new DaoException("Could not save question in database " + e.getMessage());
        } finally {
            closeStatementsAndResultSets(new Statement[]{ps}, new ResultSet[]{generatedKey});
        }
    }

    @Override public Question deleteQuestion(Question question) throws DaoException {
        logger.info("Now deleting quetion entry from database");
        isQuestionNull(question);

        if(question.getQuestionId() < 0) {
            logger.info("Question not in database, aborting");
            throw new DaoException("Question not in database");
        }

        PreparedStatement ps = null;

        try {
            ps = con.getConnection().prepareStatement(DELETE_QUESTION);
            ps.setInt(1, question.getQuestionId());
            ps.executeUpdate();
            return question;
        } catch(Exception e) {
            logger.error("Could not delete question from database", e);
            throw new DaoException("Could not delete question");
        } finally {
            closeStatementsAndResultSets(new Statement[]{ps}, new ResultSet[]{});
        }
    }

    @Override public Question updateQuestion(Question question,Topic topic) throws DaoException {
        logger.info("Now updating question in database");
        isQuestionNull(question);

        if(question.getQuestionId() < 0) {
            logger.info("Question not in database, creating new entry for the given instance");
            return this.createQuestion(question,topic);
        }

        PreparedStatement ps = null;

        try {
            ps = con.getConnection().prepareStatement(UPDATE_QUESTION);
            ps.setInt(1, question.getType().getValue());
            ps.setString(2, question.getQuestion());
            ps.setLong(3, question.getQuestionTime());
            ps.executeUpdate();
            return question;
        } catch(Exception e) {
            logger.error("Could not update question", e);
            throw new DaoException("Could not update question");
        } finally {
            closeStatementsAndResultSets(new Statement[]{ps}, new ResultSet[]{});
        }
    }

    @Override public boolean relateQuestionWithAnswers(Question q, List<Answer> al)
        throws DaoException {
        return false;
    }

    @Override public List<Answer> getRelatedAnswers(Question q) throws DaoException {
        return new ArrayList<>();
    }

    private void isQuestionNull(Question q) throws DaoException {
        if(q == null) {
            logger.error("Given question must not be null");
            throw new DaoException("Question must not be null");
        }
    }
}
