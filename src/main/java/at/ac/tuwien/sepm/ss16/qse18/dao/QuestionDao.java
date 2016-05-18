package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import java.util.List;

/**
 * @author Felix Almer
 */
public interface QuestionDao {

    /**
     * getQuestion
     * Retrieves a single question specified by primary key from the database.
     * @param id The primary key present in the database
     * @return Returns the question associated with the primary key
     * @throws DaoException
     */
    public Question getQuestion(int id) throws DaoException;

    /**
     * getQuestions
     * Retrieves all question from the database.
     * @return Returns a list of all question present in the database
     * @throws DaoException
     */
    public List<Question> getQuestions() throws DaoException;

    /**
     * createQuestion
     * Saves a given question persistently in the database and retrieves a primary key which
     * indicates that the object was saved.
     * @param q The question which shall be saved
     * @return Returns the question with inserted primary key
     * @throws DaoException
     */
    public Question createQuestion(Question q) throws DaoException;

    /**
     * deleteQuestion
     * Deletes a question permanently from the database.
     * @param q The question which shall be deleted.
     * @return Returns the instance of question which was deleted from the database.
     * @throws DaoException
     */
    public Question deleteQuestion(Question q) throws DaoException;

    /**
     * updateQuestion
     * Updates all fields of a question in the database with the same primary key as the given
     * question.
     * @param q The question which shall override the question in the database.
     * @return Returns the updated question.
     * @throws DaoException
     */
    public Question updateQuestion(Question q) throws DaoException;

    /**
     * relateQuestionWithAnswers
     * Relates a question with a list of answers.
     * @param q The question which shall relate to a list of answers
     * @param al The list of answers which shall relate to a question
     * @return
     */
    public boolean relateQuestionWithAnswers(Question q, List<Answer> al) throws DaoException;

    /**
     * getRelatedAnswers
     * Retrieves a list of answers which are related to the question.
     * @param q The question to which the relating answers should be found.
     * @return Returns a list of answers which relate to the given question.
     * @throws DaoException
     */
    public List<Answer> getRelatedAnswers(Question q) throws DaoException;
}
