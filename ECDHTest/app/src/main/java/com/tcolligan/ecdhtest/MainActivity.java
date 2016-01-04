package com.tcolligan.ecdhtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    private static final String LOG_TAG = "testjni";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        ECDHLibrary.test();

        // Test for ECDHLibrary
        ECDHKeyPair aliceKeyPair = ECDHLibrary.generateKeyPair();
        ECDHKeyPair bobKeyPair = ECDHLibrary.generateKeyPair();

        log(LOG_TAG, String.format("Alice Key Pair: %s", aliceKeyPair.toString()));
        log(LOG_TAG, String.format("Bob Key Pair: %s", bobKeyPair.toString()));

        SharedSecret sharedSecretA = ECDHLibrary.retrieveSharedSecret(bobKeyPair.publicKey, aliceKeyPair.privateKey);
        SharedSecret sharedSecretB = ECDHLibrary.retrieveSharedSecret(aliceKeyPair.publicKey, bobKeyPair.privateKey);

        log(LOG_TAG, String.format("Secret A: %s", sharedSecretA.toString()));
        log(LOG_TAG, String.format("Secret B: %s", sharedSecretB.toString()));
    }

    private void log(String tag, String message)
    {
        String text = textView.getText().toString();
        text += "\n\n";
        text += message;

        textView.setText(text);
        Log.d(tag, message);
    }
}
