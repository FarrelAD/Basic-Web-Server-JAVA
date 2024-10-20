package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import model.Users;

public class RootController {
    private Users data;

    public RootController(Users data) {
        this.data = data;
    }

    public void handleGetRequest(Socket clientSocket) throws IOException {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            // HTTP response header
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: text/html");
            out.println();

            // HTTP response body
            out.println(
                """
                <html>
    
                <head>
                    <title>Root Page</title>
                </head>
    
                <body>
                    <h1>Welcome! You successfully get response from server</h1>
                    <p>Current user: """+data.getArrayLength()+"""
                    </p>
                    
                    <br>
    
                    <div>
                        <h1>Send user data to server!</h1>
                        <form action="/users" method="post">
                            <input type="text" name="name" id="name-input" placeholder="Input name"> 
                            <br>
                            <input type="text" name="job" id="job-input" placeholder="Input job">
                            <br>
                            <button type="submit">Submit</button>
                        </form>
                    </div>
                    
                    <div>
                        <h1>Search user data!</h1>
                        <form action="/search" method="get">
                            <input type="text" name="name" id="search-box-name" placeholder="Name">
                            <br>
                            <input type="text" name="job" id="search-box-job" placeholder="Job">
                            <br>
                            <button type="submit">Search</button>
                        </form>
                        <p><i>*) Empty search box will get all user data</i></p>
                    </div>
                </body>
    
                </html>
                """);
        }
    }
}