import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import controller.RootController;
import controller.UsersController;
import data.DummyData;

public class Main {
    private static RootController rootController;
    private static UsersController usersController;
    private static DummyData data;
    
    public static void main(String[] args) throws Exception {
        data = new DummyData();
        rootController = new RootController();
        usersController = new UsersController(data);

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
                    if (inputLine.startsWith("GET / ")) {
                        rootController.handleGetRequest(clientSocket);
                    } else if (inputLine.startsWith("GET /users ")) {
                        usersController.getAllUsers(clientSocket);
                    } else if (inputLine.startsWith("GET /users/")) {
                        String userId = null;
                        String toFind = "GET /users/";
                        int startIndex = inputLine.indexOf(toFind);

                        if (startIndex != -1) {
                            String remainingString = inputLine.substring(startIndex + toFind.length());
                            int endIndex = remainingString.indexOf(" ");
                            userId = (endIndex != -1) ? remainingString.substring(0, endIndex) : remainingString;
                        }
                        usersController.getUserData(clientSocket, userId);
                    } else if (inputLine.startsWith("GET /search?")) {
                        usersController.getUserDataByQuery(clientSocket, inputLine);
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
