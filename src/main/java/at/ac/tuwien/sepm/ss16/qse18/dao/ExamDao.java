package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;

import java.util.List;

/**
 * Interface ExamDao
 * Data Access Object interface for Exam.
 * Retrieves, saves, and deletes exams from the persistency
 *
 * @author Zhang Haixiang
 */
public interface ExamDao {

    /**
     * create
     * The given exam is put into the database and the primary key is retrieved, which indicates
     * the sucess of putting the exam into the database
     *
     * @param exam      The exam, which shall be saved
     * @param questions The questions the exam, which should be saved, contains
     * @return returns the exam with inserted primary key
     * @throws DaoException
     */
    Exam create(Exam exam, List<Question> questions) throws DaoException;

    /**
     * delete
     * The given exam is deleted from the database
     *
     * @param exam The exam, which shall be deleted
     * @return returns the exam, which was deleted from the database
     * @throws DaoException
     */
    Exam delete(Exam exam) throws DaoException;

    /**
     * getExam
     * Searches for the exam with the given examID in the database
     *
     * @param examID Primary Key of Exam
     * @return returns the exam with the given examID if its in the Database
     * @throws DaoException
     */
    Exam getExam(int examID) throws DaoException;

    /**
     * getExams
     * Gets all exams in the database and stores them in a List
     *
     * @return returns a list with all the exams in the database
     * @throws DaoException
     */
    List<Exam> getExams() throws DaoException;

    /**
     * getAllExamsOfSubject
     * Gets all exams of with given subjectID
     *
     * @param subject Subject containing the exams
     * @return returns a list with all the exams in the database with the given subjectID
     * @throws DaoException
     */
    List<Exam> getAllExamsOfSubject(Subject subject) throws DaoException;

}
