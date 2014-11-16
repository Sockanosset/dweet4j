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

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import dweets.Core.DweetKeys;

public class DataBean implements Serializable
{
	private static final long serialVersionUID = 3953266680176843955L;

	private String thing;
	private Date created;
	private Map<String, Object> content = new HashMap<String, Object>();
	
	/**
	 * Construct a complete data bean
	 * 
	 * @param thing
	 * @param created
	 * @param content
	 */
	public DataBean(String thing, String created, Map<String, Object> content)
	{
		super();
		this.thing = thing;
		this.created = convertToDate(created);
		this.content = content;
	}
	
	/**
	 * Construct a data bean without the content<br>
	 * The content can be updated via 
	 * 
	 * @param thing
	 * @param created
	 * @param content
	 */
	public DataBean(String thing, String created)
	{
		super();
		this.thing = thing;
		this.created = convertToDate(created);
	}
	
	/**
	 * Convert the string date with the date object<br>
	 * dweet.io Date Format: "2014-01-15T17:28:42.556Z"<p>
	 * 
	 * Defaults to the current time is a formatting error occurred
	 * 
	 * @param str
	 * @return
	 */
	private Date convertToDate(String str)
	{
		try
		{
			String strDate = str.replace("T", " ").replace("Z", "");
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strDate);
		} catch (Exception e) {
			return new Date();
		}
	}

	public String getThing() {
		return thing;
	}

	public Date getCreated() {
		return created;
	}

	public Map<String, Object> getContent() {
		return content;
	}
	
	public String printBean()
	{
		final StringBuilder str = new StringBuilder();
		str.append("{\r\n");
		str.append(" \"").append(DweetKeys.THING).append("\": \"").append(thing).append("\",\r\n");
		str.append(" \"").append(DweetKeys.CREATED).append("\": \"").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(created)).append("\",\r\n");
		str.append(" \"").append(DweetKeys.CONTENT).append(": {");
		for (Entry<String, Object> e : content.entrySet())
			str.append("\r\n   \"").append(e.getKey()).append("\": ").append(e.getValue()).append(",");
		
		str.deleteCharAt(str.length() - 1);
		str.append("\r\n}");
		
		return str.toString();
	}
}
