package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ResourceDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidationException;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator.validate;

/**
 * @author Hans-Joerg Schroedl
 */
@Service public class ResourceServiceImpl implements ResourceService {

    private final Logger logger = LogManager.getLogger();

    private ResourceDao resourceDao;

    @Autowired public ResourceServiceImpl(ResourceDao resourceDao) {
        this.resourceDao = resourceDao;
    }

    @Override public Resource getResource(int id) throws ServiceException {
        try {
            return resourceDao.getResource(id);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override public List<Resource> getResources() throws ServiceException {
        try {
            return resourceDao.getResources();
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override public Resource createResource(Resource resource) throws ServiceException {
        try {
            validate(resource);
            return resourceDao.createResource(resource);
        } catch (DtoValidationException | DaoException e) {
            logger.error(e);
            throw new ServiceException(e);
        }
    }

    @Override public Resource deleteResource(Resource resource) throws ServiceException {
        return null;
    }

    @Override public Resource updateResource(Resource resource) throws ServiceException {
        return null;
    }

}
