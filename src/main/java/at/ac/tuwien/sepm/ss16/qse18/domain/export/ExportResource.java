package at.ac.tuwien.sepm.ss16.qse18.domain.export;

import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Felix on 14.06.2016.
 */
public class ExportResource implements Serializable {
    private Resource resource;

    public ExportResource(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

}
