package controller;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.User;
import model.Users;

import java.net.Socket;
import java.net.URI;

public class UsersController {
    private Users data;

    public UsersController(Users data) {
        this.data = data;
    }

    public void getAllUsers(Socket clientSocket) {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println();

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
            
            out.println("""
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
        } catch (IOException e) {
            handleErrorResponse(clientSocket, e);
        }
    }

    public void getUserData(Socket clientSocket, String userId) {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            int userIdNumber = -1;
            try {
                userIdNumber = Integer.parseInt(userId);
            } catch (Exception e) {
                handleErrorResponse(clientSocket, e);
            }

            String result = "";
            if (userIdNumber != -1 && userIdNumber <= data.getArrayLength()) {
                result = """
                    <h1>You get data from the server</h1>
                    <p>ID: """+userIdNumber+"""
                    </p>
                    <p>Data:
                        <ul>
                            <li>Name: """+data.getUserDataByIndex(userIdNumber - 1).getName()+"""
                            </li>
                            <li>Job: """+data.getUserDataByIndex(userIdNumber - 1).getJob()+"""
                            </li>
                        </ul>
                    </p>
                """;
            } else {
                result = "<h1>Data can not be found!</h1>";
            }

            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println();
            out.println("""
                <html>
                    <head>
                        <title>User Data</title>
                    </head>
                    <body>
                    """+ result +"""
                    </body>
                </html>
                """);
        } catch (IOException e) {
            handleErrorResponse(clientSocket, e);
        }
    }

    public void getUserDataByQuery(Socket clientSocket, String requestLine) {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            String[] arrayRequestLine = requestLine.split(" ");
            URI uri = new URI(arrayRequestLine[1]);

            Map<String, String> queryParams = extractQueryParams(uri.getQuery());

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

            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println();
            out.println("""
                <h1>Search result!</h1>
                """+dataHTMLContent+"""
            """);
        } catch (Exception e) {
            handleErrorResponse(clientSocket, e);
        }
    }

    public void postUserData(Socket clientSocket, BufferedReader inParams) {
        try (BufferedReader in = inParams;
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            String inputLine;
            int contentLength = 0;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.isEmpty()) {
                    break;
                }

                if (inputLine.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(inputLine.split(":")[1].trim());
                }
            }

            String requestBody = null;
            if (contentLength > 0) {
                char[] body = new char[contentLength];
                in.read(body, 0, contentLength);
                requestBody = new String(body);
            }

            String responseBody = "";
            String newName = extractQueryParams(requestBody).get("name");
            String newJob = extractQueryParams(requestBody).get("job");

            data.addNewData(new User(newName, newJob));
            
            responseBody = """
                <h1>Data successfully submitted to server</h1>
                <a href="/">
                    <button type="submit" >Submit data again</button>
                </a>
            """;
            
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println();
            out.println(responseBody);
        } catch (IOException e) {
            handleErrorResponse(clientSocket, e);
        }
    }

    private void handleErrorResponse(Socket clientSocket, Exception e) {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println("HTTP/1.1 500 Internal Server Error");
            out.println("Content-Type: text/html");
            out.println();
            out.println("Error occured: " + e.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Map<String, String> extractQueryParams(String query) {
        Map<String, String> queryParams = new HashMap<>();
        String[] pairs = query.split("&");

        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                queryParams.put(keyValue[0], keyValue[1]);
            } else if (keyValue.length == 1) {
                queryParams.put(keyValue[0], "");
            }
        }
        
        return queryParams;
    }
}