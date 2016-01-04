package com.tcolligan.ecdhtest;

import android.util.Base64;

import java.nio.ByteBuffer;

/**
 * Created on 1/3/16
 *
 * @author ThomasColligan
 */
public class SharedSecret
{
    public byte[] sharedSecret;

    public SharedSecret(ByteBuffer sharedSecretBuffer)
    {
        sharedSecret = new byte[sharedSecretBuffer.remaining()];
        sharedSecretBuffer.get(sharedSecret);
    }

    @Override
    public String toString()
    {
        return String.format("Shared Secret: %s",
                Base64.encodeToString(sharedSecret, Base64.DEFAULT));
    }
}
