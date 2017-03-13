package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;

import java.util.List;

/**
 * Interface SubjectTopicQuestionService
 * defines service layer of questions of subject and topic
 *
 * @author Philipp Ganiu
 */
@FunctionalInterface public interface SubjectTopicQuestionService {
    /**
     * Returns all topics to a specific {@param subject} while also initializing the numberOfQuestion
     * variable of topic. F.e. if a topic has 10 question to this specific subject the numberOfQuestion
     * variable is initialized with 'questions: 10'
     *
     * @param subject the subject to which all topics are returned
     * @return List<Topic> a List containing all above specified topics
     * */
    List<Topic> getTopicToSubjectWithNumberOfQuestions(Subject subject) throws ServiceException;
}
