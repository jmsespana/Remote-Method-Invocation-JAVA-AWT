package server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class StudentEnrollmentImpl extends UnicastRemoteObject implements StudentEnrollment {
    private List<String> students;

    protected StudentEnrollmentImpl() throws RemoteException {
        super();
        students = new ArrayList<>();
    }

    @Override
    public String enrollStudent(String name, String studentId, String email, String course) throws RemoteException {
        String studentInfo = "Name: " + name + ", ID: " + studentId + ", Email: " + email + ", Course: " + course;
        students.add(studentInfo);
        System.out.println("Enrolled: " + studentInfo);
        return "Successfully enrolled: " + name + " in " + course;
    }

    @Override
    public List<String> getEnrolledStudents() throws RemoteException {
        return students;
    }

    @Override
    public String deleteStudentById(String studentId) throws RemoteException {
        for (String student : students) {
            if (student.contains("ID: " + studentId)) {
                students.remove(student);
                System.out.println("Deleted student with ID: " + studentId);
                return "Successfully deleted student with ID: " + studentId;
            }
        }
        return "Student with ID: " + studentId + " not found.";
    }

    public static void main(String[] args) {
        try {
            StudentEnrollmentImpl server = new StudentEnrollmentImpl();
            Naming.rebind("rmi://localhost:1099/StudentEnrollmentService", server);
            System.out.println("RMI Server is running...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
