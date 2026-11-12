package com.example.assistant;

import dev.langchain4j.agent.tool.Tool;

import java.util.Map;

public class EmployeeTools {

    // In-memory sample employee data
    private static final Map<String, Employee> EMPLOYEES = Map.of(
            "EMP001", new Employee("EMP001", "Alice Johnson", "Engineering", 75000.0, 20, 5),
            "EMP002", new Employee("EMP002", "Bob Smith", "HR", 62000.0, 15, 3),
            "EMP003", new Employee("EMP003", "Carol Williams", "Finance", 88000.0, 25, 7),
            "EMP004", new Employee("EMP004", "David Brown", "Marketing", 71000.0, 18, 0)
    );

    @Tool("Retrieve employee details such as name, department, salary and leave info by employee ID. Employee IDs look like EMP001.")
    public String getEmployeeDetails(String employeeId) {
        Employee e = EMPLOYEES.get(employeeId.toUpperCase());
        if (e == null) {
            return "Employee not found for ID: " + employeeId;
        }
        return "Employee ID: " + e.id() + ", Name: " + e.name() + ", Department: " + e.department()
                + ", Monthly Salary: " + e.monthlySalary() + ", Total Leave: " + e.totalLeave()
                + " days, Used Leave: " + e.usedLeave() + " days";
    }

    @Tool("Calculate annual salary by multiplying monthly salary by 12.")
    public double calculateAnnualSalary(double monthlySalary) {
        return monthlySalary * 12;
    }

    @Tool("Check remaining leave balance for an employee by employee ID.")
    public String checkLeaveBalance(String employeeId) {
        Employee e = EMPLOYEES.get(employeeId.toUpperCase());
        if (e == null) {
            return "Employee not found for ID: " + employeeId;
        }
        return e.name() + " has " + remainingLeave(e) + " leave days remaining out of " + e.totalLeave() + " total.";
    }

    @Tool("Calculate remaining leave balance for an employee after using a given number of leave days.")
    public String calculateRemainingLeaveBalance(String employeeId, int daysUsed) {
        Employee e = EMPLOYEES.get(employeeId.toUpperCase());
        if (e == null) {
            return "Employee not found for ID: " + employeeId;
        }
        int remaining = remainingLeave(e) - daysUsed;
        return e.name() + " will have " + Math.max(remaining, 0) + " leave days remaining after using "
                + daysUsed + " days (current balance: " + remainingLeave(e) + ").";
    }

    private int remainingLeave(Employee e) {
        return e.totalLeave() - e.usedLeave();
    }

    private record Employee(String id, String name, String department,
                            double monthlySalary, int totalLeave, int usedLeave) {
    }
}
