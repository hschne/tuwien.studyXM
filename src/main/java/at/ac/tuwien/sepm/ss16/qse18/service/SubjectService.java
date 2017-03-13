package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;

import java.util.List;

/**
 * Interface that defines the service layer of subject
 *
 * @author Hans-Joerg Schroedl
 */
public interface SubjectService {

    /**
     * Returns a subject specified by the ID
     *
     * @param id The id of the subject
     * @return A subject
     * @throws ServiceException
     */
    Subject getSubject(int id) throws ServiceException;

    /**
     * Implementations return a list of resourceListView
     *
     * @return A list of resourceListView
     * @throws ServiceException
     */
    List<Subject> getSubjects() throws ServiceException;

    /**
     * Implementations serve to create a subject
     *
     * @param subject The subject to create
     * @return The created subject
     * @throws ServiceException
     */
    Subject createSubject(Subject subject) throws ServiceException;

    /**
     * Implementations delete a subject
     *
     * @param subject The subject to delete
     * @return False if the operation failed, true if it succeeded
     * @throws ServiceException
     */
    boolean deleteSubject(Subject subject) throws ServiceException;

    /**
     * Implementations update a subject
     *
     * @param subject The subject to update
     * @return The updated subject
     * @throws ServiceException
     */
    Subject updateSubject(Subject subject) throws ServiceException;

}
