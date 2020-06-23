package com.ctmy.expensemanager;

public class Projects {
    private String projectId;
    private String DueDate;
    private String projectName;
    // private double totalExpenses;
    // private double totalIncomes;

    public Projects(){}

    public Projects(String projectName, String dueDate){
        this.setProjectId(projectId);
        this.setProjectName(projectName);
        this.setDueDate(dueDate);
        // this.setTotalExpenses(totalExpenses);
        // this.setTotalIncomes(totalIncomes);
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    /*public double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public double getTotalIncomes() {
        return totalIncomes;
    }

    public void setTotalIncomes(double totalIncomes) {
        this.totalIncomes = totalIncomes;
    }
*/


}
