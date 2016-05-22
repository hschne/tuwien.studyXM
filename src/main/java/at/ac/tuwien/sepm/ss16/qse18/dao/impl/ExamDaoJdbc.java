package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExamDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExamQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.util.DTOValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import static at.ac.tuwien.sepm.ss16.qse18.dao.StatementResultsetCloser.closeStatementsAndResultSets;

import java.sql.*;
import java.util.*;

/**
 * JDBC Implementation of the CRUD-Method of Exam
 *
 * @author Zhang Haixiang
 */
@Service
public class ExamDaoJdbc implements ExamDao{
    private ConnectionH2 database;
    private final ExamQuestionDao examQuestionDao;
    private static final Logger logger = LogManager.getLogger();

    @Autowired public ExamDaoJdbc(ConnectionH2 database, ExamQuestionDao examQuestionDao) {
        this.database = database;
        this.examQuestionDao = examQuestionDao;
    }

    @Override public Exam create(Exam exam, List<Question> questions) throws DaoException {
        logger.debug("entering method create with parameters {}", exam);

        if (!DTOValidator.validate(exam)) {
            logger.error("Dao Exception create() {}", exam);
            throw new DaoException("Invalid values, please check your input");
        }

        PreparedStatement pstmt = null;
        ResultSet generatedKey = null;
        java.util.Date date = new java.util.Date();

        try {
            exam.setCreated(new Timestamp(date.getTime()));
            exam.setPassed(false);

            pstmt = database.getConnection().prepareStatement("INSERT INTO ENTITY_EXAM "
                + "(CREATED, PASSED, AUTHOR, SUBJECT) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            pstmt.setTimestamp(1, exam.getCreated());
            pstmt.setBoolean(2, exam.getPassed());
            pstmt.setString(3, exam.getAuthor());
            pstmt.setInt(4, exam.getSubjectID());
            pstmt.executeUpdate();

            generatedKey = pstmt.getGeneratedKeys();

            if(generatedKey != null && generatedKey.next()) {
               exam.setExamid(generatedKey.getInt(1));
            } else {
                logger.error("Primary Key for exam could not be created");
                throw new DaoException("Exam could not be inserted into database");
            }

            for (Question q : exam.getExamQuestions()) {
                examQuestionDao.create(exam, q);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception in create with parameters {}: ", exam, e);
            throw new DaoException(
                "Could not create Exam with values(" + exam.getExamid() + ", " + exam.getCreated()
                    + ", " + exam.getPassed() + ", " + exam.getAuthor() + ")");

        } finally {
            closeStatementsAndResultSets(new Statement[]{pstmt}, new ResultSet[]{generatedKey});
        }

        return exam;
    }

    @Override public Exam delete(Exam exam) throws DaoException {
        logger.debug("entering method delete with parameters {}", exam);

        if(!DTOValidator.validate(exam)){
            logger.error("Dao Exception delete() {}", exam);
            throw new DaoException("Invalid values, please check your input");
        }

        PreparedStatement pstmt = null;

        try {
            pstmt = this.database.getConnection()
                .prepareStatement("DELETE FROM ENTITY_EXAM WHERE EXAMID = ? AND CREATED = ?"
                + "AND PASSED = ? AND AUTHOR = ? AND SUBJECT = ?");

            examQuestionDao.delete(exam.getExamid());

            pstmt.setInt(1, exam.getExamid());
            pstmt.setTimestamp(2, exam.getCreated());
            pstmt.setBoolean(3, exam.getPassed());
            pstmt.setString(4, exam.getAuthor());
            pstmt.setInt(5, exam.getSubjectID());
            pstmt.executeUpdate();

        } catch(SQLException e){
            logger.error("SQL Exception in delete with parameters {}", exam, e);
            throw new DaoException(
                "Could not delete Exam with values(" + exam.getExamid() + ", " + exam.getCreated()
                    + ", " + exam.getPassed() + ", " + exam.getAuthor() + ")");
        } finally {
            closeStatementsAndResultSets(new Statement[]{pstmt}, new ResultSet[]{});
        }

        return exam;
    }

    @Override public List<Exam> getAllExamsOfSubject(Subject subject) throws DaoException{
        int subjectID = subject.getSubjectId();
        logger.debug("entering method getAllExamsOfSubject with parameters {}", subjectID);
        List<Exam> examList = new ArrayList<>();
        Exam exam = null;

        if(subjectID <= 0){
            logger.error("DaoException getAllExamsOfSubject with parameters {}", subjectID);
            throw new DaoException("Invalid subject ID, please check your input");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            pstmt = this.database.getConnection()
                .prepareStatement("SELECT * FROM ENTITY_EXAM WHERE SUBJECT = ?");
            pstmt.setInt(1, subjectID);
            rs = pstmt.executeQuery();

            examList = fillList(rs);

        } catch (SQLException e){
            logger.error("SQL Exception in getAllExamsOfSubject with parameters {}", subjectID, e);
            throw new DaoException("Could not get List with of Exams with subject ID " + subjectID);
        } finally {
            closeStatementsAndResultSets(new Statement[]{pstmt}, new ResultSet[]{rs});
        }

        return examList;
    }

    @Override public Exam getExam(int examID) throws DaoException {
        logger.debug("entering method getExam with parameters {}", examID);
        Exam exam = null;

        if(examID <= 0) {
            logger.error("DaoException getExam with parameters {}", examID);
            throw new DaoException("Invalid Exam ID, please check your input");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = this.database.getConnection()
                .prepareStatement("SELECT * FROM ENTITY_EXAM WHERE EXAMID = ?");
            pstmt.setInt(1, examID);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                exam = new Exam();
                exam.setExamid(rs.getInt("examid"));
                exam.setCreated(rs.getTimestamp("created"));
                exam.setAuthor(rs.getString("author"));
                exam.setPassed(rs.getBoolean("passed"));
                exam.setSubjectID(rs.getInt("subject"));
            }

        } catch (SQLException e) {
            logger.error("SQL Exception in getExam with parameters {}", examID, e);
            throw new DaoException("Could not get List with of Exams with Exam ID" + examID);
        } finally {
            closeStatementsAndResultSets(new Statement[]{pstmt}, new ResultSet[]{rs});
        }

        return exam;
    }

    @Override public List<Exam> getExams() throws DaoException {
        logger.debug("entering method getExams()");
        List<Exam> examList = new ArrayList<>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = this.database.getConnection().prepareStatement("SELECT * FROM ENTITY_EXAM");
            rs = pstmt.executeQuery();

            examList = fillList(rs);


        } catch (SQLException e){
            logger.error("SQL Exception in getExam", e);
            throw new DaoException("Could not get List with of Exams");
        } finally {
            closeStatementsAndResultSets(new Statement[]{pstmt}, new ResultSet[]{rs});
        }

        return examList;
    }

    public List<Exam> fillList(ResultSet rs) throws SQLException{
        Exam exam;
        List<Exam> examList = new ArrayList<>();

        while(rs.next()) {
            exam = new Exam();

            exam.setExamid(rs.getInt("examid"));
            exam.setCreated(rs.getTimestamp("created"));
            exam.setAuthor(rs.getString("author"));
            exam.setPassed(rs.getBoolean("passed"));
            exam.setSubjectID(rs.getInt("subject"));

            examList.add(exam);
        }

        return examList;
    }

}
