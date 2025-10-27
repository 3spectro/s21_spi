package modelo;
import java.util.Objects;

public abstract class EntidadBase {
    protected Long id;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EntidadBase)) return false;
        EntidadBase that = (EntidadBase) o;
        return Objects.equals(id, that.id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}
