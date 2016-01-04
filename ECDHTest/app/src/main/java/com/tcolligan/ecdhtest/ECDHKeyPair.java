package com.tcolligan.ecdhtest;

import android.util.Base64;

import java.nio.ByteBuffer;

/**
 * A goTenna public private key pair used for ECDH Encryption.
 *
 * Created on 12/17/15
 *
 * @author ThomasColligan
 */
public class ECDHKeyPair
{
    public byte[] publicKey;
    public byte[] privateKey;

    public ECDHKeyPair(ByteBuffer publicKeyBuffer, ByteBuffer privateKeyBuffer)
    {
        publicKey = new byte[publicKeyBuffer.remaining()];
        privateKey = new byte[privateKeyBuffer.remaining()];

        publicKeyBuffer.get(publicKey);
        privateKeyBuffer.get(privateKey);
    }

    @Override
    public String toString()
    {
        return String.format("Public Key: %s Private Key: %s",
                Base64.encodeToString(publicKey, Base64.DEFAULT),
                Base64.encodeToString(privateKey, Base64.DEFAULT));
    }
}
