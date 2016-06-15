package at.ac.tuwien.sepm.ss16.qse18.gui.observable;

import at.ac.tuwien.sepm.ss16.qse18.service.merge.QuestionConflict;
import at.ac.tuwien.sepm.ss16.qse18.service.merge.TopicConflict;

import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
public class ObservableTopicConflict {

    private TopicConflict topicConflict;

    public ObservableTopicConflict(TopicConflict topicConflict){

        this.topicConflict = topicConflict;
    }

    public List<QuestionConflict> getTopicConflicts() {
        return topicConflict.getQuestionConflicts();
    }
}
