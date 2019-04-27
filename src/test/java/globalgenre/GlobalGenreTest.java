package globalgenre;

import io.reactivex.Observable;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertTrue;

public class GlobalGenreTest {

    @Test
    public void returnValue(){
        AtomicReference<String> result = new AtomicReference<>("");
        Observable<String> stringObservable = Observable.just("hello");
        stringObservable.subscribe(s -> result.set(s));
        assertTrue(result.get().equals("hello"));
    }
}
