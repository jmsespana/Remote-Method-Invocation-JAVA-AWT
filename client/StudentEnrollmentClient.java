package client;

import server.StudentEnrollment;

import java.awt.*;
import java.awt.event.*;
import java.rmi.Naming;
import java.util.List;
import javax.swing.JOptionPane;  // For confirmation dialog

public class StudentEnrollmentClient extends Frame implements ActionListener {
    private TextField nameField, idField, emailField;
    private Choice courseChoice;
    private Button enrollButton, deleteButton, clearButton;
    private TextArea outputArea;
    private StudentEnrollment enrollmentService;

    public StudentEnrollmentClient() {
        // Create UI components
        nameField = new TextField(20);
        idField = new TextField(20);
        emailField = new TextField(20);

        //STEP 3; Add diri og courses
        courseChoice = new Choice();
        courseChoice.add("Computer Science");
        courseChoice.add("Information Technology");
        courseChoice.add("Software Engineering");
        courseChoice.add("Data Science");

        enrollButton = new Button("Enroll");
        deleteButton = new Button("Delete by ID");
        clearButton = new Button("Clear");
        outputArea = new TextArea(10, 50);
        outputArea.setEditable(false);

        // Set up the layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0; add(new Label("Name:"), gbc);
        gbc.gridx = 1; add(nameField, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1; add(new Label("Student ID:"), gbc);
        gbc.gridx = 1; add(idField, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 2; add(new Label("Email:"), gbc);
        gbc.gridx = 1; add(emailField, gbc);

        // Row 4
        gbc.gridx = 0; gbc.gridy = 3; add(new Label("Course:"), gbc);
        gbc.gridx = 1; add(courseChoice, gbc);

        // Row 5
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; add(enrollButton, gbc);

        // Row 6
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; add(deleteButton, gbc);

        // Row 7
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; add(clearButton, gbc);

        // Row 8
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; add(outputArea, gbc);

        enrollButton.addActionListener(this);
        deleteButton.addActionListener(this);
        clearButton.addActionListener(this);

        // Set up the frame
        setTitle("Student Enrollment");
        setSize(600, 400);
        setVisible(true);

        try {
            enrollmentService = (StudentEnrollment) Naming.lookup("rmi://localhost:1099/StudentEnrollmentService");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == enrollButton) {
            String name = nameField.getText();
            String studentId = idField.getText();
            String email = emailField.getText();
            String course = courseChoice.getSelectedItem();
    
            // Validate fields before enrolling
            if (name.trim().isEmpty() || studentId.trim().isEmpty() || email.trim().isEmpty()) {
                // Prompt the user to input all fields
                JOptionPane.showMessageDialog(this, "Please input all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
            } else {
                try {
                    String result = enrollmentService.enrollStudent(name, studentId, email, course);
                    outputArea.append(result + "\n");
                    displayEnrolledStudents();
                    clearFields(); // Clear fields after successful enrollment
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == deleteButton) {
            // STEP 1; method sa delete student by ID
            String studentIdToDelete = JOptionPane.showInputDialog(this, 
                "Enter the Student ID to delete:", "Delete Student", JOptionPane.QUESTION_MESSAGE);
    
            if (studentIdToDelete != null && !studentIdToDelete.trim().isEmpty()) {
                try {
                    String result = enrollmentService.deleteStudentById(studentIdToDelete);
                    outputArea.append(result + "\n");
                    displayEnrolledStudents();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                outputArea.append("Deletion cancelled or invalid input.\n");
            }
        } else if (e.getSource() == clearButton) {
            clearFields();
        }
    }
    

    private void displayEnrolledStudents() {
        try {
            outputArea.setText(""); //STEP 2: Clear the output area before displaying students
            List<String> students = enrollmentService.getEnrolledStudents();
            outputArea.append("Enrolled Students:\n");
            for (String student : students) {
                outputArea.append(student + "\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        nameField.setText("");
        idField.setText("");
        emailField.setText("");
        courseChoice.select(0);
    }

    public static void main(String[] args) {
        new StudentEnrollmentClient();
    }
}
