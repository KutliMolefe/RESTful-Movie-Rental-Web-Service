package com.movierental;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
	  private static final long serialVersionUID = 1L; 

	      protected void doPost(HttpServletRequest request, HttpServletResponse response)
	              throws ServletException, IOException {
	          String username = request.getParameter("username");
	          String password = request.getParameter("password");
	          String email = request.getParameter("email");

	          try (Connection conn = DatabaseConnection.getConnection();
	               PreparedStatement stmt = conn.prepareStatement(
	                       "INSERT INTO users (username, password, email) VALUES (?, ?, ?)")) {
	              stmt.setString(1, username);
	              stmt.setString(2, password);
	              stmt.setString(3, email);
	              int rowsAffected = stmt.executeUpdate();

	              if (rowsAffected > 0) {
	                  response.sendRedirect("Login.html"); 
	              } else {
	                  response.getWriter().println("Registration failed. Please try again.");
	              }

	          } catch (ClassNotFoundException | SQLException e) {
	              e.printStackTrace();
	              response.getWriter().println("Error: " + e.getMessage());
	          }
    }
}