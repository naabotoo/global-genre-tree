package globalgenre;

import globalgenre.domains.Genre;
import globalgenre.domains.Language;
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

        Genre genre = new Genre();
        genre.setLabel("rocket");
        genre.setLanguage(Language.ENGLISH);

        genre = genreService.addGenre(genre);

        logger.info("created genre : "+ ((genre != null) ? genre.toString() : "None"));

        logger.info("about to get total count of genres.");
        Long totalCount = genreService.countGenre();
        logger.info("total count of genres : "+ totalCount);

        String label = "fly";
        Genre foundGenre = genreService.getByLabel(label);

        logger.info("Found genre with label : "+ label
                + " : "+ ((foundGenre != null) ? foundGenre.toString() : null));


        boolean isDeleted = genreService.deleteGenre(foundGenre);
        logger.info("Is deleted genre deleted : "+ isDeleted);

        logger.info("done with running global genre application.");
    }
}
