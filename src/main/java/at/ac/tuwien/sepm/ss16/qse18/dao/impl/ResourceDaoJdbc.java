package at.ac.tuwien.sepm.ss16.qse18.dao.impl;

import at.ac.tuwien.sepm.ss16.qse18.dao.ConnectionH2;
import at.ac.tuwien.sepm.ss16.qse18.dao.DaoException;
import at.ac.tuwien.sepm.ss16.qse18.dao.ResourceDao;
import at.ac.tuwien.sepm.ss16.qse18.domain.Resource;
import at.ac.tuwien.sepm.ss16.qse18.domain.ResourceType;
import at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static at.ac.tuwien.sepm.ss16.qse18.domain.validation.DtoValidator.validate;

/**
 * @author Hans-Joerg Schroedl
 */
@Component public class ResourceDaoJdbc implements ResourceDao {

    private final Logger logger = LogManager.getLogger();

    private ConnectionH2 database;

    @Autowired public ResourceDaoJdbc(ConnectionH2 database) {
        this.database = database;
    }


    @Override public Resource getResource(int id) throws DaoException {
        String query = "SELECT * FROM ENTITY_RESOURCE WHERE RESOURCEID = ?";
        try {
            PreparedStatement statement = database.getConnection().prepareStatement(query);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return readResourceFrom(resultSet);
            }
            throw new DaoException("Resource with id  " + id + " could not be found");
        } catch (SQLException e) {
            return handleSqlError(e);
        }
    }

    @Override public List<Resource> getResources() throws DaoException {
        String query = "SELECT * FROM ENTITY_RESOURCE";
        try {
            PreparedStatement statement = database.getConnection().prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            List<Resource> resources = new ArrayList<>();
            while (resultSet.next()) {
                resources.add(readResourceFrom(resultSet));
            }
            return resources;
        } catch (SQLException e) {
            handleSqlError(e);
        }
        throw new DaoException("Getting resources was not successful");
    }

    @Override public Resource createResource(Resource resource) throws DaoException {
        String query = "INSERT INTO ENTITY_RESOURCE (TYPE, REFERENCE) VALUES (?,?)";
        tryValidateResource(resource);
        try {
            PreparedStatement statement =
                database.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, resource.getType().getValue());
            statement.setString(2, resource.getReference());
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                resource.setResourceId(generatedId);
                return resource;
            }
            throw new DaoException("Resource could not be created");
        } catch (SQLException e) {
            handleSqlError(e);
        }
        throw new DaoException("Creating resources was not successful");
    }

    @Override public Resource deleteResource(Resource resource) throws DaoException {
        return null;
    }

    @Override public Resource updateResource(Resource resource) throws DaoException {
        return null;
    }

    private void tryValidateResource(Resource resource) throws DaoException {
        try {
            validate(resource);
        } catch (DtoValidationException e) {
            logger.error(e);
            throw new DaoException(e);
        }
    }

    private Resource handleSqlError(SQLException e) throws DaoException {
        logger.error(e);
        throw new DaoException("Unknown SQL error", e);
    }

    private Resource readResourceFrom(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("resourceId");
        int type = resultSet.getInt("type");
        String reference = resultSet.getString("reference");
        return new Resource(id, ResourceType.valueOf(type), reference);
    }
}
