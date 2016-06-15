package at.ac.tuwien.sepm.ss16.qse18.service.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
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
    private AnswerConflictDetection answerConflictDetection;

    private List<QuestionConflict> questionConflicts;

    @Autowired
    public void setAnswerConflictDetection(AnswerConflictDetection answerConflictDetection) {
        this.answerConflictDetection = answerConflictDetection;
    }

    @Autowired public void setQuestionTopicDao(QuestionTopicDao questionTopicDao) {
        this.questionTopicDao = questionTopicDao;
    }

    public void initialize(Topic existing, List<Question> importedQuestions) {
        this.existingTopic = existing;
        this.importedQuestions = importedQuestions;
        questionConflicts = new ArrayList<>();
    }

    public List<QuestionConflict> getConflictingQuestions() throws ServiceException {
        List<Question> existingQuestions = getExistingQuestions();
        for (Question existingQuestion : existingQuestions) {
            for (Question importedQuestion : importedQuestions) {
                checkForConflict(existingQuestion, importedQuestion);
            }
        }
        return questionConflicts;
    }

    private void checkForConflict(Question existingQuestion,
        Question importedQuestion) throws ServiceException {
        String existingQuestionText = existingQuestion.getQuestion();
        String importedQuestionText = importedQuestion.getQuestion();
        if (existingQuestionText.equals(importedQuestionText)) {
            checkIfAnswersEqual(existingQuestion,importedQuestion);
        }
    }

    private void checkIfAnswersEqual(Question existingQuestion, Question importedQuestion)
        throws ServiceException {
        //TODO: Get imported answers here
        List<Answer> importedAnswers = new ArrayList<>();
        answerConflictDetection.initialize(existingQuestion, importedAnswers);
        createConflict(existingQuestion, importedQuestion);

    }

    private void createConflict(Question existingQuestion, Question importedQuestion)
        throws ServiceException {
        QuestionConflict questionConflict =
            new QuestionConflict(existingQuestion, importedQuestion);
        if(answerConflictDetection.areAnswersEqual()){
            questionConflict.setResolution(ConflictResolution.EXISTING);
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
