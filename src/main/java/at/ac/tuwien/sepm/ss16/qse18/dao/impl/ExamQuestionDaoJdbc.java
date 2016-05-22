package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExamQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.util.DTOValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zhang Haixiang
 */
@Service
public class ExamQuestionDaoJdbc implements ExamQuestionDao {
    private ConnectionH2 database;
    private static final Logger logger = LogManager.getLogger();

    @Autowired public ExamQuestionDaoJdbc(ConnectionH2 database) {
        this.database = database;
    }

    @Override public void create(Exam exam, Question question) throws DaoException {
        logger.debug("entering method create with parameters {}", exam, question);

        if (!DTOValidator.validate(exam) || !DTOValidator.validate(question)) {
            logger.error("Dao Exception create {}", exam);
            throw new DaoException("Invalid values, please check your input");
        }

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
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch(SQLException e) {
                logger.error("SQL Exception in create with parameters {}", exam, question, e);
                throw new DaoException("Prepared Statement could not be closed");
            }
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
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch(SQLException e) {
                logger.error("SQL Exception in delete with parameters {}", examID, e);
                throw new DaoException("Prepared Statement could not be closed");
            }
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
                    logger.error("SQL Exception in delete with parameters {}");
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
            try {
                if (rs!= null) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.error("SQL Exception in getAllQuestionBooleans with parameters {}", e);
                throw new DaoException("Result Set could not be closed");
            }

            try {
                if(pstmt != null) {
                    pstmt.close();
                }
            } catch(SQLException e){
                logger.error("SQL Excepiton in getAllQuestionBooleans with parameters {}", e);
                throw new DaoException("Prepared Statement could not be closed");
            }
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
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch(SQLException e) {
                logger.error("SQL Exception in getAllQuestionsOfExam with parameters {}",
                    examID, e);
                throw new DaoException("Result Set could not be closed");
            }

            try {
                if(pstmt != null) {
                    pstmt.close();
                }
            } catch(SQLException e) {
                logger.error("SQL Excepiton in getAllQuestionsOfExam with parameters {}",
                    examID, e);
                throw new DaoException("Prepared Statement could not be closed");
            }
        }
        return questionIDList;
    }


}
