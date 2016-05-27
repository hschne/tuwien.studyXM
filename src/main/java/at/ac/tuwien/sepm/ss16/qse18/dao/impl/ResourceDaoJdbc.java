package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ResourceDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.ResourceType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hans-Joerg Schroedl
 */
@Component
public class ResourceDaoJdbc implements ResourceDao {

    private final Logger logger = LogManager.getLogger();

    private ConnectionH2 database;

    @Autowired public ResourceDaoJdbc(ConnectionH2 database){
        this.database = database;
    }

    @Override public Resource getResource(int id) throws DaoException {
        String query = "SELECT * FROM ENTITY_RESOURCE WHERE RESOURCEID = ?";
        try {
            PreparedStatement statement = database.getConnection().prepareStatement(query);
            statement.setInt(1,id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                return readResourceFrom(resultSet);
            }
            throw new DaoException("Resource with id  "+id+" could not be found");
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException("Unknown SQL error", e);
        }
    }

    @Override public List<Resource> getResources() throws DaoException {
        String query = "SELECT * FROM ENTITY_RESOURCE";
        try {
            PreparedStatement statement = database.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            List<Resource> resources = new ArrayList<>();
            while (resultSet.next()){
                resources.add(readResourceFrom(resultSet));
            }
            return resources;
        } catch (SQLException e) {
            logger.error(e);
            throw new DaoException("Unknown SQL error", e);
        }
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

    private Resource readResourceFrom(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("resourceId");
        int type = resultSet.getInt("type");
        String reference = resultSet.getString("reference");
        return new Resource(id, ResourceType.valueOf(type), reference);
    }
}
