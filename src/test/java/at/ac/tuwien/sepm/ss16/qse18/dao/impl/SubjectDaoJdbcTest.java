package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests for the JDBC implementation in SubjectDaoJdbc. In order to be isolated while testing, this
 * test class uses mocks primarily to bypass the database connection procedure.
 *
 * @author Cem Bicer
 */
@RunWith(PowerMockRunner.class) @PrepareForTest(ConnectionH2.class)
@PowerMockIgnore("javax.management.*") public class SubjectDaoJdbcTest {

    private SubjectDaoJdbc sdao;
    @Mock private Connection mockConnection;
    @Mock private Statement mockStatement;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet mockResultSet;

    @Before public void setUp() throws Exception {
        PowerMockito.mockStatic(ConnectionH2.class);

        when(ConnectionH2.getConnection()).thenReturn(mockConnection);

        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        sdao = new SubjectDaoJdbc();
    }

    // Testing getSubject(int) method
    // -------------------------------------------------------------------------------
    @Test(expected = DaoException.class) public void test_getSubject_withNoDatabaseConnection_Fail()
        throws Exception {
        when(ConnectionH2.getConnection()).thenThrow(SQLException.class);

        sdao.getSubject(2);

        PowerMockito.verifyStatic();
        ConnectionH2.getConnection();
    }

    @Test public void test_getSubject_withTooBigInt_Fail() throws Exception {
        Subject s = sdao.getSubject(Integer.MAX_VALUE);
        assertTrue("There should be no Subject with ID 2147483647 (= max int value)", s == null);
    }

    @Test public void test_getSubject_withValidId() throws Exception {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(anyString())).thenReturn(4);
        when(mockResultSet.getFloat(anyString())).thenReturn(6.5f);
        when(mockResultSet.getString(anyString())).thenReturn("SEPM").thenReturn("SS16");

        Subject s = sdao.getSubject(4);

        assertTrue("There should be exactly one element found", s != null);
        assertEquals("Found subject should have the ID 4", s.getSubjectId(), 4);
        assertTrue("Found subject should have 6.5 ects", s.getEcts() == 6.5f);
        assertEquals("Found subject should have the name SEPM", s.getName(), "SEPM");
        assertEquals("Found subject should be in semester SS16", s.getSemester(), "SS16");
    }

    @Test public void test_getSubject_WithValidId2() throws Exception {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(anyString())).thenReturn(12).thenReturn(8);
        when(mockResultSet.getFloat(anyString())).thenReturn(4.0f).thenReturn(2.0f);
        when(mockResultSet.getString(anyString())).thenReturn("TEST").thenReturn("SS33")
            .thenReturn("OTHER").thenReturn("WS17");

        Subject s1 = sdao.getSubject(1);
        Subject s2 = sdao.getSubject(2);

        assertFalse("Subjects with different ID should not be the same Objects", s1 == s2);
        assertFalse("The results of 2 different ID-searches should not have the same ID",
            s1.getSubjectId() == s2.getSubjectId());
        assertFalse("In this case the subject's ects should differ", s1.getEcts() == s2.getEcts());
        assertFalse("In this case the subject's ects should differ",
            s1.getName().equals(s2.getName()));
        assertFalse("In this case the subject's ects should differ",
            s1.getSemester().equals(s2.getSemester()));
    }

    // -------------------------------------------------------------------------------


    // Testing getSubjects() method
    // -------------------------------------------------------------------------------
    @Test public void test_getSubjects_withEmptyDatabase_Fail() throws Exception {
        when(mockResultSet.next()).thenReturn(false);

        List<Subject> list = sdao.getSubjects();
        assertEquals("There should not be any element in the list", list.size(), 0);
    }

    @Test(expected = DaoException.class)
    public void test_getSubjects_withNoDatabaseConnection_Fail() throws Exception {
        when(ConnectionH2.getConnection()).thenThrow(SQLException.class);

        sdao.getSubjects();

        PowerMockito.verifyStatic();
        ConnectionH2.getConnection();
    }

    @Test public void test_getSubjects_withTenElementsInDatabase() throws Exception {
        when(mockResultSet.next()).thenAnswer(new Answer<Boolean>() {
            private int count = 0;

            @Override public Boolean answer(InvocationOnMock invocationOnMock) throws Throwable {
                if (count < 10) {
                    count++;
                    return true;
                } else
                    return false;
            }
        });

        List<Subject> list = sdao.getSubjects();
        assertEquals("The size of the resulting list should be 10", list.size(), 10);
    }

    @Test public void test_getSubjects_withTwoElementsInDatabaseCheckValues() throws Exception {
        when(mockResultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);

        when(mockResultSet.getInt(anyString())).thenReturn(12).thenReturn(8);
        when(mockResultSet.getFloat(anyString())).thenReturn(4.0f).thenReturn(2.0f);
        when(mockResultSet.getString(anyString())).thenReturn("TEST").thenReturn("SS33")
            .thenReturn("Cem").thenReturn("OTHER").thenReturn("WS17").thenReturn("Tester");

        List<Subject> list = sdao.getSubjects();
        assertEquals("The size of the resulting list should be 2", list.size(), 2);

        Subject first = list.get(0);
        Subject second = list.get(1);

        assertFalse("Both found elements should differ", first == second);

        assertEquals("ID of first element should be 12", first.getSubjectId(), 12);
        assertTrue("ECTS of first element should be 4.0f", first.getEcts() == 4.0f);
        assertTrue("Name of first element should be TEST", first.getName().equals("TEST"));
        assertTrue("Semester of first element should be SS33", first.getSemester().equals("SS33"));

        assertEquals("ID of second element should be 8", second.getSubjectId(), 8);
        assertTrue("ECTS of second element should be 2.0f", second.getEcts() == 2.0f);
        assertTrue("Name of second element should be OTHER", second.getName().equals("OTHER"));
        assertTrue("Semester of second element should be WS17",
            second.getSemester().equals("WS17"));
    }
    // -------------------------------------------------------------------------------

    // Testing createSubject(Subject)
    // -------------------------------------------------------------------------------
    @Test(expected = DaoException.class)
    public void test_createSubject_withNoDatabaseConnection_Fail() throws Exception {
        when(ConnectionH2.getConnection()).thenThrow(SQLException.class);

        sdao.createSubject(new Subject());

        PowerMockito.verifyStatic();
        ConnectionH2.getConnection();
    }

    @Test(expected = DaoException.class) public void test_createSubject_withAlreadyExistingId_Fail()
        throws Exception {
        when(mockPreparedStatement.executeUpdate()).thenThrow(SQLException.class);

        Subject s = new Subject();
        s.setSubjectId(1);

        sdao.createSubject(s);

        PowerMockito.verifyStatic();
        ConnectionH2.getConnection();

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).setInt(anyInt(), anyInt());
        verify(mockPreparedStatement).setString(anyInt(), anyString());
        verify(mockPreparedStatement).setFloat(anyInt(), anyFloat());
    }

    @Test(expected = DaoException.class) public void test_createSubject_withNull_Fail()
        throws Exception {
        sdao.createSubject(null);
    }

    @Test public void test_createSubject_withValidSubject() throws Exception {
        Subject s = new Subject();
        s.setSubjectId(42);
        s.setName("TESTER");
        s.setEcts(99.9f);
        s.setSemester("WS99");

        sdao.createSubject(s);

        verify(mockPreparedStatement).executeUpdate();
    }
    // -------------------------------------------------------------------------------

    // Testing updateSubject(Subject)
    // -------------------------------------------------------------------------------
    @Test(expected = DaoException.class) public void test_updateSubject_withNull_Fail()
        throws Exception {
        sdao.updateSubject(null);
    }

    @Test(expected = DaoException.class) public void test_updateSubject_withNotExistingId_Fail()
        throws Exception {
        when(mockPreparedStatement.executeUpdate()).thenThrow(SQLException.class);

        Subject s = new Subject();
        s.setSubjectId(9999999);

        sdao.updateSubject(s);

        PowerMockito.verifyStatic();
        ConnectionH2.getConnection();

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).setInt(anyInt(), anyInt());
        verify(mockPreparedStatement).setString(anyInt(), anyString());
        verify(mockPreparedStatement).setFloat(anyInt(), anyFloat());
    }

    @Test(expected = DaoException.class)
    public void test_updateSubject_withNoDatabaseConnection_Fail() throws Exception {
        when(ConnectionH2.getConnection()).thenThrow(SQLException.class);

        sdao.updateSubject(new Subject());

        PowerMockito.verifyStatic();
        ConnectionH2.getConnection();
    }

    @Test public void test_updateSubject_withValidSubject() throws Exception {
        Subject s = new Subject();
        s.setSubjectId(11);
        s.setName("TEST");
        s.setEcts(4.3f);
        s.setSemester("SS25");

        sdao.updateSubject(s);

        verify(mockPreparedStatement).executeUpdate();
    }
    // -------------------------------------------------------------------------------

    // Testing deleteSubject(Subject)
    // -------------------------------------------------------------------------------
    @Test(expected = DaoException.class) public void test_deleteSubject_withNull_Fail()
        throws Exception {
        sdao.deleteSubject(null);
    }

    @Test(expected = DaoException.class)
    public void test_deleteSubject_withNotExistingSubject_Fail() throws Exception {
        when(mockPreparedStatement.executeUpdate()).thenThrow(SQLException.class);

        sdao.deleteSubject(new Subject());

        PowerMockito.verifyStatic();
        ConnectionH2.getConnection();

        verify(mockConnection).prepareStatement(anyString());
        verify(mockPreparedStatement).setInt(anyInt(), anyInt());
        verify(mockPreparedStatement).setString(anyInt(), anyString());
        verify(mockPreparedStatement).setFloat(anyInt(), anyFloat());
    }

    @Test(expected = DaoException.class)
    public void test_deleteSubject_withNoDatabaseConnection_Fail() throws Exception {
        when(ConnectionH2.getConnection()).thenThrow(SQLException.class);

        sdao.deleteSubject(new Subject());

        PowerMockito.verifyStatic();
        ConnectionH2.getConnection();
    }

    @Test public void test_deleteSubject_withValidSubject() throws Exception {
        Subject s = new Subject();
        s.setSubjectId(1);
        s.setName("TESTING");
        s.setEcts(1.0f);
        s.setSemester("WS10");

        sdao.createSubject(s);

        verify(mockPreparedStatement).executeUpdate();
    }
    // -------------------------------------------------------------------------------

    @After public void tearDown() throws Exception {
        // nothing to tear down
    }
}
