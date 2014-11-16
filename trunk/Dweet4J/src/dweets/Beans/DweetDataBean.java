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
import java.util.ArrayList;

import dweets.Core.DweetKeys;


public class DweetDataBean implements Serializable
{
	private static final long serialVersionUID = -2013995449225413712L;

	private String _this;
	private String by;
	private String the;
	private String because;
	
	private Object with;// Can be an array list or a string
	
	private boolean error = false;// Indicates that an error response was found
	
	/**
	 * Construct a dweet.io data bean without the any data yet
	 * 
	 * @param _this
	 * @param by
	 * @param the
	 * @param with
	 */
	public DweetDataBean(String _this, String by, String the)
	{
		super();
		this._this = _this;
		this.by = by;
		this.the = the;
		
		// 
		this.with = new ArrayList<DataBean>();
		this.because = null;
	}
	
	/**
	 * Construct a dweet.io data bean that was an error response
	 * 
	 * @param _this
	 * @param with
	 * @param because
	 */
	public DweetDataBean(String _this, Object with, String because, boolean error)
	{
		super();
		this._this = _this;
		this.with = with;
		this.because = because;
		this.error = true;
		
		this.by = null;
		this.the = null;
	}
	
	
	/**
	 * Construct a dweet.io data bean that was an error response
	 * 
	 * @param _this
	 * @param because
	 * @param error
	 */
	public DweetDataBean(String _this, String because, boolean error)
	{
		super();
		this._this = _this;
		this.because = because;
		this.error = true;
		
		this.with = null;
		this.by = null;
		this.the = null;
	}
	
	
	/**
	 * Add a Data Bean to the list
	 * 
	 * @param data
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public void addData(DataBean dataBean) throws Exception
	{
		if (with instanceof ArrayList<?>)
			((ArrayList<DataBean>) with).add(dataBean);
		else
			throw new Exception("'with' was not setup as a List");
	}

	public String get_this() {
		return _this;
	}

	public String getBy() {
		return by;
	}

	public String getThe() {
		return the;
	}

	public Object getWith() {
		return with;
	}
	
	public String getBecause() {
		return because;
	}

	public boolean isError() {
		return error;
	}
	
	@SuppressWarnings("unchecked")
	public String printBean()
	{
		final StringBuilder str = new StringBuilder();
		
		if (error)
		{
			str.append("{\r\n");
			str.append(" \"").append(DweetKeys.THIS).append("\": \"").append(_this).append("\",\r\n");
			if (with != null)
				str.append(" \"").append(DweetKeys.WITH).append("\": ").append(with).append(",\r\n");
			str.append(" \"").append(DweetKeys.BECAUSE).append("\": \"").append(because).append("\"\r\n}");
			
		} else {
			
			str.append("{\r\n");
			str.append(" \"").append(DweetKeys.THIS).append("\": \"").append(_this).append("\",\r\n");
			str.append(" \"").append(DweetKeys.BY).append("\": \"").append(by).append("\",\r\n");
			str.append(" \"").append(DweetKeys.THE).append("\": \"").append(the).append("\",\r\n");
			str.append(" \"").append(DweetKeys.WITH).append("\": ");
			
			if (((ArrayList<DataBean>)with).size() > 1)
				str.append("[").append("\r\n  {");
			else
				str.append("{");
			
			for (DataBean d : ((ArrayList<DataBean>)with))
				str.append("\r\n").append(d.printBean()).append(",");
			
			str.deleteCharAt(str.length() - 1);
			
			if (((ArrayList<DataBean>)with).size() > 1)
				str.append("\r\n]");
			
			str.append("\r\n}");
		}
			
		return str.toString();
	}
}
