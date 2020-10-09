package com.ctmy.expensemanager;

import java.io.Serializable;

public class Project implements Serializable {
    private String projectId;
    private Long DueDate;
    private Long creationDate;
    private String projectName;
    private String projectType;
    private double totalExpenses;
    private double totalIncomes;

    public Project(){}

    public Project(String projectId, String projectName, Long dueDate, Long creationDate, String projectType, Double totalExpenses, Double totalIncomes){
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

    public Long getDueDate() {
        return DueDate;
    }

    public void setDueDate(Long dueDate) {
        DueDate = dueDate;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
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
