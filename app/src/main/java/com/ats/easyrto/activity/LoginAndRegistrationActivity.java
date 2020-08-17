package com.ats.easyrto.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ats.easyrto.R;

public class LoginAndRegistrationActivity extends AppCompatActivity implements View.OnClickListener {
public TextView tvLogin,tvRegistration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_registration);
        tvLogin=(TextView)findViewById(R.id.tvLogin);
        tvRegistration=(TextView)findViewById(R.id.tvRegistration);

        tvLogin.setOnClickListener(this);
        tvRegistration.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tvLogin)
        {
            startActivity(new Intent(LoginAndRegistrationActivity.this, LoginActivity.class));
            finish();

        }else if(v.getId()==R.id.tvRegistration)
        {
            startActivity(new Intent(LoginAndRegistrationActivity.this, RegistrationActivity.class));
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        // onBackPressed();
        startActivity(new Intent(LoginAndRegistrationActivity.this, HomeActivity.class));
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginAndRegistrationActivity.this, HomeActivity.class));
        finish();

    }
}
