package com.pigandtiger.photocollector.weibotools;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.pigandtiger.utility.Utility;
import com.pigandtiger.utility._Log;

public class WeiboTools implements WeiboConfig {

	public static final String tag = "WeiboTools.java";

	public static void login(WeiboContext context, String username, String password) throws ClientProtocolException, IOException, NoSuchAlgorithmException {
		preLogin(context, username);
		final String weiboLoginUrl = ssoLogin(context, username, password);
		weiboLogin(context, weiboLoginUrl);
	}

	/***
	 * Construct a http header in order to simulate the browser to fetch data
	 * from the weibo
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static final String requestData(WeiboContext context, final String url) throws ClientProtocolException, IOException {
		HttpGet request = new HttpGet(url);
		request.addHeader("Accept", "*/*");
		request.addHeader("Accept-Charset", "utf-8;q=0.7,*;q=0.3");
		request.addHeader("Accept-Encoding", "gzip,deflate,sdch");
		request.addHeader("Accept-Language", "en-US,en;q=0.8");
		request.addHeader("Host", "photo.weibo.com");
		request.addHeader("User-Agent",
				"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_6_8) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.163 Safari/535.19");
		request.addHeader("Referer", "http://photo.weibo.com");

		HttpResponse response = getNewClient().execute(request, context.getLocalContext());
		String result = "";
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream gzis = response.getEntity().getContent();
			Header contentEncoding = response.getFirstHeader("Content-Encoding");
			Reader reader = null;
			StringWriter writer = null;
			if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
				InputStream ugzis = new GZIPInputStream(gzis);
				try{
					reader = new InputStreamReader(ugzis, "UTF-8");
					writer = new StringWriter();

				    char[] buffer = new char[10240];
				    for (int length = 0; (length = reader.read(buffer)) > 0;) {
				        writer.write(buffer, 0, length);
				    }
				    result = writer.toString();

				}catch (IOException e) {
					_Log.e(tag,e, "Can not handle the result input stream.");
				}finally{
					if( reader != null )
						reader.close();
					if( writer != null )
						writer.close();
				}
			}else{
				// not encoded with gzip
				result = EntityUtils.toString(response.getEntity());
			}
//			 _Log.i(tag, "request %1$s result: %2$s",url,result);
			return result;
		} else {
			throw new IOException("Request url:" + url + " failed.");
		}

	}

	private static final String weiboLogin(WeiboContext context, String url) throws ClientProtocolException, IOException {
		_Log.i(tag, "Weibo login");
		HttpGet request = new HttpGet(url);
		HttpResponse response = getNewClient().execute(request, context.getLocalContext());
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity());
			// _Log.i(tag, "weibo login result:"+result);

			// pick up the unique id
			final String uniqueId = Utility.pickByRegx("\"uniqueid\":\"(\\d*)", result);
			_Log.i(tag, "uniqueId:" + uniqueId);
			context.setUniqueId(uniqueId);
			return uniqueId;
		} else {
			throw new IllegalStateException("Can not perform weibo login.");
		}
	}

	private static void preLogin(WeiboContext context, String username) throws ClientProtocolException, IOException {
		_Log.i(tag, "Pre login");
		Map<String, String> pair = new HashMap<String, String>();
		pair.put("entry", "weibo");
		pair.put("callback", "sinaSSOController.preloginCallBack");
		pair.put("username", "hzk47st@126.com");
		pair.put("_", String.valueOf(System.currentTimeMillis()));
		String url = WeiboTools.makeURL(PRE_LOGIN_URL, pair);
		_Log.i(tag, "url:" + url);
		HttpGet request = new HttpGet(url);
		HttpResponse response = getNewClient().execute(request, context.getLocalContext());
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity());
			_Log.i(tag, "pre login result:" + result);

			// pick up the sever time and the nonce
			final String serverTime = Utility.pickByRegx("\"servertime\"\\s*:\\s*(\\d*)", result);
			final String nonce = Utility.pickByRegx("\"nonce\"\\s*:\\s*\"([0-9A-Z]*)", result);
			_Log.i(tag, "strServerTime:%1$s,nonce:%2$s", serverTime, nonce);
			context.setServerTime(serverTime);
			context.setNonce(nonce);

		}
	}

	private static final String ssoLogin(WeiboContext context, final String username, final String password) throws NoSuchAlgorithmException,
			ClientProtocolException, IOException {
		_Log.i(tag, "SSO login");
		final UrlEncodedFormEntity entity = WeiboTools.makeLoginRequestData(username, password, context.getServerTime(), context.getNonce());
		HttpPost request = new HttpPost(SSO_LOGIN_URL);
		request.setEntity(entity);
		HttpResponse response = getNewClient().execute(request, context.getLocalContext());
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity());
			// _Log.i(tag, "sso login result:" + result);
			// pick up the weibo login url
			final String url = Utility.pickByRegx("location.replace\\('([^']*)", result);
			_Log.i(tag, "url:" + url);
			return url;
		} else {
			throw new IllegalStateException("Can not perform sso login.");
		}

	}

	private static final HttpClient getNewClient() {
		DefaultHttpClient client = new DefaultHttpClient();
		// custom cookie spec
		CookieSpecFactory csf = new CookieSpecFactory() {
			public CookieSpec newInstance(HttpParams params) {
				return new BrowserCompatSpec() {
					@Override
					public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
						// allow all cookies
						// _Log.i(tag, "custom validate");
					}
				};
			}
		};

		client.getCookieSpecs().register("custom", csf);
		client.getParams().setParameter(ClientPNames.COOKIE_POLICY, "custom");
		return client;
	}



	public static String makeURL(String url, Map<String, String> pair) {
		StringBuffer sb = new StringBuffer(url);
		sb.append("?");
		for (String key : pair.keySet()) {
			String p = String.format("%1$s=%2$s&", URLEncoder.encode(key), URLEncoder.encode(pair.get(key)));
			sb.append(p);
		}

		return sb.toString();
	}

	public static final UrlEncodedFormEntity makeLoginRequestData(final String username, final String password, final String serverTime, final String nonce)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		final String encrypt = SHA1(SHA1(SHA1(password)) + serverTime + nonce);
		// _Log.i(tag, "encrypt: " + encrypt);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("entry", "weibo"));
		params.add(new BasicNameValuePair("gateway", String.valueOf(1)));
		params.add(new BasicNameValuePair("from", ""));
		params.add(new BasicNameValuePair("savestate", String.valueOf(7)));
		params.add(new BasicNameValuePair("useticket", String.valueOf(1)));
		params.add(new BasicNameValuePair("ssosimplelogin", String.valueOf(1)));
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("service", "miniblog"));
		params.add(new BasicNameValuePair("servertime", serverTime));
		params.add(new BasicNameValuePair("nonce", nonce));
		params.add(new BasicNameValuePair("pwencode", "wsse"));
		params.add(new BasicNameValuePair("password", encrypt));
		params.add(new BasicNameValuePair("encode", "utf-8"));
		params.add(new BasicNameValuePair("url", "http://weibo.com/ajaxlogin.php?framelogin=1&callback=parent.sinaSSOController.feedBackUrlCallBack"));
		params.add(new BasicNameValuePair("returntype", "META"));

		final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);

		return entity;
	}

	private static String convertToHex(byte[] data) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < data.length; i++) {
			int halfbyte = (data[i] >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				if ((0 <= halfbyte) && (halfbyte <= 9))
					buf.append((char) ('0' + halfbyte));
				else
					buf.append((char) ('a' + (halfbyte - 10)));
				halfbyte = data[i] & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	public static String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md;
		md = MessageDigest.getInstance("SHA-1");
		byte[] sha1hash = new byte[40];
		md.update(text.getBytes("utf-8"), 0, text.length());
		sha1hash = md.digest();
		return convertToHex(sha1hash);
	}

}
