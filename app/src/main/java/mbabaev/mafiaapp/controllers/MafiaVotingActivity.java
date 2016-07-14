package mbabaev.mafiaapp.controllers;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONObject;

import mbabaev.mafiaapp.WaiterCallback;

public class MafiaVotingActivity extends AppCompatActivity implements WaiterCallback {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void waitingEnd(JSONObject response) {

    }
}
