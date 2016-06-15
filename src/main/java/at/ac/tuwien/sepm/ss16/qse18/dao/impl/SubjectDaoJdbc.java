package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.DataBaseConnection;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.dao.StatementResultsetCloser.closeStatementsAndResultSets;

/**
 * Class SubjectDaoJdbc
 * concrete implementation of Interface SubjectDao
 * This class has access to the h2 database that is defined in the ConnectionH2 class.
 *
 * @author Cem Bicer
 */
@Repository public class SubjectDaoJdbc implements SubjectDao {

    private static final Logger logger = LogManager.getLogger();

    private DataBaseConnection database;

    private TopicDaoJdbc topicDaoJdbc;

    private SubjectTopicDaoJdbc subjectTopicDaoJdbc;

    private ExamDaoJdbc examDaoJdbc;

    @Autowired public SubjectDaoJdbc(DataBaseConnection database) {
        this.database = database;
    }

    @Autowired public void setExamDaoJdbc(ExamDaoJdbc examDaoJdbc) {
        this.examDaoJdbc = examDaoJdbc;
    }

    @Autowired public void setTopicDaoJdbc(TopicDaoJdbc topicDaoJdbc) {
        this.topicDaoJdbc = topicDaoJdbc;
    }

    @Autowired public void setSubjectTopicDaoJdbc(SubjectTopicDaoJdbc subjectTopicDaoJdbc) {
        this.subjectTopicDaoJdbc = subjectTopicDaoJdbc;
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
            ps.close();
            rs.close();
        } catch (SQLException e) {
            logger.error("Could not get subject with id (" + id + ") ", e);
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
            rs.close();
            s.close();
        } catch (SQLException e) {
            logger.error("Could not get all subjectListView ", e);
            throw new DaoException("Could not get all subjectListView");
        } finally {
            closeStatementsAndResultSets(new Statement[] {s}, new ResultSet[] {rs});
        }

        return res;
    }

    @Override public Subject createSubject(Subject subject) throws DaoException {
        assertNotNull(subject);

        logger.debug("Entering createSubject with values " + subjectValues(subject));
        Subject res = null;
        PreparedStatement ps = null;
        ResultSet generetedKey = null;

        try {
            ps = database.getConnection().prepareStatement(
                "INSERT INTO ENTITY_SUBJECT (NAME,ECTS,SEMESTER,TIME_SPENT,AUTHOR) VALUES (?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS);
            fillPreparedStatementWithSubject(true, ps, subject);

            ps.executeUpdate();

            generetedKey = ps.getGeneratedKeys();
            if (generetedKey.next()) {
                res = new Subject();
                fillSubject(res, generetedKey.getInt(1), subject.getName(), subject.getEcts(),
                    subject.getSemester(), subject.getTimeSpent(), subject.getAuthor());
            }

            return res;
        } catch (SQLException e) {
            logger.error("Could not create subject with values " + subjectValues(subject), e);
            throw new DaoException(
                "Could not create subject with values " + subjectValues(subject));
        } finally {
            closeStatementsAndResultSets(new Statement[] {ps}, new ResultSet[] {generetedKey});
        }
    }

    @Override public Subject deleteSubject(Subject subject) throws DaoException {
        assertNotNull(subject);
        logger.debug("Entering deleteSubject with values " + subjectValues(subject));
        try {
            Connection connection = database.getConnection();
            connection.setAutoCommit(false);
            deleteSubjectAndRelated(subject);
            connection.commit();
            connection.setAutoCommit(true);
            return subject;
        } catch (SQLException e) {
            logger.error("Could not delete subject with values " + subjectValues(subject), e);
            throw new DaoException(
                "Could not delete subject with values " + subjectValues(subject));
        }
    }

    private void deleteSubjectAndRelated(Subject subject) throws DaoException, SQLException {
        deleteTopics(subject);
        deleteExams(subject);
        PreparedStatement ps = database.getConnection()
            .prepareStatement("DELETE FROM ENTITY_SUBJECT WHERE SUBJECTID = ?");
        ps.setInt(1, subject.getSubjectId());
        ps.executeUpdate();
        ps.close();
    }

    @Override public Subject updateSubject(Subject subject) throws DaoException {
        assertNotNull(subject);
        logger.debug("Entering updateSubject with values" + subjectValues(subject));

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
            ps.close();
            return subject;
        } catch (SQLException e) {
            logger.error(
                "Could not update subject with id (" + subject.getSubjectId() + ") to values "
                    + subjectValues(subject), e);
            throw new DaoException(
                "Could not update subject with id (" + subject.getSubjectId() + ") to values "
                    + subjectValues(subject));
        } finally {
            closeStatementsAndResultSets(new Statement[] {ps}, null);
        }
    }

    private void assertNotNull(Subject subject) throws DaoException {
        if (subject == null) {
            throw new DaoException("Subject must not be null");
        }
    }

    private void deleteExams(Subject subject) throws DaoException {
        List<Exam> exerciseExams = examDaoJdbc.getAllExamsOfSubject(subject);
        for (Exam exerciseExam : exerciseExams) {
            examDaoJdbc.delete(exerciseExam);
        }
    }

    private void deleteTopics(Subject subject) throws DaoException {
        List<Topic> topics = subjectTopicDaoJdbc.getTopicToSubject(subject);
        for (Topic topic : topics) {
            topicDaoJdbc.deleteTopic(topic);
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

    private void fillPreparedStatementWithSubject(boolean inCreateMethod, PreparedStatement ps,
        Subject s) throws SQLException {
        if (ps != null) {
            if (inCreateMethod) {
                ps.setString(1, s.getName());
                ps.setFloat(2, s.getEcts());
                ps.setString(3, s.getSemester());
                ps.setInt(4, s.getTimeSpent());
                ps.setString(5, s.getAuthor());
            } else {
                ps.setInt(1, s.getSubjectId());
                ps.setString(2, s.getName());
                ps.setFloat(3, s.getEcts());
                ps.setString(4, s.getSemester());
                ps.setInt(5, s.getTimeSpent());
                ps.setString(6, s.getAuthor());
            }
        }
    }
}
