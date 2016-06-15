package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExerciseExamQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
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
 * Class ExerciseExamQuestionDaoJdbc
 * concrete implementation of Interface ExerciseExamQuestionDao
 * This class has access to the h2 database that is defined in the ConnectionH2 class.
 *
 * @author Zhang Haixiang
 */
@Repository
public class ExerciseExamQuestionDaoJdbc implements ExerciseExamQuestionDao {
    private ConnectionH2 database;
    private static final Logger logger = LogManager.getLogger();
    private static final String UPDATE_SQL = "UPDATE REL_EXAM_QUESTION SET QUESTION_PASSED = ?, "
        + "ALREADY_ANSWERED = ? WHERE EXAMID = ? AND QUESTIONID = ?;";
    private static final String INVALID_INPUT = "Invalid exercise ID, please check your input";

    @Autowired public ExerciseExamQuestionDaoJdbc(ConnectionH2 database) {
        this.database = database;
    }

    @Override public void create(ExerciseExam exerciseExam, Question question) throws DaoException {
        logger.debug("entering method create with parameters {}", exerciseExam, question);

        tryValidateExam(exerciseExam);
        tryValidateQuestion(question);

        PreparedStatement pstmt = null;

        try {
            pstmt = this.database.getConnection()
                .prepareStatement("INSERT INTO REL_EXAM_QUESTION VALUES(?, ?, ?, ?)");
            pstmt.setInt(1, exerciseExam.getExamid());
            pstmt.setInt(2, question.getQuestionId());
            pstmt.setBoolean(3, false);
            pstmt.setBoolean(4, false);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception in create with parameters {}", exerciseExam, question, e);
            throw new DaoException(
                "Could not create ExamQuestion with values(" + exerciseExam.getExamid() + ", "
                    + question.getQuestionId() + ")");
        } finally {
            closeStatementsAndResultSets(new Statement[]{pstmt}, new ResultSet[]{});
        }
    }

    @Override public void delete(int examID) throws DaoException {
        logger.debug("entering method delete with parameters {}", examID);

        if (examID <= 0) {
            logger.error("Dao Exception delete() {}", examID);
            throw new DaoException(INVALID_INPUT);
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
        logger.debug("entering method getAllQuestionBooleans with parameters {}", questionList);
        Map<Integer, Boolean> questionBoolean = new HashMap<>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            for(int e: questionList) {
                if(e <= 0){
                    logger.error("Invalid question ID");
                    throw new DaoException(INVALID_INPUT);
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
            throw new DaoException("Could not get Map with all Question Booleans for Questions");
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
            throw new DaoException(INVALID_INPUT);
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
            throw new DaoException("Could not get List with all Questions for exercise ID " + examID);
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


    private void tryValidateExam(ExerciseExam exerciseExam) throws DaoException {
        try {
            validate(exerciseExam);
        } catch (DtoValidatorException e) {
            logger.error("exercise [" + exerciseExam + "] is invalid", e);
            throw new DaoException("exercise [" + exerciseExam + "] is invalid: " + e);
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
    
    
    @Override public List<Integer> getAnsweredQuestionsPerExam(int examID) throws DaoException {
        logger.debug("entering method getALlQuestionsOfExam with parameters {}", examID);
        ArrayList<Integer> questionIDList = new ArrayList<>();

        if(examID <= 0) {
            logger.error("Dao Exception in getAnsweredQuestionsPerExam with parameters", examID);
            throw new DaoException("Invalid exercise ID, please check your input");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = this.database.getConnection()
                .prepareStatement("SELECT * FROM REL_EXAM_QUESTION WHERE EXAMID = ? "
                    + "AND ALREADY_ANSWERED = TRUE");

            pstmt.setInt(1, examID);
            rs = pstmt.executeQuery();

            while(rs.next()) {
                questionIDList.add(rs.getInt("questionid"));
            }

        } catch(SQLException e) {
            logger.error("SQL Exception in getAnsweredQuestionsPerExam with parameters {}", examID, e);
            throw new DaoException("Could not get List with all Questions for exercise ID " + examID);
        } finally {
            closeStatementsAndResultSets(new Statement[]{pstmt}, new ResultSet[]{rs});
        }
        return questionIDList;
    }

    @Override public List<Integer> getNotAnsweredQuestionsPerExam(int examID) throws DaoException {
        logger.debug("entering method getNotAnsweredQuestionsOfExam with parameters {}", examID);
        List<Integer> questions = new ArrayList<>();

        if(examID <= 0) {
            logger.error("Dao Exception in getAnsweredQuestionsPerExam with parameters", examID);
            throw new DaoException(INVALID_INPUT);
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = this.database.getConnection()
                .prepareStatement("SELECT * FROM REL_EXAM_QUESTION WHERE EXAMID = ? "
                    + "AND ALREADY_ANSWERED = FALSE");

            pstmt.setInt(1, examID);
            rs = pstmt.executeQuery();

            while(rs.next()) {
                questions.add(rs.getInt("questionid"));
            }

        } catch(SQLException e) {
            logger.error("SQL Exception in getNotAnsweredQuestionsPerExam with parameters {}", examID, e);
            throw new DaoException("Could not get List with not answered Questions for exercise ID " + examID);
        } finally {
            closeStatementsAndResultSets(new Statement[]{pstmt}, new ResultSet[]{rs});
        }
        return questions;

    }

    @Override
    public Map<Integer, Boolean> getQuestionBooleansOfExam(int examID, List<Question> questionList)
        throws DaoException{
        logger.debug("entering method getQuestionBooleansOfExam with parameters {}", questionList);
        Map<Integer, Boolean> questionBoolean = new HashMap<>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        if(examID <= 0){
            logger.error("Dao Exception in getQuestionBooleansOfExam with parameters", examID);
            throw new DaoException(INVALID_INPUT);
        }

        try{
            for(Question e: questionList) {
                if(e.getQuestionId() <= 0){
                    logger.error("Invalid question ID");
                    throw new DaoException(INVALID_INPUT);
                }
                pstmt = this.database.getConnection()
                    .prepareStatement("SELECT * FROM REL_EXAM_QUESTION WHERE EXAMID = ? and "
                        + "QUESTIONID = ? AND ALREADY_ANSWERED = ?");

                pstmt.setInt(1, examID);
                pstmt.setInt(2, e.getQuestionId());
                pstmt.setBoolean(3, true);
                rs = pstmt.executeQuery();

                if(rs.next()) {
                    questionBoolean.put(rs.getInt("questionid"), rs.getBoolean("question_passed"));
                }
            }

        } catch(SQLException e) {
            logger.error("SQL Exception in getQuestionBooleansOfExam", e);
            throw new DaoException("Could not get Map with all Question Booleans for Exam");
        } finally {
            closeStatementsAndResultSets(new Statement[]{pstmt}, new ResultSet[]{rs});
        }
        return questionBoolean;
    }
}
