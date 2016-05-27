package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;

import java.util.List;

/**
 * Interface for creating, updating and deleting resources
 *
 * @author Hans-Joerg Schroedl
 */
public interface ResourceDao {

    Resource getResource(int id) throws DaoException;

    List<Resource> getResources() throws DaoException;

    Resource createResource(Resource resource) throws DaoException;

    Resource deleteResource(Resource resource) throws DaoException;

    Resource updateQuestion(Resource resource) throws DaoException;
}
