package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportQuestion;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Service public class QuestionConflictDetection {

    private static final  Logger logger = LogManager.getLogger();

    private Topic existingTopic;
    private ExportTopic importedTopic;
    private QuestionTopicDao questionTopicDao;
    private AnswerConflictDetection answerConflictDetection;

    private List<QuestionConflict> questionConflicts;

    @Autowired
    public void setAnswerConflictDetection(AnswerConflictDetection answerConflictDetection) {
        this.answerConflictDetection = answerConflictDetection;
    }

    @Autowired public void setQuestionTopicDao(QuestionTopicDao questionTopicDao) {
        this.questionTopicDao = questionTopicDao;
    }

    void setTopics(Topic existing, ExportTopic imported) {
        this.existingTopic = existing;
        this.importedTopic = imported;
        questionConflicts = new ArrayList<>();
    }

    List<QuestionConflict> getConflictingQuestions() throws ServiceException {
        List<Question> existingQuestions = getExistingQuestions();
        for (Question existingQuestion : existingQuestions) {
            List<ExportQuestion> importedQuestions = importedTopic.getQuestions();
            for (ExportQuestion importedQuestion : importedQuestions) {
                checkForConflict(existingQuestion, importedQuestion);
            }
        }
        return questionConflicts;
    }


    private void checkForConflict(Question existingQuestion, ExportQuestion importedQuestion)
        throws ServiceException {
        String existingQuestionText = existingQuestion.getQuestion();
        String importedQuestionText = importedQuestion.getQuestion().getQuestion();
        if (existingQuestionText.equals(importedQuestionText)) {
            checkIfAnswersEqual(existingQuestion, importedQuestion);
        }
    }

    private void checkIfAnswersEqual(Question existingQuestion, ExportQuestion importedQuestion)
        throws ServiceException {
        answerConflictDetection.setQuestions(existingQuestion, importedQuestion);
        createConflict(existingQuestion, importedQuestion);
    }

    private void createConflict(Question existingQuestion, ExportQuestion importedQuestion)
        throws ServiceException {
        QuestionConflict questionConflict = new QuestionConflict();
        questionConflict.setQuestions(existingQuestion, importedQuestion);
        if (answerConflictDetection.areAnswersEqual()) {
            questionConflict.setResolution(ConflictResolution.DUPLICATE);
        }
        questionConflicts.add(questionConflict);
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
