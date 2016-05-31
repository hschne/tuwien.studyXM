package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ResourceQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceQuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
            resourceQuestionDao.createResourceQuestion(resource, question);
        } catch (DaoException e) {
            logger.error("Could not create reference for question [" + question + "] to resource ["
                + resource + "]", e);
            throw new ServiceException(
                "Could not create reference for question [" + question + "] to resource ["
                    + resource + "]", e);
        }
    }
}
