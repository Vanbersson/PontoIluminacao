package iluminacao.com.pontoiluminacao.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Vambersson on 06/04/2017.
 */

public class NetworkUtil {


    public static boolean verificaConexao(Context ctx){

        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());

    }

    public static String streamToString(InputStream is){

        byte[] buffer = new byte[1024];

        ByteArrayOutputStream bufferzao = new ByteArrayOutputStream();

        int bytesLidos;

        try {

            while((bytesLidos = is.read(buffer)) != -1  ){
                bufferzao.write(buffer,0,bytesLidos);
            }

            return new String(bufferzao.toByteArray(),"UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HttpURLConnection getConexao(String urlBase, String requestMethod , boolean doOutPut) throws Exception {

        URL url = new URL(urlBase);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setReadTimeout(10000);
        conexao.setConnectTimeout(10000);
        conexao.setRequestMethod(requestMethod);
        conexao.setDoInput(true);
        conexao.setDoOutput(doOutPut);

        if(doOutPut){
            conexao.addRequestProperty("Content-Type","application/json");
        }

        conexao.connect();

        return conexao;

    }







}
