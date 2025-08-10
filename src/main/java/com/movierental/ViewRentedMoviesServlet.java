package com.movierental;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/ViewRentedMoviesServlet")
public class ViewRentedMoviesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username"); 
        if (username == null) {
           
            response.sendRedirect("login.html");
            return;
        }

        List<Movies> rentedMovies = getRentedMovies(username);

        request.setAttribute("rentedMovies", rentedMovies);
        request.getRequestDispatcher("ViewRentMovies.html").forward(request, response);
    }

    private List<Movies> getRentedMovies(String username) {
        List<Movies> rentedMovies = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT m.title, r.rental_date, r.end_date FROM movies m INNER JOIN rentals r ON m.movie_id = r.movie_id INNER JOIN users u ON u.user_id = r.user_id WHERE u.username = ?")) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String title = rs.getString("title");
                java.sql.Date rentalDate = rs.getDate("rental_date");
                java.sql.Date endDate = rs.getDate("end_date");

                Movies movie = new Movies(0, title, title, null);
                rentedMovies.add(movie);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            
        }

        return rentedMovies;
    }
}