package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface that defines CRUD-Methods for ExamDao-Object
 *
 * @author Zhang Haixiang
 *
 */
public interface ExamDao {

    Exam create(Exam exam, ArrayList<Question> questions) throws DaoException;
    Exam delete(Exam exam) throws DaoException;
    Exam getExam(int examID) throws DaoException;
    List<Exam> getExams() throws DaoException;

}
