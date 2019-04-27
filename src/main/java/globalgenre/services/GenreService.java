package globalgenre.services;

import globalgenre.domains.Genre;
import io.reactivex.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GenreService {
    private final static Logger logger = LoggerFactory.getLogger(GenreService.class);

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

        Genre genre = new Genre();
        genre.setId(1L);
        genre.setLabel("electronics");
        genres.add(genre);

        Genre genre1 = new Genre();
        genre1.setId(2L);
        genre1.setLabel("shoe");
        genres.add(genre1);

        Genre genre2 = new Genre();
        genre2.setId(3L);
        genre2.setLabel("book");
        genres.add(genre2);

        return genres;
    }
}
