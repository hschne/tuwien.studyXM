package at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise;

import at.ac.tuwien.sepm.ss16.qse18.service.ExerciseExamService;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.SubjectService;
import at.ac.tuwien.sepm.ss16.qse18.service.TopicService;
import javafx.fxml.FXML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Controller of the create exam window in which in which new exams can be saved into the database
 *
 * @author Zhang Haixiang, Bicer Cem, Hans-Jörg Schrödl
 */
@Component public class NewExerciseExamController extends NewExerciseExamBase {

    @Autowired public NewExerciseExamController(ExerciseExamService exerciseExamService,
        SubjectService subjectService, TopicService topicService, QuestionService questionService) {
        super(exerciseExamService, subjectService, topicService, questionService);
    }

    @Override @FXML public void handleCreate() {
        logger.debug("Create button pressed. Entering create method.");
        if (validateFields())
            return;
        createExam();
        showSuccess("exercise was created");
        mainFrameController.handleExams();

    }
}
