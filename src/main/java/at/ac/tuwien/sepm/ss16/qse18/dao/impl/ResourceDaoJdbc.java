package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ResourceDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Component
public class ResourceDaoJdbc implements ResourceDao {


    private ConnectionH2 database;

    @Autowired public ResourceDaoJdbc(ConnectionH2 database){
        this.database = database;
    }

    @Override public Resource getResource(int id) throws DaoException {
        String query = "SELECT * FROM ENTITY_RESOURCE WHERE RESOURCEID = ?";
        return null;
    }

    @Override public List<Resource> getResources() throws DaoException {
        return null;
    }

    @Override public Resource createResource(Resource resource) throws DaoException {
        return null;
    }

    @Override public Resource deleteResource(Resource resource) throws DaoException {
        return null;
    }

    @Override public Resource updateQuestion(Resource resource) throws DaoException {
        return null;
    }
}
