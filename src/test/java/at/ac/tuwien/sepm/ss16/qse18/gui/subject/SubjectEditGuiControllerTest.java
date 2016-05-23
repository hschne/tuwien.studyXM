package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.gui.JavaFxThreadingRule;
import at.ac.tuwien.sepm.ss16.qse18.gui.MainFrameController;
import at.ac.tuwien.sepm.ss16.qse18.gui.observable.ObservableSubject;
import javafx.scene.control.TextField;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

/**
 * Class SubjectEditGuiControllerTest
 * Tests for the gui layer in SubjectEditGuiController. In order to be isolated while testing, this
 * test class uses mocks primarily to bypass the database connection procedure.
 *
 * @author Hans-Joerg Schroedl
 */
@RunWith(MockitoJUnitRunner.class) public class SubjectEditGuiControllerTest {

    @Rule public JavaFxThreadingRule javafxRule = new JavaFxThreadingRule();
    private SubjectEditController controller;
    @Mock private SubjectOverviewController mockSubjectOverviewController;
    @Mock private MainFrameController mockMainFrameController;


    @Before public void setUp() {
        controller = new SubjectEditController();
        controller.name = new TextField();
        controller.author = new TextField();
        controller.ects = new TextField();
        controller.semester = new TextField();
        controller.setMainFrameController(mockMainFrameController);
        controller.setSubjectOverviewController(mockSubjectOverviewController);
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

        verify(mockSubjectOverviewController).updateSubject(eq(subjectToUpdate), any());
    }

    @Test public void testHandleCancelClosesDialogStage() throws Exception {
        controller.handleCancel();

        verify(mockMainFrameController).handleSubjects();
    }
}
