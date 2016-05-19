package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;

import java.util.List;

public interface TopicQuestionService {

    List<Question> getQuestionsFromTopic(int topicId) throws ServiceException;
    List<Topic> getTopicFromQuestion(int questionId) throws ServiceException;

}
