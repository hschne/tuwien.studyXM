package at.ac.tuwien.sepm.ss16.qse18.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface DataBaseConnection
 * This interface specifies two methods: getConnection() and closeConnection() which are
 * necessary for any database. An implementing class shall operate as singleton; only one
 * instance of java.sql.Connection shall be active at any given time.
 *
 * Created by Felix on 04.05.2016.
 */
public interface DataBaseConnection {
    /**
     * getConnection
     * This method returns a static instance of java.sql.Connection which refers to the current
     * data base.
     * @return Connection A reference to the static database instance
     * @throws SQLException If no database connection can be established, DriverManager throws a
     * SQLException.
     */
    public Connection getConnection() throws SQLException;

    /**
     * closeConnection
     * Closes the current java.sql.Connection which refers to the database. Connection.close() is
     * called
     * @throws SQLException
     */
    public void closeConnection() throws SQLException;
}
