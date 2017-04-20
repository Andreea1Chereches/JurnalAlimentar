package my.apps.web;

import my.apps.db.DishRepository;
import my.apps.domain.DishEntry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/dish")
public class Dish extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        //get input as string
        String date = request.getParameter("date");
        String name = request.getParameter("name");
        String mealId = request.getParameter("mealid");

        System.out.println(date + name);
        // write results to response
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        addStyle(out);

        try {
            Date validDate = Date.valueOf(date);
            Long validMealId = Long.valueOf(mealId);
            DishEntry entry = new DishEntry(validDate, name, validMealId);
            DishRepository.insert(entry);
            out.println("<b>Inserted new dish entry" + entry + "</b>");
        } catch (IllegalArgumentException e) {
            out.println("<dif class='error'><b>Unable to parse date! Expected format is yyyy-MM-dd but was " + date);
        } catch (ClassNotFoundException e) {
            out.println("<div class='error'><b>Unable initialize database connection<b></div>");
        } catch (SQLException e) {
            out.println("<div class='error'><b>Unable to write to database! " +  e.getMessage() +"<b></div>");
        }


        addBackButton(out);

        // finished writing, send to browser
        out.close();

    }

    private void addBackButton(PrintWriter out) {
        out.println("<div>");
        out.println("<br/><a href='index.html'>Go Back</a>");
        out.println("</div>");
    }

    private void addNewDishButton(PrintWriter out) {
        out.println("<div>");
        out.println("<form action='mealselect' method='get'>");
        out.println("<input type='submit' value='Add new dish'/>");
        out.println("</form>");
        out.println("</div>");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fetchDate = req.getParameter("fetchDate");
        String mealId = req.getParameter("mealId");

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<head>");
        out.println("<title> The list of dishes </title>");
        addStyle(out);
        out.println("</head>");

        try {
            Date validDate = Date.valueOf(fetchDate);
            Long validMealId = Long.valueOf(mealId);
            out.println("<div>");
            out.println("<h2>Dishes</h2>");
            out.println("</div>");
            out.println("<div>");
            out.println("<table>");
            out.println("<tr>");
            out.println("<th>Date</th>");
            out.println("<th>Name</th>");
            out.println("<th>Meal</th>");
            out.println("<th>Action</th>");
            out.println("</tr>");
            List<DishEntry> dishEntries = DishRepository.read(validMealId, validDate);
            for (DishEntry dishEntry : dishEntries) {
                out.println("<tr>");
                out.println("<td>"+dishEntry.getDate()+"</td>");
                out.println("<td>"+dishEntry.getName()+"</td>");
                out.println("<td>"+dishEntry.getMealName()+"</td>");
                out.println("<td> " +
                        "<div>" +
                        "<div class='sameLine'>" +
                        "<form action='dishremove' method='post' class='myForm'> "+
                        "<input type='hidden' name='dishId' value='" + dishEntry.getId() + "'/>" +
                        "<input type='submit' value='Delete'  class='alowHover'/>" +
                        "</form>" +
                        "</div>" +
                        "<div class='sameLine'>" +
                        "<form action='dishModify' method='get' class='myForm'> "+
                        "<input type='hidden' name='dishId' value='" + dishEntry.getId() + "'/>" +
                        "<input type='submit' value='Modify'  class='alowHover'/>" +
                        "</form>" +
                        "</div>" +
                        "</div>" +
                        "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
            out.println("</div>");
        } catch (IllegalArgumentException e) {
            out.println("<dif class='error'><b>Unable to parse date! Expected format is yyyy-MM-dd but was " + fetchDate);
        } catch (ClassNotFoundException e) {
            out.println("<div class='error'><b>Unable initialize database connection<b></div>");
        } catch (SQLException e) {
            out.println("<div class='error'><b>Unable to read from database! " +  e.getMessage() +"<b></div>");
        }
        addNewDishButton(out);
        addBackButton(out);
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
