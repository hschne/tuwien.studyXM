package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;

import java.util.List;

/**
 * Inteface that represents the relationship between Subject and Question.
 * @author Zhang Haixiang
 */
public interface SubjectQuestionDao {
    /**
     * getAllQuestionsOfSubject
     * Retrieves all questions from the given subjectID(contained in exam) and topic ID which are used to create an exam
     * @param exam The exam, which shall be created
     * @param topicID topic of the wanted questions
     * @return returns a List containing the ID's of the questions related to the given topicID and subjecID
     * @throws DaoException
     *
     * */
    List<Integer> getAllQuestionsOfSubject(Exam exam, int topicID)throws DaoException;
}
