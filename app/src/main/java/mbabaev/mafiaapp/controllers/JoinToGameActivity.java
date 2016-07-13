package mbabaev.mafiaapp.controllers;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import mbabaev.mafiaapp.WaitTask;
import mbabaev.mafiaapp.WaiterCallback;
import mbabaev.mafiaapp.model.MafiaApi;

public class JoinToGameActivity extends AppCompatActivity implements WaiterCallback {
    EditText codeText;
    Button joinButton;
    RequestQueue queue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_game);
        joinButton = (Button) findViewById(R.id.join_button);
        codeText = (EditText) findViewById(R.id.code_edit_text);

        queue = Volley.newRequestQueue(this);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String joinId = codeText.getText().toString();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("join_id", Integer.parseInt(joinId));
                editor.commit();

                Map<String, String> params = new HashMap<>();
                params.put("join_id", joinId);
                String userId = Integer.toString(prefs.getInt("user_id", 0));
                Log.d("userId2", userId);
                params.put("user_id", userId);

                String url = MafiaApi.makeUrl("join_user_to_game", params);
                Log.d("url", url);
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (MafiaApi.isSuccess(response)) {
                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("join_id", Integer.parseInt(joinId));
                            editor.apply();

                            ProgressDialog dialog = ProgressDialog.show(JoinToGameActivity.this, "",
                                    "Ждем начала игры...", true);
                            WaitTask waitTask = new WaitTask("check_game_status", JoinToGameActivity.this, queue, JoinToGameActivity.this);
                            waitTask.execute(dialog);
                        } else {
                            Utils.showError(JoinToGameActivity.this);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.showError(JoinToGameActivity.this);
                    }
                });

                queue.add(jsObjRequest);
            }
        });
    }

    @Override
    public void waitingEnd(JSONObject response) {
        Intent intent = new Intent(this, DayIntroductionActivity.class);
        startActivity(intent);
    }
}