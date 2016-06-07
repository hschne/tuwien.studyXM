package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;

import java.util.List;
import java.util.Map;

/**
 * Interface ExerciseExamQuestionDao
 * Data Access Object interface for the relation between exams and questions.
 * Retrieves, saves, and deletes exams with the related questions from the persistency
 *
 * @author Zhang Haixiang
 */
public interface ExerciseExamQuestionDao {

    /**
     * create
     * The given exerciseExam and question is put into the database
     * @param exerciseExam The exerciseExam, which shall be saved
     * @param question The questions that belongs to the question
     * @throws DaoException
     *
     * */
    void create(ExerciseExam exerciseExam, Question question) throws DaoException;

    /**
     * delete
     * The given exam and the related questions are deleted from the database
     * @param examID primary Key of the exam
     * @throws DaoException
     *
     * */
    void delete(int examID) throws DaoException;

    /**
     * getAllQuestionBooleans
     * The Question ID and the Boolean whether a question has been answered correctly are retrieved
     * from the database and saved in a Map
     * @param questionList List of Questions of which the Booleans should be retrieved
     * @return returns a Hashmap that contains the questionID and the Booleans
     * @throws DaoException
     *
     * */
    Map<Integer, Boolean> getAllQuestionBooleans(List<Integer> questionList) throws DaoException;

    /**
     * getAllQuestionsOfExam
     * Retrieves all QuestionID's that are related to the given examID from the database
     * @param examID primary key of the exam
     * @return returns a List with all Question'IDs that are related to the given examID
     * @throws DaoException
     *
     * */
    List<Integer> getAllQuestionsOfExam(int examID) throws DaoException;

    List<Integer> getAnsweredQuestionsPerExam(int examID) throws DaoException;

}
