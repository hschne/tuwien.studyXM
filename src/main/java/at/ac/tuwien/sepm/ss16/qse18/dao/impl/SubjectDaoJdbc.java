package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SubjectDaoJdbc implements SubjectDao {

    @Override public Subject getSubject(int id) throws DaoException {

        Subject res = null;
        Statement s = null;
        ResultSet rs = null;

        try {
            s = ConnectionH2.getConnection().createStatement();
            rs = s.executeQuery("SELECT * FROM Fach WHERE fid = " + id);

            if (rs.next()) {
                res = new Subject();
                res.setSubjectId(rs.getInt("fid"));
                res.setEcts(rs.getFloat("ects"));
                res.setName(rs.getString("name"));
                res.setSemester(rs.getString("semester"));
                res.setTimeSpent(rs.getInt("time_spent"));
                res.setAuthor(rs.getString("author"));
            }
        } catch (SQLException e) {
            throw new DaoException("Could not get subject with id (" + id + ")");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DaoException("Could not close resultset");
                }
            }

            if (s != null) {
                try {
                    s.close();
                } catch (SQLException e) {
                    throw new DaoException("Could not close statement");
                }
            }
        }
        return res;
    }

    @Override public List<Subject> getSubjects() throws DaoException {
        List<Subject> res = new ArrayList<>();

        Statement s = null;
        ResultSet rs = null;

        try {
            s = ConnectionH2.getConnection().createStatement();
            rs = s.executeQuery("SELECT * FROM Fach");

            while (rs.next()) {
                Subject tmp = new Subject();
                tmp.setSubjectId(rs.getInt("fid"));
                tmp.setEcts(rs.getFloat("ects"));
                tmp.setName(rs.getString("name"));
                tmp.setSemester(rs.getString("semester"));
                tmp.setTimeSpent(rs.getInt("time_spent"));
                tmp.setAuthor(rs.getString("author"));
                res.add(tmp);
            }
        } catch (SQLException e) {
            throw new DaoException("Could not get all subjects");
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DaoException("Could not close resultset");
                }
            }

            if (s != null) {
                try {
                    s.close();
                } catch (SQLException e) {
                    throw new DaoException("Could not close statement");
                }
            }
        }
        return res;
    }

    @Override public void createSubject(Subject subject) throws DaoException {

        if (subject == null) {
            throw new DaoException("Subject must not be null");
        }

        PreparedStatement ps = null;

        try {
            ps = ConnectionH2.getConnection()
                .prepareStatement("INSERT INTO Fach VALUES (?,?,?,?,?,?)");
            ps.setInt(1, subject.getSubjectId());
            ps.setString(2, subject.getName());
            ps.setFloat(3, subject.getEcts());
            ps.setString(4, subject.getSemester());
            ps.setInt(5, subject.getTimeSpent());
            ps.setString(6, subject.getAuthor());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(
                "Could not create subject with values (" + subject.getSubjectId() + ", " + subject
                    .getName() + ", " + subject.getEcts() + ", " + subject.getSemester() + subject
                    .getTimeSpent() + subject.getAuthor() + ")");
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DaoException("Could not close preparedstatement");
                }
            }
        }
    }

    @Override public void deleteSubject(Subject subject) throws DaoException {

        if (subject == null) {
            throw new DaoException("Subject must not be null");
        }

        PreparedStatement ps = null;

        try {
            ps = ConnectionH2.getConnection().prepareStatement(
                "DELETE FROM Fach WHERE fid = ? AND name = ? AND ects = ? AND semester = ? AND time_spent = ? AND author = ?");
            ps.setInt(1, subject.getSubjectId());
            ps.setString(2, subject.getName());
            ps.setFloat(3, subject.getEcts());
            ps.setString(4, subject.getSemester());
            ps.setInt(5, subject.getTimeSpent());
            ps.setString(6, subject.getAuthor());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(
                "Could not delete subject with values (" + subject.getSubjectId() + ", " + subject
                    .getName() + ", " + subject.getEcts() + ", " + subject.getSemester() + subject
                    .getTimeSpent() + subject.getAuthor() + ")");
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DaoException("Could not close preparedstatement");
                }
            }
        }
    }

    @Override public void updateSubject(Subject subject) throws DaoException {

        if (subject == null) {
            throw new DaoException("Subject must not be null");
        }

        PreparedStatement ps = null;

        try {
            ps = ConnectionH2.getConnection().prepareStatement(
                "UPDATE Fach SET name = ?, ects = ?, semester = ?, time_spent = ?, author = ? WHERE fid = ?");
            ps.setString(1, subject.getName());
            ps.setFloat(2, subject.getEcts());
            ps.setString(3, subject.getSemester());
            ps.setInt(4, subject.getTimeSpent());
            ps.setString(5, subject.getAuthor());
            ps.setInt(6, subject.getSubjectId());

            ps.executeUpdate();

            ps.close();
        } catch (SQLException e) {
            throw new DaoException(
                "Could not update subject with id (" + subject.getSubjectId() + ") to values ("
                    + subject.getSubjectId() + ", " + subject.getName() + ", " + subject.getEcts()
                    + ", " + subject.getSemester() + subject.getTimeSpent() + subject.getAuthor()
                    + ")");
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DaoException("Could not close preparedstatement");
                }
            }
        }
    }
}
