package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;

/**
 * @author Bicer Cem
 */
public interface ResourceQuestionService {

    /**
     * Creates a reference from {@param question} to {@param resource}
     *
     * @param resource
     * @param question
     * @throws ServiceException
     */
    void createReference(Resource resource, Question question) throws ServiceException;

    /**
     * Gets the resource a {@param question} is referring to if there is any.
     *
     * @param question The question that refers to the resource
     * @return The resource that is being referred to or null if there is none
     * @throws ServiceException
     */
    Resource getResourceFromQuestion(Question question) throws ServiceException;
}
