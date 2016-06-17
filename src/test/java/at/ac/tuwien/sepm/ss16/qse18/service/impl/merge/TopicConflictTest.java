package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportQuestion;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportTopic;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummyTopic;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Hans-Joerg Schroedl
 */
@RunWith(MockitoJUnitRunner.class) public class TopicConflictTest {

@Mock QuestionConflictDetection questionConflictDetectionMock;

    private TopicConflict topicConflict;

    @Before public void setUp(){
        topicConflict = new TopicConflict();
        topicConflict.setQuestionConflictDetection(questionConflictDetectionMock);
    }

    @Test public void test_getConflictingQuestions_conflictsFromDetectionReturned() throws Exception{
        List<QuestionConflict> conflicts = new ArrayList<>();
        when(questionConflictDetectionMock.getConflictingQuestions()).thenReturn(conflicts);

        topicConflict.setTopics(createDummyTopic(), new ExportTopic(createDummyTopic(), null));
        List<QuestionConflict> result = topicConflict.initializeQuestionConflicts();

        assertEquals(conflicts, result);
    }

    @Test public void test_getNonConflictingImported_nonConflictingReturned() throws Exception{
        List<QuestionConflict> conflicts = new ArrayList<>();
        topicConflict.setQuestionConflicts(conflicts);

        List<ExportQuestion> result = topicConflict.getNonConflictingImported();

        assertTrue(result.isEmpty());
    }


    @Test public void test_isResolved_conflictNotResolved() throws Exception{
        List<QuestionConflict> conflicts = new ArrayList<>();
        QuestionConflict conflict = Mockito.mock(QuestionConflict.class);
        when(conflict.isResolved()).thenReturn(false);
        conflicts.add(conflict);
        topicConflict.setQuestionConflicts(conflicts);

        boolean result = topicConflict.isResolved();

        assertFalse(result);
    }

    @Test public void test_isResolved_conflictResolved() throws Exception{
        List<QuestionConflict> conflicts = new ArrayList<>();
        QuestionConflict conflict = Mockito.mock(QuestionConflict.class);
        when(conflict.isResolved()).thenReturn(true);
        conflicts.add(conflict);
        topicConflict.setQuestionConflicts(conflicts);

        boolean result = topicConflict.isResolved();

        assertTrue(result);
    }



}
