package com.ctmy.expensemanager;

import java.io.Serializable;

public class Transaction implements Serializable {
    private String id;
    private String date;
    private double amount;
    private String description;
    private String imageUrl;
    private String author;


    public Transaction(){}

    public Transaction(String id, String date, double amount,String description, String imageUrl, String author){
        this.setId(id);
        this.setDate(date);
        this.setAmount(amount);
        this.setDescription(description);
        this.setImageUrl(imageUrl);
        this.setAuthor(author);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
