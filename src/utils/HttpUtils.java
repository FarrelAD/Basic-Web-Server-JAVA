package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    /**
     * Sends a 404 Not Found response to the client.
     * 
     * @param clientSocket the socket connection to the client
     * @throws IOException if an I/O error occurs while sending the response
     */
    public static void handle404ErrorResponse(Socket clientSocket) throws IOException {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println("HTTP/1.1 404 Not Found");
            out.println("Content-Type: text/html");
            out.println();
            out.println("<h1>404 Not Found!</h1>");
        }
    }

    /**
     * Sends a 500 Internal Server Error response to the client.
     * 
     * @param clientSocket the socket connection to the client
     * @param e the exception that occurred, which will be included in the response message
     */
    public static void handleServerErrorResponse(Socket clientSocket, Exception e) {
        try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            out.println("HTTP/1.1 500 Internal Server Error");
            out.println("Content-Type: text/html");
            out.println();
            out.println("Error occured: " + e.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Retrieves the entire HTTP request body from the given BufferedReader.
     * 
     * @param in the BufferedReader from which the HTTP request body is read
     * @return a String containing the entire request body
     */
    public static String getRequestBody(BufferedReader in) throws IOException {
        String requestBody = null;
        int contentLength = getContentLength(in);

        // Read the body content if not empty
        if (contentLength > 0) {
            char[] body = new char[contentLength];
            try {
                in.read(body, 0, contentLength);
            } catch (IOException e) {
                e.printStackTrace();
            }
            requestBody = new String(body);
        }

        return requestBody;
    }

    private static int getContentLength(BufferedReader in) throws IOException {
        String inputLine;
        int contentLength = 0;
        while ((inputLine = in.readLine()) != null && !inputLine.isEmpty()) {
            if (inputLine.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(inputLine.split(":")[1].trim());
            }
        }

        return contentLength;
    }
    
    /**
     * Extracts raw query parameters from a URL string and returns them as a map.
     * 
     * @param queryParam the URL query parameter string (e.g., "key1=value1&key2=value2")
     * @return a Map containing the extracted query parameters, where each key is a parameter name
     *          and the corresponding value is the parameter value
     */
    public  static Map<String, String> extractQueryParams(String queryParam) {
        Map<String, String> result = new HashMap<>();
        String[] pairs = queryParam.split("&");

        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                result.put(keyValue[0], keyValue[1]);
            } else if (keyValue.length == 1) {
                result.put(keyValue[0], "");
            }
        }
        
        return result;
    }

}
