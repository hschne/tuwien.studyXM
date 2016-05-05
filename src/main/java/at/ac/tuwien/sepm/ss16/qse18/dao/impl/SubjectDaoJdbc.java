package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SubjectDaoJdbc implements SubjectDao {

    private ConnectionH2 database;

    @Autowired public SubjectDaoJdbc(ConnectionH2 c) {
        this.database = c;
    }

    @Override public Subject getSubject(int id) throws DaoException {
        Subject res = new Subject();

        try {
            Statement s = database.getConnection().createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM Fach WHERE fid = " + id);

            if (rs.next()) {
                res.setSubjectId(rs.getInt("fid"));
                res.setEcts(rs.getInt("ects"));
                res.setName(rs.getString("name"));
                res.setSemester(rs.getString("semester"));
            }

            rs.close();
            s.close();
        } catch (SQLException e) {
            throw new DaoException("Could not get subject with id (" + id + ")");
        }
        return res;
    }

    @Override public List<Subject> getSubjects() throws DaoException {
        List<Subject> res = new ArrayList<>();

        try {
            Statement s = database.getConnection().createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM Fach");

            while (rs.next()) {
                Subject tmp = new Subject();
                tmp.setSubjectId(rs.getInt("fid"));
                tmp.setEcts(rs.getInt("ects"));
                tmp.setName(rs.getString("name"));
                tmp.setSemester(rs.getString("semester"));
                res.add(tmp);
            }

            s.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("Could not get all subjects");
        }

        return res;
    }

    @Override public void createSubject(Subject subject) throws DaoException {
        try {
            PreparedStatement ps =
                database.getConnection().prepareStatement("INSERT INTO Fach VALUES (?,?,?,?)");
            ps.setInt(1, subject.getSubjectId());
            ps.setString(2, subject.getName());
            ps.setDouble(3, subject.getEcts());
            ps.setString(4, subject.getSemester());
            ps.executeUpdate();

            ps.close();
        } catch (SQLException e) {
            throw new DaoException(
                "Could not create subject with values (" + subject.getSubjectId() + ", " + subject
                    .getName() + ", " + subject.getEcts() + ", " + subject.getSemester() + ")");
        }
    }

    @Override public void deleteSubject(Subject subject) throws DaoException {
        try {
            PreparedStatement ps = database.getConnection().prepareStatement(
                "DELETE FROM Fach WHERE fid = ? AND name = ? AND ects = ? AND semester = ?");
            ps.setInt(1, subject.getSubjectId());
            ps.setString(2, subject.getName());
            ps.setDouble(3, subject.getEcts());
            ps.setString(4, subject.getSemester());
            ps.executeUpdate();

            ps.close();
        } catch (SQLException e) {
            throw new DaoException(
                "Could not delete subject with values (" + subject.getSubjectId() + ", " + subject
                    .getName() + ", " + subject.getEcts() + ", " + subject.getSemester() + ")");
        }
    }

    @Override public void updateSubject(Subject subject) throws DaoException {
        try {
            PreparedStatement ps = database.getConnection().prepareStatement(
                "UPDATE Fach SET name = ?, ects = ?, semester = ? WHERE fid = ?");
            ps.setString(1, subject.getName());
            ps.setDouble(2, subject.getEcts());
            ps.setString(3, subject.getSemester());
            ps.setInt(4, subject.getSubjectId());

            ps.executeUpdate();

            ps.close();
        } catch (SQLException e) {
            throw new DaoException(
                "Could not update subject with id (" + subject.getSubjectId() + ") to values (" + subject.getSubjectId() + ", " + subject
                    .getName() + ", " + subject.getEcts() + ", " + subject.getSemester() + ")");
        }
    }
}
