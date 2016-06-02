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

    /**
     * getExam
     * Retrieves an exam specified by its primary key.
     * @param examID The primary key of the exam in the database
     * @throws ServiceException
     * @return Returns an instance of exam which is pulled from the database
     */
    Exam getExam(int examID) throws ServiceException;

    /**
     * getExams
     * Recieves all exams in the database.
     * @throws ServiceException
     * @return Returns a list of all exams in the database.
     */
    List<Exam> getExams()throws ServiceException;

    /**
     * createExam
     * Saves a given exam persistently in the database.
     * @param exam The exam which shall be saved in the database
     * @param topic The topic from which the questions should be chosen
     * @param examTime duration of the exam
     * @throws ServiceException
     * @return Returns a persistently saved exam with a retrieved key as ExamId.
     */
    Exam createExam(Exam exam, Topic topic, int examTime)throws ServiceException;

    /**
     * deleteExam
     * Removes an entry of exam from the database.
     * @param exam The exam which shall be removed
     * @throws ServiceException
     * @return Returns the instance of exsam which is removed from the database
     */
    Exam deleteExam(Exam exam)throws ServiceException;


    /**
     * getAllQuestionsOfExam
     * Gets all questions of the given examID
     * @param examID primary key of the given exam
     * @throws ServiceException
     * @return Returns a list of the ID of all questions of the given examID
     */
    List<Integer> getAllQuestionsOfExam(int examID)throws ServiceException;
}
