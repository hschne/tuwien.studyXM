package at.ac.tuwien.sepm.ss16.qse18.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class represents the connection to the H2-database, implemented as Singleton.
 *
 * @author Julian Strohmayer
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON) public class ConnectionH2
    implements DataBaseConnection {
    private static final Logger logger = LogManager.getLogger();
    private static Connection connection;

    /**
     * Opens up a new connection to the H2-database, if the connection is null or has been closed.
     *
     * @param path     the filepath to the H2-database
     * @param user     the username of the H2-database
     * @param password the password of the H2-database
     * @throws SQLException
     */
    private static void openConnection(String path, String user, String password)
        throws SQLException {
        logger.debug("Entering openConnection() with " + path + " " + user + " " + password);

        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.h2.Driver");
            } catch (ClassNotFoundException e) {
                logger.error("Unable to load org.h2.Driver.", e);
                return;
            }
            connection = DriverManager.getConnection(path, user, password);
        }
    }

    /**
     * Returns the connection to the H2-database.
     * If the connection is null or has been closed a new connection is opened up.
     *
     * @return connection to the H2-database
     * @throws SQLException
     */
    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            openConnection("jdbc:h2:tcp://localhost/~/studyXmDatabase", "studyXm", "xm");
        }
        return connection;
    }

    /**
     * closes the existing connection to the h2 database.
     * @throws SQLException
     */
    @Override
    public void closeConnection() throws SQLException {
        logger.debug("Closing H2 Database Connection");

        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
