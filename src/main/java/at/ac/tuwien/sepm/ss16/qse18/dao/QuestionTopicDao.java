package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;

import java.util.List;

/**
 * @author Philipp Ganiu
 */
public interface QuestionTopicDao {
    //TODO javadoc
    List<Question> getQuestionToTopic(Topic topic) throws DaoException;
}
