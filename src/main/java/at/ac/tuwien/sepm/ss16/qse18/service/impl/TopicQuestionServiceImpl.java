package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.TopicQuestionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class TopicQuestionServiceImpl implements TopicQuestionService {

    private static final Logger logger = LogManager.getLogger();

    @Override public List<Question> getQuestionsFromTopic(int topicId) throws ServiceException {
        return null;
    }

    @Override public List<Topic> getTopicFromQuestion(int questionId) throws ServiceException {
        return null;
    }
}
