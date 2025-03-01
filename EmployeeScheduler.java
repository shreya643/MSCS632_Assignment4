import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;

public class EmployeeScheduler {

    private static final String[] DAYS = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private static final String[] SHIFTS = {"Morning", "Afternoon", "Evening"};

    private Map<String, Map<String, List<String>>> schedule = new HashMap<>();
    private Map<String, Integer> daysWorked = new HashMap<>();
    private Map<String, List<String>> employeePreferences = new HashMap<>();
    private JTextArea outputArea;
    private JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                EmployeeScheduler window = new EmployeeScheduler();
                window.frame.setVisible(true); // Access the frame here
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public EmployeeScheduler() {
        initialize();
    }

    private void initialize() {
        // Create the main frame with the title "Shreya's Scheduler"
        frame = new JFrame("Shreya's Employee Scheduler");
        frame.setBounds(100, 100, 600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(204, 229, 255)); // Light blue background

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2));
        inputPanel.setBackground(new Color(173, 216, 230)); // Light cyan color for panel

        // Create input fields
        JTextField nameField = new JTextField();
        JComboBox<String> shiftBox = new JComboBox<>(SHIFTS);
        JButton submitButton = new JButton("Submit");

        submitButton.setBackground(new Color(100, 149, 237)); // Cornflower blue
        submitButton.setForeground(Color.WHITE);

        inputPanel.add(new JLabel("Employee Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Shift Preference:"));
        inputPanel.add(shiftBox);
        inputPanel.add(submitButton);

        frame.getContentPane().add(inputPanel, BorderLayout.NORTH);

        // Output area setup
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBackground(new Color(240, 248, 255)); // Alice blue background for output area
        outputArea.setFont(new Font("Arial", Font.PLAIN, 14));
        outputArea.setForeground(Color.BLACK);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Action on submit button click
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String shift = (String) shiftBox.getSelectedItem();
                if (!name.isEmpty()) {
                    addEmployee(name, shift);
                    nameField.setText("");
                }
            }
        });

        // Scheduling Button
        JButton scheduleButton = new JButton("Schedule Shifts");
        scheduleButton.setBackground(new Color(50, 205, 50)); // Lime green
        scheduleButton.setForeground(Color.WHITE);

        scheduleButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scheduleShifts();
                displaySchedule();
            }
        });
        frame.getContentPane().add(scheduleButton, BorderLayout.SOUTH);
    }

    // Method to add employees and their shift preference
    private void addEmployee(String name, String shift) {
        employeePreferences.putIfAbsent(name, new ArrayList<>());
        employeePreferences.get(name).add(shift);
    }

    // Scheduling logic
    private void scheduleShifts() {
        schedule.clear();
        daysWorked.clear();

        // Initialize schedule with empty lists
        for (String day : DAYS) {
            schedule.put(day, new HashMap<>());
            for (String shift : SHIFTS) {
                schedule.get(day).put(shift, new ArrayList<>());
            }
        }

        // Assign shifts based on preferences
        for (String employee : employeePreferences.keySet()) {
            for (String shift : employeePreferences.get(employee)) {
                assignShift(employee, shift);
            }
        }
    }

    // Assign shifts to employees while considering their preferences
    private void assignShift(String employee, String shift) {
        for (String day : DAYS) {
            List<String> shiftList = schedule.get(day).get(shift); // Using java.util.List explicitly
            if (shiftList.size() < 2) {
                shiftList.add(employee);
                break;
            }
        }
    }

    // Display the final schedule
    private void displaySchedule() {
        StringBuilder builder = new StringBuilder();
        for (String day : DAYS) {
            builder.append(day).append(":\n");
            for (String shift : SHIFTS) {
                builder.append("  ").append(shift).append(": ");
                builder.append(String.join(", ", schedule.get(day).get(shift))).append("\n");
            }
        }
        outputArea.setText(builder.toString());
    }
}
