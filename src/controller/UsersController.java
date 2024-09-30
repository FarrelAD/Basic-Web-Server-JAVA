package controller;

import java.io.PrintWriter;
import java.io.IOException;
import java.net.Socket;

public class UsersController {
    public void getAllUsers(Socket clientSocket) {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println();

            String result = "<ol>";
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