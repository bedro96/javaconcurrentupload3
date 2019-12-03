package com.sec.scloud.file;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import java.io.InputStream;

public class BlobClient {
    private static final int SINGLE_BLOB_PUT_THRESHOLD_BYTES = 5 * 1024 * 1024;
    private static final int timeout = 60000;

    private CloudBlobContainer container;

    public BlobClient() {
        try {
            CloudStorageAccount account = CloudStorageAccount.parse(Config.CONNECTION_STRING);
            CloudBlobClient serviceClient = account.createCloudBlobClient();

            container = serviceClient.getContainerReference(Config.CONTAINER_NAME);
            container.createIfNotExists();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void upload(InputStream is, String path, long length, String contentType) throws Exception {
        try {
            BlobRequestOptions options = new BlobRequestOptions();
            options.setConcurrentRequestCount(1);
            options.setSingleBlobPutThresholdInBytes(SINGLE_BLOB_PUT_THRESHOLD_BYTES);
            options.setTimeoutIntervalInMs(timeout);
            options.setMaximumExecutionTimeInMs(Config.TIMEOUT_MAX);

            CloudBlockBlob blob = container.getBlockBlobReference(path);

            blob.getProperties().setContentType(contentType);
            blob.upload(is, length, null, options, null);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getCause() instanceof StorageException) {
                StorageException storageException = (StorageException) e.getCause();
                System.out.println(storageException.getErrorCode());
                System.out.println(storageException.getExtendedErrorInformation().getErrorMessage());
                System.out.println(storageException.getExtendedErrorInformation().getAdditionalDetails());
            }
            throw e;
        }
    }

}
