package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;

import java.util.List;

/**
 * Inteface that represents the relationship between Subject and Question.
 * @author Zhang Haixiang
 */
public interface SubjectQuestionDao {
    List<Integer> getAllQuestionsOfSubject(Exam exam, int topicID)throws DaoException;
}
