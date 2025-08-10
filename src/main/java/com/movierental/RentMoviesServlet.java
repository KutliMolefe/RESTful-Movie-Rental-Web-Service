package com.movierental;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RentMoviesServlet")
public class RentMoviesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String movieId = request.getParameter("id");
        String title = request.getParameter("title");
        String username = (String) request.getSession().getAttribute("username"); // Assuming username is stored in session
        java.util.Date rentalDate = new java.util.Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(rentalDate);
        calendar.add(Calendar.DAY_OF_YEAR, 10);
        java.util.Date endDate = calendar.getTime();

        // Update rentals table in the database
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO rentals (user_id, movie_id, rental_date, end_date) VALUES (?, ?, ?, ?)")) {
        	
            int userId = getUserIdByUsername(username); // Fetch userId based on username

            stmt.setInt(1, userId); // Set the userId obtained from getUserIdByUsername method
            stmt.setInt(2, Integer.parseInt(movieId));
            stmt.setDate(3, new java.sql.Date(rentalDate.getTime()));
            stmt.setDate(4, new java.sql.Date(endDate.getTime()));
            stmt.executeUpdate();

            // Redirect to confirmation page
            response.sendRedirect("RentAMovie.html?title=" + URLEncoder.encode(title, "UTF-8") + "&rentalDate="
                    + rentalDate.getTime() + "&endDate=" + endDate.getTime());

        } catch (SQLException e) {
        	e.printStackTrace(); // Basic logging
            // Enhanced logging and handling
            System.err.println("SQL Error: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            // Handle database error gracefully
            request.setAttribute("errorMessage", "Failed to rent movie. Please try again later.");
            request.getRequestDispatcher("/errorPage.jsp").forward(request, response);
            e.printStackTrace();
            // Handle database error
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
            // Handle class not found exception
        }
    }

    private int getUserIdByUsername(String username) throws SQLException, ClassNotFoundException {
        int userId = 0;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT user_id FROM users WHERE username = ?")) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("user_id");
            }
        }
        return userId;
    }
}

