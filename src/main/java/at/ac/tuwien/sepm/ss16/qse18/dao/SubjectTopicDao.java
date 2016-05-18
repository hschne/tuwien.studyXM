package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;

import java.util.List;

/**
 * @author Philipp Ganiu
 */
public interface SubjectTopicDao {
    //TODO write javadoc
    void createSubjectTopic(Subject subject,Topic topic) throws DaoException;
    //TODO write javadoc
    void deleteSubjectTopic(Topic topic) throws DaoException;

    List<Topic> getTopicToSubject(Subject subject) throws DaoException;

}
