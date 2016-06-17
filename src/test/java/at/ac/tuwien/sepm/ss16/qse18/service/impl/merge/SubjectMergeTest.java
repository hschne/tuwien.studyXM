package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.ImportUtil;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.ImportService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.service.impl.merge.DummyExportEntityFactory.createDummyExportTopics;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Hans-Joerg Schroedl
 */
@RunWith(MockitoJUnitRunner.class) public class SubjectMergeTest {


    @Mock ImportService importServiceMock;

    @Mock ImportUtil importUtilMock;

    @Mock TopicMerge topicMergeMock;

    private SubjectMerge subjectMerge;

    @Before public void setUp() {
        subjectMerge = new SubjectMerge();
        subjectMerge.setImportService(importServiceMock);
        subjectMerge.setTopicMerge(topicMergeMock);
        subjectMerge.setImportUtil(importUtilMock);
    }

    @Test(expected = ServiceException.class) public void test_merge_notResolved() throws Exception {
        SubjectConflict subjectConflict = mock(SubjectConflict.class);

        when(subjectConflict.isResolved()).thenReturn(false);

        subjectMerge.merge(subjectConflict);
    }

    @Test public void test_merge_newTopicsAreImported() throws Exception {
        SubjectConflict subjectConflict = mock(SubjectConflict.class);
        List<ExportTopic> topics = createDummyExportTopics();
        when(subjectConflict.getNonConflictingImported()).thenReturn(topics);

        when(subjectConflict.isResolved()).thenReturn(true);

        subjectMerge.merge(subjectConflict);

        verify(importServiceMock).importTopic(any(), any());
    }

    @Test public void test_merge_newQuestionsAreImported() throws Exception {
        SubjectConflict subjectConflict = mock(SubjectConflict.class);
        List<TopicConflict> topics = new ArrayList<>();
        TopicConflict topicConflict = new TopicConflict();
        topics.add(topicConflict);
        when(subjectConflict.getConflictingTopics()).thenReturn(topics);

        when(subjectConflict.isResolved()).thenReturn(true);
        subjectMerge.merge(subjectConflict);

        verify(topicMergeMock).merge(topicConflict);
    }



}
