package ying.backend_features.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ying on 2017-04-16.
 */
public class CommonJSONUtils {
    /**
     * { "errors": [ { "code": 215, "message": "Bad Authentication data." } ] }
     */
    public static String getErrorJson(Map<String, String> errorMap) {
        StringBuffer json = new StringBuffer("{ \"errors\":  [");

        StringBuffer errorStr = null;
        for (Map.Entry<String, String> entry : errorMap.entrySet()) {
            if (errorStr == null) {
                errorStr = new StringBuffer();
                errorStr.append("{");
                errorStr.append("\"code\":\"").append(entry.getKey()).append("\",");
                errorStr.append("\"message\":\"").append(entry.getValue()).append("\"");
                errorStr.append("}");
            } else {
                errorStr.append(", {");
                errorStr.append("\"code\":\"").append(entry.getKey()).append("\",");
                errorStr.append("\"message\":\"").append(entry.getValue()).append("\"");
                errorStr.append("}");
            }

            json.append(errorStr);
            errorStr = new StringBuffer();
        }

        json.append("]}");
        return json.toString();
    }

    public static String getErrorJson(String code, String message) {
        Map<String, String> errorMap = new HashMap<String, String>();
        errorMap.put(code, message);

        return getErrorJson(errorMap);
    }

    public static String getSuccResponseJSON() {
        return "{\"success\": true} ";
    }
}
