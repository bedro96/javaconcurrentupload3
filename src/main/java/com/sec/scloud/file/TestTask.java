package com.sec.scloud.file;

import java.io.ByteArrayInputStream;
import java.util.concurrent.Callable;

public class TestTask implements Callable<Boolean> {
    private int id;
    private byte[] bytes;
    private BlobClient blobClient;

    public TestTask(int id, byte[] bytes, BlobClient blobClient) {
        this.id = id;
        this.bytes = bytes;
        this.blobClient = blobClient;
    }

    @Override
    public Boolean call() {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            String path = "ebac/testcid123/ebac94ed7b73e61b95b6437171825d9702d994e08625d9aacf226856a7c983ba";
            blobClient.upload(bis, path, bytes.length, "application/octet-stream");
            System.out.println("upload complete "+ id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
