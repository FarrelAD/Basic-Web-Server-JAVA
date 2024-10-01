package controller;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.Map;
import java.net.Socket;

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
                            <li>Name: """+data.getSingleUserData(userIdNumber - 1).get("name")+"""
                            </li>
                            <li>Job: """+data.getSingleUserData(userIdNumber - 1).get("job")+"""
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
}