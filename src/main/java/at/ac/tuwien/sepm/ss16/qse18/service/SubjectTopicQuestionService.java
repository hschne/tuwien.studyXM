package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;

import java.util.List;

/**
 * @author Philipp Ganiu
 */
public interface SubjectTopicQuestionService {
    //TODO javadoc
    List<Topic> getTopicToSubjectWithNumberOfQuestions(Subject subject) throws ServiceException;
}
