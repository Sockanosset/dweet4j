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

public class DweetURLs
{
	public final static String HTTP = "http";
	public final static String HTTP_SSL = "https";
	public final static String SEND_DWEET = "://dweet.io/dweet/for/";
	public final static String GET_LATEST_DWEET = "://dweet.io/get/latest/dweet/for/";
	public final static String GET_ALL_DWEETS = "://dweet.io/get/dweets/for/";
	public final static String STREAM_SETUP_DWEET = "://dweet.io/listen/for/dweets/from/";
	public final static String ALERT_SETUP_DWEET = "://dweet.io/alert/";
	public final static String ALERT_REMOVE_DWEET = "://dweet.io/remove/alert/for/";
	
	public final static String LOCK = "lock=";
	public final static String KEY = "key=";
	public final static String LOCK_DWEET = "://dweet.io/lock/";
	public final static String UNLOCK_DWEET = "://dweet.io/unlock/";
	public final static String LOCK_REMOVE_DWEET = "://dweet.io/remove/lock/";
	
	public DweetURLs() { super(); }
}
