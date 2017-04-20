package my.apps.domain;

import java.sql.Date;

public class DishEntry {

    private Date date;
    private long mealId;
    private String name;
    private long id;
    private String mealName;

    public DishEntry(Date date, String name, long mealId, String mealName) {
        this.date = date;
        this.name = name;
        this.mealId = mealId;
        this.mealName = mealName;
    }

    public DishEntry(Date date, String name, long mealId) {
        this.date = date;
        this.name = name;
        this.mealId = mealId;
    }

    @Override
    public String toString() {
        return "DishEntry{" +
                "date=" + date +
                ", name='" + name + '\'' +
                '}';
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public long getMealId() {
        return mealId;
    }

    public String getMealName() { return mealName; }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
