package at.ac.tuwien.sepm.ss16.qse18.service.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.impl.*;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Class TopicServiceImplTest
 * Tests for the sercvice layer in TopicServiceImpl. In order to be isolated while testing, this
 * test class uses mocks primarily to bypass the database connection procedure.
 *
 * @author Philipp Ganiu, Bicer Cem
 */
@RunWith(MockitoJUnitRunner.class) public class TopicServiceImplTest {
    @Mock private TopicDaoJdbc mockTopicDao;
    @Mock private QuestionDaoJdbc mockQuestionDao;
    @Mock private SubjectDaoJdbc mockSubjectDao;
    @Mock private SubjectTopicDaoJdbc mockSubjectTopicDao;
    @Mock private QuestionTopicDaoJdbc mockQuestionTopicDao;
    private TopicServiceImpl topicService;

    @Before public void setUp() throws Exception {
        topicService = new TopicServiceImpl(mockTopicDao);
    }

    //Testing getTopic(int)
    //----------------------------------------------------------------------------------------------
    @Test public void testIf_getTopic_callsRightMethodInDao() throws Exception {
        topicService.getTopic(1);
        verify(mockTopicDao).getTopic(1);
    }


    @Test public void testIf_getTopics_callsRightMethodInDao() throws Exception {
        topicService.getTopics();
        verify(mockTopicDao).getTopics();
    }

    //Testing createTopic(Topic, Subject)
    //----------------------------------------------------------------------------------------------
    @Test public void testIf_createTopic_callsRightMethodInDao() throws Exception {
        Topic t = new Topic(1, "x");
        Subject s = new Subject();
        topicService.createTopic(t, s);
        verify(mockTopicDao).createTopic(t, s);
    }

    @Test(expected = ServiceException.class)
    public void test_createTopic_invalidTopicThrowsException() throws Exception {
        topicService.createTopic(new Topic(), new Subject());
    }


    //----------------------------------------------------------------------------------------------


    //Testing updateTopic(Topic)
    //----------------------------------------------------------------------------------------------
    @Test public void testIf_updateTopic_callsRightMethodInDao() throws Exception {
        Topic t = new Topic(1, "x");
        topicService.updateTopic(t);
        verify(mockTopicDao).updateTopic(t);
    }

    @Test(expected = ServiceException.class)
    public void test_updateTopic_invalidTopicThrowsException() throws Exception {
        topicService.updateTopic(new Topic());
    }


    //----------------------------------------------------------------------------------------------


    //Testing deleteTopic(Topic)
    //----------------------------------------------------------------------------------------------
    @Test public void testIf_deleteTopic_callsRightMethodInDao() throws Exception {
        Topic t = new Topic(1, "x");
        topicService.deleteTopic(t);
        verify(mockTopicDao).deleteTopic(t);
    }

    @Test(expected = ServiceException.class)
    public void test_deleteTopic_invalidTopicThrowsException() throws Exception {
        topicService.deleteTopic(new Topic());
    }


    //----------------------------------------------------------------------------------------------


    //Tesing getTopicsToSubject(Subject)
    //----------------------------------------------------------------------------------------------
    @Test(expected = ServiceException.class) public void test_getTopicsToSubject_withNull()
        throws Exception {
        topicService.getTopicsFromSubject(null);
    }

    @Test public void test_getTopicsToSubject_withValidSubject() throws Exception {
        topicService =
            new TopicServiceImpl(mockSubjectTopicDao, mockSubjectDao, mockTopicDao, mockQuestionDao,
                mockQuestionTopicDao);

        Subject testSubject = new Subject();
        testSubject.setSubjectId(1);

        List<Topic> testTopics = new LinkedList<>();

        Topic testTopic1 = new Topic();
        testTopic1.setTopicId(1);
        testTopic1.setTopic("TestTopic1");

        Topic testTopic2 = new Topic();
        testTopic2.setTopicId(2);
        testTopic2.setTopic("TestTopic2");

        testTopics.add(testTopic1);
        testTopics.add(testTopic2);

        when(mockSubjectTopicDao.getTopicToSubject(any())).thenReturn(testTopics);

        List<Topic> test = topicService.getTopicsFromSubject(testSubject);

        assertTrue("Both topics should have different IDs",
            test.get(0).getTopicId() != test.get(1).getTopicId());
        assertFalse("Both topics should have different topic names",
            test.get(0).getTopic().equals(test.get(1).getTopic()));
    }
    //----------------------------------------------------------------------------------------------

    //Tesing getTopicsFromQuestion(Question)
    //----------------------------------------------------------------------------------------------
    @Test(expected = ServiceException.class) public void test_getTopicsFromQuestion_withNull()
        throws Exception {
        topicService.getTopicsFromQuestion(null);
    }

    @Test public void test_getTopicsFromQuestion_withValidQuestion() throws Exception {
        topicService =
            new TopicServiceImpl(mockSubjectTopicDao, mockSubjectDao, mockTopicDao, mockQuestionDao,
                mockQuestionTopicDao);

        Question testQuestion = new Question();
        testQuestion.setQuestionId(1);

        List<Topic> testTopics = new LinkedList<>();

        Topic testTopic1 = new Topic();
        testTopic1.setTopicId(1);
        testTopic1.setTopic("TestTopic1");

        Topic testTopic2 = new Topic();
        testTopic2.setTopicId(2);
        testTopic2.setTopic("TestTopic2");

        testTopics.add(testTopic1);
        testTopics.add(testTopic2);

        when(mockQuestionTopicDao.getTopicsFromQuestion(any())).thenReturn(testTopics);

        List<Topic> test = topicService.getTopicsFromQuestion(testQuestion);

        assertTrue("Both topics should have different IDs",
            test.get(0).getTopicId() != test.get(1).getTopicId());
        assertFalse("Both topics should have different topic names",
            test.get(0).getTopic().equals(test.get(1).getTopic()));
    }
    //----------------------------------------------------------------------------------------------

    @After public void tearDown() throws Exception {

    }

}
