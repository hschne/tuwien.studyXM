package at.ac.tuwien.sepm.ss16.qse18.dao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * @author Julian Strohmayer
 * @date 04.05.2016.
 * @info this class represents the connection to the H2-database, implemented as Singleton.
 */
@Component
public class ConnectionH2 {

    private static final Logger LOGGER = LogManager.getLogger();
    private Connection connection;

    /*
    Opens up a new connection to the H2-database, if the connection is null or has been closed.
    @param path the filepath to the H2-database
    @param user the username of the H2-database
    @param password the password of the H2-database
    */
    private void openConnection(String path, String user, String password) throws SQLException{
        LOGGER.info("entering openConnection() with " + path + " " + user + " "+ password);

        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.h2.Driver");
            }
            catch (ClassNotFoundException e) {
                System.err.println("ERROR: unable to load org.h2.Driver. "+e.getMessage());
                return;
            }
            connection = DriverManager.getConnection(path, user, password);
        }
    }
    //------------------------------------------------------------------------------------------------------------------

    /*
    Returns the connection to the H2-database.
    If the connection is null or has been closed a new connection is opened up.
    @return connection to the H2-database
    */
    public Connection getConnection()throws SQLException{
        if(connection==null || connection.isClosed())
        {
            openConnection("jdbc:h2:tcp://localhost/~/studyXmDatabase", "studyXm", "xm");
        }
        return connection;
    }
    //------------------------------------------------------------------------------------------------------------------

    //closes the existing connection to the h2 database.
    public void closeConnection() {
        LOGGER.info("entering closeConnection()");

        try {
            if(!(connection==null || connection.isClosed()))
            {
                connection.close();
            }
        }
        catch(SQLException e) {
            System.err.println("ERROR: unable to connect to database. " +e.getMessage());
        }

    }
    //------------------------------------------------------------------------------------------------------------------
}
