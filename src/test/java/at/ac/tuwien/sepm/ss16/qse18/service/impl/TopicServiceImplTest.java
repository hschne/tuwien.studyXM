package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.TopicDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.SubjectDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.dao.impl.TopicDaoJdbc;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

/**
 * @author Philipp Ganiu
 */
@RunWith(MockitoJUnitRunner.class) public class TopicServiceImplTest {
    @Mock private TopicDaoJdbc mockDaoJdbc;
    private TopicServiceImpl topicService;

    @Before public void setUp() throws Exception {
        topicService = new TopicServiceImpl(mockDaoJdbc);
    }
    @Test public void testIf_getTopic_callsRightMethodInDao() throws Exception {
        topicService.getTopic(1);
        verify(mockDaoJdbc).getTopic(1);
    }

    @Test public void testIf_getTopics_callsRightMethodInDao() throws Exception {
        topicService.getTopics();
        verify(mockDaoJdbc).getTopics();
    }

    @Test public void testIf_createTopic_callsRightMethodInDao() throws Exception {
        Topic t = new Topic(1,"x");
        Subject s = new Subject();
        topicService.createTopic(t,s);
        verify(mockDaoJdbc).createTopic(t,s);
    }
    @Test public void testIf_updateTopic_callsRightMethodInDao() throws Exception {
        Topic t = new Topic(1,"x");
        topicService.updateTopic(t);
        verify(mockDaoJdbc).updateTopic(t);
    }

    @Test public void testIf_deleteTopic_callsRightMethodInDao() throws Exception {
        Topic t = new Topic(1,"x");
        topicService.deleteTopic(t);
        verify(mockDaoJdbc).deleteTopic(t);
    }

    @Test(expected = ServiceException.class)
    public void test_createTopic_invalidTopicThrowsException() throws Exception {
        topicService.createTopic(new Topic(),new Subject());
    }

    @Test(expected =  ServiceException.class)
    public void test_createTopic_topicNullThrowsException() throws Exception{
        topicService.createTopic(null,new Subject());
    }

    @Test(expected = ServiceException.class)
    public void test_updateTopic_invalidTopicThrowsException() throws Exception {
        topicService.updateTopic(new Topic());
    }

    @Test(expected =  ServiceException.class)
    public void test_updateTopic_topicNullThrowsException() throws Exception{
        topicService.updateTopic(null);
    }

    @Test(expected = ServiceException.class)
    public void test_deleteTopic_invalidTopicThrowsException() throws Exception {
        topicService.deleteTopic(new Topic());
    }

    @Test(expected =  ServiceException.class)
    public void test_deleteTopic_topicNullThrowsException() throws Exception{
        topicService.deleteTopic(null);
    }





    @After public void tearDown() throws Exception {

    }

}
