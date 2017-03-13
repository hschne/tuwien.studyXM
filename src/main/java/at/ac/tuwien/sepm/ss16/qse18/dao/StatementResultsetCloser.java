package at.ac.tuwien.sepm.ss16.qse18.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * StaementResultsetCloser
 * contains a static method that closes all open statements and result sets
 * @author Cem Bicer
 *
 * */
public class StatementResultsetCloser {

    private static final Logger logger = LogManager.getLogger();

    private StatementResultsetCloser() {

    }

    /**
     * closeStatementsAndResultSets
     * closes all given statements and result sets
     * @param statements an array that contains the statements which should be closed
     * @param resultSets an array that contains the result sets which should be closed
     * @throws DaoException
     *
     * */
    public static void closeStatementsAndResultSets(Statement[] statements, ResultSet[] resultSets)
        throws DaoException {

        closeStatements(statements);

        closeResultSets(resultSets);
    }

    private static void closeStatements(Statement[] statements) throws DaoException {
        if(statements == null) {
            return;
        }

        for (Statement s : statements) {
            try {
                if (s != null) {
                    s.close();
                }
            } catch (Exception e) {
                logger.error("Could not close statement.", e);
                throw new DaoException("Could not close statement: " + e.getMessage());
            }
        }
    }

    private static void closeResultSets(ResultSet[] resultSets) throws DaoException {
        if (resultSets == null) {
            return;
        }

        for (ResultSet rs : resultSets) {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                logger.error("Could not close resultset.", e);
                throw new DaoException("Could not close resultset: " + e.getMessage());
            }
        }
    }
}
