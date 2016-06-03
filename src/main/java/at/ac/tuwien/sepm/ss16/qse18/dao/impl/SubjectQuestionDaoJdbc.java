package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static at.ac.tuwien.sepm.ss16.qse18.dao.StatementResultsetCloser.closeStatementsAndResultSets;
import static at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator.validate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Class SubjectQuestionDaoJdbc
 * concrete implementation of Interface SubjectQuesitonDao
 * This class has access to the h2 database that is defined in the ConnectionH2 class.
 *
 * @author Zhang Haixiang
 */
@Service
public class SubjectQuestionDaoJdbc implements SubjectQuestionDao {
    private ConnectionH2 database;
    private static final Logger logger = LogManager.getLogger();

    @Autowired public SubjectQuestionDaoJdbc(ConnectionH2 database){
        this.database = database;
    }

    @Override public List<Integer> getAllQuestionsOfSubject(Exam exam, int topicID)
        throws DaoException {
        logger.debug("entering method getAllQuestionsOfSubject with parameters {}", exam);
        ArrayList<Integer> questionIDList = new ArrayList<>();

        tryValidateExam(exam);

        if(topicID <= 0) {
            logger.error("topicId must be greater than 0");
            throw new DaoException("topicId must be greater than 0");
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = this.database.getConnection().prepareStatement("SELECT * FROM REL_SUBJECT_TOPIC "
                + "NATURAL JOIN REL_QUESTION_TOPIC TC "
                + "WHERE tc.topicid = ? AND SUBJECTID = ? ORDER BY questionid ASC");

            pstmt.setInt(1, topicID);
            pstmt.setInt(2, exam.getSubjectID());
            rs = pstmt.executeQuery();

            while(rs.next()) {
                questionIDList.add(rs.getInt("questionid"));
            }

        } catch(SQLException e){
            logger.error("SQL Exception in getAllQuestionsOfSubject with parameters {}",
                exam, topicID, e);
            throw new DaoException("Could not get List with all Questions for Exam with values("
                + exam.getExamid() + ", " + exam.getCreated() + ", " + exam.getPassed()
                + ", " + exam.getAuthor() + " and topicID " + topicID + ")");
        } finally {
            closeStatementsAndResultSets(new Statement[] {pstmt}, new ResultSet[] {rs});
        }

        return questionIDList;
    }

    private void tryValidateExam(Exam exam) throws DaoException {
        try {
            validate(exam);
        } catch (DtoValidatorException e) {
            logger.error("Exam [" + exam + "] is invalid", e);
            throw new DaoException("Exam [" + exam + "] is invalid: " + e);
        }
    }
}
