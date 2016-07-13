package mbabaev.mafiaapp.model;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Dictionary;
import java.util.Map;

public class MafiaApi {
    static public String url = "http://sammycolt.pythonanywhere.com/api/phone/";
    static public String userId = "user_id";

    static public String makeUrl(String method, Map<String, String> params) {
        String result = url + method + "?";
        for (String key : params.keySet()) {
            result += key;
            result += "=";
            result += params.get(key);
            result += "&";
        }

        return result.substring(0, result.length()-1);
    }

    static public Boolean isSuccess(JSONObject response) {
        try {
            return response.getString("result").equals("success");
        }
        catch(Exception ex) {
            return false;
        }
    }
}