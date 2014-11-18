package dweets.Beans;

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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import dweets.Core.DweetKeys;
import dweets.Core.DweetResponseKW;


public class JSONToDweet
{
	private String jsonResponse;
	
	/**
	 * Construct the object for the JSON to Bean conversion
	 * 
	 * @param json
	 */
	public JSONToDweet(String jsonResponse)
	{
		super();
		this.jsonResponse = jsonResponse;
	}
	
	
	/**
	 * Get the dweet.io bean from the JSON response
	 * 
	 * @return
	 * @throws Exception
	 */
	public DweetDataBean getBean() throws Exception
	{
		final JSONObject json = new JSONObject(jsonResponse);
		json.get(DweetKeys.THIS);
		
		/* Look for error codes from the server (i.e. thing name was not found) */
		if(!json.get(DweetKeys.THIS).equals(DweetResponseKW.SUCCEEDED))
		{
			if (json.has(DweetKeys.THIS) && json.has(DweetKeys.WITH) && json.has(DweetKeys.BECAUSE))
				return new DweetDataBean(json.get(DweetKeys.THIS).toString(), json.get(DweetKeys.WITH).toString(), json.get(DweetKeys.BECAUSE).toString(), true);
			
			else if (json.has(DweetKeys.THIS) && json.has(DweetKeys.BECAUSE))
				return new DweetDataBean(json.get(DweetKeys.THIS).toString(), json.get(DweetKeys.BECAUSE).toString(), true);
		}
		
		
		/* Process a succeeded JSON response */
		final DweetDataBean dweet = new DweetDataBean(json.get(DweetKeys.THIS).toString(), json.get(DweetKeys.BY).toString(), json.get(DweetKeys.THE).toString());
		
		// The 'with' key word can be either an JSON array for a read or just a JSON object for a write - test for null to find out
		// Must have been a read return - get all the data
		if (json.get(DweetKeys.WITH).getClass().equals(JSONArray.class))
		{
			final JSONArray with = json.getJSONArray(DweetKeys.WITH);
			
			for (int i=0;i<with.length();i++)
            {
				final JSONObject data = with.getJSONObject(i);
				final DataBean dataBean = new DataBean(data.get(DweetKeys.THING).toString(), data.get(DweetKeys.CREATED).toString());
				
				final JSONObject content = data.getJSONObject(DweetKeys.CONTENT);
				final Iterator<?> it = content.keys();
            	while(it.hasNext())
            	{
            		String key = (String) it.next();
            		
            		// See if the value is a JSON object - if so create a map for that data
            		if (content.get(key).getClass().equals(JSONObject.class))
            		{
            			final Map<String, Object> contentMap = new HashMap<String, Object>();
            			final JSONObject contentArray = content.getJSONObject(key);
            			
            			final Iterator<?> its = contentArray.keys();
            			while(its.hasNext())
                    	{
            				String keys = (String) its.next();
            				contentMap.put(keys, contentArray.get(keys));
                    	}
            					
            			dataBean.getContent().put(key, contentMap);
            		
            		} else
            			dataBean.getContent().put(key, content.get(key));
            	}
            	
            	dweet.addData(dataBean);
            }
			
		// Write Response
		} else {
			
        	final JSONObject withObject = json.getJSONObject(DweetKeys.WITH);
        	final DataBean dataBean = new DataBean(withObject.get(DweetKeys.THING).toString(), withObject.get(DweetKeys.CREATED).toString());
        	final JSONObject content = withObject.getJSONObject(DweetKeys.CONTENT);
        	
        	final Iterator<?> it = content.keys();
            while(it.hasNext())
            {
            	String key = (String) it.next();
        		dataBean.getContent().put(key, content.get(key));
            }
            dweet.addData(dataBean);
		}
		
		return dweet;
	}

}
