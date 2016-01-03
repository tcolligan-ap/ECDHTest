package com.tcolligan.ecdhtest;

/**
 * Generic helpful Utility methods.
 *
 * Created on 1/03/16.
 *
 * @author Thomas Colligan
 */
public class Utils
{
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHexString(byte[] bytes)
    {
        if (bytes == null)
        {
            return "";
        }

        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++)
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String s)
    {
        int len = s.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2)
        {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }

        return data;
    }

}
