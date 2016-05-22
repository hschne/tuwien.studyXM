package at.ac.tuwien.sepm.ss16.qse18.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.Statement;

public class StatementResultsetCloser {

    private static final Logger logger = LogManager.getLogger();

    private StatementResultsetCloser() {

    }

    public static void closeStatementsAndResultSets(Statement[] statements, ResultSet[] resultSets)
        throws DaoException {
        if (statements == null) {
            return;
        }
        for (Statement s : statements) {
            try {
                s.close();
            } catch (Exception e) {
                logger.error("Could not close statement.", e);
                throw new DaoException("Could not close statement: " + e.getMessage());
            }
        }

        if (resultSets == null) {
            return;
        }

        for (ResultSet rs : resultSets) {
            try {
                rs.close();
            } catch (Exception e) {
                logger.error("Could not close resultset.", e);
                throw new DaoException("Could not close resultset: " + e.getMessage());
            }
        }
    }
}
