package OAuthSetup;

public class ConfigurationContext {
    private static final String DEFAULT_CONFIGURATION_FACTORY = "twitter4j.conf.PropertyConfigurationFactory";
    private static final String CONFIGURATION_IMPL = "twitter4j.configurationFactory";
    private static final ConfigurationFactory factory;

    static {
        String CONFIG_IMPL;
        try {
            CONFIG_IMPL = System.getProperty(CONFIGURATION_IMPL, DEFAULT_CONFIGURATION_FACTORY);
        } catch (SecurityException ignore) {
            CONFIG_IMPL = DEFAULT_CONFIGURATION_FACTORY;
        }

        try {
            factory = (ConfigurationFactory) Class.forName(CONFIG_IMPL).newInstance();
        } catch (ClassNotFoundException cnfe) {
            throw new AssertionError(cnfe);
        } catch (InstantiationException ie) {
            throw new AssertionError(ie);
        } catch (IllegalAccessException iae) {
            throw new AssertionError(iae);
        }
    }


    public static IConfiguration getInstance() {
        return factory.getInstance();
    }

    public static IConfiguration getInstance(String configTreePath) {
        return factory.getInstance(configTreePath);
    }
}
