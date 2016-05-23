package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.QuestionTopicDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.SubjectTopicDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.TopicDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectTopicQuestionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * Class SubjectTopicQuestionServiceImplTest
 * Tests for the sercvice layer in SubjectTopicQuestionDaoImpl. In order to be isolated while testing, this
 * test class uses mocks primarily to bypass the database connection procedure.
 *
 * @author Philipp Ganiu
 */
@RunWith(MockitoJUnitRunner.class)
public class SubjectTopicQuestionServiceImplTest {
    @Mock private SubjectTopicDaoJdbc mockDaoJdbc;
    @Mock private QuestionTopicDaoJdbc mockDaoJdbc2;
    private SubjectTopicQuestionService service;

    @Before public void setUp() throws Exception {
        service = new SubjectTopicQuestionServiceImpl(mockDaoJdbc,mockDaoJdbc2);
    }

    //Testing getTopicToSubject(Subject)
    //----------------------------------------------------------------------------------------------
    @Test
    public void testIf_getTopicToSubjectWithNumberOfQuestions_callsRightMethodInDao() throws Exception {
        Subject s = new Subject();
        Topic t = new Topic();
        service.getTopicToSubjectWithNumberOfQuestions(s);
        verify(mockDaoJdbc).getTopicToSubject(s);
    }

    @Test (expected = ServiceException.class)
    public void testIf_getTopicToSubjectWithNumberOfQuestion_SubjectNullThrowsException() throws Exception{
        service.getTopicToSubjectWithNumberOfQuestions(null);
    }
    //----------------------------------------------------------------------------------------------

    @After public void tearDown() throws Exception {

    }

}
