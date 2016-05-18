package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExamDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExamQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.util.DTOValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC Implementation of the CRUD-Method of the
 *
 * @author Zhang Haixiang
 */
public class ExamDaoJdbc implements ExamDao{
    private ConnectionH2 database;
    private final ExamQuestionDao examQuestionDao;
    private static final Logger logger = LogManager.getLogger();

    @Autowired public ExamDaoJdbc(ConnectionH2 database, ExamQuestionDao examQuestionDao){
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

        try {
            pstmt = database.getConnection().prepareStatement("Insert into entity_exam Values(?, ?, ?, ?, ?)");
            pstmt.setInt(1, exam.getExamid());
            pstmt.setTimestamp(2, exam.getCreated());
            pstmt.setBoolean(3, exam.getPassed());
            pstmt.setString(4, exam.getAuthor());
            pstmt.setInt(5, exam.getSubjectID());
            pstmt.executeUpdate();

            for (Question q : exam.getExamQuestions()) {
                examQuestionDao.create(exam, q);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception in create with parameters {}", exam, e);
            throw new DaoException(
                "Could not create Exam with values(" + exam.getExamid() + ", " + exam.getCreated() + ", " + exam.getPassed()
                    + ", " + exam.getAuthor() + ")");

        } finally {
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

        if(!DTOValidator.validate(exam)){
            logger.error("Dao Exception delete() {}", exam);
            throw new DaoException("Invalid values, please check your input");
        }

        PreparedStatement pstmt = null;

        try{
            pstmt = this.database.getConnection().prepareStatement("Delete from entity_exam where examid = ? and created = ?"
                + "and passed = ? and author = ? and subject = ?");

            examQuestionDao.delete(exam);


            pstmt.setInt(1, exam.getExamid());
            pstmt.setTimestamp(2, exam.getCreated());
            pstmt.setBoolean(3, exam.getPassed());
            pstmt.setString(4, exam.getAuthor());
            pstmt.setInt(5, exam.getSubjectID());
            pstmt.executeUpdate();



        }catch(SQLException e){
            logger.error("SQL Exception in delete with parameters {}", exam, e);
            throw new DaoException(
                "Could not delete Exam with values(" + exam.getExamid() + ", " + exam.getCreated() + ", " + exam.getPassed()
                    + ", " + exam.getAuthor() + ")");
        }finally {
            if(pstmt != null){
                try{
                    pstmt.close();
                }catch (SQLException e){
                    logger.error("SQL Exception in delete with parameters {}", exam, e);
                    throw new DaoException("Prepared Statement could not be closed");
                }
            }
        }

        return exam;
    }

    @Override public Exam getExam(int examID) throws DaoException {
        logger.debug("entering method getExam with parameters {}", examID);
        Exam exam = null;

        if(examID <= 0){
            throw new DaoException("Invalid Exam ID, please check your input");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            pstmt = this.database.getConnection().prepareStatement("Select * from entity_exam where examid = ?");
            pstmt.setInt(1, examID);
            rs = pstmt.executeQuery();

            if(rs.next()){
                exam = new Exam();
                exam.setExamid(rs.getInt("examid"));
                exam.setCreated(rs.getTimestamp("created"));
                exam.setAuthor(rs.getString("author"));
                exam.setPassed(rs.getBoolean("passed"));
                exam.setSubjectID(rs.getInt("subject"));
            }

        }catch (SQLException e){
            logger.error("SQL Exception in getExam with parameters {}", examID);
            throw new DaoException("Could not get List with of Exams with Exam ID" + examID);
        }finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.error("SQL Exception in getExam with parameters {}", examID);
                throw new DaoException("Result Set could not be closed");
            }

            try{
                if(pstmt != null){
                    pstmt.close();
                }
            }catch (SQLException e){
                logger.error("SQL Excepiton in getExam with parameters {}", examID, e);
                throw new DaoException("Prepared Statement could not be closed");
            }
        }

        return exam;
    }

    @Override public List<Exam> getExams() throws DaoException {
        logger.debug("entering method getExams()");
        ArrayList<Exam> examList = new ArrayList<>();
        Exam exam;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            pstmt = this.database.getConnection().prepareStatement("Select * from entity_exam");
            rs = pstmt.executeQuery();

            while(rs.next()){
                exam = new Exam();

                exam.setExamid(rs.getInt("examid"));
                exam.setCreated(rs.getTimestamp("created"));
                exam.setAuthor(rs.getString("author"));
                exam.setPassed(rs.getBoolean("passed"));
                exam.setSubjectID(rs.getInt("subject"));

                examList.add(exam);
            }

        }catch (SQLException e){
            logger.error("SQL Exception in getExam with parameters {}");
            throw new DaoException("Could not get List with of Exams");
        }finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.error("SQL Exception in getExams()", e);
                throw new DaoException("Result Set could not be closed");
            }

            try{
                if(pstmt != null){
                    pstmt.close();
                }
            }catch (SQLException e){
                logger.error("SQL Excepiton in getExams()", e);
                throw new DaoException("Prepared Statement could not be closed");
            }
        }

        return examList;
    }


}
