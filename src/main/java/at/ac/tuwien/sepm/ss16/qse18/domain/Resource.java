package at.ac.tuwien.sepm.ss16.qse18.domain;

/**
 * Class Resource
 * represents the entity resource
 * @parameter resoureceId primary key of resourece
 * @parameter type indicated the type of resource
 * @parameter reference content of resource(the actual resource)
 *
 * @author Felix Almer on 10.05.2016.
 */
public class Resource {
    private int resourceId;
    private ResourceType type;
    private String reference;

    public Resource(int resourceId, ResourceType type, String reference) {
        this.resourceId = resourceId;
        this.type = type;
        this.reference = reference;
    }

    public Resource() {
        this.resourceId = -1;
        this.type = null;
        this.reference = "";
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
            ", reference='" + reference + '\'' +
            '}';
    }
}
