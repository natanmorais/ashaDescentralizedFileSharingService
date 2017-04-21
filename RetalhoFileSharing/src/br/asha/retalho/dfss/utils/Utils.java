package br.asha.retalho.dfss.utils;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

public class Utils
{
    //Endereço para obter IP atraves do WebService Ipify.
    private static final String IPIFY_URL = "https://api.ipify.org?format=json";

    /**
     * Obtém o endereço ip global.
     */
    public static String ipify()
    {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(IPIFY_URL)
                .build();

        try
        {
            Response response = client.newCall(request).execute();
            return new JSONObject(response.body().string()).getString("ip");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
