package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ResourceDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableResource;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator.validate;

/**
 * @author Hans-Joerg Schroedl
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    private static final Logger logger = LogManager.getLogger();

    private ResourceDao resourceDao;

    @Autowired
    public ResourceServiceImpl(ResourceDao resourceDao) {
        this.resourceDao = resourceDao;
    }

    @Override
    public Resource getResource(int id) throws ServiceException {
        logger.debug("Getting resource with id {}", id);
        try {
            return resourceDao.getResource(id);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Resource> getResources() throws ServiceException {
        logger.debug("Getting resources");
        try {
            return resourceDao.getResources();
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Resource createResource(Resource resource) throws ServiceException {
        logger.debug("Creating resource with parameters {}", resource);
        try {
            validate(resource);
            return resourceDao.createResource(resource);
        } catch (DtoValidatorException | DaoException e) {
            logger.error(e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    @Override
    public Resource deleteResource(Resource resource) throws ServiceException {
        return null;
    }

    @Override
    public Resource updateResource(Resource resource) throws ServiceException {
        return null;
    }

    /**
     * Displays a given resource with the standard program selected by the user.
     * @param resource resource to open
     * @throws ServiceException
     */
    @Override
    public void openResource(Resource resource) throws ServiceException {

        if (Desktop.isDesktopSupported()) {
            try {
                File file = new File(resource.getReference());
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                throw new ServiceException("Unable to open resource, " +
                        "please select a standard program for this resource type." +
                        e.getMessage(), e);
            }
        }
    }

}
