package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;

import java.util.List;

/**
 * @author Philipp Ganiu
 */
public interface ExamService {

    Exam getExam(int examID) throws ServiceException;
    List<Exam> getExams()throws ServiceException;
    Exam createExam(Exam exam, Topic topic, int examTime)throws ServiceException;
    Exam deleteExam(Exam exam)throws ServiceException;
}
