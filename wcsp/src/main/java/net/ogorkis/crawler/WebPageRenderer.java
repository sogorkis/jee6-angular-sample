package net.ogorkis.crawler;

import com.google.common.io.ByteStreams;
import net.ogorkis.async.CrawlResponse;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

@Stateless
public class WebPageRenderer {

    private static final String CRAWLJS_COMMAND = "crawljs";

    @Inject
    private Logger logger;

    public CrawlResponse crawlPage(URL url, OutputStream os) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(CRAWLJS_COMMAND, url.toString());

        Process p = pb.start();
        InputStream is = p.getInputStream();

        int returnCode = readReturnCode(is);

        long size = ByteStreams.copy(is, os);

        int processReturnCode = p.waitFor();

        if (processReturnCode != 0) {
              logger.warn("{} returned status code {}", CRAWLJS_COMMAND, processReturnCode);
        }

        return new CrawlResponse(returnCode, size);
    }

    private int readReturnCode(InputStream is) throws IOException {
        byte[] codeInputBuff = new byte[3];

        int read = is.read(codeInputBuff);

        if (read != 3) {
            return -1;
        }

        // skip new line character
        is.skip(1);

        return Integer.valueOf(new String(codeInputBuff));
    }

}
