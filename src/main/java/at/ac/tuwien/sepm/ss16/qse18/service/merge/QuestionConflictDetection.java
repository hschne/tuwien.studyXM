package at.ac.tuwien.sepm.ss16.qse18.service.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Service public class QuestionConflictDetection {

    private final static Logger logger = LogManager.getLogger();

    private Topic existingTopic;
    private QuestionTopicDao questionTopicDao;
    private List<Question> importedQuestions;

    @Autowired public void setQuestionTopicDao(QuestionTopicDao questionTopicDao) {
        this.questionTopicDao = questionTopicDao;
    }

    public void initialize(Topic existing, List<Question> importedQuestions) {
        this.existingTopic = existing;
        this.importedQuestions = importedQuestions;
    }

    public List<QuestionConflict> getConflictingQuestions() throws ServiceException {
        List<QuestionConflict> result = new ArrayList<>();
        List<Question> existingQuestions = getExistingQuestions();
        for (Question existingQuestion : existingQuestions) {
            for (Question importedQuestion : importedQuestions) {
                checkForConflict(result, existingQuestion, importedQuestion);
            }
        }
        return result;
    }

    private void checkForConflict(List<QuestionConflict> result, Question existingQuestion,
        Question importedQuestion) {
        String existingQuestionText = existingQuestion.getQuestion();
        String importedQuestionText = importedQuestion.getQuestion();
        if (existingQuestionText.equals(importedQuestionText)) {
            QuestionConflict questionConflict =
                new QuestionConflict(existingQuestion, importedQuestion);
            result.add(questionConflict);
        }
    }

    private List<Question> getExistingQuestions() throws ServiceException {
        try {
            return questionTopicDao.getQuestionToTopic(existingTopic);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException("Could not load questions for topic.", e);
        }
    }
}
