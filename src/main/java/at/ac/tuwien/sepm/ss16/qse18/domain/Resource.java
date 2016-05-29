package at.ac.tuwien.sepm.ss16.qse18.domain;

/**
 * Class Resource
 * represents the entity resource
 *
 * @author Felix Almer on 10.05.2016.
 */
public class Resource {
    private int resourceId;
    private ResourceType type;
    private String name;
    private String reference;

    public Resource(int resourceId, ResourceType type, String name, String reference) {
        this.resourceId = resourceId;
        this.type = type;
        this.name = name;
        this.reference = reference;
    }
    public Resource() {
        this.resourceId = -1;
        this.type = null;
        this.name = "";
        this.reference = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override public String toString() {
        return "Resource{" +
            "resourceId=" + resourceId +
            ", type=" + type +
            ", name='" + name + '\'' +
            ", reference='" + reference + '\'' +
            '}';
    }
}
