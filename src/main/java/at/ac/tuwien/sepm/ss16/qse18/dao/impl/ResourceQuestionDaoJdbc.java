package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.DataBaseConnection;
import at.ac.tuwien.sepm.ss16.qse18.dao.ResourceQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.StatementResultsetCloser;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.ResourceType;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator.validate;

/**
 * @author Bicer Cem
 */
@Repository public class ResourceQuestionDaoJdbc implements ResourceQuestionDao {

    private static final Logger logger = LogManager.getLogger();
    private DataBaseConnection database;

    @Autowired public ResourceQuestionDaoJdbc(DataBaseConnection database) {
        this.database = database;
    }

    @Override public void createResourceQuestion(Resource resource, Question question)
        throws DaoException {
        logger
            .debug("Entering createResourceQuestion with params [{}] and [{}]", resource, question);

        tryValidateResource(resource);
        tryValidateQuestion(question);

        PreparedStatement ps = null;

        try {
            ps = database.getConnection()
                .prepareStatement("INSERT INTO rel_resource_question VALUES (?,?)");
            ps.setInt(1, resource.getResourceId());
            ps.setInt(2, question.getQuestionId());

            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Could not create entry in rel_resource_question with values (" + resource
                .getResourceId() + "," + question.getQuestionId() + ")", e);
            throw new DaoException(
                "Could not create entry in rel_resource_question with values (" + resource
                    .getResourceId() + "," + question.getQuestionId() + ")", e);
        }
    }

    @Override public Resource getResourceOfQuestion(Question question) throws DaoException {
        logger.debug("Entering getResourceOfQuestion with question {}", question);

        tryValidateQuestion(question);

        Resource res = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = database.getConnection().prepareStatement(
                "SELECT t.* FROM entity_topic t NATURAL JOIN rel_resource_question"
                    + " WHERE questionId = ?");

            rs = ps.executeQuery();

            if (rs.next()) {
                res = new Resource(rs.getInt(1), ResourceType.valueOf(rs.getString(2)),
                    rs.getString(3), rs.getString(4));
            }
        } catch (SQLException e) {
            logger.error("Could not get the resource of question {}", question, e);
            throw new DaoException("Could not get resource of question " + question + ": " + e);
        } finally {
            StatementResultsetCloser
                .closeStatementsAndResultSets(new Statement[] {ps}, new ResultSet[] {rs});
        }
        return res;
    }

    private void tryValidateResource(Resource resource) throws DaoException {
        try {
            validate(resource);
        } catch (DtoValidatorException e) {
            logger.error("Resource [" + resource + "] is invalid", e);
            throw new DaoException("Resource [" + resource + "] is invalid: " + e);
        }
    }

    private void tryValidateQuestion(Question question) throws DaoException {
        try {
            validate(question);
        } catch (DtoValidatorException e) {
            logger.error("Question [" + question + "] is invalid", e);
            throw new DaoException("Question [" + question + "] is invalid: " + e);
        }
    }
}
