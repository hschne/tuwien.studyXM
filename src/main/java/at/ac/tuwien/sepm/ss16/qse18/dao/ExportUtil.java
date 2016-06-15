package at.ac.tuwien.sepm.ss16.qse18.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class ExportUtil {
    private static final Logger logger = LogManager.getLogger();

    public static void setAutocommit(boolean value) throws DaoException {
        logger.debug("Setting autocommit value to " + value);
        DataBaseConnection dataBaseConnection = new ConnectionH2();

        try {
            dataBaseConnection.getConnection().setAutoCommit(value);
        } catch (SQLException e) {
            logger.error("Could not set autocommit option", e);
            throw new DaoException("Could not set autocommit option", e);
        }
    }

    public static void rollback() throws DaoException {
        logger.debug("Rolling back changes");
        DataBaseConnection dataBaseConnection = new ConnectionH2();

        try {
            dataBaseConnection.getConnection().rollback();
        } catch (SQLException e) {
            logger.error("Could not roll back changes", e);
            throw new DaoException("Could not roll back changes", e);
        }
    }
}
