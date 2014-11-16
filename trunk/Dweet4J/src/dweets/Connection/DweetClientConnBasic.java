package dweets.Connection;

/*
 * Copyright 2014 Eric Spina
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import dweets.Core.DweetException;


/**
 * Basic implementation of the DweetConn Interface to connect to the dweet.io server<p>
 * 
 * For a more sophisticated connection implementation, go to:<br>
 * <link>http://hc.apache.org/httpcomponents-client-4.3.x/tutorial/html/connmgmt.html#d5e405</link><br>
 * and implement the DweetConnInterface
 * 
 */
public class DweetClientConnBasic implements DweetConnInterface
{
	private Log log = LogFactory.getLog(DweetClientConnBasic.class);
	
	private final HttpGet httpget = new HttpGet();
	private final CloseableHttpClient httpclient = HttpClients.createDefault();
	
	/* Initialize with a basic response handler */
	private ResponseHandler<String> responseHandler = new ResponseHandler<String>()
	{
		public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException
		{
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status < 300)
			{
				final HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
                
            } else {
            	
                log.error("Unexpected response status: "+response.getStatusLine());
            	throw new ClientProtocolException("Unexpected response status: "+response.getStatusLine());
            }
        }
    };

    
    /**
     * Constructor for a basic client connection<p>
     * 
     * Use <code>setURL()</code> to based on the dweeting.io command<br>
     * and then <code>execute()</code> to send the command
     */
	public DweetClientConnBasic()
	{
		super();
		log.debug("Connection created");
	}

	
	/**
	 * Set the URL for the communications
	 * 
	 * @param urL
	 * @throws URISyntaxException 
	 */
	@Override
	public void setURL(String url) throws DweetException
	{
		this.httpget.setURI(URI.create(url));
	}
	
	
	/**
	 * Execute the request
	 * 
	 * @return
	 * @throws Exception
	 */
	@Override
	public String execute() throws DweetException
	{
		if (this.httpget.getURI() == null || this.httpget.getURI().equals(""))
			throw new DweetException("The URI was not filled out at execute");
		
		String response = null;
		try
		{
			response = this.httpclient.execute(this.httpget, this.responseHandler);
			
		} catch (ClientProtocolException e) {
			throw new DweetException(e.getMessage());
		} catch (IOException e) {
			throw new DweetException(e.getMessage());
		}
		
		return response;
	}
	
	
	/**
	 * Close the server connection
	 * 
	 */
	public void closeConnection()
	{
		log.debug("closing connection...");
		try
		{
			this.httpget.reset();
			this.httpclient.close();
			
			log.debug("...connection closed");
			
		} catch (Exception e) {
			log.debug("...connection exception: "+e.getMessage());
		}
	}
	
	public ResponseHandler<String> getResponseHandler() {
		return this.responseHandler;
	}

	public void setResponseHandler(ResponseHandler<String> responseHandler) {
		this.responseHandler = responseHandler;
	}


}
