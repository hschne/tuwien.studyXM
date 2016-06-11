package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;

import java.util.List;

/**
 * Interface ExerciseExamDao
 * Data Access Object interface for exercise.
 * Retrieves, saves, and deletes exams from the persistency
 *
 * @author Zhang Haixiang
 */
public interface ExerciseExamDao {

    /**
     * create
     * The given exerciseExam is put into the database and the primary key is retrieved, which indicates
     * the sucess of putting the exerciseExam into the database
     *
     * @param exerciseExam      The exerciseExam, which shall be saved
     * @param questions The questions the exerciseExam, which should be saved, contains
     * @return returns the exerciseExam with inserted primary key
     * @throws DaoException
     */
    ExerciseExam create(ExerciseExam exerciseExam, List<Question> questions) throws DaoException;

    /**
     * update
     * The examtime of the exam with the given examid is updated
     *
     * @param examId the examid of the exam to be updated
     * @param examTime      the examtime that is updated
     * @throws DaoException
     */
    void update(int examId, long examTime) throws DaoException;

    /**
     * delete
     * The given exerciseExam is deleted from the database
     *
     * @param exerciseExam The exerciseExam, which shall be deleted
     * @return returns the exerciseExam, which was deleted from the database
     * @throws DaoException
     */
    ExerciseExam delete(ExerciseExam exerciseExam) throws DaoException;

    /**
     * getExam
     * Searches for the exam with the given examID in the database
     *
     * @param examID Primary Key of exercise
     * @return returns the exam with the given examID if its in the Database
     * @throws DaoException
     */
    ExerciseExam getExam(int examID) throws DaoException;

    /**
     * getExams
     * Gets all exams in the database and stores them in a List
     *
     * @return returns a list with all the exams in the database
     * @throws DaoException
     */
    List<ExerciseExam> getExams() throws DaoException;

    /**
     * getAllExamsOfSubject
     * Gets all exams of with given subjectID
     *
     * @param subject Subject containing the exams
     * @return returns a list with all the exams in the database with the given subjectID
     * @throws DaoException
     */
    List<ExerciseExam> getAllExamsOfSubject(Subject subject) throws DaoException;


}
