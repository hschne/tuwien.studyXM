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
     * This method returns the number of correct answered questions for a specific topic.
     *
     * @param topic topic for which the correct questions are counted.
     * @return the amount of correct answered questions from this topic.
     * return[0] = correct/false ratio
     * return[1] = correct answered questions
     * return[2] = number of questions in the topic
     */
    double[] getCorrectQuestionsForTopic(Topic topic) throws ServiceException;

    /**
     * Calculates the average exercise performance for a given subject.
     *
     * @param subject subject for which the average grade will calculated.
     * @return the average grade for this subject [1.0d,5.0d]
     */
    double[] gradeAllExamsForSubject(Subject subject) throws ServiceException;
}
