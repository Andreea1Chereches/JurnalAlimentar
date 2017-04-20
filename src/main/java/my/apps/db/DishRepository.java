package my.apps.db;

import my.apps.domain.DishEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class DishRepository {


    final static String URL = "jdbc:postgresql://localhost:5432/foodjournal";
    final static String USERNAME = "andreea";
    final static String PASSWORD = "andreea";

    public static void insert(DishEntry entry) throws ClassNotFoundException, SQLException {
        // 1. load the driver
        Class.forName("org.postgresql.Driver");

        // 2. obtain a connection
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // 3. create a query statement
        PreparedStatement pSt = conn.prepareStatement("INSERT INTO dish( date, name, mealId) VALUES (?,?, ?)");
        pSt.setDate(1, entry.getDate());
        pSt.setString(2, entry.getName());
        pSt.setLong(3, entry.getMealId());

        // 4. execute a prepared statement
        int rowsInserted = pSt.executeUpdate();
        System.out.println("Inserted " + rowsInserted + " rows.");

        // 5. close the objects
        pSt.close();
        conn.close();
    }

    public static void update(DishEntry entry) throws ClassNotFoundException, SQLException {
        // 1. load the driver
        Class.forName("org.postgresql.Driver");

        // 2. obtain a connection
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // 3. create a query statement
        PreparedStatement pSt = conn.prepareStatement("UPDATE dish set date = ?, name = ?, mealId = ? WHERE id = " + entry.getId());
        pSt.setDate(1, entry.getDate());
        pSt.setString(2, entry.getName());
        pSt.setLong(3, entry.getMealId());

        // 4. execute a prepared statement
        int rowsUpdated = pSt.executeUpdate();
        System.out.println("Updated " + rowsUpdated + " rows.");

        // 5. close the objects
        pSt.close();
        conn.close();
    }

    public static void delete(long dishId) throws ClassNotFoundException, SQLException {
        // 1. load the driver
        Class.forName("org.postgresql.Driver");

        // 2. obtain a connection
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // 3. create a query statement
        PreparedStatement pSt = conn.prepareStatement("DELETE FROM dish WHERE id = " + dishId);

        // 4. execute a prepared statement
        int rowsDeleted = pSt.executeUpdate();
        System.out.println("Deleted " + rowsDeleted + " rows.");

        // 5. close the objects
        pSt.close();
        conn.close();
    }

    public static List<DishEntry> read() throws ClassNotFoundException, SQLException {
        // 1. load the driver
        Class.forName("org.postgresql.Driver");

        // 2. obtain a connection
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // 3. create a query statement
        Statement st = conn.createStatement();

        // 4. execute a query
        ResultSet rs = st.executeQuery("SELECT id, date, name, mealid, name FROM dish");

        // 5. iterate the result set and print the values
        List<DishEntry> dishEntries = new ArrayList<>();
        while (rs.next()) {
            DishEntry dishEntry = new DishEntry(
                    rs.getDate("date"),
                    rs.getString("name"),
                    rs.getLong("mealid")
            );
            dishEntry.setId(rs.getLong("id"));
            dishEntries.add(dishEntry);
        }

        // 6. close the objects
        rs.close();
        st.close();
        conn.close();

        return dishEntries;
    }

    public static List<DishEntry> read(long mealId, java.sql.Date fetchDate) throws ClassNotFoundException, SQLException {
        // 1. load the driver
        Class.forName("org.postgresql.Driver");

        // 2. obtain a connection
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // 3. create a query statement
        Statement st = conn.createStatement();

        // 4. execute a query
        //ResultSet rs = st.executeQuery("SELECT id, date, name, mealid FROM dish WHERE mealid = " + mealId + " AND date = '" + fetchDate + "'");
        ResultSet rs = st.executeQuery("SELECT dish.id, dish.date, dish.name, dish.mealid, meal.name FROM dish INNER JOIN meal ON dish.mealid = meal.id WHERE dish.mealid = " + mealId + " AND dish.date = '" + fetchDate + "'");

        // 5. iterate the result set and print the values
        List<DishEntry> dishEntries = new ArrayList<>();
        while (rs.next()) {
            DishEntry dishEntry = new DishEntry(
                    rs.getDate(2),
                    rs.getString(3),
                    rs.getLong(4),
                    rs.getString(5)
            );
            dishEntry.setId(rs.getLong("id"));
            dishEntries.add(dishEntry);
        }

        // 6. close the objects
        rs.close();
        st.close();
        conn.close();

        return dishEntries;
    }

    public static DishEntry read(long dishId) throws ClassNotFoundException, SQLException {
        // 1. load the driver
        Class.forName("org.postgresql.Driver");

        // 2. obtain a connection
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // 3. create a query statement
        Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                            ResultSet.CONCUR_READ_ONLY);

        // 4. execute a query
        ResultSet rs = st.executeQuery("SELECT id, date, name, mealid FROM dish WHERE id = " + dishId);

        // 5. iterate the result set and print the values
        rs.first();
        DishEntry dishEntry = new DishEntry(
                rs.getDate("date"),
                rs.getString("name"),
                rs.getLong("mealid")
        );

        dishEntry.setId(rs.getLong("id"));
        // 6. close the objects
        rs.close();
        st.close();
        conn.close();

        return dishEntry;
    }
}
