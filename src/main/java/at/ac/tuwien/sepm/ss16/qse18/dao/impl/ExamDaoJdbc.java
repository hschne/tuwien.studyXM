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
import java.sql.SQLException;

/**
 * JDBC Implementation of the CRUD-Method of the
 *
 * @author Zhang Haixiang
 */
public class ExamDaoJdbc implements ExamDao{
    private ConnectionH2 database;
    private final ExamQuestionDao examQuestionDao = new ExamQuestionDaoJdbc(database);
    private static final Logger logger = LogManager.getLogger();

    @Autowired ExamDaoJdbc(ConnectionH2 database){
        this.database = database;
    }

    @Override public Exam create(Exam exam) throws DaoException {
        logger.debug("entering method create with parameters {}", exam);

        if(!DTOValidator.validate(exam)){
            throw new DaoException("Invalid values, please check your input");
        }

        PreparedStatement pstmt = null;

        try{
            pstmt = database.getConnection().prepareStatement("Insert into Exam Values(?, ?, ?, ?)");
            pstmt.setInt(1, exam.getExamid());
            pstmt.setTimestamp(2, exam.getCreated());
            pstmt.setBoolean(3, exam.getPassed());
            pstmt.setString(4, exam.getAuthor());
            pstmt.executeUpdate();

            for(Question q: exam.getExamQuestions()){
                examQuestionDao.create(exam, q);
            }

        }catch (SQLException e){
            throw new DaoException("Could not create Exam with values("
                + exam.getExamid() + ", " + exam.getCreated() + ", " + exam.getPassed()
                + ", " + exam.getAuthor() +")");

        }finally {
            if(pstmt != null){
                try{
                    pstmt.close();
                }catch (SQLException e){
                    throw new DaoException("Prepared Statement could not be closed");
                }
            }
        }

        return exam;
    }
}
