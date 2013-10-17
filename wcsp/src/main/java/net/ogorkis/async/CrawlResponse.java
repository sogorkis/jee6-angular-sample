package net.ogorkis.async;

import com.google.common.io.InputSupplier;

import java.io.InputStream;

public class CrawlResponse {

    private final int returnCode;
    private final long byteSize;

    public CrawlResponse(int returnCode, long byteSize) {
        this.returnCode = returnCode;
        this.byteSize = byteSize;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public long getByteSize() {
        return byteSize;
    }
}
