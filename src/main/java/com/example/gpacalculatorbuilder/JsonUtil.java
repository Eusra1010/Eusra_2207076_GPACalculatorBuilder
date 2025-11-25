package com.example.gpacalculatorbuilder;

import org.json.JSONObject;

public class JsonUtil {

    public static String toJson(Course c) {
        JSONObject obj = new JSONObject();
        obj.put("name", c.getName());
        obj.put("code", c.getCode());
        obj.put("credit", c.getCredit());
        obj.put("teacher1", c.getTeacher1());
        obj.put("teacher2", c.getTeacher2());
        obj.put("grade", c.getGrade());
        return obj.toString();
    }

    public static Course fromJson(String json) {
        JSONObject obj = new JSONObject();

        // Safely parse the JSON string:
        JSONObject parsed = new JSONObject(json);

        return new Course(
                parsed.getString("name"),
                parsed.getString("code"),
                parsed.getInt("credit"),
                parsed.getString("teacher1"),
                parsed.getString("teacher2"),
                parsed.getString("grade")
        );
    }
}
