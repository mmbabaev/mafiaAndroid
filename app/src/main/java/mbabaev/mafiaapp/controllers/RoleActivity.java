package mbabaev.mafiaapp.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import mbabaev.mafiaapp.R;
import mbabaev.mafiaapp.Utils;

public class RoleActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.role_card);

        ImageButton button = (ImageButton) findViewById(R.id.role_card_button);
        String role = Utils.getRole(this);
        if (role.equals("mafia")) {
            button.setImageResource(R.drawable.role_mafia);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(0, intent);
                finish();
            }
        });
    }
}
