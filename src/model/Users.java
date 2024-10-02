package model;

import java.util.ArrayList;
import java.util.Map;

public class Users {
    private ArrayList<User> data;

    public Users() {
        data = new ArrayList<>();

        data.add(new User("Alex", "Data Analyst"));
        data.add(new User("Budi", "Frontend Web Developer"));
        data.add(new User("Cahya", "Computer Vision Engineer"));
        data.add(new User("Dono", "Data Analyst"));
        data.add(new User("Cakra", "Frontend Web Developer"));
        data.add(new User("Zara", "Frontend Web Developer"));
        data.add(new User("Jojo", "Machine Learning Engineer"));
        data.add(new User("Pepe", "Data Analyst"));    
    }

    public ArrayList<User> getAllData() {
        return data;
    }

    public int getArrayLength() {
        return data.size();
    }

    public User getUserDataByIndex(int index) {
        return data.get(index);
    }

    public ArrayList<User> getUserDataByQuery(Map<String, String> queryParams) {
        String nameWanted = queryParams.get("name");
        String jobWanted = queryParams.get("job");

        ArrayList<User> result = new ArrayList<>();

        if (!nameWanted.equals("") && !jobWanted.equals("")) {
            for (User datum : data) {
                if (datum.getName().equalsIgnoreCase(nameWanted) &&
                    datum.getJob().equalsIgnoreCase(jobWanted)) {
                    result.add(datum);
                }
            }
        } else if (!nameWanted.equals("")) {
            for (User datum : data) {
                if (datum.getName().equalsIgnoreCase(nameWanted)) {
                    result.add(datum);
                }
            }
        } else if (!jobWanted.equals("")) {
            for (User datum : data) {
                if (datum.getJob().equalsIgnoreCase(jobWanted)) {
                    result.add(datum);
                }
            }
        } else {
            result = getAllData();
        }

        return result;
    }

    public void addNewData(User newData) {
        data.add(newData);
    }
}