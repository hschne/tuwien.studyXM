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
        subjectConflict.setTopicConflict(topicConflictMock);
    }

    @Test public void test_getConflictingQuestions_noConflictsFound() throws Exception{
        List<Duplicate<Topic>> duplicateTopics = new ArrayList<>();
        duplicateTopics.add(new Duplicate<>(null, null));
        List<Duplicate<Question>> duplicateQuestions = new ArrayList<>();
        when(topicConflictDetectionMock.getConflictingTopics()).thenReturn(duplicateTopics);
        when(topicConflictMock.getConflictingQuestions()).thenReturn(duplicateQuestions);

        List<Duplicate<Question>> result = subjectConflict.getConflictingQuestions();

        assertTrue(result.isEmpty());

    }

    @Test public void test_getConflictingQuestions_conflictingQuestionsFound() throws Exception{
        List<Duplicate<Topic>> duplicateTopics = new ArrayList<>();
        duplicateTopics.add(new Duplicate<>(null, null));
        List<Duplicate<Question>> duplicateQuestions = new ArrayList<>();
        duplicateQuestions.add(new Duplicate<>(null, null));
        when(topicConflictDetectionMock.getConflictingTopics()).thenReturn(duplicateTopics);
        when(topicConflictMock.getConflictingQuestions()).thenReturn(duplicateQuestions);

        List<Duplicate<Question>> result = subjectConflict.getConflictingQuestions();

        assertFalse(result.isEmpty());

    }

}
