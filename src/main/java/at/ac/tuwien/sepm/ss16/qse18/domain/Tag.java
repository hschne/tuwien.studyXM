package at.ac.tuwien.sepm.ss16.qse18.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * ENUM Tag
 * Specifies the importance of a question:
 * - EASY: The question is an easy-to-answer question
 * - NORMAL: The question is neither easy nor hard to answer
 * - HARD: The question is hard to answer
 * - IMPORTANT: The question will come to the exam for sure
 *
 * @author Bicer Cem
 */
public enum Tag {
    EASY(1), NORMAL(2), HARD(3), IMPORTANT(4);

    private int value;
    private static Map<Integer, Tag> hm = new HashMap<>();

    Tag(int type) {
        this.value = type;
    }

    static {
        for(Tag type : Tag.values()) {
            hm.put(type.value,type);
        }
    }

    public static Tag valueOf(int type) {
        return hm.get(type);
    }

    public int getValue() {
        return this.value;
    }

    @Override public String toString() {
        switch (value) {
            case 1:
                return "easy";
            case 2:
                return "normal";
            case 3:
                return "hard";
            case 4:
                return "important";
            default:
                return "null";
        }
    }
}
