package my.apps.domain;

import java.sql.Date;

/**
 * @author flo
 * @since 19/02/2017.
 */
public class MealEntry {

    private String name;
    private long id;

    public MealEntry(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MealEntry{" +
                "name='" + name +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
