package com.tcolligan.ecdhtest;

import java.nio.ByteBuffer;

/**
 * A class used to access the native cryptopp NDK C++ ECDH
 * library functionality.
 *
 * Created on 12/18/15
 *
 * @author ThomasColligan
 */
public class ECDHLibrary
{
    static
    {
        System.loadLibrary("stlport_shared");
        System.loadLibrary("cryptopp");
        System.loadLibrary("ecdhlibrary");
    }

    public static native ECDHKeyPair generateKeyPair();

    private static native ByteBuffer retrieveSharedSecret(byte[] remotePublicKey, byte[] myPrivateKey);

    public static byte[] getSharedSecret(byte[] remotePublicKey, byte[] myPrivateKey)
    {
        ByteBuffer sharedSecretByteBuffer = retrieveSharedSecret(remotePublicKey, myPrivateKey);
        byte[] sharedSecretBytes = new byte[sharedSecretByteBuffer.remaining()];
        sharedSecretByteBuffer.get(sharedSecretBytes );

        return sharedSecretBytes;
    }

    public static native void test();
}
