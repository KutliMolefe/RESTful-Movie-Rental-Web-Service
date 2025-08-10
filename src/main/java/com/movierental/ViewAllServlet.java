package com.movierental;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;


@WebServlet("/viewall")
public class ViewAllServlet extends HttpServlet {
	 private static final long serialVersionUID = 1L;

	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        response.setContentType("text/html");

	        Connection con = null;
	        PreparedStatement stmt = null;
	        ResultSet rs = null;

	        try {
	            con = DatabaseConnection.getConnection();
	            String sql = "SELECT * FROM movies ORDER BY genre, movie_id";
	            stmt = con.prepareStatement(sql);
	            rs = stmt.executeQuery();

	            List<Movies> movies = new ArrayList<>();

	            while (rs.next()) {
	                int movieId = rs.getInt("movie_id");
	                String title = rs.getString("title");
	                String genre = rs.getString("genre");
	                byte[] image = rs.getBytes("image");

	                Movies movie = new Movies(movieId, title, genre, image);
	                movies.add(movie);
	            }

	            if (!movies.isEmpty()) {
	                request.setAttribute("movies", movies);
	                request.getRequestDispatcher("/ViewAllMovies.jsp").forward(request, response);
	            } else {
	                response.getWriter().println("No movies found in the database.");
	            }

	        } catch (ClassNotFoundException | SQLException e) {
	            e.printStackTrace();
	            response.getWriter().println("Database Connection Error: " + e.getMessage());
	        } finally {
	            try {
	                if (rs != null) rs.close();
	                if (stmt != null) stmt.close();
	                if (con != null) con.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	        HttpSession session = request.getSession(false);
	        if (session != null) {
	            String username = (String) session.getAttribute("username");
	            String role = (String) session.getAttribute("role");

	            request.setAttribute("username", username);
	            request.setAttribute("role", role);
	        }
	    }
	}