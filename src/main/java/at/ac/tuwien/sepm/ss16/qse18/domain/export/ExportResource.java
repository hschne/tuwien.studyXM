package at.ac.tuwien.sepm.ss16.qse18.domain.export;

import at.ac.tuwien.sepm.ss16.qse18.domain.Note;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.Topic;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Felix on 14.06.2016.
 */
public class ExportResource implements Serializable {
    private Resource resource;
    private List<Note> notes;

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public ExportResource(Resource resource, List<Note> notes) {

        this.resource = resource;
        this.notes = notes;
    }
}
