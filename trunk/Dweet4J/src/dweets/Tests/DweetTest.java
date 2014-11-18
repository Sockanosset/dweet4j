package dweets.Tests;

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

import dweets.Connection.DweetClientConnBasic;
import dweets.Core.DweetException;
import dweets.Dweeting.ReceiveAllData;
import dweets.Dweeting.ReceiveLatestData;
import dweets.Dweeting.SendData;

public class DweetTest
{
	private static final String THING_NAME = "MyTest"; // Modify as needed
	private static final String THING_KEY = null; // If your thing is locked, add the key here 
	private static final boolean SSL = true; // Better to have an SSL connection
	
	public static void main(String[] args)
	{
		// Commons logging properties
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "debug");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		
		System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire.header", "debug");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");
		
		
		/* Use a basic connection to the dweet.io server */
		final DweetClientConnBasic dweetClientConn = new DweetClientConnBasic();
		
		/* Send the latest updated data to the server */
		final SendData sendDataSSL = new SendData(THING_NAME, THING_KEY, SSL);
		// Add any data to the data map (Max pay load of 2000 characters)
		sendDataSSL.getDataMap().put("Hello", "World");
		sendDataSSL.getDataMap().put("Where", "Am");
		sendDataSSL.getDataMap().put("I", "Now");
		sendDataSSL.getDataMap().put("Handle", 553);
		try
		{
			sendDataSSL.send(dweetClientConn);
			System.out.println(sendDataSSL.getDweetDataBean().printBean());
		} catch (DweetException e) {
			e.printStackTrace();
		}
		
		/* Receive the latest data test */
		final ReceiveLatestData receiveLatestDataSSL = new ReceiveLatestData(THING_NAME, THING_KEY, SSL);
		try
		{
			receiveLatestDataSSL.send(dweetClientConn);
			System.out.println(receiveLatestDataSSL.getDweetDataBean().printBean());
		} catch (DweetException e) {
			e.printStackTrace();
		}
		
		
		/* Receive all the data on the server (last 500 max) test */
		final ReceiveAllData receiveAllDataSSL = new ReceiveAllData(THING_NAME, THING_KEY, SSL);
		try
		{
			receiveAllDataSSL.send(dweetClientConn);
			System.out.println(receiveAllDataSSL.getDweetDataBean().printBean());
		} catch (DweetException e) {
			e.printStackTrace();
		}
		
		
		/* Close the connection to the dweet.io server */
		dweetClientConn.closeConnection();
	}
}
