package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExamQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import static at.ac.tuwien.sepm.ss16.qse18.dao.StatementResultsetCloser.closeStatementsAndResultSets;
import static at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator.validate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class ExamQuestionDaoJdbc
 * concrete implementation of Interface ExamQuestionDao
 * This class has access to the h2 database that is defined in the ConnectionH2 class.
 *
 * @author Zhang Haixiang
 */
@Service
public class ExamQuestionDaoJdbc implements ExamQuestionDao {
    private ConnectionH2 database;
    private static final Logger logger = LogManager.getLogger();
    private static final String UPDATE_SQL = "UPDATE REL_EXAM_QUESTION SET QUESTION_PASSED = ?, "
        + "ALREADY_ANSWERED = ? WHERE EXAMID = ? AND QUESTIONID = ?;";

    @Autowired public ExamQuestionDaoJdbc(ConnectionH2 database) {
        this.database = database;
    }

    @Override public void create(Exam exam, Question question) throws DaoException {
        logger.debug("entering method create with parameters {}", exam, question);

        tryValidateExam(exam);
        tryValidateQuestion(question);

        PreparedStatement pstmt = null;

        try {
            pstmt = this.database.getConnection()
                .prepareStatement("INSERT INTO REL_EXAM_QUESTION VALUES(?, ?, ?, ?)");
            pstmt.setInt(1, exam.getExamid());
            pstmt.setInt(2, question.getQuestionId());
            pstmt.setBoolean(3, false);
            pstmt.setBoolean(4, false);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception in create with parameters {}", exam, question, e);
            throw new DaoException(
                "Could not create ExamQuestion with values(" + exam.getExamid() + ", "
                    + question.getQuestionId() + ")");
        } finally {
            closeStatementsAndResultSets(new Statement[]{pstmt}, new ResultSet[]{});
        }
    }

    @Override public void delete(int examID) throws DaoException {
        logger.debug("entering method delete with parameters {}", examID);

        if (examID <= 0) {
            logger.error("Dao Exception delete() {}", examID);
            throw new DaoException("Invalid examID, please check your input");
        }

        PreparedStatement pstmt = null;

        try {
            pstmt = this.database.getConnection().prepareStatement("DELETE FROM REL_EXAM_QUESTION "
                + "WHERE EXAMID = ?");

            pstmt.setInt(1, examID);
            pstmt.executeUpdate();

        } catch(SQLException e) {
            logger.error("SQL Exception in delete with parameters {}", examID, e);
            throw new DaoException(
                "Could not delete ExamQuestion with values(" + examID + ")");

        } finally {
            closeStatementsAndResultSets(new Statement[]{pstmt}, new ResultSet[]{});
        }
    }

    @Override public Map<Integer, Boolean> getAllQuestionBooleans(List<Integer> questionList)
        throws DaoException{
        logger.debug("entering method getALlQuestionBooleans with parameters {}");
        Map<Integer, Boolean> questionBoolean = new HashMap<>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            for(int e: questionList) {
                if(e <= 0){
                    logger.error("Invalid question ID");
                    throw new DaoException("Invalid question ID");
                }
                pstmt = this.database.getConnection()
                    .prepareStatement("SELECT * FROM REL_EXAM_QUESTION WHERE "
                    + "QUESTIONID = ? AND ALREADY_ANSWERED = ? ORDER BY QUESTIONID, EXAMID DESC");

                pstmt.setInt(1, e);
                pstmt.setBoolean(2, true);
                rs = pstmt.executeQuery();

                if(rs.next()) {
                    questionBoolean.put(rs.getInt("questionid"), rs.getBoolean("question_passed"));
                }
            }

        } catch(SQLException e) {
            logger.error("SQL Exception in getAllQuestionBooleans", e);
            throw new DaoException("Could not get List with all Question Booleans for Questions");
        } finally {
            closeStatementsAndResultSets(new Statement[]{pstmt}, new ResultSet[]{rs});
        }
        return questionBoolean;
    }

    @Override public List<Integer> getAllQuestionsOfExam(int examID) throws DaoException {
        logger.debug("entering method getALlQuestionsOfExam with parameters {}", examID);
        ArrayList<Integer> questionIDList = new ArrayList<>();

        if(examID <= 0) {
            logger.error("Dao Exception in getAllQuestionsofExam with parameters", examID);
            throw new DaoException("Invalid Exam ID, please check your input");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = this.database.getConnection()
                .prepareStatement("SELECT * FROM REL_EXAM_QUESTION WHERE EXAMID = ?");

            pstmt.setInt(1, examID);
            rs = pstmt.executeQuery();

            while(rs.next()) {
                questionIDList.add(rs.getInt("questionid"));
            }

        } catch(SQLException e) {
            logger.error("SQL Exception in delete with parameters {}", examID, e);
            throw new DaoException("Could not get List with all Questions for Exam ID " + examID);
        } finally {
            closeStatementsAndResultSets(new Statement[]{pstmt}, new ResultSet[]{rs});
        }
        return questionIDList;
    }

    @Override public void update(int examid, int questionid, boolean questionPassed, boolean alreadyAnswered)
        throws DaoException {
        logger.debug("Entering method update with parameters {}",examid,questionid,questionPassed,alreadyAnswered);

        PreparedStatement pstmt = null;

        try{
            pstmt = database.getConnection().prepareStatement(UPDATE_SQL);
            pstmt.setBoolean(1,questionPassed);
            pstmt.setBoolean(2,alreadyAnswered);
            pstmt.setInt(3,examid);
            pstmt.setInt(4,questionid);
            pstmt.executeUpdate();
        }
        catch (SQLException e){
            logger.error("Could not update relation exam question for exam with id " + examid +
                "and question with id " + questionid,e);
            throw new DaoException("Could not update relation exam question for exam with id " + examid +
                "and question with id " + questionid,e);
        }
        finally {
            closeStatementsAndResultSets(new Statement[]{pstmt},null);
        }
    }

    private void tryValidateExam(Exam exam) throws DaoException {
        try {
            validate(exam);
        } catch (DtoValidatorException e) {
            logger.error("Exam [" + exam + "] is invalid", e);
            throw new DaoException("Exam [" + exam + "] is invalid: " + e);
        }
    }

    private void tryValidateQuestion(Question question) throws DaoException {
        try {
            validate(question);
        } catch (DtoValidatorException e) {
            logger.error("Question [" + question + "] is invalid", e);
            throw new DaoException("Question [" + question + "] is invalid: " + e);
        }
    }
}
