package globalgenre.domains;

import org.neo4j.driver.internal.shaded.io.netty.util.internal.StringUtil;

import java.util.Arrays;

public enum Language {
    ENGLISH("en"), FRENCH("fr"), SPANISH("en");

    private String initial;

    Language(String initial){
        this.initial = initial;
    }

    @Override
    public String toString(){
        return this.initial;
    }

    public String toInitial(){
        return this.initial;
    }

    public static Language fromInitial(String initial){
        Language language = null;
        if(!StringUtil.isNullOrEmpty(initial)){
            language = Arrays.stream(Language.values()).filter(l -> initial.equalsIgnoreCase(l.initial)).findFirst().orElse(null);
        }
        return language;
    }
}
