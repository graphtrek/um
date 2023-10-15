package co.grtk.um.utils.cache;

import javax.cache.annotation.GeneratedCacheKey;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TstGeneratedCacheKey implements GeneratedCacheKey {
    private static final long serialVersionUID = 1L;
    private final Object[] parameters;
    private final int hashCode;

    public TstGeneratedCacheKey(Object[] parameters) {
        this.parameters = parameters;
        this.hashCode = Arrays.deepHashCode(parameters);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public String toString() {
        return Arrays.stream(parameters).map(Object::toString).collect(Collectors.joining(""));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        if (this.hashCode != obj.hashCode())
            return false;
        TstGeneratedCacheKey other = (TstGeneratedCacheKey) obj;
        return Arrays.deepEquals(this.parameters, other.parameters);
    }
}
