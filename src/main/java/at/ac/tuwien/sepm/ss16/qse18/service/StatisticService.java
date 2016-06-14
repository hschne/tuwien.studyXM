package at.ac.tuwien.sepm.ss16.qse18.service;

import org.springframework.stereotype.Service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;

/**
 * Interface for the statistic service layer
 * @author Julian Strohmayer
 */

@FunctionalInterface public interface StatisticService {

    /**
     * This method returns the number of correct answered questions for a specific topic.
     * @param topic topic for which the correct questions are counted.
     * @return the amount of correct answered questions from this topic
     * return[0] = correct/false ratio
     * return[1] = correct answered questions
     * return[2] = number of questions in the topic
     * @throws ServiceException
     */
    double[] getCorrectFalseRatio(Topic topic) throws ServiceException;

}
