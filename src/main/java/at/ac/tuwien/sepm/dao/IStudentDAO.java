package at.ac.tuwien.sepm.dao;

import java.util.List;

import at.ac.tuwien.sepm.domain.Student;

/**
 * The DAO abstracts and encapsulates all access to the data source
 * corresponding to the Student business object
 * 
 * @author The SE-Team
 * @version 1.0-SNAPSHOT
 */
public interface IStudentDAO {
	/**
	 * Enumeration for different sort orders of students that can be used.
	 * Possible sort ordes are:
	 * <ul>
	 * <li><code>StudentId</code> for an ascending order by studentId.</li>
	 * <li><code>LastName</code> for an ascending order by lastname.</li>
	 * </ul>
	 * 
	 * @author The SE-Team
	 * @since 1.0-SNAPSHOT
	 * 
	 */
	public enum SortOrder {
		/**
		 * Use this sort order to order ascending by studentId.
		 */
		StudentId,

		/**
		 * Use this sort order to order ascending by lastname.
		 */
		LastName,
	}

	/**
	 * Retrieves one single Student from the DB.
	 * 
	 * @param id
	 *            unique database ID of Student
	 * @return Student object holding the data of one student or null if no
	 *         student with the matching id is found
	 */
	public Student getStudent(int id);

	/**
	 * Retrieves one single Student by a given matrikel number
	 * 
	 * @param matnr
	 * @return Student object if someone existed, otherwise null if no student
	 *         with the given matnr exist
	 */
	public Student getStudentByMatrNr(String matnr);

	/**
	 * Saves one single Student to the Database.
	 * 
	 * @param student
	 *            object holding the data of one student
	 * @return Student object with the generated ID, or null if an error occurs
	 */
	public Student saveStudent(Student student);

	/**
	 * Updates an existing student in the database.
	 * 
	 * @param student
	 *            object holding the data of one student
	 * @return Student object
	 */
	public Student updateStudent(Student student);

	/**
	 * Deletes an existing student from the database.
	 * 
	 * @param id
	 *            of the student to be deleted
	 * @return true if procedure was successfully, otherwise false
	 */
	public boolean deleteStudent(int id);

	/**
	 * Retrieves all students from the database.
	 * 
	 * @param order
	 *            that the result set should be ordered by
	 * @return List of type Student holding all students available
	 * 
	 * @since 1.0-SNAPSHOT
	 */
	public List<Student> getStudents(SortOrder order);

}
