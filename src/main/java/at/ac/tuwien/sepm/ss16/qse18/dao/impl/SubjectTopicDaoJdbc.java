package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectTopicDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.dao.StatementResultsetCloser.closeStatementsAndResultSets;

/**
 * @author Bicer Cem
 */
@Service public class SubjectTopicDaoJdbc implements SubjectTopicDao {

    private static final Logger logger = LogManager.getLogger();
    private ConnectionH2 database;

    @Autowired public SubjectTopicDaoJdbc(ConnectionH2 database) {
        this.database = database;
    }

    @Override public List<Integer> getTopicIdsFromSubjectId(int subjectId) throws DaoException {
        logger.debug("Entering getTopicIdsFromSubjectId(" + subjectId + ")");

        return getItems('s',
            "SELECT topicid FROM rel_subject_topic WHERE subjectid = ? ORDER BY topicid ASC",
            subjectId);
    }

    @Override public List<Integer> getSubjectIdsFromTopicId(int topicId) throws DaoException {
        logger.debug("Entering getSubjectIdsFromTopicId(" + topicId + ")");

        return getItems('t',
            "SELECT subjectid FROM rel_subject_topic WHERE topicid = ? ORDER BY subjectid ASC",
            topicId);
    }

    private List<Integer> getItems(char typeOfId, String sqlStatement, int id) throws DaoException {
        List<Integer> res = null;
        PreparedStatement ps = null;
        ResultSet resultIds = null;

        try {
            ps = database.getConnection().prepareStatement(sqlStatement);
            ps.setInt(1, id);

            resultIds = ps.executeQuery();

            res = new LinkedList<>();

            while (resultIds.next()) {
                res.add(resultIds.getInt(1));
            }

        } catch (SQLException e) {
            if (typeOfId == 's') {
                logger.error("Could not get topicIds from subjectId (" + id + "): " + e);
                throw new DaoException("Could not get topicIds from subjectId(" + id + ")");
            } else if (typeOfId == 't') {
                logger.error("Could not get subjectId from topicId (" + id + "): " + e);
                throw new DaoException("Could not get subjectId from topicId(" + id + ")");
            }
        } finally {
            closeStatementsAndResultSets(new Statement[] {ps}, new ResultSet[] {resultIds});
        }
        return res;
    }
}
