package com.movierental;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/main")
public class MainServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		 HttpSession session = request.getSession(false);
	        if (session != null) {
	            String username = (String) session.getAttribute("username");
	            String role = (String) session.getAttribute("role");

	            request.setAttribute("username", username);
	            request.setAttribute("role", role);
       
        response.setContentType("text/html");
        response.getWriter().write("<html><body>");
        response.getWriter().write("<h1>Welcome to the Movie Rental Platform</h1>");
        response.getWriter().write("<a href='/viewall'>View All Movies</a>");
        response.getWriter().write("</body></html>");
        
        
        response.getWriter().write("<script>");
        response.getWriter().write("const userData = { username: '" + username + "', role: '" + role + "' };");

        response.getWriter().write("</script>");
    }
}
}