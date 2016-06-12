package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;

import java.util.List;

/**
 * Created by Felix on 05.06.2016.
 */
public interface ExamDao {
    Exam create(Exam exam) throws DaoException;
    Exam delete(Exam exam) throws DaoException;
    Exam getExam(int examID) throws DaoException;
    List<Exam> getExams() throws DaoException;
}
