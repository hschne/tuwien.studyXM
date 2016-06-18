package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportQuestion;
import at.ac.tuwien.sepm.ss16.qse18.service.ImportService;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Component public class TopicMerge {

    private ImportService importService;
    private QuestionMerge questionMerge;

    @Autowired public void setImportService(ImportService importService) {
        this.importService = importService;
    }

    @Autowired public void setQuestionMerge(QuestionMerge questionMerge) {
        this.questionMerge = questionMerge;
    }

    public void merge(TopicConflict topicConflict) throws ServiceException {
        List<ExportQuestion> importedQuestionWithoutConflict =
            topicConflict.getNonConflictingImported();
        for (ExportQuestion exportQuestion : importedQuestionWithoutConflict) {
            importService.importQuestion(exportQuestion, topicConflict.getExistingTopic());
        }
        Topic existingTopic = topicConflict.getExistingTopic();
        List<QuestionConflict> questionConflicts = topicConflict.getQuestionConflicts();
        for (QuestionConflict questionConflict : questionConflicts) {
            questionMerge.merge(questionConflict, existingTopic);
        }
    }

}
