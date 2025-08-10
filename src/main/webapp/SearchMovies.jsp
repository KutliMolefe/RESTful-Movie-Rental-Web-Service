<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Movies</title>
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
        form {
            text-align: center;
            margin-bottom: 20px;
        }
        input[type="text"] {
            padding: 10px;
            font-size: 16px;
        }
        button[type="submit"] {
            background-color: #333;
            color: white;
            border: none;
            padding: 10px 20px;
            font-size: 16px;
            cursor: pointer;
        }
        .movie-container {
            display: flex;
            flex-wrap: wrap;
            justify-content: center;
            margin-top: 20px;
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
         .user-info {
            position: absolute;
            top: 0px;
            right: 20px;
            color: white;
            font-size: 16px;
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
    </style>
    <script>
        function searchMovies() {
            var title = document.getElementById("title").value;

            if (title.trim() !== "") {
                var xhr = new XMLHttpRequest();
                xhr.open("GET", "SearchMoviesServlet?title=" + title, true);
                xhr.onreadystatechange = function () {
                    if (xhr.readyState === 4 && xhr.status === 200) {
                        var response = xhr.responseText;
                        document.getElementById("movieResults").innerHTML = response;
                    }
                };
                xhr.send();
            } else {
                alert("Please enter a movie title to search.");
            }
        }
     
        // Fetch the username when the page loads
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
                } else {
                    usernameElement.innerText = 'Guest';
                }
            })
            .catch(error => {
                console.error('Error:', error);
                document.getElementById('username').innerText = 'Error loading username';
            });
        });
   
    </script>
</head>
<body>
    <h1>Search Movies by Title</h1>
    <div class="user-info" id="userInfo">
    Logged in as: <span id="username">Loading...</span>
    </div>
    <form>
        <label for="title">Enter Movie Title:</label>
        <input type="text" id="title" name="title">
        <button type="button" onclick="searchMovies()">Search</button>
    </form>
    
    <div id="movieResults" class="movie-container">
        <!-- Search results will be displayed here -->
    </div>
</body>
</html>

