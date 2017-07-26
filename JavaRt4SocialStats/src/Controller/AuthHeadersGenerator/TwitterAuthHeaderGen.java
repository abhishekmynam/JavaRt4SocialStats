package Controller.AuthHeadersGenerator;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import Controller.SocialMediaUserAuth;

public class TwitterAuthHeaderGen {
	
	private AuthCommon authCommon = new AuthCommon();
	private String realm = new String(); 
	private static final String HMAC_SHA1 = "HmacSHA1";
	
	public String generateAuthorizationHeader(SocialMediaUserAuth auth, String method, String url) {
		HttpParameter[] params = null;
		long timestamp = System.currentTimeMillis() / 1000;
		long nonce = timestamp + AuthCommon.RAND.nextInt();
		if (null == params) {
			params = new HttpParameter[0];
		}
		List<HttpParameter> oauthHeaderParams = new ArrayList<HttpParameter>(5);
		oauthHeaderParams.add(new HttpParameter("oauth_consumer_key", auth.authKeys.get("ConsumerKey")));
		oauthHeaderParams.add(new HttpParameter("oauth_nonce", nonce));
		oauthHeaderParams.add(new HttpParameter("oauth_signature_method", "HMAC-SHA1"));
		oauthHeaderParams.add(new HttpParameter("oauth_timestamp", timestamp));
		oauthHeaderParams.add(new HttpParameter("oauth_token", auth.authKeys.get("AuthToken")));
		oauthHeaderParams.add(new HttpParameter("oauth_version", "1.0"));

		List<HttpParameter> signatureBaseParams = new ArrayList<HttpParameter>(
				oauthHeaderParams.size() + params.length);
		signatureBaseParams.addAll(oauthHeaderParams);
		if (!HttpParameter.containsFile(params)) {
			signatureBaseParams.addAll(authCommon.toParamList(params));
		}
		parseGetParameters(url, signatureBaseParams);
		StringBuilder base = new StringBuilder(method).append("&")
				.append(HttpParameter.encode(authCommon.constructRequestURL(url))).append("&");
		base.append(HttpParameter.encode(authCommon.normalizeRequestParameters(signatureBaseParams)));
		String oauthBaseString = base.toString();
		String signature = generateSignature(oauthBaseString, auth.authKeys.get("ConsumerSecret"), auth.authKeys.get("AuthTokenSecret"));

		oauthHeaderParams.add(new HttpParameter("oauth_signature", signature));

		// http://oauth.net/core/1.0/#rfc.section.9.1.1
		if (realm != null) {
			oauthHeaderParams.add(new HttpParameter("realm", realm));
		}
		String encodedParam = AuthCommon.encodeParameters(oauthHeaderParams, ",", true);
		return "OAuth " + encodedParam;
	}
	
	private String generateSignature(String data, String consumerSecret, String AuthTokenSecret) {
		byte[] byteHMAC = null;
		try {
			Mac mac = Mac.getInstance(HMAC_SHA1);
			SecretKeySpec spec;

			String oauthSignature = HttpParameter.encode(consumerSecret) + "&" +HttpParameter.encode(AuthTokenSecret);
			spec = new SecretKeySpec(oauthSignature.getBytes(), HMAC_SHA1);

			mac.init(spec);
			byteHMAC = mac.doFinal(data.getBytes());
		} catch (InvalidKeyException ike) {
			throw new AssertionError(ike);
		} catch (NoSuchAlgorithmException nsae) {
			throw new AssertionError(nsae);
		}
		return AuthCommon.BASE64Encoder(byteHMAC);
	}
	
	private void parseGetParameters(String url, List<HttpParameter> signatureBaseParams) {
		int queryStart = url.indexOf("?");
		if (-1 != queryStart) {
			url.split("&");
			String[] queryStrs = url.substring(queryStart + 1).split("&");
			try {
				for (String query : queryStrs) {
					String[] split = query.split("=");
					if (split.length == 2) {
						signatureBaseParams.add(new HttpParameter(URLDecoder.decode(split[0], "UTF-8"),
								URLDecoder.decode(split[1], "UTF-8")));
					} else {
						signatureBaseParams.add(new HttpParameter(URLDecoder.decode(split[0], "UTF-8"), ""));
					}
				}
			} catch (UnsupportedEncodingException ignore) {
			}

		}

	}
}
