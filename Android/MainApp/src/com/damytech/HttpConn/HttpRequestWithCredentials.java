package com.damytech.HttpConn;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class HttpRequestWithCredentials {
	
	/**
	 * make request with credential info and send
	 * @param urlPath [in], server url
	 * @param userName [in], user name
	 * @param userPwd [in], user password
	 * @return HttpResponse : request result
	 * @throws Exception
	 */
	public HttpResponse makeRequest(String urlPath, String userName, String userPwd) throws Exception {
		HttpClient httpclient;
		HttpParams httpParameters;
		HttpGet request;

		int timeoutConnection = 10000;
		int timeoutSocket = 10000;

		httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		httpclient = new DefaultHttpClient(httpParameters);
		// make credential info
		Credentials creds = new UsernamePasswordCredentials(userName, userPwd);

		((AbstractHttpClient) httpclient).getCredentialsProvider().setCredentials(
						new AuthScope(null, -1), creds);
		// make request : (method) Get
		request = new HttpGet(urlPath);

		// execute request
		HttpResponse res = httpclient.execute(request);

		return res;
	}

	public String processRequest(HttpResponse resp) throws Exception{
		int status = resp.getStatusLine().getStatusCode();

		if (status == 200){
			//Get the body
			//return getBodyFromResponse(resp);
			return "OK";
		} else if (status == 400) {
			//Bad Request
			throw new Exception("Error: HTTP 400 error");
		} else if (status == 401) {
			//Unauthorized
			throw new Exception("Error: HTTP 401 error: Incorrect Server Name");
		} else if (status == 403) {
			//Forbidden
			throw new Exception("Error: HTTP 403 error: Check UserName and password");
		} else if (status == 500) {
			//Internal Server Error
			throw new Exception("Error: HTTP 500 error");
		} else {
			throw new Exception("Error Unknown: Status is " + status);
		}
	}

	//HTTP Response is passed in, and the JSON String from the Body is returned
   	public String getBodyFromResponse(HttpResponse resp) throws Exception{
		ResponseHandler<String> handler = new BasicResponseHandler();
		try {
				return handler.handleResponse(resp);
		} catch (IOException e) {
				throw new Exception ("Error: HTTP Response 200. Error when converting response. " + e.getMessage());
		} catch (Exception e) {
				throw new Exception ("Error: HTTP Response 200. Error when converting response. " + e.getMessage());
		}
	}
}
