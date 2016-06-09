package at.ac.tuwien.sepm.ss16.qse18.service;

import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;

import java.util.List;
import java.util.Map;

/**
 * Interface ExerciseExamService
 * defines service layer of exam
 *
 * @author Philipp Ganiu, Zhang Haixiang
 */
public interface ExerciseExamService {

    /**
     * getExam
     * Retrieves an exam specified by its primary key.
     * @param examID The primary key of the exam in the database
     * @throws ServiceException
     * @return Returns an instance of exam which is pulled from the database
     */
    ExerciseExam getExam(int examID) throws ServiceException;

    /**
     * getExams
     * Recieves all exams in the database.
     * @throws ServiceException
     * @return Returns a list of all exams in the database.
     */
    List<ExerciseExam> getExams()throws ServiceException;

    /**
     * createExam
     * Saves a given exam persistently in the database.
     * @param exerciseExam The exam which shall be saved in the database
     * @param topic The topic from which the questions should be chosen
     * @param examTime duration of the exam
     * @throws ServiceException
     * @return Returns a persistently saved exam with a retrieved key as ExamId.
     */
    ExerciseExam createExam(ExerciseExam exerciseExam, Topic topic, int examTime)throws ServiceException;

    /**
     * deleteExam
     * Removes an entry of exam from the database.
     * @param exerciseExam The exam which shall be removed
     * @throws ServiceException
     * @return Returns the instance of exsam which is removed from the database
     */
    ExerciseExam deleteExam(ExerciseExam exerciseExam)throws ServiceException;


    /**
     * getAllQuestionsOfExam
     * Gets all questions of the given examID
     * @param examID primary key of the given exam
     * @throws ServiceException
     * @return Returns a list of the ID of all questions of the given examID
     */
    List<Integer> getAllQuestionsOfExam(int examID)throws ServiceException;

    /**
     * gradeExam
     * grades the given exam
     * @param exerciseExam the given exam that will be graded
     * @throws ServiceException
     * @return returns a String-Array with the length 3 containing the number of correct/incorrect
     * answered questions and the grade
     *
     * */
    String[] gradeExam(ExerciseExam exerciseExam) throws ServiceException;

    /**
     * topicGrade
     * grades the given exam based on the related topics
     * @param exerciseExam the given exam that will be graded
     * @throws ServiceException
     * @return returns a Map containing the topic and a String-Array with the length 3 containing
     * the number of correct/incorrect answered questions and the grade
     *
     * */
    Map<Topic, String[]> topicGrade(ExerciseExam exerciseExam) throws ServiceException;

    List<Integer> getAnsweredQuestionsOfExam(int examID) throws ServiceException;

    void update(int examid, int questionid, boolean questionPassed, boolean alreadyAnswered)
        throws ServiceException;

}
