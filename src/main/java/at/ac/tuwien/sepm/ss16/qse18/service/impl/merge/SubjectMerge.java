package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportQuestion;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.ImportService;
import at.ac.tuwien.sepm.ss16.qse18.service.QuestionService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Component public class SubjectMerge {

    @Autowired ImportService importService;
    @Autowired QuestionService questionService;
    private Logger logger = LogManager.getLogger();

    public void merge(SubjectConflict subjectConflict) throws ServiceException {
        if (!subjectConflict.isResolved()) {
            throw new ServiceException(
                "The conflict has not been resolved. Please make sure that all conflicting questions have a resolution.");
        }
        List<ExportTopic> importedWithoutConflict = subjectConflict.getNonConflictingImported();
        for (ExportTopic exportTopic : importedWithoutConflict) {
            importService.importTopic(exportTopic.getTopic());
        }
        List<TopicConflict> conflictingTopics = subjectConflict.getConflictingTopics();
        for (TopicConflict topicConflict : conflictingTopics) {
            merge(topicConflict);
        }
    }

    private void merge(TopicConflict topicConflict) throws ServiceException {
        List<ExportQuestion> importedQuestionWithoutConflict =
            topicConflict.getNonConflictingImported();
        for (ExportQuestion exportQuestion : importedQuestionWithoutConflict) {
            importService.importQuestion(exportQuestion, topicConflict.getExistingTopic());
        }
        Topic existingTopic = topicConflict.getExistingTopic();
        List<QuestionConflict> questionConflicts = topicConflict.getQuestionConflicts();
        for (QuestionConflict questionConflict : questionConflicts) {
            merge(questionConflict, existingTopic);
        }
    }

    private void merge(QuestionConflict questionConflict, Topic existingTopic)
        throws ServiceException {
        ConflictResolution resolution = questionConflict.getResolution();
        if (resolution == null) {
            String message = "No conflict resolution selected. Conflict has not been resolved";
            logger.error(message);
            throw new ServiceException(message);
        }
        if (resolution == ConflictResolution.EXISTING
            || resolution == ConflictResolution.DUPLICATE) {
            logger.debug("Keeping existing question");
            return;
        }
        if (resolution == ConflictResolution.IMPORTED) {
            questionService.deleteQuestion(questionConflict.getExistingQuestion());
            ExportQuestion exportQuestion = questionConflict.getImportedQuestion();
            Question importedQuestion = exportQuestion.getQuestion();
            Question createdQuestion =
                questionService.createQuestion(importedQuestion, existingTopic);
            questionService.setCorrespondingAnswers(createdQuestion, exportQuestion.getAnswers());
        }
    }

}
