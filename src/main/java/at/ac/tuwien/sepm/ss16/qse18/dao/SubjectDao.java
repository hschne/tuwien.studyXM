package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;

import java.util.List;

/**
 * Interface SubjectDao
 * Data Access Object interface for subject.
 * Retrieves, saves, updates and deletes subjects from the persistency
 *
 * @author Cem Bicer
 */

public interface SubjectDao {

    /**
     * Searches for the subject with subjectid {@param id}.
     *
     * @param id The subjectid to search for
     * @return If an subject with subjectid {@param id} exists then it returns the subject with
     * its information, else it returns null
     * @throws DaoException if there is no connection to the resource
     */
    Subject getSubject(int id) throws DaoException;


    /**
     * Gets very subject from resource.
     *
     * @return all a list containing every subject that has been found
     * @throws DaoException if there is no connection to the resource
     */
    List<Subject> getSubjects() throws DaoException;

    /**
     * Creates a subject with values from parameter {@param subject}.
     *
     * @param subject DTO that contains information for the subject to create
     * @throws DaoException if there is no connection to the resource or a subject with the
     *                      subjectid of {@param subject} already exists
     */
    Subject createSubject(Subject subject) throws DaoException;

    /**
     * Deletes an existing subject. All the information is stored in {@param subject}.
     *
     * @param subject DTO that contains information for the subject to delete
     * @throws DaoException if there is no connection to the resource or a subject with the
     *                      subjectid of {@param subject} does not exist
     */
    Subject deleteSubject(Subject subject) throws DaoException;

    /**
     * Updates an existing subject. All the information is stored in {@param subject}.
     *
     * @param subject DTO that contains information for the subject to update
     * @throws DaoException if there is no connection to the resource or a subject with the
     *                      subjectid of {@param subject} does not exist
     */
    Subject updateSubject(Subject subject) throws DaoException;

}
