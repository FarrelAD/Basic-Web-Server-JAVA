package controller;

import java.io.PrintWriter;
import java.io.IOException;
import java.util.Map;
import java.net.Socket;

import data.DummyData;

public class UsersController {
    public void getAllUsers(Socket clientSocket, DummyData data) {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println();

            String result = "<ol>";
            for (int i = 0; i < data.getData().size(); i++) {
                result += "<li>";
                for (Map.Entry<String, String> entry : data.getData().get(i).entrySet()) {
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
            e.printStackTrace();
        }
    }
}