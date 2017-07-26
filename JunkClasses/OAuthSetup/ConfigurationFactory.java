package OAuthSetup;

public interface ConfigurationFactory {

	IConfiguration getInstance();

	IConfiguration getInstance(String configTreePath);

	void dispose();
}
