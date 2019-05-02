package globalgenre.connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

public class GraphDBConnectionSettings {
    private final static Logger logger = LoggerFactory.getLogger(GraphDBConnectionSettings.class);
    private static String APPLICATION_PROPERTIES = "application.properties";

    private String url;
    private String username;
    private String password;

    public GraphDBConnectionSettings() {
        this.url = getUrlProperties();
        this.username = getUsernameProperties();
        this.password = getPasswordProperties();
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    private InputStream getResourceAsInputStream(){
        ClassLoader classLoader = this.getClass().getClassLoader();
        return classLoader.getResourceAsStream(APPLICATION_PROPERTIES);
    }

    private Properties getProperties(){
        Properties properties = new Properties();
        InputStream inputStream = getResourceAsInputStream();
        if(inputStream != null){
            try {
                properties.load(inputStream);
            } catch (Exception e){
                logger.warn("Error occurred while getting application properties from input stream. Message : "
                        + e.getMessage());
            } finally {
                try {
                    inputStream.close();
                } catch (Exception e){
                    logger.warn("Error occurred while closing input stream. Message : "+ e.getMessage());
                }
            }
        }
        return properties;
    }

    private String getPasswordProperties(){
        String key = "graph.password";
        return getProperties().getProperty(key);
    }

    private String getUsernameProperties(){
        String key = "graph.username";
        return getProperties().getProperty(key);
    }

    private String getUrlProperties(){
        String key = "graph.url";
        return getProperties().getProperty(key);
    }
}
