package org.yearup.models;

import java.util.Objects;

public class Category
{
    private int categoryId;
    private String name;
    private String description;

    public Category()
    {
    }

    public Category(int categoryId, String name, String description)
    {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
    }

    public int getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(int categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return getCategoryId() == category.getCategoryId() && Objects.equals(getName(), category.getName()) && Objects.equals(getDescription(), category.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCategoryId(), getName(), getDescription());
    }
}
