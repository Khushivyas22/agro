import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.sql.*;

@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String search = request.getParameter("search");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/agrotech", "root", "yourpassword");

            String sql = "SELECT name, category, price, description, image_path FROM services WHERE name LIKE ? OR category LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + search + "%");
            ps.setString(2, "%" + search + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String category = rs.getString("category");
                String price = rs.getString("price");
                String desc = rs.getString("description");
                String img = rs.getString("image_path"); // image_path DB me store karna padega

                out.println("<div class='product'>");
                out.println("<img src='" + img + "' alt='" + name + "'>");
                out.println("<div>");
                out.println("<h3>" + name + "</h3>");
                out.println("<p><strong>Category:</strong> " + category + "</p>");
                out.println("<p><strong>Price:</strong> ₹" + price + "</p>");
                out.println("<p>" + desc + "</p>");
                out.println("<a href='#' class='buy-btn'>Buy Now</a>");
                out.println("</div></div>");
            }

            con.close();
        } catch (Exception e) {
            out.println("<p>Error: " + e.getMessage() + "</p>");
        }
    }
}
