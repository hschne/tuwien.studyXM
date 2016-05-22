package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExamDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExamQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.util.DTOValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC Implementation of the CRUD-Method of Exam
 *
 * @author Zhang Haixiang
 */
@Service public class ExamDaoJdbc implements ExamDao {
    private static final Logger logger = LogManager.getLogger();
    private final ExamQuestionDao examQuestionDao;
    private ConnectionH2 database;

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

            pstmt = database.getConnection().prepareStatement(
                "INSERT INTO entity_exam " + "(created, passed, author, subject)VALUES(?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
            pstmt.setTimestamp(1, exam.getCreated());
            pstmt.setBoolean(2, exam.getPassed());
            pstmt.setString(3, exam.getAuthor());
            pstmt.setInt(4, exam.getSubjectID());
            pstmt.executeUpdate();

            generatedKey = pstmt.getGeneratedKeys();

            if (generatedKey != null && generatedKey.next()) {
                exam.setExamid(generatedKey.getInt(1));
            } else {
                logger.error("Primary Key for exam could not be created");
                throw new DaoException("Exam could not be inserted into database");
            }

            for (Question q : exam.getExamQuestions()) {
                examQuestionDao.create(exam, q);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception in create with parameters {}: " + e, exam);
            throw new DaoException(
                "Could not create Exam with values(" + exam.getExamid() + ", " + exam.getCreated()
                    + ", " + exam.getPassed() + ", " + exam.getAuthor() + ")");

        } finally {
            if (generatedKey != null) {
                try {
                    generatedKey.close();
                } catch (SQLException e) {
                    logger.error("SQL Exception in create with parameters {}", exam, e);
                    throw new DaoException("Result Set could not be closed");
                }
            }

            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    logger.error("SQL Exception in create with parameters {}", exam, e);
                    throw new DaoException("Prepared Statement could not be closed");
                }
            }
        }

        return exam;
    }

    @Override public Exam delete(Exam exam) throws DaoException {
        logger.debug("entering method delete with parameters {}", exam);

        if (!DTOValidator.validate(exam)) {
            logger.error("Dao Exception delete() {}", exam);
            throw new DaoException("Invalid values, please check your input");
        }
        PreparedStatement pstmt = null;
        try {
            pstmt = this.database.getConnection().prepareStatement(
                "DELETE FROM entity_exam WHERE examid = ? AND created = ?"
                    + "AND passed = ? AND author = ? AND subject = ?");

            examQuestionDao.delete(exam.getExamid());
            pstmt.setInt(1, exam.getExamid());
            pstmt.setTimestamp(2, exam.getCreated());
            pstmt.setBoolean(3, exam.getPassed());
            pstmt.setString(4, exam.getAuthor());
            pstmt.setInt(5, exam.getSubjectID());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception in delete with parameters {}", exam, e);
            throw new DaoException(
                "Could not delete Exam with values(" + exam.getExamid() + ", " + exam.getCreated()
                    + ", " + exam.getPassed() + ", " + exam.getAuthor() + ")");
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    logger.error("SQL Exception in delete with parameters {}", exam, e);
                    throw new DaoException("Prepared Statement could not be closed");
                }
            }
        }

        return exam;
    }

    @Override public List<Exam> getAllExamsOfSubject(Subject subject) throws DaoException {
        int subjectID = subject.getSubjectId();
        logger.debug("entering method getAllExamsOfSubject with parameters {}", subjectID);
        List<Exam> examList = new ArrayList<>();

        Exam exam = null;

        if (subjectID <= 0) {
            logger.error("DaoException getAllExamsOfSubject with parameters {}", subjectID);
            throw new DaoException("Invalid subject ID, please check your input");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = this.database.getConnection()
                .prepareStatement("SELECT * FROM entity_exam WHERE subject = ?");
            pstmt.setInt(1, subjectID);
            rs = pstmt.executeQuery();

            examList = fillList(rs);

        } catch (SQLException e) {
            logger.error("SQL Exception in getAllExamsOfSubject with parameters {}", subjectID);
            throw new DaoException("Could not get List with of Exams with subject ID " + subjectID);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.error("SQL Exception in getAllExamsOfSubject with parameters {}", subjectID);
                throw new DaoException("Result Set could not be closed");
            }

            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                logger.error("SQL Excepiton in getAllExamsOfSubject with parameters {}", subjectID,
                    e);
                throw new DaoException("Prepared Statement could not be closed");
            }
        }

        return examList;
    }

    @Override public Exam getExam(int examID) throws DaoException {
        logger.debug("entering method getExam with parameters {}", examID);
        Exam exam = null;

        if (examID <= 0) {
            logger.error("DaoException getExam with parameters {}", examID);
            throw new DaoException("Invalid Exam ID, please check your input");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = this.database.getConnection()
                .prepareStatement("SELECT * FROM entity_exam WHERE examid = ?");
            pstmt.setInt(1, examID);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                exam = new Exam();
                exam.setExamid(rs.getInt("examid"));
                exam.setCreated(rs.getTimestamp("created"));
                exam.setAuthor(rs.getString("author"));
                exam.setPassed(rs.getBoolean("passed"));
                exam.setSubjectID(rs.getInt("subject"));
            }

        } catch (SQLException e) {
            logger.error("SQL Exception in getExam with parameters {}", examID);
            throw new DaoException("Could not get List with of Exams with Exam ID" + examID);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.error("SQL Exception in getExam with parameters {}", examID);
                throw new DaoException("Result Set could not be closed");
            }

            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                logger.error("SQL Excepiton in getExam with parameters {}", examID, e);
                throw new DaoException("Prepared Statement could not be closed");
            }
        }

        return exam;
    }

    @Override public List<Exam> getExams() throws DaoException {
        logger.debug("entering method getExams()");
        List<Exam> examList = new ArrayList<>();

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = this.database.getConnection().prepareStatement("SELECT * FROM entity_exam");
            rs = pstmt.executeQuery();

            examList = fillList(rs);


        } catch (SQLException e) {
            logger.error("SQL Exception in getExam with parameters {}");
            throw new DaoException("Could not get List with of Exams");
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.error("SQL Exception in getExams()", e);
                throw new DaoException("Result Set could not be closed");
            }

            try {
                if (pstmt != null) {
                    pstmt.close();
                }
            } catch (SQLException e) {
                logger.error("SQL Excepiton in getExams()", e);
                throw new DaoException("Prepared Statement could not be closed");
            }
        }

        return examList;
    }

    public List<Exam> fillList(ResultSet rs) throws SQLException {
        Exam exam;
        List<Exam> examList = new ArrayList<>();

        while (rs.next()) {
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
