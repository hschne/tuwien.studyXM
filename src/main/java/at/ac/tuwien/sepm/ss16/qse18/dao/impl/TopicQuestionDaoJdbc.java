package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.TopicQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service public class TopicQuestionDaoJdbc implements TopicQuestionDao {

    private static final Logger logger = LogManager.getLogger();
    private ConnectionH2 database;

    @Autowired public TopicQuestionDaoJdbc(ConnectionH2 database) {
        this.database = database;
    }

    @Override public List<Question> getQuestionsFromTopic(int topicId) throws DaoException {

        // TODO: Return all questions that belong to the topic with the given topicId

        return null;
    }

    @Override public List<Topic> getTopicsFromQuestion(int questionId) throws DaoException {

        // TODO: Return all topics that belong to the question with the given questionId

        return null;
    }
}
