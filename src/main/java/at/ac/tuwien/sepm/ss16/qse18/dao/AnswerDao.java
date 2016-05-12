package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;

import java.util.List;

/**
 * Interface AnswerDao
 * Data Access Object interface for answer.
 * Retrieves, saves, updates and deletes answers from the persistency
 * Created by Felix on 06.05.2016.
 */
public interface AnswerDao {
    /**
     * getAnswer
     * Receives an answer entity from the database by its primary key.
     * @param answerId The primary key which indicates the answer in the database.
     * @return Returns the fetched answer, returns null if not found.
     * @throws DaoException
     */
    public Answer getAnswer(int answerId) throws DaoException;

    /**
     * getAnswer
     * Receives a list of all answers in the database.
     * @return The list of all answers.
     * @throws DaoException
     */
    public List<Answer> getAnswer() throws DaoException;

    /**
     * createAnswer
     * Saves an answer in the database. The generated primary key is collected and set as new
     * answerId.
     * @param a The instance of answer which shall be saved persistently.
     * @return Returns the answer with its persistend primary key as answerId.
     * @throws DaoException
     */
    public Answer createAnswer(Answer a) throws DaoException;

    /**
     * updateAnswer
     * Changes one or more fields of a persistently saved instance of answer. If it is not yet
     * present in the database, a new entry is created.
     * @param a The answer which shall update a persistent answer with the same answerId.
     * @return Returns the up-to-date version of answer after it is written into the database.
     * @throws DaoException
     */
    public Answer updateAnswer(Answer a) throws DaoException;

    /**
     * deleteAnswer
     * Permanently removes an entry of answer in the database. If no valid answerId is present,
     * this method returns the given answer.
     * @param a The answer which shall be deleted from the database.
     * @return Returns the deleted answer.
     * @throws DaoException
     */
    public Answer deleteAnswer(Answer a) throws DaoException;
}
