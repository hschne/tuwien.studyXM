package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import java.util.List;


/**
* @author Philipp Ganiu
* */
public interface QuestionDao {

    Question getQuestion(int id);

    /**
     * Returns a List of all questions.
     *
     * @return a List containing all questions.
     * @throws DaoException if there is no conncetion to the resource
     * */
    List<Question> getQuestions() throws DaoException;

    Question createQuestion(Question subject);

    Question deleteQuestion(Question subject);

    Question updateQuestion(Question subject);
}
