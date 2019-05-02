package globalgenre.services;

import globalgenre.connection.GraphDBConnection;
import globalgenre.domains.Genre;
import io.reactivex.Observable;
import org.neo4j.driver.internal.shaded.io.netty.util.internal.StringUtil;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Statement;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GenreService {
    private final static Logger logger = LoggerFactory.getLogger(GenreService.class);


    public Genre getByLabel(String label){
        return findByLabel(label);
    }

    public boolean deleteGenre(Genre genre){
        return deleteByGenre(genre);
    }

    private boolean deleteByGenre(Genre genre){
        boolean isDeleted = false;
        if(genre != null){
            Driver driver = GraphDBConnection.setUp().getDriver();

            if(driver != null){
                try {
                    isDeleted = executeDeleteGenre(driver, genre);
                } catch (Exception e){
                    logger.warn("Error occurred while deleting genre. Message : "+ e.getMessage());
                } finally {
                    try {
                        driver.close();
                    } catch (Exception e){
                        logger.warn("Error occurred while closing graph db connection. Message : "+ e.getMessage());
                    }
                }
            }
        }
        return isDeleted;
    }

    private boolean executeDeleteGenre(Driver driver, Genre genre){
        boolean isDeleted = false;
        if(genre != null){
            Session session = driver.session();

            isDeleted = session.readTransaction(new TransactionWork<Boolean>() {
                @Override
                public Boolean execute(Transaction transaction) {
                    String query = "match (g:Genre) where g.label = $label delete g";

                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("label", genre.getLabel());

                    Statement statement = new Statement(query, parameters);
                    StatementResult statementResult = transaction.run(statement);

                    logger.info("delete genre : "+ genre.toString() + " : "
                            + ((statementResult != null) ? statementResult.toString() : "None"));

                    return true;
                }
            });
        }
        return isDeleted;
    }

    private Genre findByLabel(String label){
        Genre genre = null;
        if(!StringUtil.isNullOrEmpty(label)){
            Driver driver = GraphDBConnection.setUp().getDriver();
            if(driver != null){
                try {
                    genre = executeFindByLabel(driver, label);
                } catch (Exception e){
                    logger.warn("Error occurred while finding genre by label. Message : "+ e.getMessage());
                } finally {
                    try {
                        driver.close();
                    } catch (Exception e){
                        logger.warn("Error occurred while closing driver connection. Message : "+ e.getMessage());
                    }
                }
            }
        }
        return genre;
    }

    private Genre executeFindByLabel(Driver driver, String label){
        Genre genre = null;
        Session session = driver.session();
        genre = session.readTransaction(new TransactionWork<Genre>() {
            @Override
            public Genre execute(Transaction transaction) {
                String query = "match (g:Genre) where g.label = $label return g";
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("label", label);

                StatementResult statementResult = transaction.run(query, parameters);
                boolean hasNext = statementResult.hasNext();

                return (hasNext) ? getGenreFromRecord(statementResult.single()) : null;
            }
        });
        return genre;
    }

    public Long countGenre(){
        long count = 0;
        Driver driver = GraphDBConnection.setUp().getDriver();
        if(driver != null){
            try {
                count = getGenreTotalCount(driver);
            } catch (Exception e){
                logger.warn("Error occurred while getting count of genres. Message : "+ e.getMessage());
            } finally {
                try {
                    driver.close();
                } catch (Exception e){
                    logger.warn("Error occurred while closing driver. Message : "+ e.getMessage());
                }
            }
        }
        return count;
    }

    public Genre addGenre(Genre genre){
        if(genre != null){
            Driver driver = GraphDBConnection.setUp().getDriver();
            if(driver != null){
                try {
                    genre = persistGenre(driver, genre);
                } catch (Exception e){
                    logger.warn("Error occurred while persisting genre in GraphDB. Message : "+ e.getMessage());
                } finally {
                    try {
                        driver.close();
                    } catch (Exception e){
                        logger.warn("Error occurred while closing driver. Message : "+ e.getMessage());
                    }
                }
            }
        }
        return genre;
    }

    private Genre persistGenre(Driver driver, Genre genre){
        Genre createdGenre = null;
        if(driver != null){
            Session session = driver.session();

            final String createStatement = "CREATE (g:Genre) SET g.label = $label, g.language = $language RETURN g";

            createdGenre = session.writeTransaction(new TransactionWork<Genre>() {
                @Override
                public Genre execute(Transaction transaction) {
                    StatementResult statementResult = transaction.run(createStatement, genre.toMap());

                    Genre createdGenre = getGenreFromRecord(statementResult.single());

                    logger.info("created genre result : " + createdGenre.toString());

                    return createdGenre;
                }
            });
        }
        return createdGenre;
    }

    public Observable<Genre> genreObservable(){
        return Observable.create(emitter -> {
            try {
                List<Genre> genreList = getGenreList();

                if(!genreList.isEmpty()) {
                    genreList.forEach(emitter::onNext);
                }

                emitter.onComplete();
            } catch (Exception e){
                emitter.onError(e);
            }
        });
    }

    private List<Genre> getGenreList(){
        List<Genre> genres = new ArrayList<>();

        Driver driver = GraphDBConnection.setUp().getDriver();
        if(driver != null){
            try {
                Session session = driver.session();

                String query = "MATCH (g:Genre) RETURN g";
                session.readTransaction(new TransactionWork<Object>() {
                    @Override
                    public Object execute(Transaction transaction) {

                        StatementResult statementResult = transaction.run(query);

                        List<Genre> genresFound = statementResult.stream().map( record -> {
                            return getGenreFromRecord(record);
                        }).collect(Collectors.toList());

                        if(!genresFound.isEmpty()){
                            genres.addAll(genresFound);
                        }

                        return null;
                    }
                });
            } catch (Exception e){
                logger.warn("Error occurred while getting a list of genres from graph db. Message : "+ e.getMessage());
            } finally {
                try {
                    driver.close();
                } catch (Exception e){
                    logger.warn("Error occurred while closing Graph DB connection. Message : "+ e.getMessage());
                }
            }
        }
        return genres;
    }

    private long getGenreTotalCount(Driver driver){
        long count = 0;
        Session session = driver.session();
        count = session.readTransaction(new TransactionWork<Long>() {
            @Override
            public Long execute(Transaction transaction) {
                String query = "match (g:Genre) return count(*) as totalCount";
                StatementResult statementResult = transaction.run(query);
                return statementResult.single().get(0).asLong();
            }
        });
        return count;
    }

    private Genre getGenreFromRecord(Record record){
        long id = record.get(0).asNode().id();

        Map<String, Object> genreMap = new HashMap<>(record.get(0).asMap());
        genreMap.put("id", id);

        return Genre.fromMap(genreMap);
    }
}
