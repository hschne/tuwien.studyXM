package at.ac.tuwien.sepm.ss16.qse18.gui.subject;

import at.ac.tuwien.sepm.ss16.qse18.domain.Subject;
import at.ac.tuwien.sepm.ss16.qse18.gui.JavaFxThreadingRule;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;
import at.ac.tuwien.sepm.util.SpringFXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.*;

/**
 * @author Hans-Joerg Schroedl
 */
@RunWith(MockitoJUnitRunner.class)
public class SubjectOverviewControllerTest {

    @Rule public JavaFxThreadingRule javafxRule = new JavaFxThreadingRule();

    @Mock private SpringFXMLLoader mockSpringFXMLLoader;

    @Mock private SubjectService mockSubjectService;

    private SubjectOverviewController controller;

    @Before public void setUp(){
        controller = new SubjectOverviewController(mockSpringFXMLLoader, mockSubjectService);

        controller.subjects = new TableView<>();
        controller.nameColumn = new TableColumn<>();
        controller.semesterColumn = new TableColumn<>();
        controller.ectsColumn = new TableColumn<>();
        controller.authorColumn = new TableColumn<>();
        controller.timeSpentColumn = new TableColumn<>();
    }

    @Test public void testInitialzeOk() throws Exception{
        when(mockSubjectService.getSubjects()).thenReturn(CreateSubjectList());

        controller.initialize();

        assertTrue(controller.subjects.getItems().size() == 1);
    }

    @Test public void testHandleNew() throws Exception {
    }

    @Test public void testHandleDelete() throws Exception {

    }

    @Test public void testHandleEdit() throws Exception {

    }

    @Test public void testAddSubject() throws Exception {

    }

    @Test public void testUpdatesubject() throws Exception {

    }

    private List<Subject> CreateSubjectList(){
        List<Subject> subjects = new ArrayList<>();
        Subject subject = new Subject();
        subject.setName("Test");
        subjects.add(subject);
        return subjects;
    }

}
