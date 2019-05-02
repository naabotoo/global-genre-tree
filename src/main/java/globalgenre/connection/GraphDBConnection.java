package globalgenre.connection;

import org.neo4j.driver.v1.AuthToken;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphDBConnection {
    private final static Logger logger = LoggerFactory.getLogger(GraphDBConnection.class);

    private String url;
    private String username;
    private String password;

    public static GraphDBConnection setUp(){
        return new GraphDBConnection();
    }

    private GraphDBConnection(){
        GraphDBConnectionSettings settings = new GraphDBConnectionSettings();

        this.url = settings.getUrl();
        this.username = settings.getUsername();
        this.password = settings.getPassword();
    }

    public Driver getDriver(){
        return GraphDatabase.driver(url, authToken());
    }

    private AuthToken authToken(){
        return AuthTokens.basic(this.username, this.password);
    }
}
