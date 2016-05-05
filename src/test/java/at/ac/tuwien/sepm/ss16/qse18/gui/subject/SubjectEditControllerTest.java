package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.gui.JavaFxThreadingRule;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * @author Hans-Joerg Schroedl
 */
@RunWith(MockitoJUnitRunner.class) public class SubjectEditControllerTest {

    @Rule public JavaFxThreadingRule javafxRule = new JavaFxThreadingRule();
    private SubjectEditController controller;
    @Mock private SubjectOverviewController mockSubjectOverviewController;

    @Mock private Stage mockDialogStage;

    @Before public void setUp() {
        controller = new SubjectEditController(mockSubjectOverviewController);
        controller.setStage(mockDialogStage);
        controller.name = new TextField();
        controller.author = new TextField();
        controller.ects = new TextField();
        controller.semester = new TextField();
    }

    @Test public void testHandleOkWithNewSubjectAddsNewSubject() throws Exception {
        controller.setSubject(null);
        controller.handleOk();

        verify(mockSubjectOverviewController).addSubject(any(ObservableSubject.class));
    }

    @Test public void testHandleOkWithExistingSubjectUpdatesSubject() throws Exception {
        ObservableSubject subjectToUpdate = new ObservableSubject(new Subject());
        controller.setSubject(subjectToUpdate);
        controller.handleOk();

        verify(mockSubjectOverviewController).updatesubject(subjectToUpdate);
    }

    @Test public void testHandleCancelClosesDialogStage() throws Exception{
        controller.handleCancel();

        verify(mockDialogStage).close();
    }
}
