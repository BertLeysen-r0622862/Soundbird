package be.wienert.soundbird.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
    public static long copy(InputStream inputStream, File destination, long maxSize) throws IOException {
        long size = 0;
        try (FileOutputStream outStream = new FileOutputStream(destination)) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0 && size < maxSize) {
                outStream.write(buf, 0, len);
                size += len;
            }
        } finally {
            inputStream.close();
            if (size >= maxSize) {
                destination.delete();
            }
        }
        return size;
    }

    public static long copy(InputStream inputStream, File destination) throws IOException {
        return copy(inputStream, destination, Long.MAX_VALUE);
    }
}
