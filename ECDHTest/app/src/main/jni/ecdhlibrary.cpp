//
// Created by Thomas Colligan on 1/3/16.
//

#include <cryptopp/osrng.h>
using CryptoPP::AutoSeededRandomPool;
using CryptoPP::AutoSeededX917RNG;

#include <cryptopp/aes.h>
using CryptoPP::AES;

#include <cryptopp/eccrypto.h>
using CryptoPP::ECP;
using CryptoPP::ECDH;
using CryptoPP::DL_GroupParameters_EC;

#include <cryptopp/secblock.h>
using CryptoPP::SecByteBlock;

#include <cryptopp/oids.h>
using CryptoPP::OID;

// ASN1 is a namespace, not an object
#include <cryptopp/asn.h>
using namespace CryptoPP::ASN1;

#include <cryptopp/integer.h>
using CryptoPP::Integer;

#include <stdexcept>
using std::runtime_error;

#include <iostream>
using std::cout;
using std::cerr;
using std::endl;

#include <sstream>
using std::stringstream;

#include "com_tcolligan_ecdhtest_ECDHLibrary.h"
#include <android/log.h>

#define  LOG_TAG    "testjni"
#define  ALOG(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)


JNIEXPORT jobject JNICALL Java_com_tcolligan_ecdhtest_ECDHLibrary_generateKeyPair
        (JNIEnv *env, jclass)
{
        // Generate a public private key pair using ECDH (Elliptic Curve Diffie Hellman)
        OID CURVE = secp256r1(); // the key is 256 bits (32 bytes) long
        AutoSeededRandomPool rng;

        // Because we are using point compression
        // Private Key 32 bytes
        // Public Key 33 bytes
        // If compression was not used the public key would be 65 bytes long
        ECDH < ECP >::Domain dhA( CURVE );
        dhA.AccessGroupParameters().SetPointCompression(true);

        SecByteBlock privA(dhA.PrivateKeyLength()), pubA(dhA.PublicKeyLength());
        dhA.GenerateKeyPair(rng, privA, pubA);

        jobject publicKeyByteBuffer = (*env).NewDirectByteBuffer(pubA.BytePtr(), pubA.SizeInBytes());
        jobject privateKeyByteBuffer = (*env).NewDirectByteBuffer(privA.BytePtr(), privA.SizeInBytes());

        // Return the ECDH Key Pair back as a Java ECDHKeyPair object
        jclass keyPairClass = (*env).FindClass("com/tcolligan/ecdhtest/ECDHKeyPair");
        jmethodID midConstructor = (*env).GetMethodID(keyPairClass, "<init>", "(Ljava/nio/ByteBuffer;Ljava/nio/ByteBuffer;)V");
        jobject keyPairObject = (*env).NewObject(keyPairClass, midConstructor, publicKeyByteBuffer, privateKeyByteBuffer);

        return keyPairObject;
}

JNIEXPORT jobject JNICALL Java_com_tcolligan_ecdhtest_ECDHLibrary_retrieveSharedSecret
        (JNIEnv *env, jclass, jbyteArray publicKeyArray, jbyteArray privateKeyArray)
{
        // Use the same ECDH Setup that is specified in the generateKeyPair method above
        OID CURVE = secp256r1();
        DL_GroupParameters_EC<ECP> params(CURVE);
        ECDH<ECP>::Domain dhAgreement(params);
        dhAgreement.AccessGroupParameters().SetPointCompression(true);

        // Figure out how big the public and private keys are
        // Public Key: This belongs to the other user
        // Private Key: This is out personal private key
        int pubLen = (int)(*env).GetArrayLength(publicKeyArray);
        int privLen = (int)(*env).GetArrayLength(privateKeyArray);

        // Convert the keys from a jbyteArray to a SecByteBlock so that they can be passed
        // into the CryptoPP Library functions.
        unsigned char* pubData = new unsigned char[pubLen];
        (*env).GetByteArrayRegion(publicKeyArray, 0, pubLen, reinterpret_cast<jbyte*>(pubData));

        unsigned char* privData = new unsigned char[privLen];
        (*env).GetByteArrayRegion(privateKeyArray, 0, privLen, reinterpret_cast<jbyte*>(privData));

        SecByteBlock pubB(pubData, pubLen) , privA(privData, privLen);

        // Now extract shared secret between the two keys
        SecByteBlock sharedSecretByteBlock(dhAgreement.AgreedValueLength());
        ALOG("Shared Agreed Value Length: %d", dhAgreement.AgreedValueLength());

        bool didWork = dhAgreement.Agree(sharedSecretByteBlock, privA, pubB);

        ALOG("Key Agreement: %s", didWork ? "It Worked" : "It Failed");
        ALOG("Shared Secret Byte Size: %d", sharedSecretByteBlock.SizeInBytes());

        // Return the shared secret as a Java ByteBuffer
        jobject publicKeyByteBuffer = (*env).NewDirectByteBuffer(sharedSecretByteBlock.BytePtr(), sharedSecretByteBlock.SizeInBytes());

        return publicKeyByteBuffer;
}

JNIEXPORT void JNICALL Java_com_tcolligan_ecdhtest_ECDHLibrary_test
(JNIEnv *env, jclass)
{
        OID CURVE = secp256r1();
        AutoSeededRandomPool rng;

        ECDH < ECP >::Domain dhA( CURVE ), dhB( CURVE );
        SecByteBlock privA(dhA.PrivateKeyLength()), pubA(dhA.PublicKeyLength());
        SecByteBlock privB(dhB.PrivateKeyLength()), pubB(dhB.PublicKeyLength());

        dhA.GenerateKeyPair(rng, privA, pubA);
        dhB.GenerateKeyPair(rng, privB, pubB);

        if(dhA.AgreedValueLength() != dhB.AgreedValueLength())
        {
                ALOG("Shared shared size mismatch");
                return;
        }

        SecByteBlock sharedA(dhA.AgreedValueLength()), sharedB(dhB.AgreedValueLength());

        if(!dhA.Agree(sharedA, privA, pubB))
        {
                ALOG("Failed to reach shared secret (A)");
                return;
        }

        if(!dhB.Agree(sharedB, privB, pubA))
        {

                ALOG("Failed to reach shared secret (B)");
                return;
        }

        Integer ssa, ssb;

        ssa.Decode(sharedA.BytePtr(), sharedA.SizeInBytes());
        std::stringstream buffer;
        buffer << "(A): " << std::hex << ssa << endl;
        //ALOG("(A) %s", bufffer.str());

        ssb.Decode(sharedB.BytePtr(), sharedB.SizeInBytes());
        cout << "(B): " << std::hex << ssb << endl;

        if(ssa != ssb)
        {
                ALOG("Failed to reach shared secret (C)");
                return;
        }

        ALOG("Agreed to shared secret");
}
