package mbabaev.mafiaapp.controllers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import mbabaev.mafiaapp.R;
import mbabaev.mafiaapp.Utils;
import mbabaev.mafiaapp.WaitTask;
import mbabaev.mafiaapp.WaiterCallback;

public class NightActivity extends AppCompatActivity implements WaiterCallback {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.night);

        RequestQueue queue = Volley.newRequestQueue(this);

        ProgressDialog dialog = ProgressDialog.show(this, "",
                "Ждем пока пройдет ночь", true);
        String method = "waiting_for_day";

        if (Utils.getRole(this).equals("mafia")) {
            method = "waiting_for_mafia_start_voting";

        }
        WaitTask waitTask = new WaitTask(method, this, queue, this);
        waitTask.execute(dialog);
    }

    @Override
    public void waitingEnd(JSONObject response) {
        Intent intent;
        if (Utils.isMafia(this)) {
            if (Utils.dayCount <= 1) {
                intent = new Intent(this, NightIntroductionActivity.class);
            }
            else {
                intent = new Intent(this, MafiaVotingActivity.class);
            }
        }
        else {
            intent = new Intent(this, DayActivity.class);
        }
        startActivity(intent);
    }
}
