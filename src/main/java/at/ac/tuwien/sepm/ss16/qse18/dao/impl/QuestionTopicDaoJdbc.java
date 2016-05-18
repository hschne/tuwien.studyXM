package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Philipp Ganiu
 */
@Service
public class QuestionTopicDaoJdbc implements QuestionTopicDao {
    private ConnectionH2 database;
    private static final Logger logger = LogManager.getLogger(QuestionTopicDaoJdbc.class);
    private static final String QUESTIONTOTOPIC_SQL = "SELECT Q.QUESTIONID,Q.QUESTION,Q.TYPE,Q.QUESTION_TIME "
        + "FROM ENTITY_QUESTION Q NATURAL JOIN REL_QUESTION_TOPIC R WHERE R.TOPICID = ?;";


    @Autowired
    public QuestionTopicDaoJdbc(ConnectionH2 database) {
        this.database = database;
    }

    @Override
    public List<Question> getQuestionToTopic(Topic topic) throws DaoException{
        logger.debug("Entering getQuesitonToTopic with parameters {}",topic);
        if(topic == null){
            logger.error("Topic cannot be null");
            throw new DaoException("Topic cannot be null");
        }
        List<Question> questions = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            pstmt = database.getConnection().prepareStatement(QUESTIONTOTOPIC_SQL);
            pstmt.setInt(1,topic.getTopicId());
            rs = pstmt.executeQuery();

            while (rs.next()){
               Question q = new Question(rs.getInt(1),rs.getString(2),
                   QuestionType.valueOf(rs.getInt(3)),rs.getLong(4));
                questions.add(q);
            }
        }
        catch (SQLException e){
            logger.error("Couldn't get all Quesitons to this Topic " + topic,e);
            throw new DaoException("Couldn't get all Questions to this Topic" + topic);
        }
        finally {
            if(pstmt != null){
                try{
                    pstmt.close();
                }
                catch (SQLException e){
                    logger.error("Couldn't close Statement");
                    throw new DaoException("Couldn't close Statement");
                }
            }
            if(rs != null){
                try{
                    rs.close();
                }
                catch (SQLException e){
                    logger.error("Couldn't close ResultSet");
                    throw new DaoException("Couldn't close ResultSet");
                }
            }
        }
        return questions;
    }
}
