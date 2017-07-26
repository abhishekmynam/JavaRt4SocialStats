package OAuthSetup;

public class RequestToken extends OAuthToken implements java.io.Serializable {
    private static final long serialVersionUID = -8806439091674811734L;
    private final IConfiguration conf;
    private IOAuth oauth;

    /*RequestToken(HttpResponse res, IOAuth oauth) throws Exception {
        super(res);
        conf = ConfigurationContext.getInstance();
        this.oauth = oauth;
    }*/

    public RequestToken(String token, String tokenSecret) {
        super(token, tokenSecret);
        conf = ConfigurationContext.getInstance();
    }

    RequestToken(String token, String tokenSecret, IOAuth oauth) {
        super(token, tokenSecret);
        conf = ConfigurationContext.getInstance();
        this.oauth = oauth;
    }

    public String getAuthorizationURL() {
        return conf.getOAuthAuthorizationURL() + "?oauth_token=" + getToken();
    }

    public String getAuthenticationURL() {
        return conf.getOAuthAuthenticationURL() + "?oauth_token=" + getToken();
    }
}
