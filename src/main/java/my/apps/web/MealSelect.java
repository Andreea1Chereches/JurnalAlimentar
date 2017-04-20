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

@WebServlet("/mealselect")
public class MealSelect extends HttpServlet {

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

    private void addBackButton(PrintWriter out) {
        out.println("<div>");
        out.println("<br/><a href='index.html'>Go Back</a>");
        out.println("</div>");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String errorGet;
        List<MealEntry> meals = null;


        try {
            meals = MealRepository.read();
        } catch (ClassNotFoundException e) {
            errorGet = "Unable initialize database connection";
        } catch (SQLException e) {
            errorGet = "Unable to read from database! " +  e.getMessage();
        }

        req.setAttribute("meals", meals);

        RequestDispatcher view = getServletContext().getRequestDispatcher("/addnewdish.jsp");
        view.forward(req, resp);
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
