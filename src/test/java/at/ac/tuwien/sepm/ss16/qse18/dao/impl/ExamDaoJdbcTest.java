package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.DaoBaseTest;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ExerciseExamDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Exam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import static org.junit.Assert.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 *  Created by Felix on 18.06.2016.
 */
public class ExamDaoJdbcTest extends DaoBaseTest {

    private ExamDaoJdbc examDaoJdbc;
    @Mock private ExamDaoJdbc mockExamDao;
    @Mock private ExerciseExamDao mockExerciseExamDao;

    @Before public void setUp() throws Exception {
        super.setUp();
        examDaoJdbc = new ExamDaoJdbc(mockConnectionH2);
        examDaoJdbc.setExerciseExamDao(mockExerciseExamDao);
    }

    @Test (expected = DaoException.class) public void test_getExam_noDatabaseConnection()
        throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        examDaoJdbc.getExam(1);
        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }

    @Test (expected = DaoException.class) public void test_getExam_invalidExamId() throws Exception {
        examDaoJdbc.getExam(-1);
    }

    @Test public void test_getExam_notInDatabase() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        assertTrue(examDaoJdbc.getExam(1) == null);
    }

    @Test public void test_getExam_withValidId() throws Exception {
        when(mockResultSet.next()).thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("examid")).thenReturn(1).thenReturn(2);
        when(mockResultSet.getInt("subject")).thenReturn(1).thenReturn(2);
        when(mockResultSet.getTimestamp("created"))
            .thenReturn(new Timestamp(System.currentTimeMillis())).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(mockResultSet.getTimestamp("due_date"))
            .thenReturn(new Timestamp(System.currentTimeMillis())).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(mockResultSet.getString("name")).thenReturn("Test1").thenReturn("Test2");

        Exam a = examDaoJdbc.getExam(1);
        Exam b = examDaoJdbc.getExam(2);

        assertTrue("Entry a: " + a, a != null);
        assertTrue("Entry b: " + b, b != null);
        assertFalse(a.equals(b));
    }

    @Test public void test_getExams_emptyDatabase() throws Exception {
        when(mockResultSet.next()).thenReturn(false);
        assertTrue(examDaoJdbc.getExams().size() < 1);
    }

    @Test public void test_getExams_twoElementsInDatabase() throws Exception {
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("examid")).thenReturn(1).thenReturn(2);
        when(mockResultSet.getInt("subject")).thenReturn(1).thenReturn(2);
        when(mockResultSet.getTimestamp("created"))
            .thenReturn(new Timestamp(System.currentTimeMillis())).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(mockResultSet.getTimestamp("due_date"))
            .thenReturn(new Timestamp(System.currentTimeMillis())).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(mockResultSet.getString("name")).thenReturn("Test1").thenReturn("Test2");

        List<Exam> exams = examDaoJdbc.getExams();
        assertTrue(exams.size() == 2);
        assertFalse(exams.get(0).equals(exams.get(1)));
    }

    @Test (expected = DaoException.class) public void test_createExam_noDatabaseConnection()
        throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);
        examDaoJdbc.create(new Exam());
        PowerMockito.verifyStatic();
        mockConnectionH2.getConnection();
    }

    @Test (expected = DaoException.class) public void test_createExam_createNull_fail()
        throws Exception {
        examDaoJdbc.create(null);
    }

    @Test public void test_createExam_valid() throws Exception {
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);
        Exam e = new Exam("Test", new Timestamp(System.currentTimeMillis()), 1);

        e = examDaoJdbc.create(e);
        assertTrue(e.getExamid() == 1 && e.getName().equals("Test") && e.getSubject() == 1);
    }

    @Test (expected = DaoException.class) public void test_createExam_generatedKeyNull()
        throws Exception {
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(null);
        Exam e = new Exam("Test", new Timestamp(System.currentTimeMillis()), 1);
        examDaoJdbc.create(e);
    }

    @Test (expected = DaoException.class) public void test_createExam_noGeneratedKey()
        throws Exception {
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        Exam e = new Exam("Test", new Timestamp(System.currentTimeMillis()), 1);
        examDaoJdbc.create(e);
    }

    @Test (expected = DaoException.class) public void test_deleteExam_deleteNull()
        throws Exception {
        examDaoJdbc.delete(null);
    }

    @Test (expected = DaoException.class) public void test_deleteExam_notInDatabase()
        throws Exception {
        examDaoJdbc.delete(new Exam("Test", new Timestamp(System.currentTimeMillis()), 1));
    }

    @Test public void test_deleteExam_success() throws Exception {
        when(mockExerciseExamDao.getExerciseExamsFrom(anyObject())).thenReturn(new ArrayList<>());
        Exam e = new Exam("test", new Timestamp(System.currentTimeMillis()), 1);
        e.setExamid(1);
        examDaoJdbc.delete(e);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test (expected = DaoException.class) public void test_getAllExamsOfSubject_nullSubject()
        throws Exception {
        examDaoJdbc.getAllExamsOfSubject(null);
    }

    @Test (expected = DaoException.class) public void test_getAllExamsOfSubject_subjectNotInDb()
        throws Exception {
        examDaoJdbc.getAllExamsOfSubject(new Subject());
    }

    @Test public void test_getAllExamsOfSubject_success() throws Exception {
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(mockResultSet.getInt("examid")).thenReturn(1).thenReturn(2);
        when(mockResultSet.getInt("subject")).thenReturn(1).thenReturn(2);
        when(mockResultSet.getTimestamp("created"))
            .thenReturn(new Timestamp(System.currentTimeMillis())).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(mockResultSet.getTimestamp("due_date"))
            .thenReturn(new Timestamp(System.currentTimeMillis())).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(mockResultSet.getString("name")).thenReturn("Test1").thenReturn("Test2");

        Subject s = new Subject();
        s.setSubjectId(1);
        List<Exam> el = examDaoJdbc.getAllExamsOfSubject(s);
        assertTrue(el.size() == 2);
        verify(mockPreparedStatement).executeQuery();
    }
}
