package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportSubject;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportTopic;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummySubject;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
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
        Subject existingSubject = createDummySubject();
        subjectConflict.setSubjects(existingSubject,new ExportSubject(createDummySubject(),null));

        List<TopicConflict> result = subjectConflict.getConflictingTopics();
        assertTrue(result.isEmpty());

    }

    @Test public void test_getConflictingQuestions_conflictingQuestionsFound() throws Exception{
        List<TopicConflict> topicConflicts = new ArrayList<>();
        TopicConflict conflict = Mockito.mock(TopicConflict.class);
        topicConflicts.add(conflict);
        when(topicConflictDetectionMock.getConflictingTopics()).thenReturn(topicConflicts);
        Subject existingSubject = createDummySubject();
        subjectConflict.setSubjects(existingSubject,new ExportSubject(createDummySubject(),null));

        subjectConflict.getConflictingTopics();
        List<TopicConflict> result = subjectConflict.getConflictingTopics();

        assertFalse(result.isEmpty());
    }

    @Test public void test_getNonConflictingImported_noneReturned() throws Exception{
        TopicConflict conflict = new TopicConflict();
        List<TopicConflict> topicConflicts = new ArrayList<>();
        topicConflicts.add(conflict);
        subjectConflict.setTopicConflicts(topicConflicts);

        List<ExportTopic> topics = subjectConflict.getNonConflictingImported();

        assertTrue(topics.isEmpty());
    }

    @Test public void test_isResolved_trueReturned() throws Exception{
        TopicConflict conflict = Mockito.mock(TopicConflict.class);
        when(conflict.getResolution()).thenReturn(ConflictResolution.EXISTING);
        List<TopicConflict> topicConflicts = new ArrayList<>();
        topicConflicts.add(conflict);
        subjectConflict.setTopicConflicts(topicConflicts);

        Boolean result = subjectConflict.isResolved();

        assertTrue(result);
    }


    @Test public void test_isResolved_falseReturned() throws Exception{
        TopicConflict conflict = Mockito.mock(TopicConflict.class);
        when(conflict.getResolution()).thenReturn(ConflictResolution.UNRESOLVED);
        List<TopicConflict> topicConflicts = new ArrayList<>();
        topicConflicts.add(conflict);
        subjectConflict.setTopicConflicts(topicConflicts);

        Boolean result = subjectConflict.isResolved();

        assertFalse(result);
    }





}
