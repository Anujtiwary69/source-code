package co.stutzen.shopzen.constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.RemoteException;
import android.util.Log;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class Connection {
     
	public InputStream getConnection(String stringUrl, String sess){		
		InputStream respStream = null;
        try {
        	HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 15000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 15000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			trustEveryone();
			HttpClient client = new DefaultHttpClient(httpParameters);
			HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

			SchemeRegistry registry = new SchemeRegistry();
			SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
			socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
			registry.register(new Scheme("https", socketFactory, 443));
			SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
			DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
			HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

	        HttpGet request = new HttpGet(stringUrl);
	        HttpResponse con = httpClient.execute(request);
	        con.setHeader("Cache-Control", "max-age=0, no-cache, no-store");
	        respStream = con.getEntity().getContent();
        } catch (Exception e) {
        	e.printStackTrace();
        	Log.i("Connection", "error :"+e.getMessage());
        }
		return respStream;
		
	}
	
	public String sendHttpPost(String url, JSONArray nameValuePairs, String session) throws IOException{
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 15000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		int timeoutSocket = 15000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		//trustAllHosts();
		trustEveryone();
		HttpClient client = new DefaultHttpClient(httpParameters);
		HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

		SchemeRegistry registry = new SchemeRegistry();
		SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
		socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
		registry.register(new Scheme("https", socketFactory, 443));
		SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
		DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

		HttpPost post = new HttpPost(url);
	    post.setHeader("Accept", "application/json");
	    post.setHeader("Content-Type", "application/json");
        post.setHeader("Cache-Control", "max-age=0, no-cache, no-store");
	    String result = null;
	    try {
	      post.setEntity(new StringEntity(nameValuePairs.toString(), "UTF-8"));
	      HttpResponse response = httpClient.execute(post);
	      result = readIt(response.getEntity().getContent());
	    } catch (IOException e) {
        	Log.i("Connection", "error :"+e.getMessage());
	      throw e;
	    }
    	return result;
    }
	
	public String sendHttpPostjson(String url, JSONObject nameValuePairs, String session) throws IOException{
		HttpParams httpParameters = new BasicHttpParams();
		int timeoutConnection = 15000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		int timeoutSocket = 15000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		//trustAllHosts();
		trustEveryone();
		HttpClient client = new DefaultHttpClient(httpParameters);
		HostnameVerifier hostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

		SchemeRegistry registry = new SchemeRegistry();
		SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
		socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
		registry.register(new Scheme("https", socketFactory, 443));
		SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
		DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());
		HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
	    HttpPost post = new HttpPost(url);
	    post.setHeader("Accept", "application/json");
	    post.setHeader("Content-Type", "application/json");

        post.setHeader("Cache-Control", "max-age=0, no-cache, no-store");
	    String result = null;
	    try {
	      post.setEntity(new StringEntity(nameValuePairs.toString(), "UTF-8"));
	      HttpResponse response = httpClient.execute(post);
	      result = readIt(response.getEntity().getContent());
	    } catch (IOException e) {
        	Log.i("Connection", "error :"+e.getMessage());
	      throw e;
	    }
    	return result;
    }

    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        InputStreamReader is = new InputStreamReader(stream);
    	StringBuilder sb=new StringBuilder();
    	BufferedReader br = new BufferedReader(is);
    	String read = br.readLine();
    	while(read != null) {
    	    sb.append(read);
    	    read =br.readLine();

    	}
    	return sb.toString();
    }
    
	public String connStringResponse(String stringUrl, String session) {
        InputStream stream;
        String respString = null;
		try {
			stream = getConnection(stringUrl, session);        
			respString = readIt(stream);
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("Result", "error :" + e.getMessage());	
		}
        return respString;
    }

	public static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain,
										   String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
										   String authType) throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void trustEveryone() {
		try {
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}});
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new X509TrustManager[]{new X509TrustManager(){
				public void checkClientTrusted(X509Certificate[] chain,
											   String authType) throws CertificateException {}
				public void checkServerTrusted(X509Certificate[] chain,
											   String authType) throws CertificateException {}
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}}}, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(
					context.getSocketFactory());
		} catch (Exception e) { // should never happen
			e.printStackTrace();
		}
	}



}

