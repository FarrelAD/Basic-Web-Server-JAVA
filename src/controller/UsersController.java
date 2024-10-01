package controller;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.net.Socket;
import java.net.URI;

import data.DummyData;

public class UsersController {
    private DummyData data;

    public UsersController(DummyData data) {
        this.data = data;
    }

    public void getAllUsers(Socket clientSocket) {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println();

            String result = "<ol>";
            for (int i = 0; i < data.getAllData().size(); i++) {
                result += "<li>";
                for (Map.Entry<String, String> entry : data.getAllData().get(i).entrySet()) {
                    result += "<p>" + entry.getKey() + ": " + entry.getValue() + "</p>";
                }
                result += "</li>";
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
            if (userIdNumber != -1 && userIdNumber <= data.getAllData().size()) {
                result = """
                    <h1>You get data from the server</h1>
                    <p>ID: """+userIdNumber+"""
                    </p>
                    <p>Data:
                        <ul>
                            <li>Name: """+data.getUserDataByIndex(userIdNumber - 1).get("name")+"""
                            </li>
                            <li>Job: """+data.getUserDataByIndex(userIdNumber - 1).get("job")+"""
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

            ArrayList<LinkedHashMap<String, String>> dataResult = data.getUserDataByQuery(queryParams);
            String dataHTMLContent;
            if (!dataResult.isEmpty()) {
                dataHTMLContent = "<ul>";
                for (LinkedHashMap<String,String> datum : dataResult) {
                    dataHTMLContent += """
                        <li>
                            <p>Name: """+datum.get("name")+"""
                            </p>
                            <p>Job: """+datum.get("job")+"""
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