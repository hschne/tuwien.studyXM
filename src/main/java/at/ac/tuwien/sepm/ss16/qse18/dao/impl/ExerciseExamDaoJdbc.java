package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExerciseExamDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExerciseExamQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import static at.ac.tuwien.sepm.ss16.qse18.dao.StatementResultsetCloser.closeStatementsAndResultSets;
import static at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator.validate;

import java.sql.*;
import java.util.*;

/**
 * Class ExerciseExamDaoJdbc
 * concrete implementation of Interface ExerciseExamDao
 * This class has access to the h2 database that is defined in the ConnectionH2 class.
 *
 * @author Zhang Haixiang
 */
@Repository
public class ExerciseExamDaoJdbc implements ExerciseExamDao {
    private ConnectionH2 database;
    private final ExerciseExamQuestionDao exerciseExamQuestionDao;
    private static final Logger logger = LogManager.getLogger();

    @Autowired public ExerciseExamDaoJdbc(ConnectionH2 database, ExerciseExamQuestionDao exerciseExamQuestionDao) {
        this.database = database;
        this.exerciseExamQuestionDao = exerciseExamQuestionDao;
    }

    @Override public ExerciseExam create(ExerciseExam exerciseExam, List<Question> questions) throws DaoException {
        logger.debug("entering method create with parameters {}", exerciseExam);

        tryValidateExam(exerciseExam);

        PreparedStatement pstmt = null;
        ResultSet generatedKey = null;
        java.util.Date date = new java.util.Date();

        try {
            exerciseExam.setCreated(new Timestamp(date.getTime()));
            exerciseExam.setPassed(false);

            pstmt = database.getConnection().prepareStatement("INSERT INTO ENTITY_EXERCISE_EXAM "
                + "(EXAM, CREATED, PASSED, AUTHOR, SUBJECT, EXAMTIME) VALUES (?, ?, ?, ?, ?,?)",
                Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, exerciseExam.getExam());
            pstmt.setTimestamp(2, exerciseExam.getCreated());
            pstmt.setBoolean(3, exerciseExam.getPassed());
            pstmt.setString(4, exerciseExam.getAuthor());
            pstmt.setInt(5, exerciseExam.getSubjectID());
            pstmt.setLong(6, exerciseExam.getExamTime());
            pstmt.executeUpdate();

            generatedKey = pstmt.getGeneratedKeys();

            if(generatedKey != null && generatedKey.next()) {
               exerciseExam.setExamid(generatedKey.getInt(1));
            } else {
                logger.error("Primary Key for exerciseExam could not be created");
                throw new DaoException("ExerciseExam could not be inserted into database");
            }

            for (Question q : exerciseExam.getExamQuestions()) {
                exerciseExamQuestionDao.create(exerciseExam, q);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception in create with parameters {}: ", exerciseExam, e);
            throw new DaoException(
                "Could not create ExerciseExam with values(" + exerciseExam.getExamid() + ", " + exerciseExam
                    .getCreated()
                    + ", " + exerciseExam.getPassed() + ", " + exerciseExam.getAuthor() + ")");

        } finally {
            closeStatementsAndResultSets(new Statement[]{pstmt}, new ResultSet[]{generatedKey});
        }

        return exerciseExam;
    }

    @Override public ExerciseExam delete(ExerciseExam exerciseExam) throws DaoException {
        logger.debug("entering method delete with parameters {}", exerciseExam);

        tryValidateExam(exerciseExam);

        PreparedStatement pstmt = null;

        try {
            pstmt = this.database.getConnection()
                .prepareStatement("DELETE FROM ENTITY_EXAM WHERE EXAMID = ? AND CREATED = ?"
                + "AND PASSED = ? AND AUTHOR = ? AND SUBJECT = ?");

            exerciseExamQuestionDao.delete(exerciseExam.getExamid());

            pstmt.setInt(1, exerciseExam.getExamid());
            pstmt.setTimestamp(2, exerciseExam.getCreated());
            pstmt.setBoolean(3, exerciseExam.getPassed());
            pstmt.setString(4, exerciseExam.getAuthor());
            pstmt.setInt(5, exerciseExam.getSubjectID());
            pstmt.executeUpdate();

        } catch(SQLException e){
            logger.error("SQL Exception in delete with parameters {}", exerciseExam, e);
            throw new DaoException(
                "Could not delete ExerciseExam with values(" + exerciseExam.getExamid() + ", " + exerciseExam
                    .getCreated()
                    + ", " + exerciseExam.getPassed() + ", " + exerciseExam.getAuthor() + ")");
        } finally {
            closeStatementsAndResultSets(new Statement[]{pstmt}, new ResultSet[]{});
        }

        return exerciseExam;
    }

    @Override public List<ExerciseExam> getAllExamsOfSubject(Subject subject) throws DaoException{
        int subjectId = subject.getSubjectId();
        logger.debug("entering method getAllExamsOfSubject with parameters {}", subjectId);
        List<ExerciseExam> exerciseExamList = new ArrayList<>();

        if(subjectId <= 0){
            logger.error("DaoException getAllExamsOfSubject with parameters {}", subjectId);
            throw new DaoException("Invalid subject ID, please check your input");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            pstmt = this.database.getConnection()
                .prepareStatement("SELECT * FROM ENTITY_EXERCISE_EXAM WHERE SUBJECT = ?");
            pstmt.setInt(1, subjectId);
            rs = pstmt.executeQuery();

            exerciseExamList = fillList(rs);

        } catch (SQLException e){
            logger.error("SQL Exception in getAllExamsOfSubject with parameters {}", subjectId, e);
            throw new DaoException("Could not get List with of Exams with subject ID " + subjectId);
        } finally {
            closeStatementsAndResultSets(new Statement[]{pstmt}, new ResultSet[]{rs});
        }

        return exerciseExamList;
    }

    @Override public ExerciseExam getExam(int examID) throws DaoException {
        logger.debug("entering method getExam with parameters {}", examID);
        ExerciseExam exerciseExam = null;

        if(examID <= 0) {
            logger.error("DaoException getExam with parameters {}", examID);
            throw new DaoException("Invalid ExerciseExam ID, please check your input");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = this.database.getConnection()
                .prepareStatement("SELECT * FROM ENTITY_EXERCISE_EXAM WHERE EXAMID = ?");
            pstmt.setInt(1, examID);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                exerciseExam = new ExerciseExam();
                exerciseExam.setExam(rs.getInt("exam"));
                exerciseExam.setExamid(rs.getInt("examid"));
                exerciseExam.setCreated(rs.getTimestamp("created"));
                exerciseExam.setAuthor(rs.getString("author"));
                exerciseExam.setPassed(rs.getBoolean("passed"));
                exerciseExam.setSubjectID(rs.getInt("subject"));
            }

        } catch (SQLException e) {
            logger.error("SQL Exception in getExam with parameters {}", examID, e);
            throw new DaoException("Could not get List with of Exams with ExerciseExam ID" + examID);
        } finally {
            closeStatementsAndResultSets(new Statement[]{pstmt}, new ResultSet[]{rs});
        }

        return exerciseExam;
    }

    @Override public List<ExerciseExam> getExams() throws DaoException {
        logger.debug("entering method getExams()");
        List<ExerciseExam> exerciseExamList = new ArrayList<>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = this.database.getConnection().prepareStatement("SELECT * FROM ENTITY_EXERCISE_EXAM");
            rs = pstmt.executeQuery();

            exerciseExamList = fillList(rs);


        } catch (SQLException e){
            logger.error("SQL Exception in getExam", e);
            throw new DaoException("Could not get List with of Exams");
        } finally {
            closeStatementsAndResultSets(new Statement[]{pstmt}, new ResultSet[]{rs});
        }

        return exerciseExamList;
    }

    public List<ExerciseExam> fillList(ResultSet rs) throws SQLException{
        ExerciseExam exerciseExam;
        List<ExerciseExam> exerciseExamList = new ArrayList<>();

        while(rs.next()) {
            exerciseExam = new ExerciseExam();

            exerciseExam.setExamid(rs.getInt("examid"));
            exerciseExam.setExam(rs.getInt("exam"));
            exerciseExam.setCreated(rs.getTimestamp("created"));
            exerciseExam.setAuthor(rs.getString("author"));
            exerciseExam.setPassed(rs.getBoolean("passed"));
            exerciseExam.setSubjectID(rs.getInt("subject"));
            exerciseExam.setExamTime(rs.getLong("examtime"));

            exerciseExamList.add(exerciseExam);
        }

        return exerciseExamList;
    }

    private void tryValidateExam(ExerciseExam exerciseExam) throws DaoException {
        try {
            validate(exerciseExam);
        } catch (DtoValidatorException e) {
            logger.error("ExerciseExam [" + exerciseExam + "] is invalid", e);
            throw new DaoException("ExerciseExam [" + exerciseExam + "] is invalid: " + e);
        }
    }

}
