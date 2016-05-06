package at.ac.tuwien.sepm.ss16.qse18.dao;

import at.ac.tuwien.sepm.ss16.qse18.domain.Answer;

import java.util.List;

/**
 * Interface AnswerDao
 * Data Access Object interface for answer.
 * Retrieves, saves, updates and deletes answers from the persistency
 * Created by Felix on 06.05.2016.
 */
public interface AnswerDao {
    public Answer getAnswer(int answerId) throws DaoException;
    public List<Answer> getAnswer();
    public Answer createAnswer(Answer a);
    public Answer updateAnswer(Answer a);
    public Answer deleteAnswer(Answer a);
}
