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
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/dishremove")
public class DishRemove extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String dishId = req.getParameter("dishId");

        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        addStyle(out);

        try {
            Long validDishId = Long.valueOf(dishId);
            DishRepository.delete(validDishId);
            out.println("<b>Deleted the dish</b>");
        } catch (IllegalArgumentException e) {
            out.println("<dif class='error'><b>Unable to parse dish ID!</b></div>");
        } catch (ClassNotFoundException e) {
            out.println("<div class='error'><b>Unable initialize database connection</b></div>");
        } catch (SQLException e) {
            out.println("<div class='error'><b>Unable to write to database! " +  e.getMessage() +"</b></div>");
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
