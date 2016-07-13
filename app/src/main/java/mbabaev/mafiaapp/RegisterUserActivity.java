package mbabaev.mafiaapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import mbabaev.mafiaapp.model.MafiaApi;


public class RegisterUserActivity extends AppCompatActivity {

    TextView nameTextView;
    Button startGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);
        final RequestQueue queue = Volley.newRequestQueue(this);

        nameTextView = (TextView) findViewById(R.id.editText);
        startGameButton = (Button) findViewById(R.id.start_game_button);


        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> params = new HashMap<>();
                params.put("name", nameTextView.getText().toString());
                params.put("image_url", "asd");

                String url = MafiaApi.makeUrl("register_user", params);
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int id = response.getInt("user_id");
                            Toast.makeText(RegisterUserActivity.this, Integer.toString(id), Toast.LENGTH_SHORT).show();

                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt(MafiaApi.userId, id);
                            editor.commit();

                            Log.d("userId", Integer.toString(sharedPref.getInt("user_id", 0)));
                            showJoinActivity();
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                            Utils.showError(RegisterUserActivity.this);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Utils.showError(RegisterUserActivity.this);
                    }
                });

                queue.add(jsObjRequest);
            }
        });
    }

    public void showJoinActivity() {
        Intent intent = new Intent(this, JoinToGameActivity.class);
        startActivity(intent);
    }
}
