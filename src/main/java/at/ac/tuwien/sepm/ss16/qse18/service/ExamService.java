package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
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

    ExerciseExam getExam(int examID) throws ServiceException;
    List<ExerciseExam> getExams()throws ServiceException;
    ExerciseExam createExam(ExerciseExam exerciseExam, Topic topic, int examTime)throws ServiceException;
    ExerciseExam deleteExam(ExerciseExam exerciseExam)throws ServiceException;
    List<Question> getRightQuestions(ExerciseExam exerciseExam, int topicID, int examTime) throws ServiceException;
    List<Integer> getAllQuestionsOfExam(int examID) throws ServiceException;
    List<Integer> getAnsweredQuestionsOfExam(int examID) throws ServiceException;
}
