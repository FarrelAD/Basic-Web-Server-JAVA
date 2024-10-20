package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    /**
     * Retrieves the entire HTTP request body from the given BufferedReader.
     * 
     * @param in the BufferedReader from which the HTTP request body is read
     * @return a String containing the entire request body
     */
    public static String getRequestBody(Socket clientSocket) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            
            String requestBody = null;
            int contentLength = getContentLength(clientSocket);

    
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
    }

    private static int getContentLength(Socket clientSocket) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

            String inputLine;
            int contentLength = 0;
            while ((inputLine = in.readLine()) != null && !inputLine.isEmpty()) {
                System.out.println("inputline => " + inputLine);
                if (inputLine.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(inputLine.split(":")[1].trim());
                }
            }

            return contentLength;
        }
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
