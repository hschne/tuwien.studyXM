package at.ac.tuwien.sepm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import at.ac.tuwien.sepm.domain.Student;

/**
 * Implementation of the Student Data Access Object for JDBC. This class makes
 * heavy use of the Spring Framework's facilities and provides access to the
 * data stored in the database. It also defines how this data is mapped from
 * the application objects to the database tables and vice-versa.
 * Also see the Bean-Config file 
 * ({@value at.ac.tuwien.sepm.helper.Constants#SPRINGBEANS})
 * for configuration.
 * 
 * @author The SE-Team
 * @version 2.1
 * @see IStudentDAO
 */
public class JdbcStudentDAO implements IStudentDAO {

	/**
	 * Retrieves the logger for this class.
	 */
	private static Log log = LogFactory.getLog(JdbcStudentDAO.class);

	/**
	 * Provides access to the Datasource, set by setDataSource().
	 * Wrapper class for JdbcTemplate providing Java-5-based convenience and exposing
	 * only the most commonly required operations.
	 */
	protected SimpleJdbcTemplate simpleJdbcTemplate = null;

	/**
	 * Transaction Manager. For encapsulating insert and updates in transaction.
	 */
	private PlatformTransactionManager transactionManager = null;

	/**
	 * SQL Query Strings.
	 */
	private String sql_selectAllStudents = "";
	private String sql_selectStudent = "";
	private String sql_insertStudent = "";
	private String sql_getInsertId = "";
	private String sql_updateStudent = "";
	private String sql_deleteStudent = "";

	/** ******************************************************************* */
	/** ******************************************************************* */
	/* C O N S T R U C T O R */
	/** ******************************************************************* */
	/** ******************************************************************* */

	public JdbcStudentDAO() {
		super();
	}

	/**
	 * The Initialise Method must be called after all bean values are set,
	 * particularly the datasource and the transaction manager. This is actually
	 * performed by the Spring Framework, which sets first of all, all Java Bean
	 * Properties and eventually calls this init method (see bean definition in
	 * beans.xml configuration file)
	 */
	public void init() {
		log.info("Initialise StudentDAO");
	}

	/**
	 * The Destroy Method is called by the Spring Framework to end the lifecycle
	 * of this bean, but <b>only</b> when the bean is created as singleton.
	 * Check the bean definition in beans.xml configuration file for details.
	 */
	public void destroy() {
		log.info("Destroy StudentDAO");
	}

	/** ******************************************************************* */
	/** ******************************************************************* */
	/*
	 * BEAN SETTERS FOR DEPENDENCY INJECTION
	 * 
	 * Dependency Injection is a design pattern in which the responsibility for
	 * object creation and object linking is removed from the objects themselves
	 * and transferred to a factory object. It is a way to achieve loose
	 * coupling between objects and results in highly testable objects
	 * (controlled unit tests).
	 * 
	 * Factory Object: (Design Pattern) is an object for creating other objects.
	 * Typically a factory has a method for every kind of object it is capable
	 * of creating. these methods optionally accept parameters defining how the
	 * object is created and then return the created object.
	 */
	/** ******************************************************************* */
	/** ******************************************************************* */

	/**
	 * Sets the SQL String to get all students.
	 * 
	 * @param sql_selectAllStudents
	 *            SQL Statement as String
	 */
	public void setSql_selectAllStudents(String sql_selectAllStudents) {
		this.sql_selectAllStudents = sql_selectAllStudents;
	}

	/**
	 * Sets the SQL String to get one student with one SQL parameter.
	 * 
	 * @param sql_selectStudent
	 *            SQL Statement as String
	 */
	public void setSql_selectStudent(String sql_selectStudent) {
		this.sql_selectStudent = sql_selectStudent;
	}

	/**
	 * Sets the SQL String to insert one student into the database.
	 * 
	 * @param sql_insertStudent
	 *            SQL Statement as String
	 */
	public void setSql_insertStudent(String sql_insertStudent) {
		this.sql_insertStudent = sql_insertStudent;
	}

	/**
	 * Sets the SQL String to retrieve the ID of the last executed SQL
	 * Statement.
	 * 
	 * @param sql_getInsertId
	 *            SQL Statement as String
	 */
	public void setSql_getInsertId(String sql_getInsertId) {
		this.sql_getInsertId = sql_getInsertId;
	}

	/**
	 * Sets the SQL String to update a student.
	 * 
	 * @param sql_updateStudent
	 *            SQL Statement as String
	 */
	public void setSql_updateStudent(String sql_updateStudent) {
		this.sql_updateStudent = sql_updateStudent;
	}

	/**
	 * Sets the SQL String to delete a student.
	 * 
	 * @param sql_deleteStudent
	 *            SQL Statement as String
	 */
	public void setSql_deleteStudent(String sql_deleteStudent) {
		this.sql_deleteStudent = sql_deleteStudent;
	}

	/**
	 * Sets the Datasource to connect to database.
	 * 
	 * @param dataSource SQL Datasource
	 */
	public void setDataSource(DataSource dataSource) {
		this.simpleJdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}

	/**
	 * Sets the transaction manager.
	 * 
	 * @param transactionManager
	 *            central interface in Spring's transaction infrastructure
	 * @see #transactionManager
	 */
	public void setTransactionManager(
			PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	/**
	 * Maps data from a database row to an application object.
	 * This method is used by all DAO methods which query the database 
	 * with an SQL SELECT statement.
	 *
	 * @since 2.1
	 *
	 */
	public static final class StudentMapper implements RowMapper<Student> {
	      public Student mapRow(ResultSet rs, int rowNumber)
			throws SQLException {
	    	    Student student = new Student();
				student.setId(rs.getInt("id"));
				student.setMatnr(rs.getString("matnr"));
				student.setFirstname(rs.getString("firstname"));
				student.setLastname(rs.getString("lastname"));
				student.setEmail(rs.getString("email"));
				return student;
			}
	  } 
	
	/** ******************************************************************* */
	/** ******************************************************************* */
	/*
	 * DAO METHODS
	 * 
	 * A Data Access Object (DAO) is a component which provides a common
	 * interface between the application and one or more data storage devices,
	 * such as a database or a file. The advantage of using data access objects
	 * is that any business object (which contains application or operation
	 * specific details) does not require direct knowledge of the final
	 * destination for the information it manipulates. As a result, _if_ it is
	 * necessary to change where or how the data is stored, these modifications
	 * can be made without needing to change the main application.
	 */
	/** ******************************************************************* */
	/** ******************************************************************* */

	public Student getStudent(int id) {
		log.info("Get Student ID = " + id);
		
		List<Student> students = simpleJdbcTemplate.query(sql_selectStudent, new StudentMapper(), id);
		//List<Student> students = query_getStudent.execute(id);
		if (students.size() == 1) {
			Student s = students.get(0);
			log.debug("Returning Student \"" + s.getFirstname() + " "
					+ s.getLastname() + "\"");
			return s;
		} else {
			log.debug("No Student data");
			return null;
		}
	}

	/**
	 * Inserts one Student into the database. This method uses a transaction
	 * manager for performing two queries in one transaction:
	 * <ol>
	 * <li> the insert statement is performed adding the dataset to the database
	 * (first query)</li>
	 * <li> the database then automatically generates a unique ID for this
	 * dataset, </li>
	 * <li> the second query asks the database <i>which</i> id was used for
	 * this particular dataset</li>
	 * <li> this ID then is set in the student bean and returned by the function</li>
	 * </ol>
	 * 
	 * @param student
	 *            Student object holding data of one student
	 * @return unique id generated by database assigned to the newly created
	 *         Student
	 */
	public Student saveStudent(final Student student) {
		log.info("Add Student Name = " + student.getFirstname() + " "
				+ student.getLastname());
		log.debug("Initialise Transaction Manager");
		TransactionTemplate tt = new TransactionTemplate(transactionManager);
		Object result = tt.execute(new TransactionCallback<Object>() {
			public Object doInTransaction(TransactionStatus status) {
				// The transaction is run from here
				log.debug("Start Transaction");
				//query_insertStudent.update(param);
				//KeyHolder keyHolder = new GeneratedKeyHolder();
				simpleJdbcTemplate.update(sql_insertStudent, student.getMatnr(),
						student.getFirstname(), student.getLastname(), student.getEmail());
				
				/*
				 * activate the following error line to create an Error which
				 * terminates this method. One will see, that the complete
				 * transaction is rolled back, hence the insert statement above
				 * is not executed, alternatively the second rollback statement
				 * can be activated with the same result which in that case is a
				 * manual rollback of the transaction
				 */

				// if (true) throw new Error("Test Exception");
				// or
				// status.setRollbackOnly();
				/*
				 * result from query is a list, actually containing only one row
				 * and one column
				 */
				Integer id = simpleJdbcTemplate.queryForInt(sql_getInsertId);
				log.debug("End Transaction");
				return id;
				/*
				 * and the transaction ends here! if no error occurs the
				 * transaction is committed by Spring otherwise it is rolled
				 * back
				 */
			}
		});
		Integer id = (Integer) result;
		student.setId(id);
		log.info("Return ID from inserted dataset = " + id);
		return (id!=0) ? student : null;
	}
	
	public Student updateStudent(Student student) {
		return updateStudent(student, student.getId());
	}

	public Student updateStudent(Student student, long id) {
		log.info("Update Student, ID = " + student.getId() + " new ID = "
				+ id);
		log.debug("Execute Update");
		if (simpleJdbcTemplate.update(sql_updateStudent, student.getMatnr(),
				student.getFirstname(), student.getLastname(),
				student.getEmail(), id) == 1) {
			log.debug("Update Successfull");
			return student;
		}
		log.error("Update for Student ID = " + id + " failed.");
		return null;
	}

	/**
	 * Retrieves all students from the database. <br>
	 * <b>Warning:</b> this type of DAO method would not be used in a real-
	 * world application because there may be thousands of students in the
	 * database and this method would retrieve them all. <br>
	 * This is usually not needed: it will generate a huge load on the database
	 * and also require enormous amounts of memory. Morover, there is hardly an
	 * application conceivable that needs more than a few dozen datasets at any
	 * one time.
	 * 
	 * @since 1.0-SNAPSHOT
	 */
	public List<Student> getStudents(SortOrder order) {
		log.info("Get all Students order = " + order.toString());
		List<Student> students = null;
		if (order.equals(SortOrder.StudentId)) {
			students = simpleJdbcTemplate.query(sql_selectAllStudents+" order by matnr", new StudentMapper());
			log.debug("Student List contains " + students.size() + " students ordered by studentId");
		} else if (order.equals(SortOrder.LastName)) {
			students = simpleJdbcTemplate.query(sql_selectAllStudents+" order by lastname", new StudentMapper());
			log.debug("Student List contains " + students.size() + " students ordered by lastname");
		}
		return students;
	}

	public boolean deleteStudent(int id) {
		log.info("Delete Student ID = " + id);
		log.debug("Initialize SQL Parameters");
		final Object[] param = new Object[] { id };
		log.debug("Executing SQL");
		if (simpleJdbcTemplate.update(sql_deleteStudent, param) == 1) {
			log.debug("Deleting successfull");
			return true;
		}
		log.error("Deleting Student ID = " + id + " failed");
		return false;
	}

	public Student getStudentByMatrNr(String arg0) {
		// TODO Auto-generated method stub
		// TODO Implement this method
		return null;
	}

}
