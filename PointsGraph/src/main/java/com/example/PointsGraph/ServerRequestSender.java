package com.example.PointsGraph;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Makarov A D
 */
public class ServerRequestSender {

    private URL url;
    private HttpsURLConnection conn;
    private String http_url;

    /**
     * @param url        адрес для доступа
     * @param trustStore путь, где расположены сертификаты. Например: "C:\Program Files\Java\jre6\lib\security\cacerts"
     * @author Makarov A D
     */
    public ServerRequestSender(String url, String trustStore) {
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
    public ServerRequestSender(String url, boolean valid_sert) throws NoSuchAlgorithmException, KeyManagementException {
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
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        http_url = url;
    }

    /**
     * @param params запрос к удаленному узлу
     * @return InputStream поток с ответом.
     * @author Makarov A D
     */

    public InputStream sendRequest(String params) throws IOException {
        url = new URL(http_url);
        conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setAllowUserInteraction(true);
        Writer out = new OutputStreamWriter(conn.getOutputStream());
        out.write(params);
        out.flush();
        out.close();
        return conn.getInputStream();
    }
}
