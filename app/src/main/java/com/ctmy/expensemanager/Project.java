package com.ctmy.expensemanager;

import java.io.Serializable;

public class Project implements Serializable {
    private String projectId;
    private String DueDate;
    private String creationDate;
    private String projectName;
    private String projectType;
    private double totalExpenses;
    private double totalIncomes;

    public Project(){}

    public Project(String projectId, String projectName, String dueDate, String creationDate, String projectType, Double totalExpenses, Double totalIncomes){
        this.setProjectId(projectId);
        this.setProjectName(projectName);
        this.setDueDate(dueDate);
        this.setCreationDate(creationDate);
        this.setProjectType(projectType);
        this.setTotalExpenses(totalExpenses);
        this.setTotalIncomes(totalIncomes);
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public double getTotalExpenses() {
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



}
