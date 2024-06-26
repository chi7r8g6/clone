import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/PriceCheckServlet")
public class PriceCheckServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // JDBC URL, username, and password of MySQL server
    private final String Jdbc_password = "123@Root";
    private final String Jdbc_user = "root";
    private final String Jdbc_url = "jdbc:mysql://localhost:3306/cruud";
    
    private Connection con;
    
    
            
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Retrieve form parameters
        String carModel = request.getParameter("car-model");
        String name = request.getParameter("name");
        String email = request.getParameter("email");

        // Set response content type
        response.setContentType("text/html");

        // Initialize PrintWriter
        PrintWriter out = response.getWriter();

        try {
            // Register JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // Establish database connection
            Connection conn = DriverManager.getConnection( "jdbc:mysql://localhost:3306/cruud", "root", "123@Root");

            // Prepare SQL query to retrieve price based on car model
            String sql = "SELECT price FROM cruud WHERE model = ";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, carModel);
            
            // Execute query
            ResultSet rs = pstmt.executeQuery();
   
            // Process the result
            if (rs.next()) {
                int price = rs.getInt("price");
                
                // Display result
                out.println("<html><head><title>Price Check Result</title></head><body>");
                out.println("<h2>Price Check Result</h2>");
                out.println("<p>Car Model: " + carModel + "</p>");
                out.println("<p>Price: $" + price + "</p>");
                out.println("<p>Your Name: " + name + "</p>");
                out.println("<p>Your Email: " + email + "</p>");
                
                out.println("</body></html>");
            } else {
                out.println("<html><head><title>Price Check Result</title></head><body>");
                out.println("<h2>Price Check Result</h2>");
                out.println("<p>Sorry, no price found for " + carModel + "</p>");
                out.println("</body></html>");
            }

            // Close resources
            rs.close();
            pstmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            out.println("Error: " + e.getMessage());
        }

        // Close PrintWriter
        out.close();
    }
}
