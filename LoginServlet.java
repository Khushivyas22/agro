import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/LoginServlet")  // must match form action
public class LoginServlet extends HttpServlet{

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String s1 = request.getParameter("username");
        String s2 = request.getParameter("password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/minor?useSSL=false", "root", "khushi27")) {

                String sql = "SELECT * FROM regis WHERE name = ? AND password = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, s1);
                ps.setString(2, s2);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    HttpSession session = request.getSession();
                    // Save all user info in session
                    session.setAttribute("name", rs.getString("name"));
                    session.setAttribute("contact", rs.getString("contact"));
                    session.setAttribute("email", rs.getString("email"));
                    session.setAttribute("address", rs.getString("address"));
                    session.setAttribute("area", rs.getString("area"));
                    session.setAttribute("state", rs.getString("state"));
                    session.setAttribute("pincode", rs.getString("pincode"));

               
                    response.sendRedirect("home.html");
                } else {
                    out.println("<h3 style='color:red;'>Invalid username or password</h3>");
                    out.println("<a href='login.html'>Try again</a>");
                }
            }
        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace(out);
        } finally {
            out.close();
        }
    }
}
