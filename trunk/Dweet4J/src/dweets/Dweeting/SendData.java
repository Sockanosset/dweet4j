package dweets.Dweeting;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dweets.Beans.JSONToDweet;
import dweets.Connection.DweetConnInterface;
import dweets.Core.DweetCommandAbstract;
import dweets.Core.DweetException;
import dweets.Core.DweetURLs;



/**
 * Class to send data to the dweet.io<p>
 * 
 * Send data from your thing to the cloud by "dweeting" it with a simple HAPI web API
 * 
 */
public class SendData extends DweetCommandAbstract
{
	private Log log = LogFactory.getLog(SendData.class);
	private final Map<String, Object> data = new HashMap<String, Object>();
	
	/**
	 * Construct the object for sending data to the dweet.io server<P>
	 * 
	 * Use the <code>send()</code> to request the data and if the request was accepted<br>
	 * get the DweetDataBean with <code>getDweetDataBean()</code><p>
	 * 
	 * When evaluating the received data, always check for error with <code>dweetDataBean.isError()</code>
	 * 
	 * @param thingName Must contain a name
	 * @param key pass <code>null</code> if not using a lock
	 * @param ssl secured connection
	 */
	public SendData(String thingName, String key, boolean ssl)
	{
		super(thingName, key, ssl);
	}
	
	/**
	 * Construct the object for sending data to the dweet.io server
	 * 
	 * @param thingName Must contain a name
	 * @param key pass <code>null</code> if not using a lock
	 * @param ssl secured connection
	 * @param data
	 */
	public SendData(String thingName, String key, boolean ssl, Map<String, Object> data)
	{
		super(thingName, key, ssl);
		
		data.putAll(this.data);
	}
	
	@Override
	protected void init()
	{
		constructBaseURL();
	}
	
	@Override
	protected void constructBaseURL()
	{
		final StringBuilder str = new StringBuilder();
		
		if (this.isSsl())
			str.append(DweetURLs.HTTP_SSL);
		else
			str.append(DweetURLs.HTTP);
		
		str.append(DweetURLs.SEND_DWEET).append(this.thingName).append("?");
			
		if (this.key != null)
			str.append(DweetURLs.KEY).append(this.key).append("&");
		
		this.baseUrl = str.toString();
	}
	
	/**
	 * Get the data map
	 * 
	 * @return
	 */
	public Map<String, Object> getDataMap()
	{
		return this.data;
	}
	
	@Override
	public void send(DweetConnInterface conn) throws DweetException
	{
		if (this.thingName == null)
		{
			log.debug("A thing name must be added");
			throw new DweetException("A thing name must be added");
		}
		
		// Nothing to send
		if (data.size() == 0)
		{
			log.debug("No Data being send to the server");
			throw new DweetException("No Data being send to the server");
		}
		
		addDataToUrl();
		
		if (!this.dweetPayloadCheck())
		{
			log.debug("Data sent was too long- "+this.completeUrl.length()+" characters");
			throw new DweetException("Data sent was too long- "+this.completeUrl.length()+" characters");
		}
		
		try
		{
			conn.setURL(this.completeUrl);
			log.debug("Attempting to connect to execute the dweet: "+this.completeUrl);
			
			String response = conn.execute();
			log.debug("Server response: "+response);
			
			final JSONToDweet json = new JSONToDweet(response);
			this.dweetDataBean = json.getBean();
			
		} catch (Exception e) {
			
			log.error(e.getMessage());
			throw new DweetException(e.getMessage());
		}
		
		if (!sendValidation())
		{
			log.debug("Dweet return data was validated as false");
			throw new DweetException("Dweet return data was validated as false");
		}
	}
	
	/**
	 * Add the data to the URL
	 * 
	 */
	private void addDataToUrl()
	{
		final StringBuilder str = new StringBuilder();
		
		str.append(this.baseUrl);
		
		for (Entry<String, Object> e : data.entrySet())
			str.append(e.getKey()).append("=").append(e.getValue()).append("&");
		
		str.deleteCharAt(str.length()-1);
		
		this.completeUrl = str.toString();
	}
	
	
	/**
	 * Check to make sure the data sent matches the data returned by the server
	 * 
	 * @return
	 */
	private boolean sendValidation()
	{
		if (dweetDataBean == null || !(dweetDataBean.getWith() instanceof ArrayList<?>))
			return false;
		
		final Map<String, Object> map = this.getReturnedDataMap();
		
		for (Entry<String, Object> i : data.entrySet())
		{
			if (!map.containsKey(i.getKey()))
				return false;
				
			if (!map.get(i.getKey()).equals(i.getValue()))
				return false;
		}
		
		return true;
	}
	
}
