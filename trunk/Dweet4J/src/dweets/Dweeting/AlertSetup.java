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
import dweets.Core.DweetKeys;
import dweets.Core.DweetURLs;

/**
 * Class to setup an alert for a thing<p>
 * 
 * Alerts notify you when something in the data you dweet falls outside set of conditions.<br>
 * Alerts are reserved for locked things only
 * 
 */
public class AlertSetup extends DweetCommandAbstract
{
	private Log log = LogFactory.getLog(AlertSetup.class);
	
	private String[] recipients;
	private String condition;
	
	/**
	 * Construct the Alert setup object
	 * 
	 * @param thingName
	 * @param key must have a locked thing in order to setup an alert
	 * @param ssl
	 * @param recipients array of email addresses (can text by configuring the email address as such: <href>http://www.emailtextmessages.com/</href>)
	 * @param condition
	 * A simple javascript expression to evaluate the data in a dweet and to return whether or not an alert should be sent.<br>
	 * You can reference the actual data in the dweet as a javascript object, like dweet.my_field or dweet["my_field"].<br>
	 * If the javascript expression returns anything other than a "falsey" value (false, null, undefined, 0, etc.), <br>
	 * an alert will be sent.<p>
	 * 
	 * For example, alert me when a temperature becomes extreme:<br>
	 * dweet.temp <= 32 || dweet.temp >= 212<p>
	 * 
	 * You can also create more complex, multi-state alerts by returning a string:<br>
	 * if(dweet.temp <= 32) return "frozen"; else if(dweet.temp >= 212) return "boiling";
	 * Note that the javascript expression you provide for the condition is limited to 2000 characters and<br>
	 * may not contain complex things like loops and some other javascript reserved words.<br>
	 * However, things like Math functions are supported.
	 * 
	 */
	public AlertSetup(String thingName, String key, boolean ssl, String[] recipients, String condition)
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
		
		str.append(DweetURLs.ALERT_SETUP_DWEET).append(this.thingName);
		
		if (this.recipients != null)
			for (String r : recipients)
				str.append(r).append(",");
		str.deleteCharAt(str.length() - 1);
		
		str.append("/").append(DweetKeys.WHEN).append("/").append(this.thingName).append("/").append(this.condition);
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
		if (this.key == null)
		{
			log.debug("To setup an alert, the thing must be locked and have a key");
			throw new DweetException("To setup an alert, the thing must be locked and have a key");
		}
		if (this.recipients == null)
		{
			log.debug("To setup an alert, the thing must have at lease 1 email recipient");
			throw new DweetException("To setup an alert, the thing must have at lease 1 email recipient");
		}
		if (this.condition == null)
		{
			log.debug("To setup an alert, the thing must have a properly formatted condition");
			throw new DweetException("To setup an alert, the thing must have a properly formatted condition");
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
