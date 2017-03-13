package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportSubject;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportTopic;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummySubject;
import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummyTopics;
import static at.ac.tuwien.sepm.ss16.qse18.service.impl.merge.DummyExportEntityFactory.createDummyExportTopics;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Hans-Joerg Schroedl
 */
@RunWith(MockitoJUnitRunner.class) public class TopicConflictDetectionTest {

    @Mock private SubjectTopicDao subjectTopicDaoMock;

    @Mock private ApplicationContext applicationContextMock;

    private TopicConflictDetection topicConflictDetection;

    @Before public void setUp(){
        topicConflictDetection = new TopicConflictDetection(applicationContextMock);
        topicConflictDetection.setSubjectTopicDao(subjectTopicDaoMock);
    }

    @Test public void test_getConflictingTopics_noConflictingTopics() throws Exception {
        List<Topic> existingTopics = new ArrayList<>();

        when(subjectTopicDaoMock.getTopicToSubject(any())).thenReturn(existingTopics);

        List<TopicConflict> duplicates = topicConflictDetection.getConflictingTopics();

        assertTrue(duplicates.isEmpty());
    }

    @Test public void test_getConflictingTopics_conflictingTopicsReturned() throws Exception {
        List<Topic> existingTopics = createDummyTopics();

        List<ExportTopic> importedTopics = createDummyExportTopics();
        when(subjectTopicDaoMock.getTopicToSubject(any())).thenReturn(existingTopics);
        when(applicationContextMock.getBean(TopicConflict.class)).thenReturn(new TopicConflict());
        ExportSubject importSubject = Mockito.mock(ExportSubject.class);
        when(importSubject.getTopics()).thenReturn(importedTopics);

        TopicConflict returnedTopicConflict = mock(TopicConflict.class);
        List<QuestionConflict> questionConflicts = new ArrayList<>();
        questionConflicts.add(new QuestionConflict());
        when(returnedTopicConflict.initializeQuestionConflicts()).thenReturn(questionConflicts);
        when(applicationContextMock.getBean(TopicConflict.class)).thenReturn(returnedTopicConflict);

        topicConflictDetection.setSubjects(null,importSubject);
        List<TopicConflict> duplicates = topicConflictDetection.getConflictingTopics();

        assertFalse(duplicates.isEmpty());
    }

    @Test public void test_getConflictingTopics_conflictingWithNoConflictingQuestions_NoConflict() throws Exception {
        List<Topic> existingTopics = createDummyTopics();

        List<ExportTopic> importedTopics = createDummyExportTopics();
        when(subjectTopicDaoMock.getTopicToSubject(any())).thenReturn(existingTopics);
        when(applicationContextMock.getBean(TopicConflict.class)).thenReturn(new TopicConflict());
        ExportSubject importSubject = Mockito.mock(ExportSubject.class);
        when(importSubject.getTopics()).thenReturn(importedTopics);

        TopicConflict returnedTopicConflict = mock(TopicConflict.class);
        List<QuestionConflict> questionConflicts = new ArrayList<>();
        when(returnedTopicConflict.initializeQuestionConflicts()).thenReturn(questionConflicts);
        when(applicationContextMock.getBean(TopicConflict.class)).thenReturn(returnedTopicConflict);

        topicConflictDetection.setSubjects(null,importSubject);
        List<TopicConflict> duplicates = topicConflictDetection.getConflictingTopics();

        assertTrue(duplicates.isEmpty());
    }


    @Test public void test_initialize_subjectSet() throws Exception{
        Subject existingSubject = createDummySubject();
        when(subjectTopicDaoMock.getTopicToSubject(any())).thenReturn(new ArrayList<>());

        ExportSubject importSubject = Mockito.mock(ExportSubject.class);
        when(importSubject.getTopics()).thenReturn(new ArrayList<>());

        topicConflictDetection.setSubjects(existingSubject, importSubject);
        topicConflictDetection.getConflictingTopics();

        verify(subjectTopicDaoMock).getTopicToSubject(existingSubject);
    }




}
