package at.ac.tuwien.sepm.ss16.qse18.dao;

import java.util.List;

/**
 * @author Bicer Cem
 */
public interface SubjectTopicDao {

    List<Integer> getTopicIdsFromSubjectId(int subjectId) throws DaoException;
    List<Integer> getSubjectIdsFromTopicId(int topicId) throws DaoException;
}
