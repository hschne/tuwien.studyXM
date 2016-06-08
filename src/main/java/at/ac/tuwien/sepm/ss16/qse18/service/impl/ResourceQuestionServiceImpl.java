package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ResourceQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceQuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator.validate;

/**
 * @author Bicer Cem
 */
@Component public class ResourceQuestionServiceImpl implements ResourceQuestionService {

    private final static Logger logger = LogManager.getLogger();
    private ResourceQuestionDao resourceQuestionDao;

    @Autowired public ResourceQuestionServiceImpl(ResourceQuestionDao resourceQuestionDao) {
        this.resourceQuestionDao = resourceQuestionDao;
    }

    @Override public void createReference(Resource resource, Question question)
        throws ServiceException {
        logger.debug("Entering createReference with params [{}] and [{}]", resource, question);

        try {
            tryValidateInput(resource, question);

            resourceQuestionDao.createResourceQuestion(resource, question);
        } catch (DaoException e) {
            logger.error("Could not create reference for question [" + question + "] to resource ["
                + resource + "]", e);
            throw new ServiceException(
                "Could not create reference for question [" + question + "] to resource ["
                    + resource + "]", e);
        } catch (DtoValidatorException e) {
            logger.error("Input {} and {} is invalid", question, resource, e);
            throw new ServiceException("Input [" + question + "] and [" + resource + "] is invalid",
                e);
        }
    }

    @Override public Resource getResourceFromQuestion(Question question) throws ServiceException {
        logger.debug("Entering getResourceFromQuestion with question {}", question);

        try {
            validate(question);

            return resourceQuestionDao.getResourceOfQuestion(question);
        } catch (DtoValidatorException e) {
            logger.error("Question {} is invalid", question, e);
            throw new ServiceException("Question [" + question + "] is invalid", e);
        } catch (DaoException e) {
            logger.error("Could not get resource from question {}", question, e);
            throw new ServiceException("Could not get resource from question [" + question + "]",
                e);
        }
    }

    private void tryValidateInput(Resource resource, Question question)
        throws DtoValidatorException {
        validate(resource);
        validate(question);
    }
}
