package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;

import java.util.List;

public interface QuestionDao {

    Question getQuestion(int id) throws DaoException;

    List<Question> getQuestions() throws DaoException;

    Question createQuestion(Question q) throws DaoException;

    Question deleteQuestion(Question q) throws DaoException;

    Question updateQuestion(Question q) throws DaoException;
}
