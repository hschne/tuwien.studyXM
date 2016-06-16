package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportQuestion;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Service public class AnswerConflictDetection {

    private Logger logger = LogManager.getLogger();

    private Question question;
    private ExportQuestion importedQuestion;
    private QuestionDao questionDao;

    public void setQuestions(Question question, ExportQuestion importedQuestion) {
        this.question = question;
        this.importedQuestion = importedQuestion;

    }

    @Autowired public void setQuestionDao(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    public boolean areAnswersEqual() throws ServiceException {
        List<Answer> existingAnswers = getExistingAnswers();
        List<Answer> importedAnswers = importedQuestion.getAnswers();
        if (existingAnswers.size() != importedAnswers.size()) {
            logger.debug("Not the same number of answers.");
            return false;
        }

        return answerTextsEqual(existingAnswers);
    }

    private boolean answerTextsEqual(List<Answer> existingAnswers) {
        for (Answer existingAnswer : existingAnswers) {
            if(matchingImportedExists(existingAnswer)){
                continue;
            }
            //No exact matching imported answer has been found, so not identical.
            return false;
        }
        return true;
    }

    private boolean matchingImportedExists(Answer existingAnswer){
        List<Answer> importedAnswers = importedQuestion.getAnswers();
        for (Answer importedAnswer : importedAnswers) {
            if (answerTextEqual(existingAnswer, importedAnswer) &&
                answerCorrectEqual(existingAnswer, importedAnswer)){
                return true;
            }
        }
        return false;
    }

    private boolean answerTextEqual(Answer existingAnswer, Answer importedAnswer){
        String existingAnswerText = existingAnswer.getAnswer();
        String importedAnswerText = importedAnswer.getAnswer();
        return existingAnswerText.equals(importedAnswerText);
    }

    private boolean answerCorrectEqual(Answer existingAnswer, Answer importedAnswer){
        boolean existingCorrect = existingAnswer.isCorrect();
        boolean importedCorrect = importedAnswer.isCorrect();
        return existingCorrect == importedCorrect;
    }

    private List<Answer> getExistingAnswers() throws ServiceException {
        try {
            return questionDao.getRelatedAnswers(question);
        } catch (DaoException e) {
            logger.error(e);
            throw new ServiceException("Could not load importedAnswers from database.", e);
        }
    }



}
