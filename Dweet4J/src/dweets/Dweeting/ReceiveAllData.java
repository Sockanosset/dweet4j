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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import dweets.Beans.JSONToDweet;
import dweets.Connection.DweetConnInterface;
import dweets.Core.DweetCommandAbstract;
import dweets.Core.DweetException;
import dweets.Core.DweetURLs;

/**
 * Class to receive all the data on the dweet.io server for the thing<p>
 * Note that dweet.io only holds on to the last 500 dweets over a 24 hour period.<br>
 * If the thing hasn't dweeted in the last 24 hours, its history will be removed
 * 
 */
public class ReceiveAllData extends DweetCommandAbstract
{
	private Log log = LogFactory.getLog(ReceiveAllData.class);
	
	/**
	 * Constructor for the receive all data class<p>
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
	public ReceiveAllData(String thingName, String key, boolean ssl)
	{
		super(thingName, key, ssl);
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
		
		str.append(DweetURLs.GET_ALL_DWEETS).append(this.thingName);
		
		if (this.key != null)
			str.append("?").append(DweetURLs.KEY).append(this.key);
		
		this.baseUrl = str.toString();
		this.completeUrl = this.baseUrl;
	}

	@Override
	public void send(DweetConnInterface conn) throws DweetException
	{
		if (this.thingName == null)
		{
			log.debug("A thing name must be added");
			throw new DweetException("A thing name must be added");
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
	}

}
