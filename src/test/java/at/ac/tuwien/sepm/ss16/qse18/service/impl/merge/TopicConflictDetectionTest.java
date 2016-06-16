package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportTopic;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummySubject;
import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummyTopics;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Hans-Joerg Schroedl
 */
@RunWith(MockitoJUnitRunner.class) public class TopicConflictDetectionTest {

    @Mock private SubjectTopicDao subjectTopicDaoMock;

    private TopicConflictDetection topicConflictDetection;

    @Before public void setUp(){
        topicConflictDetection = new TopicConflictDetection();
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

        topicConflictDetection.initialize(null,importedTopics);
        List<TopicConflict> duplicates = topicConflictDetection.getConflictingTopics();

        assertFalse(duplicates.isEmpty());
    }


    @Test public void test_initialize_subjectSet() throws Exception{
        Subject existingSubject = createDummySubject();
        when(subjectTopicDaoMock.getTopicToSubject(any())).thenReturn(new ArrayList<>());

        topicConflictDetection.initialize(existingSubject, new ArrayList<>());
        topicConflictDetection.getConflictingTopics();

        verify(subjectTopicDaoMock).getTopicToSubject(existingSubject);
    }

    private List<ExportTopic> createDummyExportTopics(){
        List<Topic> topics = createDummyTopics();
        return topics.stream().map(p -> new ExportTopic(p, null)).collect(Collectors.toList());
    }



}
