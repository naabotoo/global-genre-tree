package globalgenre;

import globalgenre.domains.Genre;
import globalgenre.services.GenreService;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainExec {
    private final static Logger logger = LoggerFactory.getLogger(MainExec.class);

    public static void main(String[] args){
        logger.info("about to start global genre application.");

        GenreService genreService = new GenreService();
        Observable<Genre> genreObservable = genreService.genreObservable();

        Disposable disposable = genreObservable.subscribe( genre -> { logger.info("Genre : "+ genre.toString() );},
                throwable -> logger.warn(throwable.getMessage()));

        disposable.dispose();


        logger.info("done with running global genre application.");
    }
}
