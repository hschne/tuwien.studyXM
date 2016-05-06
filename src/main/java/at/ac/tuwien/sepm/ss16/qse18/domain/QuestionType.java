package at.ac.tuwien.sepm.ss16.qse18.domain;

/**
 * ENUM QuestionType
 * Specifies the type of question:
 * - Multiplechoice: 0 to n answers can be correct
 * - Singlechoice:   Variety of multiple choice where exactly one answer is correct
 * - Openquestion:   A text is required as user input instead of a radiobox choice, a correct answer
 *                   is determined by matching the given answer to the user input
 * - Notecard:       Learning by notecard principle: mainly used for offline learning -> printouts
 * Created by Felix on 06.05.2016.
 */
public enum QuestionType {
    MULTIPLECHOICE(), SINGLECHOICE(), OPENQUESTION(), NOTECARD()
}
