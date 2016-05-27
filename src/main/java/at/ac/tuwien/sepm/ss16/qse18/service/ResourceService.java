package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;

import java.util.List;

/**
 * Implementors provide functionality for resources
 *
 * @author Hans-Joerg Schroedl
 */
public interface ResourceService {

    Resource getResource(int id) throws ServiceException;

    List<Resource> getResources() throws ServiceException;

    Resource createResource(Resource resource) throws ServiceException;

    Resource deleteResource(Resource resource) throws ServiceException;

    Resource updateQuestion(Resource resource) throws ServiceException;
}
