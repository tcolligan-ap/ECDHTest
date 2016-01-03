package com.tcolligan.ecdhtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    private static final String LOG_TAG = "TEST";
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

        byte[] sharedSecretA = ECDHLibrary.getSharedSecret(bobKeyPair.publicKey, aliceKeyPair.privateKey);
        byte[] sharedSecretB = ECDHLibrary.getSharedSecret(aliceKeyPair.publicKey, bobKeyPair.privateKey);

        String hexA = Utils.bytesToHexString(sharedSecretA);
        String hexB = Utils.bytesToHexString(sharedSecretB);

        log(LOG_TAG, String.format("Secret A: %s", hexA));
        log(LOG_TAG, String.format("Secret B: %s", hexB));
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
