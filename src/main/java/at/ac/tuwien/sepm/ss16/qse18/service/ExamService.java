package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;

import java.util.List;

/**
 * Interface ExamService
 * defines service layer of exam
 *
 * @author Philipp Ganiu
 */
public interface ExamService {

    Exam getExam(int examID) throws ServiceException;
    List<Exam> getExams()throws ServiceException;
    Exam createExam(Exam exam, Topic topic, int examTime)throws ServiceException;
    Exam deleteExam(Exam exam)throws ServiceException;
    List<Integer> getAllQuestionsOfExam(int examID)throws ServiceException;
    void update(int examid, int questionid, boolean questionPassed, boolean alreadyAnswered) throws ServiceException;
}
