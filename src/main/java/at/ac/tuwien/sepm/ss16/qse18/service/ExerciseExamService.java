package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;

import java.util.List;

/**
 * Interface ExerciseExamService
 * defines service layer of exam
 *
 * @author Philipp Ganiu
 */
public interface ExerciseExamService {

    ExerciseExam getExam(int examID) throws ServiceException;
    List<ExerciseExam> getExams()throws ServiceException;
    ExerciseExam createExam(ExerciseExam exerciseExam, Topic topic, int examTime)throws ServiceException;
    ExerciseExam deleteExam(ExerciseExam exerciseExam)throws ServiceException;
    List<Question> getRightQuestions(ExerciseExam exerciseExam, int topicID, int examTime) throws ServiceException;
    List<Integer> getAnsweredQuestionsOfExam(int examID) throws ServiceException;
    List<Integer> getAllQuestionsOfExam(int examID) throws ServiceException;
    void update(int examid, int questionid, boolean questionPassed, boolean alreadyAnswered)
        throws ServiceException;

}
