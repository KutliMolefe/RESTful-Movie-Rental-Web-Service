package com.movierental;
import java.io.IOException;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/image")
public class ImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("image/png");
        int movieId = Integer.parseInt(request.getParameter("id"));

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = DatabaseConnection.getConnection();
            String sql = "SELECT image FROM movies WHERE movie_id = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, movieId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                byte[] imgData = rs.getBytes("image");
                response.setContentLength(imgData.length);
                response.getOutputStream().write(imgData);
            } else {
                response.getWriter().println("No image found for movie ID: " + movieId);
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
    }
}