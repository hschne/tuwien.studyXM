package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.DataBaseConnection;
import at.ac.tuwien.sepm.ss16.qse18.dao.ResourceQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator.validate;

/**
 * @author Bicer Cem
 */
@Service public class ResourceQuestionDaoJdbc implements ResourceQuestionDao {

    private static final Logger logger = LogManager.getLogger();
    private DataBaseConnection database;

    @Autowired public ResourceQuestionDaoJdbc(DataBaseConnection database) {
        this.database = database;
    }

    @Override public void createResourceQuestion(Resource resource, Question question)
        throws DaoException {
        logger.debug("Entering createResourceQuestion with params [{}] and [{}]");

        tryValidateResource(resource);

        if (!validate(question)) {
            logger.error("Question [" + question + "] is invalid");
            throw new DaoException("Validation of question [" + question + "] failed");
        }

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

    private void tryValidateResource(Resource resource) throws DaoException {
        try {
            validate(resource);
        } catch (DtoValidatorException e) {
            logger.error("Resource is invalid", e);
            throw new DaoException("Resource is invalid: " + e);
        }
    }
}
