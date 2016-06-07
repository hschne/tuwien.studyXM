package at.ac.tuwien.sepm.ss16.qse18.gui.exam;

import at.ac.tuwien.sepm.ss16.qse18.application.MainApplication;
import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.service.*;
import at.ac.tuwien.sepm.ss16.qse18.service.impl.PdfExporterImpl;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * The controller for the create print exam view
 *
 * @author Hans-Joerg Schroedl
 */
@Component public class NewExerciseExamPrintController extends NewExerciseExamBase {

    @Autowired private MainApplication mainApplication;

    @Autowired public NewExerciseExamPrintController(ExerciseExamService exerciseExamService,
        SubjectService subjectService, TopicService topicService, QuestionService questionService) {
        super(exerciseExamService, subjectService, topicService, questionService);
    }

    @FXML public void handleCreate() {
        logger.debug("Create button pressed. Entering create method.");
        if (validateFields())
            return;
        ExerciseExam exam = createExam();
        if (exam == null) {
            return;
        }
        File file = selectFile();
        if (file != null) {
            tryPrint(exam, file);
        }
        showSuccess("ExerciseExam was created");
        mainFrameController.handleExams();
    }

    private File selectFile() {
        FileChooser fileChooser = new FileChooser();
        String defaultPath = "src/main/resources/";
        File defaultDirectory = new File(defaultPath);
        fileChooser.setInitialDirectory(defaultDirectory);
        fileChooser.getExtensionFilters()
            .addAll(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
        fileChooser.setTitle("Add image");
        Stage mainStage = mainApplication.getPrimaryStage();
        return fileChooser.showSaveDialog(mainStage);
    }

    private void tryPrint(ExerciseExam exam, File file) {
        try {
            PdfExporterImpl pdfExporter = new PdfExporterImpl(file.getPath(), exam);
            pdfExporter.exportPdf();
        } catch (ServiceException e) {
            logger.error(e);
            showError(e);
        }
    }
}
