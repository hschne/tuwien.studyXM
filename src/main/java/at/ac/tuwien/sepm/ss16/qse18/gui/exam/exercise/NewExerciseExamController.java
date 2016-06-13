package at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise;

import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.service.*;
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

    @FXML public void handleCreate() {
        ExerciseExam exerciseExam;
        logger.debug("Create button pressed. Entering create method.");
        if (validateFields())
            return;

        try {
            exerciseExam = createExam();

            showSuccess("exercise was created with an exam time of " + exerciseExam.getExamTime());
            mainFrameController.handleExams();
        } catch (ServiceException e) {
            logger.error("Could not create exerciseExam: ", e);
            showError(e.getMessage());
        }
    }
}
