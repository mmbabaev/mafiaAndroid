package mbabaev.mafiaapp.controllers;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import mbabaev.mafiaapp.R;
import mbabaev.mafiaapp.Utils;
import mbabaev.mafiaapp.adapters.MafiaUserAdapter;
import mbabaev.mafiaapp.model.MafiaApi;

public class DayActivity extends AppCompatActivity {
    ListView voteList;
    ImageButton roleButton;
    RequestQueue queue;
    JSONObject voteJson;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.day);

        queue = Volley.newRequestQueue(this);
        roleButton = (ImageButton) findViewById(R.id.role_button_day);
        voteList = (ListView) findViewById(R.id.vote_list);


    }

    public void loadVoteList() {
        Map<String, String> params = new HashMap<>();
        params.put("join_id", Utils.getJoinId(this));
        params.put("user_id", Utils.getUserId(this));
        params.put("is_mafia_list", "false");

        String url = MafiaApi.makeUrl("get_vote_list", params);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    voteJson = response;

                }
                catch (Exception ex) {
                    Utils.showError(DayActivity.this);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showError(DayActivity.this);
            }
        });

        queue.add(jsObjRequest);
    }
}
