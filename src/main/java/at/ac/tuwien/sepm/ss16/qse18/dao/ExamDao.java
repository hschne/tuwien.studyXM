package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;

/**
 * Interface that defines CRUD-Methods for ExamDao-Object
 *
 * @author Zhang Haixiang
 *
 */
public interface ExamDao {

    Exam create(Exam exam) throws DaoException;
}
