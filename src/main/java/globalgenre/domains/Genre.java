package globalgenre.domains;

import java.util.List;
import java.util.Objects;

public class Genre {
    private Long id;
    private String label;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return Objects.equals(id, genre.id) &&
                Objects.equals(label, genre.label) &&
                Objects.equals(children, genre.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, children);
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", children=" + children +
                '}';
    }
}
