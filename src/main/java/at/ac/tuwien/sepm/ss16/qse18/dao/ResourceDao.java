package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;

import java.util.List;

/**
 * Interface for creating, updating and deleting resources
 *
 * @author Hans-Joerg Schroedl
 */
public interface ResourceDao {

    /**
     * Returns a resource with resourceid {@param id}.
     *
     * @param id The id of the resource
     * @return A resource with the given parameter
     * @throws DaoException If resource could not be retrieved
     */
    Resource getResource(int id) throws DaoException;

    /**
     * Gets all available resources from the database
     *
     * @return A list of all resources
     * @throws DaoException If no resources could be retrieved
     */
    List<Resource> getResources() throws DaoException;


    /**
     * Creates a new resource
     *
     * @param resource The {@link Resource} to be saved to the database
     * @return A resource with a set resourceid
     * @throws DaoException If the resource could not be saved
     */
    Resource createResource(Resource resource) throws DaoException;

    /**
     * Deletes the given resource
     * @param resource The resource to be deleted
     * @return The deleted resource
     * @throws DaoException If the resource could not be deleted
     */
    Resource deleteResource(Resource resource) throws DaoException;

    /**
     * Updates the given resources
     *
     * @param resource The resource to be updated
     * @return The resource with updated fields
     * @throws DaoException If the resource could not be updated
     */
    Resource updateResource(Resource resource) throws DaoException;
}
