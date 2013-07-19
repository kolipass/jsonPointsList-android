package com.example.PointsGraph;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Makarov A D
 */
public class SendRequest {

    private URL url;
    private HttpsURLConnection conn;
    private String http_url;

    /**
     * @param url        адрес для доступа
     * @param trustStore путь, где расположены сертификаты. Например: "C:\Program Files\Java\jre6\lib\security\cacerts"
     * @author Makarov A D
     */
    public SendRequest(String url, String trustStore) {
        http_url = url;
        //System.setProperty("https.proxyHost","server");
        //System.setProperty("https.proxyPort","port");
        System.setProperty("javax.net.ssl.trustStore", trustStore);
    }

    /**
     * @param url        - адрес для доступа
     * @param valid_sert - метка. При подключении использовать неподписанный сертификат
     * @author Makarov A D
     */
    public SendRequest(String url, boolean valid_sert) {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }
        http_url = url;
    }

    /**
     * @param xmlData запрос к удаленному узлу
     * @return BufferedReader поток с ответом.
     * @author Makarov A D
     */
    public BufferedReader sendRequest(String xmlData) {
        BufferedReader in = null;
        try {
            url = new URL(http_url);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(true);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(conn.getOutputStream()));
            out.println(xmlData);
            out.flush();
            out.close();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            return in;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public InputStream sendRequestGetInputStream(String xmlData) throws IOException {
            url = new URL(http_url);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(true);
            Writer out =new OutputStreamWriter(conn.getOutputStream());
            out.write(xmlData);
            out.flush();
            out.close();
            return conn.getInputStream();
    }
}
