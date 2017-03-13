package at.ac.tuwien.sepm.ss16.qse18.gui.observable;

import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.ResourceType;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Hans-Joerg Schroedl
 */
public class ObservableResource {
    private IntegerProperty id;
    private StringProperty type;
    private StringProperty reference;
    private StringProperty name;
    private Resource resource;

    public ObservableResource(Resource resource) {
        this.resource = resource;
        id = new SimpleIntegerProperty(resource.getResourceId());
        type = new SimpleStringProperty(resource.getType().toString());
        name = new SimpleStringProperty(resource.getName());
        reference = new SimpleStringProperty(resource.getReference());
    }

    public Resource getResource() {
        return resource;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.resource.setResourceId(id);
        this.id.set(id);
    }

    public String getType() {
        return type.get();
    }

    public ResourceType getResourceType(){
        return resource.getType();
    }

    public void setType(String type) {
        this.resource.setType(ResourceType.valueOf(type));
        this.type.set(type);
    }

    public String getReference() {
        return reference.get();
    }

    public void setReference(String reference) {
        this.resource.setReference(reference);
        this.reference.set(reference);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.resource.setName(name);
        this.name.set(name);
    }
}
