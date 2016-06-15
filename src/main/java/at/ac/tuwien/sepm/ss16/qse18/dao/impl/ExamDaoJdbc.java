package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExamDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExerciseExamDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.dao.StatementResultsetCloser.closeStatementsAndResultSets;

/**
 * Implements {@link ExamDao}
 * for H2 Databases
 *
 * Created by Felix on 05.06.2016.
 */
@Repository public class ExamDaoJdbc implements ExamDao {
    private static final Logger logger = LogManager.getLogger(ExamDaoJdbc.class);
    private ConnectionH2 database;
    private ExerciseExamDao exerciseExamDao;

    @Autowired public ExamDaoJdbc(ConnectionH2 database) {
        this.database = database;
    }

    @Autowired public void setExerciseExamDao(ExerciseExamDao exerciseExamDao) {
        this.exerciseExamDao = exerciseExamDao;
    }

    @Override public Exam create(Exam exam) throws DaoException {
        logger.debug("entering method create with parameters {}", exam);

        //TODO: validate
        PreparedStatement pstmt = null;
        ResultSet generatedKey = null;
        java.util.Date now = new java.util.Date();

        try {
            exam.setCreated(new Timestamp(now.getTime()));

            pstmt = database.getConnection().prepareStatement("INSERT INTO ENTITY_EXAM"
                    + "(SUBJECT, CREATED, DUE_DATE, NAME) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, exam.getSubject());
            pstmt.setTimestamp(2, exam.getCreated());
            pstmt.setTimestamp(3, exam.getDueDate());
            pstmt.setString(4, exam.getName());

            pstmt.executeUpdate();

            generatedKey = pstmt.getGeneratedKeys();

            if (generatedKey != null && generatedKey.next()) {
                exam.setExamid(generatedKey.getInt(1));
            } else {
                logger.error("Primary key for exam could not be created");
                throw new DaoException("Exam could not be inserted into database");
            }
        } catch (SQLException e) {
            logger.error("SQL Exception in create with parameters {}", exam, e);
            throw new DaoException("Could not create exam", e);
        } finally {
            closeStatementsAndResultSets(new Statement[] {pstmt}, new ResultSet[] {generatedKey});
        }
        return exam;
    }

    @Override public void delete(Exam exam) throws DaoException {
        logger.debug("entering method delete with parameters {}", exam);
        deleteExerciseExams(exam);
        try {
            PreparedStatement pstmt = this.database.getConnection()
                .prepareStatement("DELETE FROM ENTITY_EXAM WHERE EXAMID = ?");
            pstmt.setInt(1, exam.getExamid());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException("Error deleting exam " + exam.toString());
        }
    }

    private void deleteExerciseExams(Exam exam) throws DaoException {
        List<ExerciseExam> exerciseExams = exerciseExamDao.getExerciseExamsFrom(exam);
        for(ExerciseExam exerciseExam : exerciseExams){
            exerciseExamDao.delete(exerciseExam);
        }
    }

    @Override public Exam getExam(int examID) throws DaoException {
        logger.debug("entering method getExam with parameter {}", examID);
        Exam exam = null;

        if (examID <= 0) {
            logger.error("DaoException getExam with parameters {}", examID);
            throw new DaoException("Invalid Exam ID, please check your input");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = this.database.getConnection()
                .prepareStatement("SELECT * FROM ENTITY_EXAM" + " WHERE EXAMID = ?");
            pstmt.setInt(1, examID);

            rs = pstmt.executeQuery();

            exam = fillList(rs).get(0);
        } catch (SQLException e) {
            logger.error("SQL Exception in getExam with parameters {}", examID, e);
            throw new DaoException("Could not get List with of Exams with Exam ID" + examID);
        } finally {
            closeStatementsAndResultSets(new Statement[] {pstmt}, new ResultSet[] {rs});
        }

        return exam;
    }

    @Override public List<Exam> getExams() throws DaoException {
        logger.debug("entering method getExams");
        List<Exam> examList = new ArrayList<>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = this.database.getConnection()
                .prepareStatement("SELECT * FROM ENTITY_EXAM " + "ORDER BY DUE_DATE ASC");
            rs = pstmt.executeQuery();
            examList = fillList(rs);

        } catch (SQLException e) {
            logger.error("SQL Exception in getExams", e);
            throw new DaoException("Could not get List with of Exams");
        } finally {
            closeStatementsAndResultSets(new Statement[] {pstmt}, new ResultSet[] {rs});
        }

        return examList;
    }

    private List<Exam> fillList(ResultSet rs) throws SQLException {
        Exam exam;
        List<Exam> examList = new ArrayList<>();

        while (rs.next()) {
            exam = new Exam();
            exam.setExamid(rs.getInt("examid"));
            exam.setSubject(rs.getInt("subject"));
            exam.setCreated(rs.getTimestamp("created"));
            exam.setDueDate(rs.getTimestamp("due_date"));
            exam.setName(rs.getString("name"));

            examList.add(exam);
        }

        return examList;
    }

    @Override public List<Exam> getAllExamsOfSubject(Subject subject) throws DaoException {
        logger.debug("entering method getExamsFor {}", subject);
        try {
            PreparedStatement pstmt = this.database.getConnection()
                .prepareStatement("SELECT * FROM ENTITY_EXAM WHERE SUBJECT = ?");
            pstmt.setInt(1, subject.getSubjectId());
            ResultSet rs = pstmt.executeQuery();
            return fillList(rs);
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException("Could not get list with of Exams", e);
        }
    }
}
