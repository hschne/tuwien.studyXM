package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportQuestion;
import at.ac.tuwien.sepm.ss16.qse18.service.ImportService;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Hans-Joerg Schroedl
 */
@RunWith(MockitoJUnitRunner.class) public class QuestionMergeTest {

    @Mock ImportService importServiceMock;

    @Mock QuestionService questionServiceMock;

    private QuestionMerge questionMerge;

    @Before public void setUp() {
        questionMerge = new QuestionMerge();
        questionMerge.setQuestionService(questionServiceMock);
        questionMerge.setImportService(importServiceMock);
    }

    @Test(expected = ServiceException.class) public void test_merge_NoResultionSet() throws Exception {
        QuestionConflict questionConflict = mock(QuestionConflict.class);
        when(questionConflict.getResolution()).thenReturn(null);
        Topic topic= new Topic();

        questionMerge.merge(questionConflict, topic);

    }

    @Test public void test_merge_keepExisting() throws Exception {
        QuestionConflict questionConflict = mock(QuestionConflict.class);
        when(questionConflict.getResolution()).thenReturn(ConflictResolution.EXISTING);
        Topic topic= new Topic();

        questionMerge.merge(questionConflict, topic);

    }

    @Test public void test_merge_keepImported() throws Exception {
        QuestionConflict questionConflict = mock(QuestionConflict.class);
        when(questionConflict.getResolution()).thenReturn(ConflictResolution.IMPORTED);
        when(questionConflict.getImportedQuestion()).thenReturn(new ExportQuestion(null, null,null));
        when(questionConflict.getExistingQuestion()).thenReturn(new Question());
        Topic topic= new Topic();

        questionMerge.merge(questionConflict, topic);

        verify(questionServiceMock).deleteQuestion(any());
        verify(importServiceMock).importQuestion(any(), any());

    }


}
