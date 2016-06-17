package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;

/**
 * Interface for the statistic service layer
 *
 * @author Julian Strohmayer
 */

public interface StatisticService {

    /**
     * This method returns the number of answered questions for a specific topic.
     *
     * @param topic topic for which the answered questions are counted.
     * @return the amount of answered questions from this topic.
     * [0] = answered/not answered ratio
     * [1] = answered questions
     * [2] = number of questions in the topic
     * @throws ServiceException
     */
    double[] getAnsweredQuestionsForTopic(Topic topic) throws ServiceException;

    /**
     * Calculates the average exercise performance for a given subject.
     *
     * @param subject subject for which the average grade will calculated.
     * @return the average grade for this subject [1.0d,5.0d]
     * @throws ServiceException
     */
    double[] gradeAllExamsForSubject(Subject subject) throws ServiceException;

    /**
     * Generates basic hints for every subject in the database.
     * @throws ServiceException
     */
    void initializeHints() throws ServiceException;

    /**
     * Returns the hint for a given subject.
     * @param subject the subject
     * @return the hint for this subject
     */
    String getHint(Subject subject) throws ServiceException;

    /**
     * Checks if the user has answered all questions of this subject
     * @param subject the subject
     * @return true if all questions were answered, false else
     * @throws ServiceException
     */
    boolean checkKnowItAllAchievement(Subject subject) throws ServiceException;
}
