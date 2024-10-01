package data;

import java.util.ArrayList;
import java.util.LinkedHashMap;

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

    public LinkedHashMap<String, String> getSingleUserData(int index) {
        return data.get(index);
    }

    public void addNewData(ArrayList<LinkedHashMap<String, String>> data) {
        data.add(null);
    }
}
