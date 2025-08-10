package com.movierental;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@WebServlet("/SearchMoviesServlet")
public class SearchMoviesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String title = request.getParameter("title");

        try {
            con = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM movies WHERE title LIKE ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, "%" + title + "%"); 

            rs = stmt.executeQuery();

            StringBuilder sb = new StringBuilder();

            while (rs.next()) {
                int movieId = rs.getInt("movie_id");
                String movieTitle = rs.getString("title");
                String genre = rs.getString("genre");
                byte[] image = rs.getBytes("image");

                sb.append("<div class='movie-card'>");
                sb.append("<img src='image?id=").append(movieId).append("' alt='").append(movieTitle).append("'>");
                sb.append("<h3>").append(movieTitle).append("</h3>");
                sb.append("<p>").append(genre).append("</p>");
                sb.append("<button class='rent-button'><a href='RentAMovie.html?id=").append(movieId).append("'>Rent</a></button>");
                sb.append("</div>");
            }

            if (sb.length() > 0) {
                response.getWriter().write(sb.toString());
            } else {
                response.getWriter().println("<p>No movies found matching the search criteria.</p>");
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}