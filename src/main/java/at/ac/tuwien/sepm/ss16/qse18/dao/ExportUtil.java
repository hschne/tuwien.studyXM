package at.ac.tuwien.sepm.ss16.qse18.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component public class ExportUtil {
    private static final Logger logger = LogManager.getLogger();
    @Autowired private ConnectionH2 databaseConnection;

    public void setAutocommit(boolean value) throws DaoException {
        logger.debug("Setting autocommit value to " + value);

        try {
            databaseConnection.getConnection().setAutoCommit(value);
        } catch (SQLException e) {
            logger.error("Could not set autocommit option", e);
            throw new DaoException("Could not set autocommit option", e);
        }
    }

    public void rollback() throws DaoException {
        logger.debug("Rolling back changes");

        try {
            databaseConnection.getConnection().rollback();
        } catch (SQLException e) {
            logger.error("Could not roll back changes", e);
            throw new DaoException("Could not roll back changes", e);
        }
    }
}
