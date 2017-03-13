package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;

import java.util.List;

/**
 * Interface QuestionService
 * defines service layer of question
 *
 * @author Felix Almer
 */
public interface QuestionService {
    /**
     * getQuestion
     * Queries the database for an entry of question with a given primary key.
     * @param questionId The primary key of the question which shall be returned.
     * @return Returns the question with the unique question ID if it is present.
     */
    public Question getQuestion(int questionId) throws ServiceException;

    /**
     * getQuestion
     * Queries the database for a list of all occurrences of question entities.
     * @return Returns a list of all question.
     */
    public List<Question> getQuestion() throws ServiceException;

    /**
     * createQuestion
     * Saves a question in the database, the primary key which was created is inserted into the
     * question instance as question ID and returned.
     * @param q The question which shall be saved persistently
     * @return Returns the question with a valid question ID
     */
    public Question createQuestion(Question q,Topic t) throws ServiceException;

    /**
     * updateQuestion
     * Updates a question: Changes one or more fields in the database entry of the given question.
     * If the question is note yet present in the database, it is created.
     * @param q The question with updated fields.
     * @return Returns the up-to-date question.
     */
    public Question updateQuestion(Question q,Topic t) throws ServiceException;

    /**
     * deleteQuestion
     * Removes an entry of question from the database.
     * @param q The question which shall be removed.
     * @return Returns the instance of question which is removed from the database.
     */
    public Question deleteQuestion(Question q) throws ServiceException;

    /**
     * setCorrespondingAnswers
     * Relates a question to multiple answers.
     * @param q The question which relates to answers in the given list of answers
     * @param al The answer
     * @return Returns true if the operation was successful, false otherwise
     */
    public boolean setCorrespondingAnswers(Question q, List<Answer> al) throws ServiceException;

    /**
     * getCorrespondingAnswers
     * Retrieves all corresponding answers to a given question.
     * @param q The question to which answer relation shall be found
     * @return Returns a list of all answers which correspond to the given question
     */
    public List<Answer> getCorrespondingAnswers(Question q) throws ServiceException;


    /**
     * getQuestionFromTopic
     * Returns a list of all questions that belong to a topic
     * @return topicId The given id of the topic that we want the questions to
     */
    public List<Question> getQuestionsFromTopic(Topic topic) throws ServiceException;

}
