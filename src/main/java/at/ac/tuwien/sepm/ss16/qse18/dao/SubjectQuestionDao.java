package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;

import java.util.List;

/**
 * Inteface SubjectQuestionDao
 * Data Access Object interface for the relation between subject and question.
 * Retrieves, questions from a certain subject from the persistency.
 *
 * @author Zhang Haixiang
 */
@FunctionalInterface
public interface SubjectQuestionDao {
    /**
     * getAllQuestionsOfSubject
     * Retrieves all questions from the given subjectID(contained in exerciseExam) and topic ID which are used to create an exerciseExam
     * @param exerciseExam The exerciseExam, which shall be created
     * @param topicID topic of the wanted questions
     * @return returns a List containing the ID's of the questions related to the given topicID and subjecID
     * @throws DaoException
     *
     * */
    List<Integer> getAllQuestionsOfSubject(ExerciseExam exerciseExam, int topicID)throws DaoException;
}
