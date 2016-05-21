package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;

import java.util.List;

/**
 * @author Philipp Ganiu, Bicer Cem
 */
public interface QuestionTopicDao {
    /**
     * Returns all questions to a specific topic in a List from the Resource
     *
     * @param topic the topic for which all quesitons are returned
     * @return a List of all questions to the {@param topic}
     * @throws DaoException if there is no connection to the resource
     * */
    List<Question> getQuestionToTopic(Topic topic) throws DaoException;

    List<Topic> getTopicsFromQuestion(Question question) throws DaoException;
}
