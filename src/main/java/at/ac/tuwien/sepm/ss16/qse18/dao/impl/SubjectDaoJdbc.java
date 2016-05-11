package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC implementation of the CRUD methods from SubjectDao. This class has access to the h2 database
 * that is defined in the ConnectionH2 class.
 *
 * @author Cem Bicer
 */
@Service public class SubjectDaoJdbc implements SubjectDao {

    private static final Logger logger = LogManager.getLogger();

    private ConnectionH2 database;

    @Autowired SubjectDaoJdbc(ConnectionH2 database) {
        this.database = database;
    }

    @Override public Subject getSubject(int id) throws DaoException {
        logger.debug("Entering getSubject(" + id + ")");

        Subject res = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = database.getConnection()
                .prepareStatement("SELECT * FROM SUBJECT WHERE SUBJECTID = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                res = new Subject();
                fillSubject(res, rs.getInt("subjectId"), rs.getString("name"), rs.getFloat("ects"),
                    rs.getString("semester"), rs.getInt("time_spent"), rs.getString("author"));
            }
        } catch (SQLException e) {
            logger.error("Could not get subject with id (" + id + "): " + e);
            throw new DaoException("Could not get subject with id (" + id + ")");
        } finally {
            closeStatementsAndResultSets(new Statement[] {ps}, new ResultSet[] {rs});
        }
        return res;
    }

    @Override public List<Subject> getSubjects() throws DaoException {
        logger.debug("Entering getSubjects");

        List<Subject> res = new ArrayList<>();

        Statement s = null;
        ResultSet rs = null;

        try {
            s = database.getConnection().createStatement();
            rs = s.executeQuery("SELECT * FROM SUBJECT");

            while (rs.next()) {
                Subject tmp = new Subject();
                fillSubject(tmp, rs.getInt("subjectid"), rs.getString("name"), rs.getFloat("ects"),
                    rs.getString("semester"), rs.getInt("time_spent"), rs.getString("author"));
                res.add(tmp);
            }
        } catch (SQLException e) {
            logger.error("Could not get all subjects: " + e);
            throw new DaoException("Could not get all subjects");
        } finally {
            closeStatementsAndResultSets(new Statement[] {s}, new ResultSet[] {rs});
        }
        return res;
    }

    @Override public void createSubject(Subject subject) throws DaoException {
        logger.debug("Entering createSubject with values " + (subject == null ?
            "null" :
            subjectValues(subject)));

        if (subject == null) {
            throw new DaoException("Subject must not be null");
        }

        PreparedStatement ps = null;

        try {
            ps = database.getConnection()
                .prepareStatement("INSERT INTO SUBJECT VALUES (?,?,?,?,?,?)");
            fillPreparedStatement(ps, subject.getSubjectId(), subject.getName(), subject.getEcts(),
                subject.getSemester(), subject.getTimeSpent(), subject.getAuthor());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger
                .error("Could not create subject with values " + subjectValues(subject) + ": " + e);
            throw new DaoException(
                "Could not create subject with values " + subjectValues(subject));
        } finally {
            closeStatementsAndResultSets(new Statement[] {ps}, null);
        }
    }

    @Override public void deleteSubject(Subject subject) throws DaoException {
        logger.debug("Entering deleteSubject with values " + (subject == null ?
            "null" :
            subjectValues(subject)));

        if (subject == null) {
            throw new DaoException("Subject must not be null");
        }

        PreparedStatement ps = null;

        try {
            ps = database.getConnection().prepareStatement(
                "DELETE FROM SUBJECT WHERE SUBJECTID = ? AND name = ? AND ects = ? AND semester = ? AND time_spent = ? AND author = ?");
            ps.setInt(1, subject.getSubjectId());
            ps.setString(2, subject.getName());
            ps.setFloat(3, subject.getEcts());
            ps.setString(4, subject.getSemester());
            ps.setInt(5, subject.getTimeSpent());
            ps.setString(6, subject.getAuthor());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger
                .error("Could not delete subject with values " + subjectValues(subject) + ": " + e);
            throw new DaoException(
                "Could not delete subject with values " + subjectValues(subject));
        } finally {
            closeStatementsAndResultSets(new Statement[] {ps}, null);
        }
    }

    @Override public void updateSubject(Subject subject) throws DaoException {
        logger.debug("Entering updateSubject with values" + (subject == null ?
            "null" :
            subjectValues(subject)));

        if (subject == null) {
            throw new DaoException("Subject must not be null");
        }

        PreparedStatement ps = null;

        try {
            ps = database.getConnection().prepareStatement(
                "UPDATE SUBJECT SET name = ?, ects = ?, semester = ?, time_spent = ?, author = ? WHERE SUBJECTID = ?");
            ps.setString(1, subject.getName());
            ps.setFloat(2, subject.getEcts());
            ps.setString(3, subject.getSemester());
            ps.setInt(4, subject.getTimeSpent());
            ps.setString(5, subject.getAuthor());
            ps.setInt(6, subject.getSubjectId());

            ps.executeUpdate();

            ps.close();
        } catch (SQLException e) {
            logger.error(
                "Could not update subject with id (" + subject.getSubjectId() + ") to values "
                    + subjectValues(subject) + ": " + e);
            throw new DaoException(
                "Could not update subject with id (" + subject.getSubjectId() + ") to values "
                    + subjectValues(subject));
        } finally {
            closeStatementsAndResultSets(new Statement[] {ps}, null);
        }
    }

    private String subjectValues(Subject subject) {
        return "(" + subject.getSubjectId() + ", " + subject.getName() + ", " + subject.getEcts()
            + ", " + subject.getSemester() + subject.getTimeSpent() + subject.getAuthor() + ")";
    }

    private void fillSubject(Subject subject, int id, String name, float ects, String semester,
        int timeSpent, String author) {
        if (subject != null) {
            subject.setSubjectId(id);
            subject.setName(name);
            subject.setEcts(ects);
            subject.setSemester(semester);
            subject.setTimeSpent(timeSpent);
            subject.setAuthor(author);
        }
    }

    private void fillPreparedStatement(PreparedStatement ps, int id, String name, float ects,
        String semester, int timeSpent, String author) throws SQLException {
        if (ps != null) {
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setFloat(3, ects);
            ps.setString(4, semester);
            ps.setInt(5, timeSpent);
            ps.setString(6, author);
        }
    }

    private void closeStatementsAndResultSets(Statement[] statements, ResultSet[] resultSets)
        throws DaoException {
        if (statements != null) {
            for (Statement s : statements) {
                try {
                    s.close();
                } catch (SQLException e) {
                    logger.error("Could not close statement " + e.getMessage());
                    throw new DaoException("Could not close statement " + e.getMessage());
                }
            }
        }
        if (resultSets != null) {
            for (ResultSet rs : resultSets) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.error("Could not close resultset " + e.getMessage());
                    throw new DaoException("Could not close resultset " + e.getMessage());
                }
            }
        }
    }
}
