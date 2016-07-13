package mbabaev.mafiaapp.controllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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

public class DayIntroductionActivity extends AppCompatActivity implements WaiterCallback {
    ImageButton roleButton;
    Button nextButton;
    RequestQueue queue;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.day_introduction);

        roleButton = (ImageButton) findViewById(R.id.role_butoon_introduction);
        nextButton = (Button) findViewById(R.id.next_button_introduction);

        roleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DayIntroductionActivity.this, RoleActivity.class);
                startActivityForResult(intent, RESULT_OK);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog dialog = ProgressDialog.show(DayIntroductionActivity.this, "",
                        "Ждем начала ночи", true);
                WaitTask waitTask = new WaitTask("waiting_for_night", DayIntroductionActivity.this, queue, DayIntroductionActivity.this);
                waitTask.execute(dialog);
            }
        });

        roleButton.setEnabled(false);

        RequestQueue queue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<>();
        params.put("join_id", Utils.getJoinId(this));
        params.put("user_id", Utils.getUserId(this));

        String url = MafiaApi.makeUrl("get_role", params);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("role", response.getString("result"));
                    editor.commit();
                    roleButton.setEnabled(true);
                }
                catch (Exception ex) {
                    Utils.showError(DayIntroductionActivity.this);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showError(DayIntroductionActivity.this);
            }
        });

        queue.add(jsObjRequest);
    }

    @Override
    public void waitingEnd(JSONObject response) {
        Toast.makeText(this, "КОНЕЦ ЗНАКОМСТВА", Toast.LENGTH_LONG);
    }
}
