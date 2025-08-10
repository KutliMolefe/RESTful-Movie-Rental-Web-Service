<%@ page import="java.util.List, com.movierental.Movies" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>View All Movies</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        body {
            background-color: #1b1b1b;
            color: white;
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
        }
        h1 {
            text-align: center;
            
        }
        .movie-container {
        margin-top:50px;
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
        }
        .movie-card {
            background-color: #333;
            border-radius: 10px;
            margin: 10px;
            padding: 10px;
            text-align: center;
            width: 200px;
        }
        .movie-card img {
            border-radius: 10px;
            height: 300px;
            width: 200px;
        }
         .rent-button {
            background-color: red;
            border: none;
            border-radius: 5px;
            color: white;
            cursor: pointer;
            font-size: 16px;
            margin-top: 10px;
            padding: 10px 20px;
        }
         .user-info {
            position: absolute;
            top: 0px;
            right: 20px;
            color: white;
            font-size: 16px;
        }
        .search-form {
    display: flex; /* Use flexbox for layout */
    justify-content: flex-end; /* Align items to the right */
}

.search-button {
    padding: 10px 20px; /* Padding for button */
    background-color: #007bff; /* Primary color (blue) */
    color: #fff; /* Text color */
    border: none; /* Remove default border */
    border-radius: 5px; /* Rounded corners */
    cursor: pointer; /* Pointer cursor on hover */
    font-size: 14px; /* Font size */
    transition: background-color 0.3s ease; /* Smooth transition for background color */
}

.search-button:hover {
    background-color: #0056b3; /* Darker shade of blue on hover */
}
        
    </style>
</head>
<body>
    <h1>All Movies</h1>
    <div class="user-info" id="userInfo">
    Logged in as: <span id="username">Loading...</span>
    <form action="SearchMovies.jsp" method="GET" class="search-form">
    <button type="submit" class="search-button">Search For Movie</button>
</form>
    <form action="RentMoviesServlet" method="POST" id="rentForm"> <!-- Changed action to RentMoviesServlet and method to POST -->
    <div class="movie-container">
        <% 
            List<Movies> movies = (List<Movies>) request.getAttribute("movies");
            if (movies != null && !movies.isEmpty()) {
                for (Movies movie : movies) {
        %>
        <div class="movie-card">
            <img src="image?id=<%= movie.getId() %>" alt="<%= movie.getTitle() %>">
            <h3><%= movie.getTitle() %></h3>
            <p><%= movie.getGenre() %></p>
            <input type="hidden" name="id" value="<%= movie.getId() %>"> <!-- Hidden input to pass movie id -->
            <input type="hidden" name="title" value="<%= movie.getTitle() %>"> <!-- Hidden input to pass movie title -->
            <button type="submit" class="rent-button">Rent</button> <!-- Submit button to rent the movie -->
        </div>
        <% 
                }
            } else {
        %>
        <p>No movies found.</p>
        <% } %>
    </div>
</form>
    <script>
    window.addEventListener('DOMContentLoaded', (event) => {
        fetch('login', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(data => {
            const usernameElement = document.getElementById('username');
            if (data.username) {
                usernameElement.innerText = data.username;
                if (data.role === 'admin') {
                    document.querySelectorAll('.admin-actions').forEach(elem => {
                        elem.style.display = 'flex';
                    });
                }
            } else {
                usernameElement.innerText = 'Guest';
            }
        })
      
        .catch(error => {
            console.error('Error:', error);
            document.getElementById('username').innerText = 'Error loading username';
        });
    });
    
    document.getElementById("rentForm").addEventListener("submit", function(event) {
        event.preventDefault(); // Prevent the default form submission

        // Perform any necessary form validation or processing here

        // Redirect to rentamovie.html
        window.location.href = "RentAMovie.html";
    });
</script>
</body>
</html>
