package controller;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import utils.HttpUtils;
import utils.UserUtils;
import model.User;
import model.Users;

public class UsersController {
    private Users data;

    public UsersController(Users data) {
        this.data = data;
    }

    public void getAllUsers(Socket clientSocket) throws IOException {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
    
            ArrayList<User> dataResult = data.getAllData();
            String result = "<ol>";
            for (User datum : dataResult) {
                result += """
                    <li>
                        <p>Name : """+datum.getName()+"""
                        </p>
                        <p>Job : """+datum.getJob()+"""
                        </p>
                    </li>
                """;
            }
            result += "</ol>";


            // HTTP response header
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println();
            
            // HTTP response body
            out.println(
                """
                <html>
                    <head>
                        <title>Welcome!</title>
                    </head>
                    <body>
                        <h1>This is all users data</h1>
                        """+result+"""
                    </body>
                </html>
                """);
        } 
    }

    public void getUserDataById(Socket clientSocket) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            String requestLine = in.readLine();
            int userId = UserUtils.getUserId(requestLine);

            String result = "";
            if (userId != -1 && userId <= data.getArrayLength()) {
                result = """
                    <h1>You get data from the server</h1>
                    <p>ID: """+userId+"""
                    </p>
                    <p>Data:
                        <ul>
                            <li>Name: """+data.getUserDataByIndex(userId - 1).getName()+"""
                            </li>
                            <li>Job: """+data.getUserDataByIndex(userId - 1).getJob()+"""
                            </li>
                        </ul>
                    </p>
    
                    <br><br>
                    <div>
                        <h1>Update this user data!</h1>
                        <form id="form-put">
                            <input type="text" name="new-name" id="new-name-input" placeholder="New name"> 
                            <br>
                            <input type="text" name="new-job" id="new-job-input" placeholder="New job">
                            <br>
                            <button type="submit">Submit</button>
                        </form>
                        <br><br>
                        <div>
                            <h1>Delete this user data!</h1>
                            <form id="form-delete">
                                <button type="submit">Submit</button>
                            </form>
                        </div>
                    </div>
    
                    <script>
                        const newNameInput = document.getElementById('new-name-input');
                        const newJobInput = document.getElementById('new-job-input');
                        const formPut = document.getElementById('form-put');
    
                        formPut.addEventListener('submit', function(event) {
                            event.preventDefault();
    
                            fetch('http://localhost:8000/users/"""+userId+"""
                            ', {
                                method: 'PATCH', 
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                body: JSON.stringify({
                                    name: newNameInput.value,
                                    job: newJobInput.value
                                })
                            })
                            .then(response => {
                                if (!response.ok) {
                                    throw new Error('Network response was not ok ' + response.statusText);
                                }
                                return response;
                            })
                            .then(data => {
                                alert('Data has been successfully updated.');
                                location.reload();
                            })
                            .catch(error => console.error('Error:', error));
                        });
                    </script>
                """;
            } else {
                result = "<h1>Data can not be found!</h1>";
            }
    

            // HTTP response header
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println();

            // HTTP response body
            out.println(
                """
                <html>
                    <head>
                        <title>User Data</title>
                    </head>
                    <body>
                    """+ result +"""
                    </body>
                </html>
                """);
        }
    }

    public void getUserDataByQuery(Socket clientSocket) throws IOException, URISyntaxException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String requestLine = in.readLine();
            String[] arrayRequestLine = requestLine.split(" ");
            URI uri = new URI(arrayRequestLine[1]);

            Map<String, String> queryParams = HttpUtils.extractQueryParams(uri.getQuery());
            ArrayList<User> dataResult = data.getUserDataByQuery(queryParams);

            String dataHTMLContent;
            if (!dataResult.isEmpty()) {
                dataHTMLContent = "<ul>";
                for (User datum : dataResult) {
                    dataHTMLContent += """
                        <li>
                            <p>Name: """+datum.getName()+"""
                            </p>
                            <p>Job: """+datum.getJob()+"""
                            </p>
                        </li>
                    """;
                }
                dataHTMLContent += "</ul>";
            } else {
                dataHTMLContent = "<h2>Data not found!</h2>";
            }
    

            // HTTP response header
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println();

            // HTTP response body
            out.println(
                """
                <h1>Search result!</h1>
                """+dataHTMLContent);
        }
    }

    public void postUserData(Socket clientSocket) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            String requestBody = HttpUtils.getRequestBody(clientSocket);
            String newName = HttpUtils.extractQueryParams(requestBody).get("name");
            String newJob = HttpUtils.extractQueryParams(requestBody).get("job");
            
            data.addNewData(new User(newName, newJob));
            

            // HTTP response header
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println();

            // HTTP response body
            out.println(
                """
                <h1>Data successfully submitted to server</h1>
                <a href="/">
                    <button type="submit" >Submit data again</button>
                </a>
                """);
        }
    }
}