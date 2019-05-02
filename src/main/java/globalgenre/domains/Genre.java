package globalgenre.domains;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Genre {
    private final static Logger logger = LoggerFactory.getLogger(Genre.class);

    private Long id;
    private String label;
    private Language language;
    private List<Genre> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Genre> getChildren() {
        return children;
    }

    public void setChildren(List<Genre> children) {
        this.children = children;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return Objects.equals(id, genre.id) &&
                Objects.equals(label, genre.label) &&
                Objects.equals(children, genre.children) &&
                Objects.equals(language, genre.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, children, language);
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", children=" + children +
                ", language=" + language +
                '}';
    }

    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", this.id);
        map.put("label", this.label);
        map.put("children", this.children);
        map.put("language", this.language.toInitial());
        return map;
    }

    public boolean hasChildren(){
        return (this.children != null && !this.children.isEmpty());
    }

    public static Genre fromMap(Map<String, Object> map){
        Genre genre = new Genre();

        Long id = getIdFromMap(map);

        genre.setId(id);
        genre.setLabel(map.getOrDefault("label", null).toString());

        Object languageObj = map.getOrDefault("language", null);
        Language language = Language.fromInitial(languageObj.toString());

        genre.setLanguage(language);

        return genre;
    }

    private static Long getIdFromMap(Map<String, Object> map){
        long id = 0;
        Object idObj = map.getOrDefault("id", null);
        if(idObj != null){
            if(idObj instanceof Long){
                id = (Long) idObj;
            }

            if(idObj instanceof String){
                try {
                    id = Long.parseLong(idObj.toString());
                } catch (Exception e){
                    logger.warn("Error occurred while converting id from string to object. Message : "+ e.getMessage());
                }
            }
        }
        return id;
    }
}
