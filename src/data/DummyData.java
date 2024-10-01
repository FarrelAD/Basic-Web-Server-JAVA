package data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class DummyData {
    private ArrayList<LinkedHashMap<String, String>> data;

    public DummyData() {
        data = new ArrayList<>();

        data.add(new LinkedHashMap<String, String>() {{
            put("name", "Alex");
            put("job", "Data Analyst");
        }});
        data.add(new LinkedHashMap<String, String>() {{
            put("name", "Budi");
            put("job", "Frontend Web Developer");
        }});
        data.add(new LinkedHashMap<String, String>() {{
            put("name", "Cahya");
            put("job", "Computer Vision Engineer");
        }});
        data.add(new LinkedHashMap<String, String>() {{
            put("name", "Dono");
            put("job", "Data Analyst");
        }});
        data.add(new LinkedHashMap<String, String>() {{
            put("name", "Cakra");
            put("job", "Frontend Web Developer");
        }});
        data.add(new LinkedHashMap<String, String>() {{
            put("name", "Zara");
            put("job", "Frontend Web Developer");
        }});
        data.add(new LinkedHashMap<String, String>() {{
            put("name", "Jojo");
            put("job", "Machine Learning Engineer");
        }});
        data.add(new LinkedHashMap<String, String>() {{
            put("name", "Pepe");
            put("job", "Data Analyst");
        }});
    }

    public ArrayList<LinkedHashMap<String, String>> getAllData() {
        return data;
    }

    public LinkedHashMap<String, String> getUserDataByIndex(int index) {
        return data.get(index);
    }

    public ArrayList<LinkedHashMap<String, String>> getUserDataByQuery(Map<String, String> queryParams) {
        String nameWanted = queryParams.get("name");
        String jobWanted = queryParams.get("job");

        ArrayList<LinkedHashMap<String, String>> result = new ArrayList<>();

        if (!nameWanted.equals("") && !jobWanted.equals("")) {
            for (LinkedHashMap<String, String> datum : data) {
                if (datum.get("name").equalsIgnoreCase(nameWanted) &&
                    datum.get("job").equalsIgnoreCase(jobWanted)) {
                    result.add(datum);
                }
            }
        } else if (!nameWanted.equals("")) {
            for (LinkedHashMap<String,String> datum : data) {
                if (datum.get("name").equalsIgnoreCase(nameWanted)) {
                    result.add(datum);
                }
            }
        } else if (!jobWanted.equals("")) {
            for (LinkedHashMap<String,String> datum : data) {
                if (datum.get("job").equalsIgnoreCase(jobWanted)) {
                    result.add(datum);
                }
            }
        } else {
            result = getAllData();
        }

        return result;
    }

    public void addNewData(ArrayList<LinkedHashMap<String, String>> data) {
        data.add(null);
    }
}
