package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.gui.JavaFxThreadingRule;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;
import at.ac.tuwien.sepm.util.AlertBuilder;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Hans-Joerg Schroedl
 */
@RunWith(MockitoJUnitRunner.class) public class SubjectOverviewControllerTest {

    @Rule public JavaFxThreadingRule javafxRule = new JavaFxThreadingRule();

    @Mock private SpringFXMLLoader mockSpringFXMLLoader;

    @Mock private SubjectService mockSubjectService;

    @Mock private AlertBuilder mockAlertBuilder;

    private SubjectOverviewController controller;

    @Before public void setUp() {
        controller = new SubjectOverviewController(mockSubjectService,
            mockAlertBuilder);
        controller.subjectListView = new ListView<>();
        controller.editButton = new Button();
        controller.deleteButton = new Button();
    }

    @Test public void testInitialzeOk() throws Exception {
        when(mockSubjectService.getSubjects()).thenReturn(CreateSubjectList());
        controller.initialize();
        assertTrue(controller.subjectListView.getItems().size() == 1);
    }


    private List<Subject> CreateSubjectList() {
        List<Subject> subjects = new ArrayList<>();
        Subject subject = new Subject();
        subject.setName("Test");
        subjects.add(subject);
        return subjects;
    }

}
