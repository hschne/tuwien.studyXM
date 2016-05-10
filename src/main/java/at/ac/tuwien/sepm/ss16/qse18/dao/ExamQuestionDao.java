package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;

/**
 * @author Zhang Haixiang
 */
public interface ExamQuestionDao {
    void create(Exam exam, Question question) throws DaoException;

}
