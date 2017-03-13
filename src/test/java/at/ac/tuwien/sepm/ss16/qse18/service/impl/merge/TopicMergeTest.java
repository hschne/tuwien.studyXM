package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportQuestion;
import at.ac.tuwien.sepm.ss16.qse18.service.ImportService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author Hans-Joerg Schroedl
 */
@RunWith(MockitoJUnitRunner.class) public class TopicMergeTest {

    @Mock ImportService importServiceMock;

    @Mock QuestionMerge questionMergeMock;

    private TopicMerge topicMerge;

    @Before public void setUp() {
        topicMerge = new TopicMerge();
        topicMerge.setImportService(importServiceMock);
        topicMerge.setQuestionMerge(questionMergeMock);
    }


    @Test public void test_merge_newQuestionsAreImported() throws Exception {
        TopicConflict topicConflict = mock(TopicConflict.class);
        List<ExportQuestion> questions = new ArrayList<>();
        questions.add(new ExportQuestion(null, null, null));
        when(topicConflict.getNonConflictingImported()).thenReturn(questions);
        when(topicConflict.getExistingTopic()).thenReturn(new Topic());

        topicMerge.merge(topicConflict);

        verify(importServiceMock).importQuestion(any(), any());
    }

    @Test public void test_merge_conflictingQuestionsAreMerged() throws Exception {
        TopicConflict topicConflict = mock(TopicConflict.class);
        List<QuestionConflict> questions = new ArrayList<>();
        questions.add(new QuestionConflict());
        when(topicConflict.getQuestionConflicts()).thenReturn(questions);

        topicMerge.merge(topicConflict);

        verify(questionMergeMock).merge(any(), any());
    }



}
