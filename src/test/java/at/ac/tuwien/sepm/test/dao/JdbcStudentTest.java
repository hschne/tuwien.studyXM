package at.ac.tuwien.sepm.test.dao;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import at.ac.tuwien.sepm.dao.IStudentDAO;
import at.ac.tuwien.sepm.dao.IStudentDAO.SortOrder;
import at.ac.tuwien.sepm.domain.Student;
import at.ac.tuwien.sepm.helper.Constants;

/**
 * Class containing the testcases for IStudentDAO. 
 * 
 * The test assumes that there is specific test-data in the database and reads additional test-data from test-beans.xml.
 * 
 * The hsqldb database with the test-data can be found in 
 * 
 *  /ss16_sepm_qse_18/src/test/resources/data
 * 
 * This database is used for the tests (as it is defined in the src/test/resources/test-beans.xml as the datasource). 
 * 
 * when "mvn compile" is executed, this database files are copied to the target folder which is:
 * 
 *  /ss16_sepm_qse_18/target/test-classes/data
 *  
 * Consequently, if you should "destroy" the test data in the target directory,
 * just execute a "mvn clean compile" and a fresh database is again copied from the src/test/resources/data directory.
 *  
 *  
 * One addtional thing should be noted: there are two beans.xml files and two database directories:
 * 
 * One set in the src/main directory the other set in the src/test directory.
 *  
 * This allows us to distinguish between running tests and running the application. For example: when running the application
 * changing data cannot destroy the data that we need for the tests.
 * 
 * The "operative" beans.xml does not contain any test-data.
 * 
 * 
 * @author The SE-Team
 * @version 2.1
 * 
 */
public class JdbcStudentTest {
	
	/**
	 * Data Access Object for Student, fetched with xbf.
	 */
	private IStudentDAO studentDAO = null;

	/**
	 * Spring Framework XML Bean Factory.
	 */
	private static XmlBeanFactory xbf;
	
	private static Logger log = Logger.getLogger(JdbcStudentTest.class);

	
	/**
	 * This method is executed before all testcases.
	 * 
	 */
	@BeforeClass
	public static void setUpBefore() throws Exception {
		// notice, that the TEST beans.xml is loaded!
		ClassPathResource res = new ClassPathResource(Constants.SPRINGBEANS_TEST);
		xbf = new XmlBeanFactory(res);
		/* since the datasource configured in test-beans.xml is pointing to a file 
		   with predefined test-data, we don't need to insert any test-data here. */
	}
	
	/**
	 * This method is executed before every testcase.
	 * 
	 */
	@Before
	public void setUp() throws Exception {
		studentDAO = (IStudentDAO) xbf.getBean("StudentDAO");
	}

	/**
	 * This method is executed after each testcase.
	 * 
	 */
	@After
	public void tearDown() throws Exception {
		studentDAO = null;
		//xbf.destroySingletons();
	}

	/**
	 * Test StudentBean initialisation, make sure Spring Framework works and the 
	 * XML Bean Factory is returning test-data for our save & updateStudent tests.
	 * 
	 */
	@Test
	public void beanInit_shouldLoadStudentFromSpringBeanConfig() {
		Student student = (Student) xbf.getBean("StudentAlexanderSchatten");
		assertThat(student, is(notNullValue()));
		assertThat(student.getId(), is(1));
		assertThat(student.getFirstname(), is("Alexander"));
		assertThat(student.getLastname(), is("Schatten"));
		assertThat(student.getEmail(), is("alexander@schatten.info"));
	}

	/**
	 * Test the method getStudent.
	 * 
	 * Test-data already exists in the database and the dataset with id=0 is read.
	 * 
	 * 
	 */
	@Test
	public void getStudent_shouldRetrieveStudentFromDB() {
		Student student = studentDAO.getStudent(0);
		assertThat(student, is(notNullValue()));
		assertThat(student.getFirstname(), is("Fritz"));
		assertThat(student.getLastname(), is("Haber"));
		assertThat(student.getEmail(), is("fritz@haber.test"));
	}

	/**
	 * Test the methods saveStudent and deleteStudent - actually this is 
	 * a complete CRD (Create, Read, Delete) lifecycle.
	 * 
	 * 
	 */
	@Test
	public void saveStudent_shouldAddStudentToDB() {
		// get a test-dataset from Spring config
		Student student = (Student) xbf.getBean("StudentAddUpdateDelete");
		Integer oldId = student.getId();
		
		// Add Student
		studentDAO.saveStudent(student);
		// new ID should be different from old ID
		assertThat(student.getId(), is(not(oldId)));

		// check if student was added
		// read student from database using the new generated ID
		// then compare if the newly read data equals the old one,
		// then check is complete
		Student checkStudent = studentDAO.getStudent(student.getId());
		assertThat(checkStudent.getFirstname(), is("Test"));
		assertThat(checkStudent.getLastname(), is("Student"));
		assertThat(checkStudent.getMatnr(), is("0926759"));
	
		//	 Delete Student
		assertThat(studentDAO.deleteStudent(student.getId()), is(notNullValue()));
		
		// check if student was deleted
		// try to retrieve student by ID again
		checkStudent = studentDAO.getStudent(student.getId());
		// no dataset must be found (as it was deleted above)
		assertThat(checkStudent, is(nullValue()));
	}
	
	/**
	 * Specifically test the update method 
	 *
	 */
	@Test
	public void updateStudent_shouldUpdateStudentInDB() {
		// First add one student to database
		Student student = (Student) xbf.getBean("StudentAddUpdateDelete");
		studentDAO.saveStudent(student);
		
		//change the student data and update this student in database
		student.setFirstname(student.getFirstname() + "2");
		student.setLastname(student.getLastname() + "2");
		student.setEmail(student.getEmail() + "2");
		assertThat(studentDAO.updateStudent(student), is(notNullValue()));

		// retrieve student again from database and check if all fields are updated correctly
		Student newStudent = studentDAO.getStudent(student.getId());
		assertThat(newStudent.getLastname(), is(student.getLastname()));
		assertThat(newStudent.getFirstname(), is(student.getFirstname()));
		assertThat(newStudent.getEmail(),is(student.getEmail()));
		
		// delete student again
		studentDAO.deleteStudent(student.getId());
	}

	/**
	 * Test the method getStudents, ordered by Matrikelnummer.
	 * 
	 */
	@Test
	public void getStudents_shouldRetrieveAllStudentsOrderedByMatnrFromDB() {
		// Order by Matrikelnummer
		List<Student> students = studentDAO.getStudents(SortOrder.StudentId);
		assertThat(students, is(notNullValue()));
		Student pstud = null;
		// See if the Matrikelnumbers in the list are actually in order
		for (Student stud : students) {
			if (pstud != null) {
				// ID from DB aswell as Matrikelnumber must be unique
				assertThat(pstud.getId(), is(not(stud.getId())));
				assertThat(pstud.getMatnr(), is(not(stud.getMatnr())));
				log.info("Comparing student " + stud.getMatnr() + " to student " + pstud.getMatnr());
				assertThat(stud.getMatnr().compareTo(pstud.getMatnr()), is(greaterThan(0)));
			}
			pstud = stud;
		}
	}

	/**
	 * Test the method getStudents, ordered by Nachname.
	 * 
	 * 
	 */
	@Test
	public void getStudents_shouldRetrieveAllStudentsOrderedByNachnameFromDB() {
		// Order by Nachname
		List<Student> students = studentDAO.getStudents(SortOrder.LastName);
		assertThat(students, is(notNullValue()));
		Student pstud = null;
		// See if the Last Names (Nachname) in the list are actually in order
		for (Student stud : students) {
			if (pstud != null) {
				// ID from DB aswell as Matrikelnumber must be unique
				assertThat(pstud.getId(), is(not(stud.getId())));
				assertThat(pstud.getMatnr(), is(not(stud.getMatnr())));
				assertThat(stud.getLastname().compareTo(pstud.getLastname()), is(greaterThanOrEqualTo(0)));
			}
			pstud = stud;
		}
	}

}
