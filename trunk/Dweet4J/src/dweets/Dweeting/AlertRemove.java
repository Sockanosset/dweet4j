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
 * Class to Remove an alert
 * 
 * Alerts notify you when something in the data you dweet falls outside set of conditions.<br>
 * Alerts are reserved for locked things only
 *
 */
public class AlertRemove extends DweetCommandAbstract
{
	private Log log = LogFactory.getLog(AlertSetup.class);
	
	/**
	 * Constructor for the Alert removal
	 * 
	 * @param thingName Must contain a name
	 * @param key pass <code>null</code> if not using a lock
	 * @param ssl secured connection
	 */
	public AlertRemove(String thingName, String key, boolean ssl)
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
		
		str.append(DweetURLs.ALERT_REMOVE_DWEET).append(this.thingName);
		
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
		if (!this.validateURL())
		{
			log.debug("Remove any spaces from the URL: "+this.validateURL());
			throw new DweetException("Remove any spaces from the URL: "+this.validateURL());
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
