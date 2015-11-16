package core.io;

import java.io.IOException;

/**
 * @author Mikhail Boldinov
 */
public interface IReader<T> {
    T read() throws IOException;
}
