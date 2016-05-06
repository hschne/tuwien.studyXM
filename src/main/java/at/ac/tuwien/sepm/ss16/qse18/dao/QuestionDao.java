package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;

import java.util.List;

public interface QuestionDao {

    Question getQuestion(int id);

    List<Question> getQuestions();

    Question createQuestion(Question q);

    Question deleteQuestion(Question q);

    Question updateQuestion(Question q);
}
