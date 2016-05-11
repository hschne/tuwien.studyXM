package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;

import java.util.List;

/**
 * @author Philipp Ganiu
 */
public interface ExamService {

    Exam getExam(int id) throws ServiceException;
    List<Exam> getExams()throws ServiceException;
    Exam createExam(Exam exam)throws ServiceException;
    boolean deleteExam(Exam exam)throws ServiceException;
    Exam updateExam(Exam exam)throws ServiceException;

}
