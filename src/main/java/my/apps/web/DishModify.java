package my.apps.web;

import my.apps.db.DishRepository;
import my.apps.domain.DishEntry;
import my.apps.domain.MealEntry;
import my.apps.db.MealRepository;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/dishModify")
public class DishModify extends HttpServlet {

    private void addBackButton(PrintWriter out) {
        out.println("<div>");
        out.println("<br/><a href='index.html'>Go Back</a>");
        out.println("</div>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        //get input as string
        String date = request.getParameter("date");
        String name = request.getParameter("name");
        String mealId = request.getParameter("mealid");
        String id = request.getParameter("id");

        System.out.println(date + name);
        // write results to response
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        addStyle(out);

        try {
            Date validDate = Date.valueOf(date);
            Long validMealId = Long.valueOf(mealId);
            Long validId = Long.valueOf(id);
            DishEntry entry = new DishEntry(validDate, name, validMealId);
            entry.setId(validId);
            DishRepository.update(entry);
            out.println("<b>Updated dish entry" + entry + "</b>");
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

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String errorGet;
        List<MealEntry> meals = null;
        String dishId = req.getParameter("dishId");
        DishEntry dish = null;

        try {
            Long validDishId = Long.valueOf(dishId);
            dish = DishRepository.read(validDishId);
            meals = MealRepository.read();
        } catch (IllegalArgumentException e) {
            errorGet = "Unable to convert to long";
        } catch (ClassNotFoundException e) {
            errorGet = "Unable initialize database connection";
        } catch (SQLException e) {
            errorGet = "Unable to read from database! " +  e.getMessage();
        }
        req.setAttribute("dishId", dish.getId());
        req.setAttribute("dishName", dish.getName());
        req.setAttribute("dishDate", dish.getDate());
        req.setAttribute("dishMealId", dish.getMealId());
        req.setAttribute("meals", meals);

        RequestDispatcher view = getServletContext().getRequestDispatcher("/updatedish.jsp");
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
