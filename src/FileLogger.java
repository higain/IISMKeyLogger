/**
 * Created by maxis on 07.12.2017.
 */

import java.io.*;
import java.nio.file.*;
import java.util.Date;

public class FileLogger {
    public Path p;

    public FileLogger() {

        String dateiname = "log/" + System.currentTimeMillis() + "_keylog.txt";
        File datei = new File(dateiname);
        datei.getParentFile().mkdirs();

        p = Paths.get(dateiname);
        try {
            Files.createFile(p);
        } catch (FileAlreadyExistsException ignored) {
        } catch (IOException ioe) {

        }
        appendToLog("Start: " + new Date());
    }

    public void appendToLog(String text) {
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p, StandardOpenOption.APPEND))) {
            out.write(text.getBytes());
            out.write(System.lineSeparator().getBytes());

        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
