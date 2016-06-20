package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ResourceQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ResourceQuestionDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.Tag;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceQuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Bicer Cem
 */
@RunWith(PowerMockRunner.class) @PrepareForTest(ResourceQuestionDaoJdbc.class)
public class ResourceQuestionServiceImplTest {

    @Mock private ResourceQuestionDao mockResourceQuestionDao;
    private ResourceQuestionService resourceQuestionService;
    private Resource dummyResource;
    private Question dummyQuestion;

    @Before public void setUp() throws Exception {
        dummyResource = DummyEntityFactory.createDummyResource();
        dummyResource.setReference("src/main/resources/resources/dummy");

        dummyQuestion = new Question(1, "Frage?", QuestionType.MULTIPLECHOICE, 2, Tag.NORMAL);

        resourceQuestionService = new ResourceQuestionServiceImpl(mockResourceQuestionDao);
    }

    @Test(expected = ServiceException.class)
    public void test_createReference_withDaoException_Fail() throws Exception {
        doThrow(DaoException.class).when(mockResourceQuestionDao)
            .createResourceQuestion(any(), any());

        resourceQuestionService.createReference(dummyResource, dummyQuestion);
    }

    @Test(expected = ServiceException.class)
    public void test_createReference_withInvalidInput_Fail() throws Exception {
        resourceQuestionService.createReference(dummyResource, null);
    }

    @Test public void test_createReference_withValidInput() throws Exception {
        resourceQuestionService.createReference(dummyResource, dummyQuestion);

        verify(mockResourceQuestionDao).createResourceQuestion(dummyResource, dummyQuestion);
    }

    @Test(expected = ServiceException.class)
    public void test_getResourceFromQuestion_withInvalidQuestion_Fail() throws Exception {
        resourceQuestionService.getResourceFromQuestion(null);
    }

    @Test(expected = ServiceException.class)
    public void test_getResourceFromQuestion_withDaoException_Fail() throws Exception {
        when(mockResourceQuestionDao.getResourceOfQuestion(any(Question.class)))
            .thenThrow(DaoException.class);

        resourceQuestionService.getResourceFromQuestion(dummyQuestion);
    }

    @Test public void test_getResourceFromQuestion_withValidQuestion() throws Exception {
        resourceQuestionService.getResourceFromQuestion(dummyQuestion);

        verify(mockResourceQuestionDao).getResourceOfQuestion(dummyQuestion);
    }

    @After public void tearDown() throws Exception {
        // nothing to tear down
    }
}
