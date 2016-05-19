package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;

import java.util.List;

/**
 * @author Philipp Ganiu
 */
public interface SubjectTopicDao {
    /**
     * Inserts a relation between a {@param topic} and a {@param topic} in the resource.
     *
     * @param subject the subject for which the relation is inserted
     * @param topic the topic for which the relation is inserted
     * @throws DaoException if there is no connection to the resource
     * */
    void createSubjectTopic(Subject subject,Topic topic) throws DaoException;
    /**
     * Deletes all relations a specific {@param topic} has to subjects.
     *
     * @param topic the topic for which all relations are deleted
     * @throws DaoException if there is no connection to the resource
     * */
    void deleteSubjectTopic(Topic topic) throws DaoException;

    /**
     * Returns all topics for a specific {@param subject}
     *
     * @param subject the subject for which all topics are returned
     * @throws DaoException if there is no connection to the resource
     * */
    List<Topic> getTopicToSubject(Subject subject) throws DaoException;

}
