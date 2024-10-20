package utils;

public class UserUtils {

    /**
     * Retrieves the user ID from the specified URL request line.
     * 
     * @param requestLine the HTTP request line containing the URL with the user ID parameter
     * @return the user ID extracted from the request line
     */
    public static int getUserId(String requestLine) {
        String[] arrayRequestLine = requestLine.split(" ");
        int userId = -1;

        try {
            userId = Integer.parseInt(arrayRequestLine[1].substring("/users/".length()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return userId;
    }
}
