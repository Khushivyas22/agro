import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/UpdateAccountServlet")
public class UpdateAccountServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get updated values from form
        String name = request.getParameter("name");
        String contact = request.getParameter("contact");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        String area = request.getParameter("area");
        String state = request.getParameter("state");
        String pincode = request.getParameter("pincode");

        // Get current session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("email") == null) {
            response.sendRedirect("login.html"); // User not logged in
            return;
        }

        String oldEmail = (String) session.getAttribute("email"); // use email as unique identifier

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/minor?useSSL=false", "root", "khushi27")) {

                // Update query
                String sql = "UPDATE regis SET name=?, contact=?, email=?, address=?, area=?, state=?, pincode=? WHERE email=?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, contact);
                ps.setString(3, email);
                ps.setString(4, address);
                ps.setString(5, area);
                ps.setString(6, state);
                ps.setString(7, pincode);
                ps.setString(8, oldEmail);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    // Update session values
                    session.setAttribute("name", name);
                    session.setAttribute("contact", contact);
                    session.setAttribute("email", email);
                    session.setAttribute("address", address);
                    session.setAttribute("area", area);
                    session.setAttribute("state", state);
                    session.setAttribute("pincode", pincode);

                    response.sendRedirect("myaccount.jsp"); // Redirect back to account page
                } else {
                    response.getWriter().println("<h3>Update Failed!</h3>");
                }
            }
        } catch (Exception e) {
            response.getWriter().println("<h3>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace(response.getWriter());
        }
    }
}
