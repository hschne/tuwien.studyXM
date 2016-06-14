package at.ac.tuwien.sepm.ss16.qse18.service.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.dao.SubjectTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.DummyEntityFactory.createDummyQuestion;
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
        topicConflict = new TopicConflict(null,null);
        topicConflict.setQuestionConflictDetection(questionConflictDetectionMock);
    }

    @Test public void test_getConflictingQuestions_conflictsFromDetectionReturned() throws Exception{
        List<QuestionConflict> conflicts = new ArrayList<>();
        when(questionConflictDetectionMock.getConflictingQuestions()).thenReturn(conflicts);

        List<QuestionConflict> result = topicConflict.getConflictingQuestions();

        assertEquals(conflicts, result);
    }

    @Test public void test_getNonConflictingImported_nonConflictingReturned() throws Exception{
        List<QuestionConflict> conflicts = new ArrayList<>();
        topicConflict.setQuestionConflicts(conflicts);

        List<Question> result = topicConflict.getNonConflictingImported();

        assertTrue(result.isEmpty());
    }


    @Test public void test_resolve_questionConflictResolveCalled() throws Exception{
        List<QuestionConflict> conflicts = new ArrayList<>();
        QuestionConflict conflict = Mockito.mock(QuestionConflict.class);
        conflicts.add(conflict);
        topicConflict.setQuestionConflicts(conflicts);

        topicConflict.resolve();

        verify(conflict).resolve();
    }



}
