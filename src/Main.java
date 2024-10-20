import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import controller.RootController;
import controller.UsersController;
import model.Users;

public class Main {
    private static Users data = new Users();
    private static RootController rootController = new RootController(data);
    private static UsersController usersController = new UsersController(data);
    
    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            System.out.println("Server is running on http://localhost:" + serverSocket.getLocalPort());

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    handleRequest(clientSocket);
                } catch (Exception e) {
                    e.printStackTrace();    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String inputLine = in.readLine();

            // client HTTP request line log
            System.out.println(inputLine);

            // Routing
            if (inputLine.startsWith("GET / ")) {
                rootController.handleGetRequest(clientSocket);
            } else if (inputLine.startsWith("GET /users ")) {
                usersController.getAllUsers(clientSocket);
            } else if (inputLine.startsWith("POST /users ")) {
                usersController.postUserData(clientSocket);
            } else if (inputLine.startsWith("GET /users/")) {
                usersController.getUserDataById(clientSocket);
            } else if (inputLine.startsWith("GET /search?")) {
                usersController.getUserDataByQuery(clientSocket);
            } else {
                handle404ErrorResponse(clientSocket);
            }
        } catch (Exception e) {
            handleServerErrorResponse(clientSocket, e);
        }
    }


    private static void handle404ErrorResponse(Socket clientSocket) throws IOException {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println("HTTP/1.1 404 Not Found");
            out.println("Content-Type: text/html");
            out.println();
            out.println("<h1>404 Not Found!</h1>");
        }
    }

    private static void handleServerErrorResponse(Socket clientSocket, Exception e) {
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
