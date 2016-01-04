package com.tcolligan.ecdhtest;

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

    public static native SharedSecret retrieveSharedSecret(byte[] remotePublicKey, byte[] myPrivateKey);

    public static native void test();
}
