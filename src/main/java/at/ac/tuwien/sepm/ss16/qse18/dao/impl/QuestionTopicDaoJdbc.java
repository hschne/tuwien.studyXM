package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.DataBaseConnection;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidatorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.dao.StatementResultsetCloser.closeStatementsAndResultSets;
import static at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator.validate;

/**
 * Class QuestionTopicDaoJdbc
 * concrete implementation of Interface QuestionTopicDao
 * This class has access to the h2 database that is defined in the ConnectionH2 class.
 *
 * @author Philipp Ganiu, Bicer Cem, Hans-Jörg Schrödl
 */
@Repository public class QuestionTopicDaoJdbc implements QuestionTopicDao {

    private static final Logger logger = LogManager.getLogger();

    private DataBaseConnection database;

    /**
     * Default constructor.
     *
     * @param database The database to use.
     */
    @Autowired public QuestionTopicDaoJdbc(DataBaseConnection database) {
        this.database = database;
    }

    @Override public List<Topic> getTopicsFromQuestion(Question question) throws DaoException {
        logger.debug("Entering getTopicsFromQuestion with parameter [{}]", question);

        if (question == null) {
            logger.error("Question must not be null");
            throw new DaoException("Question is null in getTopicsFromQuestion()");
        }

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Topic> res = null;

        try {
            ps = database.getConnection().prepareStatement(
                "SELECT t.TOPICID,t.TOPIC FROM rel_question_topic NATURAL JOIN entity_topic t WHERE questionId = ?;");
            ps.setInt(1, question.getQuestionId());
            rs = ps.executeQuery();

            res = new LinkedList<>();

            while (rs.next()) {
                Topic tmp = new Topic();
                tmp.setTopicId(rs.getInt(1));
                tmp.setTopic(rs.getString(2));

                res.add(tmp);
            }
        } catch (SQLException e) {
            logger.error("Could not get all topics from question {}", question, e);
            throw new DaoException("Could not get all topics from question " + question);

        } finally {
            closeStatementsAndResultSets(new Statement[] {ps}, new ResultSet[] {rs});
        }

        return res;
    }


    @Override public List<Question> getQuestionToTopic(Topic topic) throws DaoException {
        logger.debug("Entering getQuestionToTopic with parameters {}", topic);
        tryValidateTopic(topic);
        List<Question> questions = new ArrayList<>();
        try (PreparedStatement statement = prepareGetQuestionsForTopicStatement(topic);
            ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String questionText = resultSet.getString(2);
                QuestionType type = QuestionType.valueOf(resultSet.getInt(3));
                Long questionTime = resultSet.getLong(4);
                Question q = new Question(id, questionText, type, questionTime);
                questions.add(q);
            }
        } catch (SQLException e) {
            logger.error("Couldn't get all Quesitons to this Topic " + topic, e);
            throw new DaoException("Couldn't get all Questions to this Topic" + topic);
        }
        return questions;
    }

    @Override public void removeTopic(Topic topic) throws DaoException {
        tryValidateTopic(topic);
        try (PreparedStatement statement = prepareRemoveTopicStatment(topic)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException("Could not remove topic.", e);
        }
    }

    @Override public void removeQuestion(Question question) throws DaoException {
        tryValidateQuestion(question);
        try (PreparedStatement statement = prepareRemoveQuestionStatement(question)) {

            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException("Could not remove question.", e);
        }
    }

    @Override public void createQuestionInTopic(Question question, Topic topic)
        throws DaoException {
        logger.debug("Entering createQuestionInTopic with parameters {}", question, topic);
        tryValidateTopic(topic);
        tryValidateQuestion(question);
        try (PreparedStatement statement = prepareCreateQuestionInTopicStatment(question, topic)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException("Could not insert question for topic.", e);
        }
    }

    private void tryValidateTopic(Topic topic) throws DaoException {
        try {
            validate(topic);
        } catch (DtoValidatorException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    private void tryValidateQuestion(Question question) throws DaoException {
        try {
            validate(question);
        } catch (DtoValidatorException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    private PreparedStatement prepareGetQuestionsForTopicStatement(Topic topic)
        throws SQLException {
        String getQuestionForTopicStatment =
            "SELECT Q.QUESTIONID,Q.QUESTION,Q.TYPE,Q.QUESTION_TIME "
                + "FROM ENTITY_QUESTION Q JOIN REL_QUESTION_TOPIC R "
                + "ON Q.QUESTIONID = R.QUESTIONID WHERE R.TOPICID = ?";
        PreparedStatement statement =
            database.getConnection().prepareStatement(getQuestionForTopicStatment);
        statement.setInt(1, topic.getTopicId());
        return statement;
    }

    private PreparedStatement prepareCreateQuestionInTopicStatment(Question question, Topic topic)
        throws SQLException {
        String createStatement = "INSERT INTO REL_QUESTION_TOPIC VALUES(?,?);";
        PreparedStatement statement = database.getConnection().prepareStatement(createStatement);
        statement.setInt(1, question.getQuestionId());
        statement.setInt(2, topic.getTopicId());
        return statement;
    }

    private PreparedStatement prepareRemoveQuestionStatement(Question question)
        throws SQLException {
        PreparedStatement statement = database.getConnection()
            .prepareStatement("DELETE FROM REL_QUESTION_TOPIC WHERE QUESTIONID = ? ;");
        statement.setInt(1, question.getQuestionId());
        return statement;
    }

    private PreparedStatement prepareRemoveTopicStatment(Topic topic) throws SQLException {
        PreparedStatement statement = database.getConnection()
            .prepareStatement("DELETE FROM REL_QUESTION_TOPIC WHERE TOPICID =?;");
        statement.setInt(1, topic.getTopicId());
        return statement;
    }

}
