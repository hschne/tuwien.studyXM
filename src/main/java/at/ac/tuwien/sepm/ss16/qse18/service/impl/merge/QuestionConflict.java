package at.ac.tuwien.sepm.ss16.qse18.service.impl.merge;

import at.ac.tuwien.sepm.ss16.qse18.domain.Question;
import at.ac.tuwien.sepm.ss16.qse18.domain.export.ExportQuestion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Provides access to the questions causing a conflict.
 *
 * @author Hans-Joerg Schroedl
 */

public class QuestionConflict {

    private static final Logger logger = LogManager.getLogger();

    private ConflictResolution resolution;

    private Question existingQuestion;

    private ExportQuestion importedQuestion;

    /**
     * Default constructor sets resolution to unresolved
     */
    public QuestionConflict() {
        resolution = ConflictResolution.UNRESOLVED;
    }

    /**
     * @return The imported question
     */
    public ExportQuestion getImportedQuestion() {
        return importedQuestion;
    }

    /**
     * @return The existing question
     */
    public Question getExistingQuestion() {
        return existingQuestion;
    }

    /**
     *
     * @return The resolution of this conflict
     */
    public ConflictResolution getResolution() {
        return resolution;
    }

    /**
     *
     * @param resolution The desired resolution of this conflict
     */
    public void setResolution(ConflictResolution resolution) {
        logger.debug("Setting resolution to {}", resolution);
        this.resolution = resolution;
    }

    void setQuestions(Question existingQuestion, ExportQuestion importedQuestion) {
        this.existingQuestion = existingQuestion;
        this.importedQuestion = importedQuestion;
    }

}
