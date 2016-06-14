package at.ac.tuwien.sepm.ss16.qse18.service.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummyTopic;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Hans-Joerg Schroedl
 */
@RunWith(MockitoJUnitRunner.class) public class SubjectConflictTest {

    private SubjectConflict subjectConflict;

    @Mock private TopicConflict topicConflictMock;

    @Mock private TopicConflictDetection topicConflictDetectionMock;

    @Before public void setUp(){
        subjectConflict = new SubjectConflict();
        subjectConflict.setTopicConflictDetection(topicConflictDetectionMock);
    }

    @Test public void test_getConflictingQuestions_noConflictsFound() throws Exception{
        List<TopicConflict> topicConflicts = new ArrayList<>();
        when(topicConflictDetectionMock.getConflictingTopics()).thenReturn(topicConflicts);


        subjectConflict.getConflictingTopics();
        List<TopicConflict> result = subjectConflict.getTopicConflicts();

        assertTrue(result.isEmpty());

    }

    @Test public void test_getConflictingQuestions_conflictingQuestionsFound() throws Exception{
        List<TopicConflict> topicConflicts = new ArrayList<>();
        topicConflicts.add(new FakeTopicConflict(createDummyTopic(), createDummyTopic()));
        when(topicConflictDetectionMock.getConflictingTopics()).thenReturn(topicConflicts);

        subjectConflict.getConflictingTopics();
        List<TopicConflict> result = subjectConflict.getTopicConflicts();

        assertFalse(result.isEmpty());
    }

    private class FakeTopicConflict extends TopicConflict{

        public FakeTopicConflict(Topic existingTopic, Topic importedTopic) {
            super(existingTopic, importedTopic);
        }

        @Override public void getConflictingQuestions(){

        }
    }

}
