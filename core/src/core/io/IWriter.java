package core.io;

import java.io.IOException;

/**
 * @author Mikhail Boldinov
 */
public interface IWriter<T> {
    void write(T writeable) throws IOException;
}
