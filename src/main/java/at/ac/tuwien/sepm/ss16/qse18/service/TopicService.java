package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;

import java.util.List;

/**
 * Interface TopicService
 * defines service layer of topic
 *
 * @author Philipp Ganiu, Bicer Cem
 */
public interface TopicService {
    /**
     * Returns a topic specified by {@param topicid}.
     *
     * @param topicid The id of the topic
     * @return the topid with {@param topicid}
     * @throws ServiceException if a DaoException is caught
     */
    Topic getTopic(int topicid) throws ServiceException;

    /**
     * This method returns a list of all topics in a resource
     *
     * @return a list of all topics in a resource
     * @throws ServiceException if a DaoException is caught
     */
    List<Topic> getTopics() throws ServiceException;

    /**
     * This method creates a new {@param topic}
     *
     * @param topic The topic to create
     * @param subject the subject to which the topic was created
     * @return The created subject
     * @throws ServiceException if a DaoException is caught
     */
    Topic createTopic(Topic topic,Subject subject) throws ServiceException;

    /**
     * This method deletes the {@param topic}
     *
     * @param topic The topic to delete
     * @return False if the operation failed, true if it succeeded
     * @throws ServiceException if a DaoException is caught
     */
    boolean deleteTopic(Topic topic) throws ServiceException;

    /**
     * This method updates a {@param topic}
     *
     * @param topic The topic to update
     * @return The updated topic
     * @throws ServiceException if a DaoException is caught
     */
    Topic updateTopic(Topic topic) throws ServiceException;

    /**
     * Returns a list of all topics that belong to {@param subject}
     *
     * @param subject the subject we want the topics to
     * @return a list containing every topic of the given {@param subject}
     * @throws ServiceException if an error occurs in the DAO layer
     */
    List<Topic> getTopicsFromSubject(Subject subject) throws ServiceException;

    /**
     * Returns a list of all topics that belong to {@param question}
     *
     * @param question the question we want the topics to
     * @return a list containing every topic of the given {@param question}
     * @throws ServiceException if an error occurs in the DAO layer
     */
    List<Topic> getTopicsFromQuestion(Question question) throws ServiceException;
}
