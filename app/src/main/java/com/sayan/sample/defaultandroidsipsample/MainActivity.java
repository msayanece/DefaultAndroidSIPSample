package com.sayan.sample.defaultandroidsipsample;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;

public class MainActivity extends AppCompatActivity implements SipRegistrationListener {
    public SipManager mSipManager = null;
    public SipProfile mSipProfile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (mSipManager == null) {
            mSipManager = SipManager.newInstance(this);
        }
        try {
            SipProfile.Builder builder = new SipProfile.Builder("Titir", domain);
            builder.setPassword("1234");
            mSipProfile = builder.build();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent();
        intent.setAction("android.SipDemo.INCOMING_CALL");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, Intent.FILL_IN_DATA);
        try {
            mSipManager.open(mSipProfile, pendingIntent, null);
            mSipManager.setRegistrationListener(mSipProfile.getUriString(), this);
        } catch (SipException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRegistering(String localProfileUri) {
        Toast.makeText(this, "Registering...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegistrationDone(String localProfileUri, long expiryTime) {
        Toast.makeText(this, "Registration successful.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRegistrationFailed(String localProfileUri, int errorCode, String errorMessage) {
        Toast.makeText(this, "Registration failed.", Toast.LENGTH_SHORT).show();
    }

    public void closeLocalProfile() {
        if (mSipManager == null) {
            return;
        }
        try {
            if (mSipProfile != null) {
                mSipManager.close(mSipProfile.getUriString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
