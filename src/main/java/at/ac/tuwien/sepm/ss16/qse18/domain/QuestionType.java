package at.ac.tuwien.sepm.ss16.qse18.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * ENUM QuestionType
 * Specifies the type of question:
 * - Multiplechoice: 0 to n answers can be correct
 * - Singlechoice:   Variety of multiple choice where exactly one answer is correct
 * - Openquestion:   A text is required as user input instead of a radiobox choice, a correct answer
 *                   is determined by matching the given answer to the user input
 * - Notecard:       Learning by notecard principle: mainly used for offline learning -> printouts
 *
 * - Self_evaluation: This type has a text as a question and an image as an answer. The user has to
 *                    evaluate on his own if the answer is correct.
 *
 * @author Felix Almer on 06.05.2016.
 */
public enum QuestionType {
    MULTIPLECHOICE(1), SINGLECHOICE(2), OPENQUESTION(3), NOTECARD(4),SELF_EVALUATION(5);

    private int value;
    private static Map<Integer, QuestionType> hm = new HashMap<>();

    QuestionType(int type) {
        this.value = type;
    }

    static {
        for(QuestionType type : QuestionType.values()) {
            hm.put(type.value,type);
        }
    }

    public static QuestionType valueOf(int type) {
        return hm.get(type);
    }

    public int getValue() {
        return this.value;
    }
}
