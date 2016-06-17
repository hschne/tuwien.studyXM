package at.ac.tuwien.sepm.ss16.qse18.gui.observable;

import at.ac.tuwien.sepm.ss16.qse18.service.impl.merge.QuestionConflict;

/**
 * @author Hans-Joerg Schroedl
 */
public class ObservableQuestionConflict {

    private QuestionConflict conflict;

    public ObservableQuestionConflict(QuestionConflict conflict){
        this.conflict = conflict;
    }

    public QuestionConflict getConflict(){
        return conflict;
    }


}
