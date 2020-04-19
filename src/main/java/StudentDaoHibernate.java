import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class StudentDaoHibernate {

    private static StudentDaoHibernate instance;

    public static StudentDaoHibernate getInstance() throws Exception {
        if (instance == null) {
            instance = new StudentDaoHibernate();
        }

        return instance;
    }

    public List<Student> getStudents() throws Exception {

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Student> criteria = builder.createQuery(Student.class);
            criteria.from(Student.class);
            List<Student> students = session.createQuery(criteria).getResultList();
            return students;

        } finally {
            if (session != null) session.close();
        }
    }

    public void addStudent(Student student) throws Exception {

        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            session.beginTransaction();
            session.save(student);
            session.getTransaction().commit();

        } finally {
            if (session != null) session.close();
        }
    }

    public Student getStudent(int studentId) throws Exception {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction txn = session.getTransaction();

        try {
            txn.begin();

            Student student = session.get(Student.class, studentId);

            txn.commit();
            return student;

        } catch (Exception e) {
            if (txn != null) txn.rollback();
            throw e;

        } finally {
            session.close();
        }
    }

    public void updateStudent(Student student) throws Exception {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction txn = session.getTransaction();

        try {
            txn.begin();

            Student student_updated = session.get(Student.class, student.getId());
            student_updated.setFirstName(student.getFirstName());
            student_updated.setLastName(student.getLastName());
            student_updated.setEmail(student.getEmail());

            txn.commit();

        } catch (Exception e) {
            if (txn != null) txn.rollback();
            throw e;

        } finally {
            session.close();
        }
    }

    public void deleteStudent(int studentId) throws Exception {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction txn = session.getTransaction();

        try {
            txn.begin();

            Student student = session.get(Student.class, studentId);
            session.delete(student);

            txn.commit();

        } catch (Exception e) {
            if (txn != null) txn.rollback();
            throw e;

        } finally {
            session.close();
        }
    }
}
