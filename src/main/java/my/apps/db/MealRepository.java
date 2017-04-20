package my.apps.db;

import my.apps.domain.MealEntry;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealRepository {


    final static String URL = "jdbc:postgresql://localhost:5432/foodjournal";
    final static String USERNAME = "andreea";
    final static String PASSWORD = "";

    public static void insert(MealEntry entry) throws ClassNotFoundException, SQLException {
        // 1. load the driver
        Class.forName("org.postgresql.Driver");

        // 2. obtain a connection
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // 3. create a query statement
        PreparedStatement pSt = conn.prepareStatement("INSERT INTO meal(name) VALUES (?)");
        pSt.setString(1, entry.getName());

        // 4. execute a prepared statement
        int rowsInserted = pSt.executeUpdate();
        System.out.println("Inserted " + rowsInserted + " rows.");

        // 5. close the objects
        pSt.close();
        conn.close();
    }

    public static List<MealEntry> read() throws ClassNotFoundException, SQLException {
        // 1. load the driver
        Class.forName("org.postgresql.Driver");

        // 2. obtain a connection
        Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        // 3. create a query statement
        Statement st = conn.createStatement();

        // 4. execute a query
        ResultSet rs = st.executeQuery("SELECT id, name FROM meal");

        // 5. iterate the result set and print the values
        List<MealEntry> mealEntries = new ArrayList<>();
        while (rs.next()) {
            MealEntry mealEntry = new MealEntry(
                    rs.getString("name")
            );
            mealEntry.setId(rs.getLong("id"));
            mealEntries.add(mealEntry);
        }

        // 6. close the objects
        rs.close();
        st.close();
        conn.close();

        return mealEntries;
    }
}
