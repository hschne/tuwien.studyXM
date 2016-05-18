package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExamQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.util.DTOValidator;
import org.apache.commons.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Zhang Haixiang
 */
public class SubjectQuestionDaoJdbc implements SubjectQuestionDao {
    private ConnectionH2 database;
    private static final Logger logger = LogManager.getLogger();

    @Autowired public SubjectQuestionDaoJdbc(ConnectionH2 database){
        this.database = database;
    }

    @Override public List<Integer> getAllQuestionsOfSubject(Exam exam, int topicID) throws DaoException {
        logger.debug("entering method getAllQuestionsOfSubject with parameters {}", exam);
        ArrayList<Integer> questionIDList = new ArrayList<>();

        if(!DTOValidator.validate(exam) || topicID <= 0){
            logger.error("Dao Exception create() {}", exam, topicID);
            throw new DaoException("Invalid values, please check your input");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            pstmt = this.database.getConnection().prepareStatement("Select * from rel_subject_topic "
                + "natural join rel_question_topic tc "
                + "where tc.topicid = ? and subjectid = ? order by questionid asc");

            pstmt.setInt(1, exam.getSubjectID());
            pstmt.setInt(2, topicID);
            rs = pstmt.executeQuery();

            while(rs.next()){
                questionIDList.add(rs.getInt("questionid"));
            }


        }catch (SQLException e){
            logger.error("SQL Exception in getAllQuestionsOfSubject with parameters {}", exam, topicID);
            throw new DaoException("Could not get List with all Questions for Exam with values("
                + exam.getExamid() + ", " + exam.getCreated() + ", " + exam.getPassed()
                + ", " + exam.getAuthor() + " and topicID " + topicID + ")");
        }finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.error("SQL Exception in getAllQuestionsOfSubject with parameters {}", exam, topicID);
                throw new DaoException("Result Set could not be closed");
            }

            try{
                if(pstmt != null){
                    pstmt.close();
                }
            }catch (SQLException e){
                logger.error("SQL Excepiton in getAllQuestionsOfSubject with parameters {}", exam, topicID, e);
                throw new DaoException("Prepared Statement could not be closed");
            }
        }

        return questionIDList;
    }
}
