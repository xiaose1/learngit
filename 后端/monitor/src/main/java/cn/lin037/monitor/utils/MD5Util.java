package cn.lin037.monitor.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    private static final String salt = "lin037";

    /**
     * 对字符串进行md5加密
     * @param str 待加密字符串
     * @return 加密后字符串
     */
    public static String transformMD5(String str){

        StringBuilder stringBuilder = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //加盐
            str += salt;
            byte[] strBytes = str.getBytes();
            byte[] hashBytes = md5.digest(strBytes);

            stringBuilder = new StringBuilder();
            for (byte b : hashBytes){
                stringBuilder.append(String.format("%02x", b & 0xff));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return stringBuilder.toString();
    }

    /**
     * 判断字符串是否与md5加密后的字符串一致
     * @param str 欲判断字符串
     * @param strMD5 加密后字符串
     * @return true为一致，false为不一致
     */
    public static boolean compareMD5(String str, String strMD5){

        String transformed = transformMD5(str);

        if (null != strMD5){
            return strMD5.equals(transformed);
        }

        return false;
    }


}
