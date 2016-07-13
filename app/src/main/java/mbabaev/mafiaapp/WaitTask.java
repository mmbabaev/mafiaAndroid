package mbabaev.mafiaapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mbabaev.mafiaapp.model.MafiaApi;

public class WaitTask extends AsyncTask<ProgressDialog, Void, Void> {
    String method;
    Context context;
    RequestQueue queue;
    WaiterCallback callback;

    final List<JSONObject> responseContainer = new ArrayList<>();

    public WaitTask(String method, Context context, RequestQueue queue, WaiterCallback callback) {
        this.method = method;
        this.context = context;
        this.queue = queue;
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(ProgressDialog... dialogs) {

        try {
            final ProgressDialog dialog = dialogs[0];
            final SharedPreferences prefs = Utils.prefs(context);

            Map<String, String> params = new HashMap<>();
            params.put("join_id", Integer.toString(prefs.getInt("join_id", 0)));
            prefs.edit().putBoolean(method, true).commit();

            while (prefs.getBoolean(method, true)) {
                String url = MafiaApi.makeUrl(method, params);

                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response:", response.toString());
                        if (MafiaApi.isSuccess(response)) {
                            responseContainer.add(response);
                            dialog.setIndeterminate(false);
                            dialog.dismiss();
                            prefs.edit().putBoolean(method, false).commit();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", error.getLocalizedMessage());
                        Utils.showError(context);
                    }
                });

                queue.add(jsObjRequest);
                Log.d("wait: ", method);
                Thread.sleep(1000);
            }
        }
        catch (Exception ex) {
            Utils.showError(context);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        callback.waitingEnd(responseContainer.get(0));
    }
}