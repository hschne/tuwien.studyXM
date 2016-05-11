package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
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
 * @author Zhang Haixiang
 */
public class ExamQuestionDaoJdbc implements ExamQuestionDao {
    private ConnectionH2 database;
    private static final Logger logger = LogManager.getLogger();

    @Autowired ExamQuestionDaoJdbc(ConnectionH2 database){
        this.database = database;
    }

    @Override public void create(Exam exam, Question question) throws DaoException {
        logger.debug("entering method create with parameters {}", exam, question);

        if(!DTOValidator.validate(exam) || !DTOValidator.validate(question)){
            throw new DaoException("Invalid values, please check your input");
        }

        PreparedStatement pstm = null;

        try{
            pstm.getConnection().prepareStatement("?, ?, ? ?");
            pstm.setInt(1, exam.getExamid());
            pstm.setInt(2, question.getQuestionid());
            pstm.setBoolean(3, false);
            pstm.setBoolean(4, false);
            pstm.executeUpdate();

        }catch (SQLException e){
            throw new DaoException("Could not create ExamQuestion with values("
                + exam.getExamid() + ", " + question.getQuestionid() + ")");

        }finally {
            try {
                if (pstm != null) {
                    pstm.close();
                }
            }catch (SQLException e){
                throw new DaoException("Prepared Statement could not be closed");
            }
        }
    }
}
