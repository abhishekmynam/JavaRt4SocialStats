package OAuthSetup;

public interface IOAuth {

    void setOAuthConsumer(String consumerKey, String consumerSecret);
    RequestToken getOAuthRequestToken() throws Exception;
    RequestToken getOAuthRequestToken(String callbackURL) throws Exception;
    RequestToken getOAuthRequestToken(String callbackURL, String xAuthAccessType) throws Exception;
    RequestToken getOAuthRequestToken(String callbackURL, String xAuthAccessType, String xAuthMode) throws Exception;
    AccessToken getOAuthAccessToken() throws Exception;
    AccessToken getOAuthAccessToken(String oauthVerifier) throws Exception;
    AccessToken getOAuthAccessToken(RequestToken requestToken) throws Exception;
    AccessToken getOAuthAccessToken(RequestToken requestToken, String oauthVerifier) throws Exception;
    AccessToken getOAuthAccessToken(String screenName, String password) throws Exception;
    void setOAuthAccessToken(AccessToken accessToken);
}
