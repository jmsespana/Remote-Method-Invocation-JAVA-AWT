package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface StudentEnrollment extends Remote {
    // Method to enroll a student by passing name, ID, email, and course
    String enrollStudent(String name, String studentId, String email, String course) throws RemoteException;

    // Method to retrieve the list of enrolled students
    List<String> getEnrolledStudents() throws RemoteException;

    // Method to delete a student by ID
    String deleteStudentById(String studentId) throws RemoteException;
}
