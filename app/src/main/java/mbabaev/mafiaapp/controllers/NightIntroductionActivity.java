package mbabaev.mafiaapp.controllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

public class NightIntroductionActivity extends AppCompatActivity implements WaiterCallback {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.night_introduction);

        final RequestQueue queue = Volley.newRequestQueue(this);

        Button readyButton = (Button) findViewById(R.id.mafia_introduction_button);
        readyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendFinish();

                ProgressDialog dialog = ProgressDialog.show(NightIntroductionActivity.this, "",
                        "Ждем начала дня", true);

                WaitTask waitTask = new WaitTask("waiting_for_day", NightIntroductionActivity.this, queue, NightIntroductionActivity.this);
                waitTask.execute(dialog);
            }
        });
    }

    public void sendFinish() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("join_id", Utils.getJoinId(NightIntroductionActivity.this));
        String method = "finish_introduction";
        String url = MafiaApi.makeUrl(method, params);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    response.getString("result");
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    Utils.showError(NightIntroductionActivity.this);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.showError(NightIntroductionActivity.this);
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);
    }

    @Override
    public void waitingEnd(JSONObject response) {
        startActivity(new Intent(this, DayActivity.class));
    }
}