package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;

/**
 * @author Bicer Cem
 */
public interface ResourceQuestionDao {

    /**
     * Creates the reference from a {@param resource} to a {@param question}
     *
     * @param resource The resource that is beeing referred to from the {@param question}
     * @param question The question to refer to the {@param resource}
     * @exception DaoException
     */
    void createResourceQuestion(Resource resource, Question question) throws DaoException;

}
