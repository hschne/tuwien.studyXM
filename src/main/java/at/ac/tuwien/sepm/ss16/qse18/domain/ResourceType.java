package at.ac.tuwien.sepm.ss16.qse18.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * ENUM ResourceType
 * Specifies the type of resource:
 * - PDF:       A filesystem reference to a pdf document
 * - WEBLINK:   A HTML reference
 * - NOTE:      Plain text
 * - OTHER:     Different type of resource, filesystem reference
 * Created by Felix on 10.05.2016.
 */
public enum ResourceType {
    PDF(1), WEBLINK(2), NOTE(3), OTHER(4);

    private int value;
    private static Map<Integer, ResourceType> rm = new HashMap<>();

    private ResourceType(int type) {
        this.value = value;
    }

    static {
        for(ResourceType type : ResourceType.values()) {
            rm.put(type.value, type);
        }
    }

    public static ResourceType valueOf(int type) {
        return rm.get(type);
    }

    public int getValue() {
        return this.value;
    }
}
