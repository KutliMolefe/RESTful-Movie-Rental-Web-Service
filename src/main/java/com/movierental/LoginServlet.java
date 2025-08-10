package com.movierental;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.SQLException;
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    		 throws ServletException, IOException {
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        JsonObject jsonObject = JsonParser.parseString(jsonBuffer.toString()).getAsJsonObject();
        String username = jsonObject.get("username").getAsString();
        String password = jsonObject.get("password").getAsString();

        User user = authenticate(username, password);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());

            LoginResponse loginResponse = new LoginResponse(true, "Login successful.", user.getUsername(), user.getRole());
            String jsonResponse = new Gson().toJson(loginResponse);
            response.getWriter().write(jsonResponse);
        } else {
            LoginResponse loginResponse = new LoginResponse(false, "Invalid username or password. Please try again.", null, null);
            String jsonResponse = new Gson().toJson(loginResponse);
            response.getWriter().write(jsonResponse);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    		 throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;
        String role = (session != null) ? (String) session.getAttribute("role") : null;

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        if (username != null) {
            out.write(new Gson().toJson(new LoginResponse(false, username, role, role)));
        } else {
            out.write(new Gson().toJson(new LoginResponse(false, null, null, role)));
        }
        out.close();
    }

    private User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("user_id");
                    String email = rs.getString("email");
                    String role = rs.getString("role");
                    return new User(id, username, password, email, role);
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private class LoginResponse {
        private boolean success;
        private String message;
        private String username;
        private String role;

        public LoginResponse(boolean success, String message, String username, String role) {
            this.success = success;
            this.message = message;
            this.username = username;
            this.role = role;
        }



    private class UsernameResponse {
        private String username;
        private String role;

        public UsernameResponse(String username, String role) {
            this.username = username;
            this.role = role;
        }

        public String getUsername() {
            return username;
        }

        public String getRole() {
            return role;
        }

		public void setUsername(String username) {
			this.username = username;
		}

		public void setRole(String role) {
			this.role = role;
		}
    }
}
}