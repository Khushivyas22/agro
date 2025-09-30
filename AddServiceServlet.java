import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;

@WebServlet("/AddServiceServlet")
@MultipartConfig(maxFileSize = 16177215)  // ~16MB
public class AddServiceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String category = request.getParameter("category");
        String priceStr = request.getParameter("price");
        String description = request.getParameter("description");
        Part imagePart = request.getPart("image");

        double price = Double.parseDouble(priceStr);

     // DB credentials
String dbURL = "jdbc:mysql://localhost:3306/minor?useSSL=false&allowPublicKeyRetrieval=true";
String dbUser = "root";
String dbPass = "khushi27";

try {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass);
    
    String sql = "INSERT INTO services (name, category, price, description, image) VALUES (?, ?, ?, ?, ?)";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, name);
    ps.setString(2, category);
    ps.setDouble(3, price);
    ps.setString(4, description);

    if(imagePart != null && imagePart.getSize() > 0){
        InputStream inputStream = imagePart.getInputStream();
        ps.setBlob(5, inputStream);
    } else {
        ps.setNull(5, Types.BLOB);
    }

    int rows = ps.executeUpdate();
    if(rows > 0){
        out.println("<h3 style='color:green;'>Service added successfully!</h3>");
    } else {
        out.println("<h3 style='color:red;'>Failed to add service.</h3>");
    }

    ps.close();
    conn.close();
} catch(Exception e){
    e.printStackTrace(out);
}

    }
}
