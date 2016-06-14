package at.ac.tuwien.sepm.ss16.qse18.service.merge;

import at.ac.tuwien.sepm.ss16.qse18.dao.QuestionTopicDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;
import at.ac.tuwien.sepm.ss16.qse18.service.ServiceException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Service public class TopicConflict {

    private final Logger logger = LogManager.getLogger();
    List<QuestionConflict> questionConflicts;
    private QuestionConflictDetection questionConflictDetection;

    private Topic existingTopic;

    private Topic importedTopic;

    private QuestionTopicDao questionTopicDao;
    private Duplicate<Topic> duplicate;

    public TopicConflict(Topic existingTopic, Topic importedTopic) {
        this.existingTopic = existingTopic;
        this.importedTopic = importedTopic;
    }

    @Autowired
    public void setQuestionConflictDetection(QuestionConflictDetection questionConflictDetection) {
        this.questionConflictDetection = questionConflictDetection;
    }

    public void initialize(Topic existingTopic, Topic importedTopic) {
        this.existingTopic = existingTopic;
        this.importedTopic = importedTopic;
    }

    public void getConflictingQuestions() throws ServiceException {
        List<Question> importedQuestions = new ArrayList<>();
        questionConflictDetection.initialize(existingTopic, importedQuestions);
        questionConflicts.addAll(questionConflictDetection.getConflictingQuestions());
    }

    public void resolve() {

    }

}
