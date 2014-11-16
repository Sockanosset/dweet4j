package dweets.Core;

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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import dweets.Beans.DataBean;
import dweets.Beans.DweetDataBean;
import dweets.Connection.DweetConnInterface;


/**
 * Abstract class to build the basis of the dweet.io server interface
 */
public abstract class DweetCommandAbstract
{
	protected String thingName;
	protected String key;
	protected boolean ssl;
	protected DweetDataBean dweetDataBean;
	
	protected String baseUrl = null;
	protected String completeUrl = null;
	
	/**
	 * Construct the dweet.io object
	 * 
	 * @param thingName Must contain a name or will fail upon use
	 * @param key pass <code>null</code> if not using a lock
	 * @param ssl secure connection
	 */
	public DweetCommandAbstract(String thingName, String key, boolean ssl) 
	{
		super();
		this.thingName = thingName;
		this.key = key;
		this.ssl = ssl;
		init();
	}
	
	/**
	 * Initialize the object<p>
	 * 
	 * Called in the super class construction
	 */
	protected abstract void init();
	
	/**
	 * Construct the URL based on the intention of the extension<br>
	 * This should typically be done in the initializing method
	 * 
	 * @return
	 */
	protected abstract void constructBaseURL();
	
	
	/**
	 * Send the Dweet to the server
	 * 
	 * @param conn
	 * @return
	 */
	public abstract void send(DweetConnInterface conn) throws DweetException;
	
	/**
	 * Check for proper pay load 
	 * @return
	 */
	protected boolean dweetPayloadCheck()
	{
		return this.completeUrl.length() <= 2000;
	}
	
	
	public String getThingName() {
		return thingName;
	}

	public String getKey() {
		return key;
	}
	
	public boolean isSsl() {
		return ssl;
	}
	
	public String getBaseUrl() {
		return baseUrl;
	}
	
	public String getCompleteUrl() {
		return completeUrl;
	}

	/**
	 * Get the dweet.io data that was requested or returned
	 * 
	 * @return
	 */
	public DweetDataBean getDweetDataBean() {
		return dweetDataBean;
	}
	
	/**
	 * Get the map of data returned from the server<br>
	 * This should only be used for data requests or to validate the send data
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getReturnedDataMap()
	{
		final Map<String, Object> data = new HashMap<String, Object>();
		
		if (dweetDataBean.getWith() instanceof ArrayList<?>)
		{
			for (DataBean i : (ArrayList<DataBean>) dweetDataBean.getWith())
				for (Entry<String, Object> e : i.getContent().entrySet())
					data.put(e.getKey(), e.getValue());
		}
		
		return Collections.unmodifiableMap(data);
	}

}
