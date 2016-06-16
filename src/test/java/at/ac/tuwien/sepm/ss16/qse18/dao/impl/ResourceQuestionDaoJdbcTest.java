package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoBaseTest;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ResourceDao;
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

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Bicer Cem
 */
@RunWith(PowerMockRunner.class) @PowerMockIgnore("javax.management.*")
public class ResourceQuestionDaoJdbcTest extends DaoBaseTest {

    private ResourceQuestionDaoJdbc resourceQuestionDaoJdbc;
    @Mock private ResourceDao resourceDao;
    private Resource dummyResource;
    private Question dummyQuestion;


    @Before public void setUp() throws Exception {
        super.setUp();
        resourceQuestionDaoJdbc = new ResourceQuestionDaoJdbc(mockConnectionH2);
        resourceDao = new ResourceDaoJdbc(mockConnectionH2);

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

    @Test(expected = DaoException.class)
    public void test_getResourceFromQuestion_withNoDatabaseConnection_Fail() throws Exception {
        when(mockConnectionH2.getConnection()).thenThrow(SQLException.class);

        resourceQuestionDaoJdbc.getResourceOfQuestion(dummyQuestion);
    }

    @Test(expected = DaoException.class)
    public void test_getResourceFromQuestion_withInvalidQuestion_Fail() throws Exception {
        resourceQuestionDaoJdbc.getResourceOfQuestion(null);
    }

    //TODO Test ausbessern
/*
    @Test public void test_getResourceFromQuestion_qithValidQuestion() throws Exception {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(anyInt())).thenReturn(1);
        when(resourceDao.getResource(1)).thenReturn(new Resource());
        when(mockResultSet.getInt(anyInt())).thenReturn(1);


        Resource tmp = resourceQuestionDaoJdbc.getResourceOfQuestion(dummyQuestion);

        assertEquals("The ID should be 1", tmp.getResourceId(), 1);
        assertEquals("The name should be \"TestResource\"", tmp.getName(), "TestResource");
        assertEquals("The reference should be \"/src/resource/test\"", tmp.getReference(),
            "/src/resource/test");
        assertEquals("The type should be ResourceType.PDF", tmp.getType(), ResourceType.PDF);
    }
    */

    @After public void tearDown() throws Exception {
        // nothing to tear down
    }
}
