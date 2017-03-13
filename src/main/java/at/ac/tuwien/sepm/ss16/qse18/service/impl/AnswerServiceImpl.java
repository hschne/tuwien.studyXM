package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.AnswerDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.service.AnswerService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Class AnswerServiceImpl
 * concrete implementatioin of AnswerService
 *
 * @author Felix Almer on 10.05.2016.
 */
@Service
public class AnswerServiceImpl implements AnswerService {
    private AnswerDao adao;
    private static final Logger logger = LogManager.getLogger(AnswerServiceImpl.class);

    @Autowired  public AnswerServiceImpl(AnswerDao ad) {
        this.adao = ad;
    }

    @Override public Answer getAnswer(int answerId) throws ServiceException {
        try {
            return this.adao.getAnswer(answerId);
        } catch(DaoException e) {
            logger.error("Could not fetch answer from database: ", e);
            throw new ServiceException("Could not fetch answer from database");
        }
    }

    @Override public List<Answer> getAnswer() throws ServiceException {
        try {
            return this.adao.getAnswer();
        } catch(DaoException e) {
            logger.error("Could not fetch list of all answers from the database: ",
                e);
            throw new ServiceException("Could not fetch list of all answers from database");
        }
    }

    @Override public Answer createAnswer(Answer a) throws ServiceException {
        try {
            return this.adao.createAnswer(a);
        } catch(DaoException e) {
            logger.error("Could not save answer persistently: ", e);
            throw new ServiceException("Could not save answer persistently");
        }
    }

    @Override public Answer updateAnswer(Answer a) throws ServiceException {
        try {
            return this.adao.updateAnswer(a);
        } catch(DaoException e) {
            logger.error("Could not update answer: ", e);
            throw new ServiceException("Could not update answer");
        }
    }

    @Override public void deleteAnswer(Answer a) throws ServiceException {
        try {
            adao.deleteAnswer(a);
        } catch(DaoException e) {
            logger.error("Could not delete answer: ", e);
            throw new ServiceException("Could not delete answer");
        }
    }

    @Override public Question getCorrespondingQuestion(Answer a) {
        //TODO
        return null;
    }

    @Override public boolean checkIfAnswersAreCorrect(Map<Answer, Boolean> answers){
        for(Map.Entry<Answer,Boolean> a : answers.entrySet()){
            if(a.getKey() != null) {
                if (a.getKey().isCorrect() && !a.getValue().booleanValue()){
                    return false; //correct answer not selected
                }
                if(!a.getKey().isCorrect() && a.getValue().booleanValue()){
                    return false; // wrong answer selected
                }
            }
        }
        return true;
    }

    @Override public boolean checkIfOpenAnswersAreCorrect(String text, Answer[] answers) {
        return text.contains(answers[0].getAnswer()) && text.contains(answers[1].getAnswer())
            && text.contains(answers[2].getAnswer()) && text.contains(answers[3].getAnswer());
    }
}
