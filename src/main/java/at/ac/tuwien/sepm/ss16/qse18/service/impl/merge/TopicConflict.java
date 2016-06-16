package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportQuestion;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportTopic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Component @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE) public class TopicConflict {

    private final Logger logger = LogManager.getLogger();
    private List<QuestionConflict> questionConflicts;
    private QuestionConflictDetection questionConflictDetection;
    private Topic existingTopic;
    private ExportTopic importedTopic;


    public void setTopics(Topic existingTopic, ExportTopic importedTopic) {
        this.existingTopic = existingTopic;
        this.importedTopic = importedTopic;
        questionConflicts = new ArrayList<>();
    }

    public List<QuestionConflict> getQuestionConflicts() {
        return questionConflicts;
    }

    public void setQuestionConflicts(List<QuestionConflict> questionConflicts) {
        this.questionConflicts = questionConflicts;
    }

    @Autowired
    public void setQuestionConflictDetection(QuestionConflictDetection questionConflictDetection) {
        this.questionConflictDetection = questionConflictDetection;
    }


    public List<QuestionConflict> initializeQuestionConflicts() throws ServiceException {
        questionConflictDetection.setTopics(existingTopic, importedTopic);
        setQuestionConflicts(questionConflictDetection.getConflictingQuestions());
        return questionConflicts;
    }

    public List<ExportQuestion> getNonConflictingImported() {
        List<ExportQuestion> imported = new ArrayList<>();
        for (QuestionConflict questionConflict : questionConflicts) {
            imported.remove(questionConflict.getImportedQuestion());
        }
        return imported;
    }

    public void resolve() throws ServiceException {
        for (QuestionConflict questionConflict : questionConflicts) {
            questionConflict.resolve();
        }
    }

    public ExportTopic getImportedTopic() {
        return importedTopic;
    }
}
