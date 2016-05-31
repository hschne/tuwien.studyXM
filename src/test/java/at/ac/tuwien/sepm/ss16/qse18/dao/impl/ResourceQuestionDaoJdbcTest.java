package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory;
import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.ResourceType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Bicer Cem
 */
@RunWith(PowerMockRunner.class) @PowerMockIgnore("javax.management.*")
public class ResourceQuestionDaoJdbcTest {

    private ResourceQuestionDaoJdbc resourceQuestionDaoJdbc;
    @Mock private ConnectionH2 mockConnectionH2;
    @Mock private Connection mockConnection;
    @Mock private Statement mockStatement;
    @Mock private PreparedStatement mockPreparedStatement;
    @Mock private ResultSet mockResultSet;

    private Resource dummyResource;
    private Question dummyQuestion;

    @Before public void setUp() throws Exception {
        when(mockConnectionH2.getConnection()).thenReturn(mockConnection);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        resourceQuestionDaoJdbc = new ResourceQuestionDaoJdbc(mockConnectionH2);

        dummyResource = DummyEntityFactory.createDummyResource();
        dummyResource.setReference("src/main/resources/resources/dummy");
        dummyQuestion = new Question(1, "Frage?", QuestionType.MULTIPLECHOICE, 2);
    }

    @Test(expected = DaoException.class)
    public void test_createResourceQuestion_withNoDatabaseConnection_Fail() throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);

        resourceQuestionDaoJdbc.createResourceQuestion(dummyResource, dummyQuestion);
    }

    @Test(expected = DaoException.class)
    public void test_createResourceQuestion_withInvalidResource_Fail() throws Exception {
        resourceQuestionDaoJdbc.createResourceQuestion(null, dummyQuestion);
    }

    @Test(expected = DaoException.class)
    public void test_createResourceQuestion_withInvalidQuestion_Fail() throws Exception {
        resourceQuestionDaoJdbc.createResourceQuestion(dummyResource, null);
    }

    @Test public void test_createResourceQuestion_withValidInput() throws Exception {
        resourceQuestionDaoJdbc.createResourceQuestion(dummyResource, dummyQuestion);

        verify(mockPreparedStatement).executeUpdate();
    }

    @After public void tearDown() throws Exception {
        // nothing to tear down
    }
}
