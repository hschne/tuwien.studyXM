package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;

import java.util.List;

/**
 * Implementors provide functionality for resources.
 *
 * Supported functions are adding, updating, retrieving and deleting resources. Additionally,
 * provides function for opening the resource.
 *
 * @author Hans-Joerg Schroedl
 */
public interface ResourceService {

    /**
     * Returns a resource with id {@param id}
     *
     * @param id The id of the resource
     * @return A resource with the given parameter
     * @throws ServiceException If resource could not be retrieved
     */
    Resource getResource(int id) throws ServiceException;

    /**
     * Gets all available resources.
     *
     * @return A list of all resources
     * @throws ServiceException If no resources could be retrieved
     */
    List<Resource> getResources() throws ServiceException;

    /**
     * Creates a new resource.
     *
     * @param resource The {@link Resource} to be saved to the database
     * @return A resource with a set resourceid
     * @throws ServiceException If the resource could not be saved
     */
    Resource createResource(Resource resource) throws ServiceException;

    /**
     * Deletes the given resource.
     *
     * @param resource The resource to be deleted
     * @return The deleted resource
     * @throws ServiceException If the resource could not be deleted
     */
    Resource deleteResource(Resource resource) throws ServiceException;


    /**
     * Updates the given resource.
     *
     * @param resource The resource to be updated
     * @return The resource with updated fields
     * @throws ServiceException If the resource could not be updated
     */
    Resource updateResource(Resource resource) throws ServiceException;

    /**
     * Displays a given resource.
     * @param resource resource to open
     * @throws ServiceException
     */
    void openResource(Resource resource) throws ServiceException;

}
