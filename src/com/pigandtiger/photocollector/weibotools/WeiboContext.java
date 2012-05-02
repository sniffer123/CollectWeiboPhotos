package com.pigandtiger.photocollector.weibotools;

import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class WeiboContext {
	private CookieStore cookieStore = null;
	private HttpContext localContext = null;
	private String serverTime = "";
	private String nonce = "";
	private String uniqueId = null;
	
	public WeiboContext() {
		// Create a local instance of cookie store
		cookieStore = new BasicCookieStore();
		// Bind custom cookie store to the local context
		localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	}

	public CookieStore getCookieStore() {
		return cookieStore;
	}

	public void setCookieStore(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}

	public HttpContext getLocalContext() {
		return localContext;
	}

	public void setLocalContext(HttpContext localContext) {
		this.localContext = localContext;
	}

	public String getServerTime() {
		return serverTime;
	}

	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
	
	
}
