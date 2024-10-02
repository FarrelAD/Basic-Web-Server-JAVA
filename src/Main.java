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
    private static RootController rootController;
    private static UsersController usersController;
    private static Users data;
    
    public static void main(String[] args) throws Exception {
        data = new Users();
        rootController = new RootController(data);
        usersController = new UsersController(data);

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
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            String inputLine = in.readLine();

            // Routing
            if (inputLine.startsWith("GET / ")) {
                rootController.handleGetRequest(clientSocket);
            } else if (inputLine.startsWith("GET /users ")) {
                usersController.getAllUsers(clientSocket);
            } else if (inputLine.startsWith("POST /users ")) {
                usersController.postUserData(clientSocket, in);
            } else if (inputLine.startsWith("GET /users/")) {
                usersController.getUserDataById(clientSocket, inputLine);
            } else if (inputLine.startsWith("GET /search?")) {
                usersController.getUserDataByQuery(clientSocket, inputLine);
            } else {
                out.println("HTTP/1.1 404 Not Found");
                out.println("Content-Type: text/html");
                out.println();
                out.println("<h1>404 Not Found!</h1>");
            }

            System.out.println("Response success !\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
