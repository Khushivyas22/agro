import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String contact = request.getParameter("contact");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String address = request.getParameter("address");
        String area = request.getParameter("area");
        String state = request.getParameter("state");
        String pincode = request.getParameter("pincode");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/minor?useSSL=false", "root", "khushi27")) {

                String sql = "INSERT INTO regis(name, contact, email, password, address, area, state, pincode) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, contact);
                ps.setString(3, email);
                ps.setString(4, password);
                ps.setString(5, address);
                ps.setString(6, area);
                ps.setString(7, state);
                ps.setString(8, pincode);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    // ✅ Set session attributes
                    HttpSession session = request.getSession();
                    session.setAttribute("name", name);
                    session.setAttribute("contact", contact);
                    session.setAttribute("email", email);
                    session.setAttribute("address", address);
                    session.setAttribute("area", area);
                    session.setAttribute("state", state);
                    session.setAttribute("pincode", pincode);

                    // ✅ Redirect to account.jsp
                    response.sendRedirect("login.html");
                } else {
                    response.getWriter().println("<h2>Registration Failed!</h2>");
                }
            }
        } catch (Exception e) {
            response.getWriter().println("<h3>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace(response.getWriter());
        }
    }
}
