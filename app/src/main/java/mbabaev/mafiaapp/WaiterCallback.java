package mbabaev.mafiaapp;

import android.content.Context;

import org.json.JSONObject;

public interface WaiterCallback {
    void waitingEnd(JSONObject response);
}
