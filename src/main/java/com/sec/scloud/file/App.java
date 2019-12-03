package com.sec.scloud.file;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class App
{
    public static void main( String[] args )
    {
        byte[] bytes = new byte[Config.UPLOAD_BYTES];
        Random random = new Random();
        random.nextBytes(bytes);

        BlobClient blobClient = new BlobClient();

        int threadSize = Config.NUM_OF_THREAD;
        ExecutorService executorService = Executors.newFixedThreadPool(threadSize);
        List<Future<Boolean>> futureList = new ArrayList<>();
        for(int i=0; i<threadSize; i++) {
            futureList.add(executorService.submit(new TestTask(i, bytes, blobClient)));
        }

        for(Future<Boolean> res : futureList) {
            try {
                res.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
    }
}
