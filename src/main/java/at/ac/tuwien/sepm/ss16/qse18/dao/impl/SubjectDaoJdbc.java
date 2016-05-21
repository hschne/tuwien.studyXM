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

import static at.ac.tuwien.sepm.ss16.qse18.dao.StatementResultsetCloser.closeStatementsAndResultSets;

/**
 * JDBC implementation of the CRUD methods from SubjectDao. This class has access to the h2 database
 * that is defined in the ConnectionH2 class.
 *
 * @author Cem Bicer
 */
@Service public class SubjectDaoJdbc implements SubjectDao {

    private static final Logger logger = LogManager.getLogger();

    private ConnectionH2 database;

    @Autowired public SubjectDaoJdbc(ConnectionH2 database) {
        this.database = database;
    }

    @Override public Subject getSubject(int id) throws DaoException {
        logger.debug("Entering getSubject(" + id + ")");

        Subject res = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = database.getConnection()
                .prepareStatement("SELECT * FROM ENTITY_SUBJECT WHERE SUBJECTID = ?");
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
            rs = s.executeQuery("SELECT * FROM ENTITY_SUBJECT");

            while (rs.next()) {
                Subject tmp = new Subject();
                fillSubject(tmp, rs.getInt("subjectid"), rs.getString("name"), rs.getFloat("ects"),
                    rs.getString("semester"), rs.getInt("time_spent"), rs.getString("author"));
                res.add(tmp);
            }
        } catch (SQLException e) {
            logger.error("Could not get all subjectListView: " + e);
            throw new DaoException("Could not get all subjectListView");
        } finally {
            closeStatementsAndResultSets(new Statement[] {s}, new ResultSet[] {rs});
        }
        return res;
    }

    @Override public Subject createSubject(Subject subject) throws DaoException {
        logger.debug("Entering createSubject with values " + (subject == null ?
            "null" :
            subjectValues(subject)));

        if (subject == null) {
            throw new DaoException("Subject must not be null");
        }

        Subject res = null;
        PreparedStatement ps = null;
        ResultSet generetedKey = null;

        try {
            ps = database.getConnection()
                .prepareStatement("INSERT INTO ENTITY_SUBJECT (NAME,ECTS,SEMESTER,TIME_SPENT,AUTHOR) VALUES (?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            fillPreparedStatement(true, ps, 0, subject.getName(), subject.getEcts(),
                subject.getSemester(), subject.getTimeSpent(), subject.getAuthor());

            ps.executeUpdate();

            generetedKey = ps.getGeneratedKeys();
            if (generetedKey.next()) {
                res = new Subject();
                fillSubject(res, generetedKey.getInt(1), subject.getName(), subject.getEcts(),
                    subject.getSemester(), subject.getTimeSpent(), subject.getAuthor());
            }

            return res;
        } catch (SQLException e) {
            logger
                .error("Could not create subject with values " + subjectValues(subject) + ": " + e);
            throw new DaoException(
                "Could not create subject with values " + subjectValues(subject));
        } finally {
            closeStatementsAndResultSets(new Statement[] {ps}, new ResultSet[] {generetedKey});
        }
    }

    @Override public Subject deleteSubject(Subject subject) throws DaoException {
        logger.debug("Entering deleteSubject with values " + (subject == null ?
            "null" :
            subjectValues(subject)));

        if (subject == null) {
            throw new DaoException("Subject must not be null");
        }

        PreparedStatement ps = null;

        try {

            ps = database.getConnection().prepareStatement(
                "DELETE FROM ENTITY_SUBJECT WHERE SUBJECTID = ? AND name = ? AND ects = ? AND semester = ? AND time_spent = ? AND author = ?");
            fillPreparedStatement(false, ps, subject.getSubjectId(), subject.getName(),
                subject.getEcts(), subject.getSemester(), subject.getTimeSpent(),
                subject.getAuthor());
            ps.executeUpdate();

            return subject;
        } catch (SQLException e) {
            logger
                .error("Could not delete subject with values " + subjectValues(subject) + ": " + e);
            throw new DaoException(
                "Could not delete subject with values " + subjectValues(subject));
        } finally {
            closeStatementsAndResultSets(new Statement[] {ps}, null);
        }
    }

    @Override public Subject updateSubject(Subject subject) throws DaoException {
        logger.debug("Entering updateSubject with values" + (subject == null ?
            "null" :
            subjectValues(subject)));

        if (subject == null) {
            throw new DaoException("Subject must not be null");
        }

        PreparedStatement ps = null;

        try {
            ps = database.getConnection().prepareStatement(
                "UPDATE ENTITY_SUBJECT SET name = ?, ects = ?, semester = ?, time_spent = ?, author = ? WHERE SUBJECTID = ?");
            ps.setString(1, subject.getName());
            ps.setFloat(2, subject.getEcts());
            ps.setString(3, subject.getSemester());
            ps.setInt(4, subject.getTimeSpent());
            ps.setString(5, subject.getAuthor());
            ps.setInt(6, subject.getSubjectId());

            ps.executeUpdate();

            return subject;
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
            + ", " + subject.getSemester() + ", " + subject.getTimeSpent() + ", " + subject
            .getAuthor() + ")";
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

    private void fillPreparedStatement(boolean inCreateMethod, PreparedStatement ps, int id,
        String name, float ects, String semester, int timeSpent, String author)
        throws SQLException {
        if (ps != null) {
            if (inCreateMethod) {
                ps.setString(1, name);
                ps.setFloat(2, ects);
                ps.setString(3, semester);
                ps.setInt(4, timeSpent);
                ps.setString(5, author);
            } else {
                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setFloat(3, ects);
                ps.setString(4, semester);
                ps.setInt(5, timeSpent);
                ps.setString(6, author);
            }

        }
    }
}
