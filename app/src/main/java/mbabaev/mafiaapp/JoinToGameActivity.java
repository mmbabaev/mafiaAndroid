package mbabaev.mafiaapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

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
                        }
                        else {
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
        // идем в следующее активити знакомство день

//        Intent intent = new Intent(this, JoinToGameActivity.class);
//        startActivity(intent);
    }

    //    class WaitTask extends AsyncTask<ProgressDialog, Void, Void> {
//        Map<String, String> params = new HashMap<>();
//        String method;
//
//
//
//        @Override
//        protected Void doInBackground(ProgressDialog... dialogs) {
//            try {
//                final ProgressDialog dialog = dialogs[0];
//                Map<String, String> params = new HashMap<>();
//                final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(JoinToGameActivity.this);
//                String joinId = Integer.toString(prefs.getInt("join_id", 0));
//                params.put("join_id", joinId);
//
//                prefs.edit().putBoolean("wait_start_game", true).commit();
//
//                while (prefs.getBoolean("wait_start_game", true)) {
//                    String url = MafiaApi.makeUrl("check_game_status", params);
//
//                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            Log.d("response:", response.toString());
//                            if (MafiaApi.isSuccess(response)) {
//                                dialog.setIndeterminate(false);
//                                dialog.dismiss();
//                                prefs.edit().putBoolean("wait_start_game", false).commit();
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.e("error", error.getLocalizedMessage());
//                            Utils.showError(JoinToGameActivity.this);
//                        }
//                    });
//
//                    JoinToGameActivity.this.queue.add(jsObjRequest);
//                    Log.d("0", "wait game");
//                    Thread.sleep(1000);
//                }
//            }
//            catch (Exception ex) {
//                Utils.showError(JoinToGameActivity.this);
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            Toast.makeText(JoinToGameActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
//        }
//
//        public void sendWaitMessage(final ProgressDialog dialog, Map<String, String> params) {
//
//
//        }
//    }
}
