package com.ctmy.expensemanager;

public class ProjectType {
    private String typeName;
    private int typeImage;

    public ProjectType(String typeName, int typeImage){
        this.typeName = typeName;
        this.typeImage = typeImage;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getTypeImage() {
        return typeImage;
    }

    public void setTypeImage(int typeImage) {
        this.typeImage = typeImage;
    }

}
