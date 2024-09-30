import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import controller.UsersController;

public class Main {
    private static UsersController usersController;
    public static void main(String[] args) throws Exception {
        usersController = new UsersController();

        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            System.out.println("Server is running on http://localhost:" + serverSocket.getLocalPort());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                handleRequest(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            String inputLine;
            int inputLineNumber = 0;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.isEmpty()) {
                    break;
                }

                inputLineNumber++;
                if (inputLineNumber == 1) {
                    if (inputLine.startsWith("GET /users")) {
                        usersController.getAllUsers(clientSocket);
                    } else if (inputLine.startsWith("GET / ")) {
                        out.println("HTTP/1.1 200 OK");
                        out.println("Content-Type: text/html");
                        out.println();
                        out.println("""
                            <html>
                                <head>
                                    <title>Users Page</title>
                                </head>
                                <body>
                                    <h1>Welcome! You successfully get response from server</h1>
                                </body>
                            </html>
                        """);
                    } else {
                        out.println("HTTP/1.1 404 Not Found");
                        out.println("Content-Type: text/html");
                        out.println();
                        out.println("<h1>404 Not Found!</h1>");
                    }

                    System.out.println("Response success !\n\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
