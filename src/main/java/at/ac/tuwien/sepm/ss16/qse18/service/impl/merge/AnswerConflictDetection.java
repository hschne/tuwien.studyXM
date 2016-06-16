package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
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
    private List<Answer> importedAnswers;
    private QuestionDao questionDao;

    public void initialize(Question question, List<Answer> importedAnswers) {
        this.question = question;
        this.importedAnswers = importedAnswers;
    }

    @Autowired public void setQuestionDao(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    public boolean areAnswersEqual() throws ServiceException {
        List<Answer> existingAnswers = getExistingAnswers();
        //TODO: Get actual imported importedAnswers here.
        if(existingAnswers.size() != importedAnswers.size()){
            logger.debug("Not the same number of answers.");
            return false;
        }

        return answerTextsEqual(existingAnswers);
    }

    private boolean answerTextsEqual(List<Answer> existingAnswers) {
        for (Answer existingAnswer : existingAnswers) {
            String existingText = existingAnswer.getAnswer();
            for (Answer importedAnswer : importedAnswers) {
                String importedText = importedAnswer.getAnswer();
                if (existingText.equals(importedText)) {
                    continue;
                }
                //No matching imported answer has been found, so not identical.
                return false;
            }
        }
        return true;
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
