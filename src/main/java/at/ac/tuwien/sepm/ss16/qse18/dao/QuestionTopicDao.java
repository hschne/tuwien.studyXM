package at.ac.tuwien.sepm.ss16.qse18.dao;

import java.util.List;

public interface QuestionTopicDao {

    List<Integer> getQuestionIdsFromTopicId(int topicId) throws DaoException;
    List<Integer> getTopicIdsFromQuestionId(int questionId) throws DaoException;

}
