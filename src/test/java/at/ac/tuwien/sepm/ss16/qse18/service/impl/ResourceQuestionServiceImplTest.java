package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ResourceQuestionDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.ResourceQuestionDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.QuestionType;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.service.ResourceQuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Bicer Cem
 */
@RunWith(PowerMockRunner.class) public class ResourceQuestionServiceImplTest {

    @Mock private ResourceQuestionDao mockResourceQuestionDao;
    private ResourceQuestionService resourceQuestionService;
    private Resource dummyResource;
    private Question dummyQuestion;

    @Before public void setUp() throws Exception {
        dummyResource = DummyEntityFactory.createDummyResource();
        dummyResource.setReference("src/main/resources/resources/dummy");

        dummyQuestion = new Question(1, "Frage?", QuestionType.MULTIPLECHOICE, 2);

        resourceQuestionService = new ResourceQuestionServiceImpl(mockResourceQuestionDao);
    }

    @Test(expected = ServiceException.class)
    public void test_createReference_withInvalidInput_Fail() throws Exception {
        doThrow(DaoException.class).when(mockResourceQuestionDao)
            .createResourceQuestion(any(), any());

        resourceQuestionService.createReference(dummyResource, dummyQuestion);
    }

    @Test public void test_createReference_withValidInput() throws Exception {
        resourceQuestionService.createReference(dummyResource, dummyQuestion);

        verify(mockResourceQuestionDao).createResourceQuestion(dummyResource, dummyQuestion);
    }

    @After public void tearDown() throws Exception {
        // nothing to tear down
    }
}
