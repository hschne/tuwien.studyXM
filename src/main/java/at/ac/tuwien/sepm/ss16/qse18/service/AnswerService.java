package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;

import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;

/**
 * Interface AnswerService
 * defines service layer of answer
 *
 * Created by Felix on 10.05.2016.
 */
public interface AnswerService {
    /**
     * getAnswer
     * Retrieves an answer specified by its primary key.
     * @param answerId The primary key of the answer in the database
     * @return Returns an instance of answer which is pulled from the database
     */
    public Answer getAnswer(int answerId) throws ServiceException;

    /**
     * getAnswer
     * Recieves all answers in the database.
     * @return Returns a list of all answers in the database.
     */
    public List<Answer> getAnswer() throws ServiceException;

    /**
     * createAnswer
     * Saves a given answer persistently in the database.
     * @param a The answer which shall be saved in the database
     * @return Returns a persistently saved answer with a retrieved key as answerId.
     */
    public Answer createAnswer(Answer a) throws ServiceException;

    /**
     * updateAnswer
     * Updates a persisten instance of answer with fields provided by the argument answer.
     * @param a The up-to-date answer
     * @return Returns the up-to-date answer.
     */
    public Answer updateAnswer(Answer a) throws ServiceException;

    /**
     * deleteAnswer
     * Removes an entry of answer from the database.
     * @param a The answer which shall be removed
     * @return Returns the instance of answer which is removed from the database
     */
    public Answer deleteAnswer(Answer a) throws ServiceException;

    /**
     * getCorrespondingQuestion
     * Returns the corresponding question to a given answer.
     * @param a The answer to which the corresponding question shall be fetched.
     * @return The corresponding question.
     */
    public Question getCorrespondingQuestion(Answer a) throws ServiceException;

    boolean checkIfAnswersAreCorrect(Map<Answer,Boolean> answers);
    boolean checkIfOpenAnswersAreCorrect(String text, Answer[] answers);
}
