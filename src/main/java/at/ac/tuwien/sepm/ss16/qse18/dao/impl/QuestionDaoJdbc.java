package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Ganiu
 */
public class QuestionDaoJdbc implements QuestionDao {
    private ConnectionH2 database;
    private Logger logger = LogManager.getLogger(QuestionDaoJdbc.class);

    @Autowired QuestionDaoJdbc(ConnectionH2 database){
        this.database = database;
    }

    @Override public Question getQuestion(int id) {
        return null;
    }

    @Override public List<Question> getQuestions() throws DaoException {
        logger.debug("entering method getQuestions()");
        List<Question> questions = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;

        try{
            stmt = database.getConnection().createStatement();
            rs = stmt.executeQuery("SELECT * FROM question;");

            while (rs.next()){
                Question q = new Question();
                q.setQuestionid(rs.getInt("questionid"));
                q.setQuestion(rs.getString("question"));
                q.setType(rs.getInt("type"));
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
            if(stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    logger.error("Statement couldn't close properly",e);
                    throw new DaoException("Statement couldn't close properly");
                }
            }
        }
        return questions;
    }

    @Override public Question createQuestion(Question subject) {
        return null;
    }

    @Override public Question deleteQuestion(Question subject) {
        return null;
    }

    @Override public Question updateQuestion(Question subject) {
        return null;
    }
}
