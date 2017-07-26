package OAuthSetup;

import java.util.Properties;

public interface IConfiguration extends java.io.Serializable {

	boolean isDebugEnabled();

	boolean isApplicationOnlyAuthEnabled();

	String getUser();

	String getPassword();

	HttpClientConfiguration getHttpClientConfiguration();

	int getHttpStreamingReadTimeout();

	String getOAuthConsumerKey();

	String getOAuthConsumerSecret();

	String getOAuthAccessToken();

	String getOAuthAccessTokenSecret();

	String getOAuth2TokenType();

	String getOAuth2AccessToken();

	String getOAuth2Scope();

	String getRestBaseURL();

	String getUploadBaseURL();

	String getStreamBaseURL();

	String getOAuthRequestTokenURL();

	String getOAuthAuthorizationURL();

	String getOAuthAccessTokenURL();

	String getOAuthAuthenticationURL();

	String getOAuth2TokenURL();

	String getOAuth2InvalidateTokenURL();

	String getUserStreamBaseURL();

	String getSiteStreamBaseURL();

	boolean isIncludeMyRetweetEnabled();

	boolean isJSONStoreEnabled();

	boolean isMBeanEnabled();

	boolean isUserStreamRepliesAllEnabled();

	boolean isUserStreamWithFollowingsEnabled();

	boolean isStallWarningsEnabled();

	String getMediaProvider();

	String getMediaProviderAPIKey();

	Properties getMediaProviderParameters();

	int getAsyncNumThreads();

	long getContributingTo();

	String getDispatcherImpl();

	String getLoggerFactory();

	boolean isIncludeEntitiesEnabled();

	boolean isTrimUserEnabled();

	boolean isIncludeExtAltTextEnabled();

	boolean isDaemonEnabled();

	boolean isIncludeEmailEnabled();

	String getStreamThreadName();

}
