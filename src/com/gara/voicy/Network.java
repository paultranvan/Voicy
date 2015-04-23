package com.gara.voicy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

/* Warning : the AsyncTask class can be executed only once.
 * So a new Network object needs to be created each time one wants to
 * execute the task.
 */
public class Network extends AsyncTask<String, Void, String> 
{
	
	public Network()
	{
	}
	
	@Override
	/* 
	 * params[0] => server address
	 * params[1] => command
	 */
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		StringBuffer response = new StringBuffer();
		
		if ( params.length == 0 )
		{
			return null;
		}
		try 
		{
			String url = null;
			/* Test the server connectivity */
			if(params.length == 1)
			{
				url = "http://" + params[0] + "/" + Constants.SERVER_FILE;
				URL obj;
				obj = new URL(url);
				
				HttpURLConnection con = (HttpURLConnection) obj.openConnection();
				int responseCode = con.getResponseCode();
				
				return String.valueOf(responseCode);
			}
			/* Command sent, pass it to the server */
			else if(params.length == 2 )
				url = "http://" + params[0] + "/" + Constants.SERVER_FILE + 
					"?action=" + params[1];
			
			Log.d("url", url);
			URL obj;
			obj = new URL(url);
			
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			//InputStream inp = con.getInputStream(); 
			// optional default is GET
			//con.setRequestMethod("GET");
	 
			int responseCode = con.getResponseCode();
			Log.d("code", String.valueOf(responseCode));
			
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			
			in.close();
	 
			//print result
			Log.d("reader", response.toString());
		
		} catch (MalformedURLException e) {
			
		} catch (IOException e) {
		}
		
		return response.toString();
	}
	
	
	public void testURL() throws Exception {
	    String strUrl = "http://stackoverflow.com/about";

	    try {
	        URL url = new URL(strUrl);
	        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
	        urlConn.connect();

	        //assertEquals(HttpURLConnection.HTTP_OK, urlConn.getResponseCode());
	    } catch (IOException e) {
	        System.err.println("Error creating HTTP connection");
	        e.printStackTrace();
	        throw e;
	    }
	}

	 protected void onPostExecute(String result) {
       
     }
	 
	public static boolean testServerConnectivity(String serverAddress)
	{
		Socket socket = null;
		boolean reachable = false;
		try {
		    socket = new Socket("http://" + serverAddress, 80);
		    reachable = true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {            
		    if (socket != null)
		    	try { 
		    		socket.close(); 
		    	} 
		    	catch(IOException e)
		    	{
		    		return reachable;
		    	}
		    else
		    	return reachable;
		}
		
		return reachable;
	}
	
	public static boolean isServerConnected(String server) 
	{
		Network net = new Network();
		net.execute(server);
		int ret = 0;
		try {
			ret = Integer.parseInt(net.get());
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(ret != 200)
			return false;
		else
			return true;
	}
	

	public static boolean isNetworkOn(Context context)
	{
		ConnectivityManager connMgr = (ConnectivityManager) 
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		    
	    if (networkInfo != null && networkInfo.isConnected()) {
	    	Log.d("network_test", networkInfo.getExtraInfo());
	    	return true;
	    }
	    else
	    	return false;
	    
	}
	

}
