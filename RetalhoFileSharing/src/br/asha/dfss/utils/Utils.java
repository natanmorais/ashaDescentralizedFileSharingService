package br.asha.dfss.utils;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Utils {
    private static final String IPIFY_URL = "http://ipinfo.io/ip";

    public static String ipify() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(15, TimeUnit.SECONDS);
        client.setReadTimeout(15, TimeUnit.SECONDS);
        client.setWriteTimeout(15, TimeUnit.SECONDS);

        Request request = new Request.Builder()
                .url(IPIFY_URL)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String ip = response.body().string();
            System.out.printf(ip);
            return ip.trim();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String generateSHA1ForFile(File file)
            throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        InputStream fis = new FileInputStream(file);
        int n = 0;
        byte[] buffer = new byte[8192];
        StringBuilder sb = new StringBuilder();

        digest.update(file.getName().getBytes());

        while (n != -1) {
            n = fis.read(buffer);
            if (n > 0) {
                digest.update(buffer, 0, n);
            }
        }

        for (byte b : digest.digest()) {
            sb.append(String.format("%02X", b));
        }

        return sb.toString();
    }

    public static void log(String str, Object... args) {
        System.out.print(Thread.currentThread());
        System.out.print(": ");
        System.out.printf(str, args);
        System.out.println();
    }

    public static String getIpFromUID(String uid) {
        return uid.split("\\+")[0];
    }
}
