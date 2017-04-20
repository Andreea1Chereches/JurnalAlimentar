package my.apps.web;

import my.apps.db.MealRepository;
import my.apps.domain.MealEntry;
import my.apps.db.DishRepository;
import my.apps.domain.DishEntry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;
import java.util.Date;

@WebServlet("/mealstart")
public class MealStart extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        //get input as string
        String name = request.getParameter("name");

        System.out.println(name);
        // write results to response
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        addStyle(out);

        try {
            MealEntry entry = new MealEntry(name);
            MealRepository.insert(entry);
            out.println("<b>Inserted new meal entry" + entry + "</b>");
        } catch (ClassNotFoundException e) {
            out.println("<div class='error'><b>Unable initialize database connection<b></div>");
        } catch (SQLException e) {
            out.println("<div class='error'><b>Unable to write to database! " +  e.getMessage() +"<b></div>");
        }


        addBackButton(out);

        // finished writing, send to browser
        out.close();

    }

    private void addNewDishButton(PrintWriter out) {
        out.println("<div>");
        out.println("<form action='mealselect' method='get'>");
        out.println("<input type='submit' value='Add new dish'/>");
        out.println("</form>");
        out.println("</div>");
    }

    private void addBackButton(PrintWriter out) {
        out.println("<div>");
        out.println("<br/><a href='index.html'>Go Back</a>");
        out.println("</div>");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String dishForMeal;
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        Date firstDayOfWeek;
        Date currentDate;
        int substractDays = cal.get(Calendar.DAY_OF_WEEK);
        java.sql.Date fetchDate;
        String eol = System.getProperty("line.separator");
        if (substractDays == 1) {
            substractDays = 6;
        }
        else {
            substractDays = substractDays - 2;
        }

        cal.setFirstDayOfWeek(cal.MONDAY);
        //set the date from the calendar to the first day of the week
        cal.add(Calendar.DAY_OF_MONTH, (-1 * substractDays));

        firstDayOfWeek = cal.getTime();

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<head>");
        out.println("<title> The list of dishes </title>");
        addStyle(out);
        out.println("</head>");

        try {
            out.println("<div>");
            out.println("<h2>Dishes consumed this week</h2>");
            out.println("</div>");
            out.println("<script src='https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js'></script>");
            out.println("<script>" +
                    "$(function(){" +
                    "$('.myForm').click(function() {" +
                    "$(this).submit();" +
                    "});" +
                    "});" +
                    "</script>");
            out.println("<div>");
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>Meals</th>");
            out.println("<th>Monday</th>");
            out.println("<th>Tuesday</th>");
            out.println("<th>Wednesday</th>");
            out.println("<th>Thursday</th>");
            out.println("<th>Friday</th>");
            out.println("<th>Saturday</th>");
            out.println("<th>Sunday</th>");
            out.println("</tr>");
            List<MealEntry> mealEntries = MealRepository.read();
            for (MealEntry mealEntry : mealEntries) {
                cal.setTime(firstDayOfWeek);
                out.println("<tr>");
                out.println("<td>"+mealEntry.getName()+"</td>");
                for (int i = 0; i <= 6; i++) {
                    dishForMeal = "";
                    currentDate = cal.getTime();
                    fetchDate = new java.sql.Date(currentDate.getTime());
                    List<DishEntry> dishEntries = DishRepository.read(mealEntry.getId(), fetchDate);
                    for (DishEntry dishEntry : dishEntries) {
                        if (dishForMeal == "") {
                            dishForMeal = dishEntry.getName();
                        } else {
                            dishForMeal = dishForMeal + eol + dishEntry.getName();
                        }
                    }
                    if (dishForMeal == "") {
                        dishForMeal = "&nbsp";
                    }
                    out.println("<td class='alowHover'> <form action='dish' method='get' class='myForm'> "+ dishForMeal +
                            "<input type='hidden' name='fetchDate' value='" + fetchDate + "'/>" +
                            "<input type='hidden' name='mealId' value='" + mealEntry.getId() + "'/>" +
                            "</form></td>");
                    //increment the day so we will fetch the data for all week
                    cal.add(cal.DAY_OF_MONTH, 1);
                }
                out.println("</tr>");
            }
            out.println("</table>");
            out.println("</div>");
        } catch (ClassNotFoundException e) {
            out.println("<div class='error'><b>Unable initialize database connection<b></div>");
        } catch (SQLException e) {
            out.println("<div class='error'><b>Unable to read from database! " +  e.getMessage() +"<b></div>");
        }
        addNewDishButton(out);
        out.close();
    }

    private void addStyle(PrintWriter out) {
        out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"css/style.css\">");
    }

    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("init() called.");
    }

    @Override
    public void destroy() {
        System.out.println("Destroying Servlet!");
        super.destroy();
    }
}
