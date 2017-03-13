package at.ac.tuwien.sepm.util.database;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A little helper class to restore the database to defaults
 *
 * @author Hans-Joerg Schroedl
 */
public class DatabaseInitialization {

    public static void main(String[] args) throws SQLException, IOException {
        ConnectionH2 connectionH2 = new ConnectionH2();

        Connection connection = connectionH2.getConnection();
        ScriptRunner runner = new ScriptRunner(connection, false, false);

        String drop = DatabaseInitialization.class.getResource("/scripts/drop.sql").getPath().replaceAll("%20", " ");
        String create = DatabaseInitialization.class.getResource("/scripts/create.sql").getPath().replaceAll("%20", " ");
        String insert = DatabaseInitialization.class.getResource("/scripts/insert.sql").getPath().replaceAll("%20", " ");

        runner.runScript(new BufferedReader(new FileReader(drop)));
        runner.runScript(new BufferedReader(new FileReader(create)));
        runner.runScript(new BufferedReader(new FileReader(insert)));

        connection.close();
    }



}
