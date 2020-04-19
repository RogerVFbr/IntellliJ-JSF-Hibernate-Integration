import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@SessionScoped
public class StudentController implements Serializable {

	private List<Student> students;
	private StudentDaoHibernate studentDAO;
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public StudentController() throws Exception {
		students = new ArrayList<Student>();
		studentDAO = StudentDaoHibernate.getInstance();
	}
	
	public List<Student> getStudents() {
		return students;
	}

	public void loadStudents() {
		logger.info("Loading students");
		students.clear();

		try {
			students = studentDAO.getStudents();
			
		} catch (Exception exc) {
			logger.log(Level.SEVERE, "Error loading students", exc);
			addErrorMessage(exc);
		}
	}

	public String addStudent(Student student) {
		logger.info("Adding student: " + student);

		try {
			studentDAO.addStudent(student);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error adding students", e);
			addErrorMessage(e);
			return null;
		}

		return "index?faces-redirect=true";
	}

	public String updateStudent(Student student) {
		logger.info("Updating student: " + student);

		try {
			studentDAO.updateStudent(student);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error updating student: " + student, e);
			addErrorMessage(e);
			return null;
		}

		return "index?faces-redirect=true";
	}

	public String deleteStudent(int studentId) {
		logger.info("Deleting student: " + studentId);

		try {
			studentDAO.deleteStudent(studentId);

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error deleting student: " + studentId, e);
			addErrorMessage(e);
			return null;
		}

		return "index?faces-redirect=true";
	}

	public String loadStudent(int studentId) {
		logger.info("Loading student: " + studentId);

		try {
			Student student = studentDAO.getStudent(studentId);

			// Add to session attribute to make id available on the update page.
			ExternalContext extContext = FacesContext.getCurrentInstance().getExternalContext();
			Map<String, Object> requestMap = extContext.getSessionMap();
			requestMap.put("studentUpdate", student);
			logger.info(requestMap.toString());

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error loading student id: " + studentId, e);
			addErrorMessage(e);
			return null;
		}

		return "update-student";
	}
				
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
}
