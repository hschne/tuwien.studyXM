package at.ac.tuwien.sepm.ss16.qse18.gui.exam.exercise;

import at.ac.tuwien.sepm.ss16.qse18.domain.ExerciseExam;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.service.*;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.SplitMenuButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller of the create exam window in which in which new exams can be saved into the database
 *
 * @author Zhang Haixiang, Bicer Cem, Hans-Jörg Schrödl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class NewExerciseExamController extends NewExerciseExamBase {
    private int createFlag = 0;
    @FXML private SplitMenuButton createButton;

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
            if(createFlag == 0) {
                mainFrameController.handleExams();
            }
            else if(createFlag == 1){
                mainFrameController.handleStartExam(exerciseExam);
                createFlag = 0;
            }
            else if(createFlag == 2){
                this.initializeTopicList();
                createFlag = 0;
            }
        } catch (ServiceException e) {
            logger.error("Could not create exerciseExam: ", e);
            showError(e.getMessage());
        }
    }

    @FXML public void handleCreateAndStart(){
        createFlag = 1;
        handleCreate();
    }

    @FXML public void handleCreateAndContinue(){
        createFlag = 2;
        handleCreate();
    }
}
