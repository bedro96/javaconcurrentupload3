package com.sec.scloud.file;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobRequestOptions;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.AccessCondition;
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

    public void upload(final InputStream is, final String path, final long length, final String contentType) throws Exception {
        BlobRequestOptions options = new BlobRequestOptions();
        options.setConcurrentRequestCount(1);
        options.setSingleBlobPutThresholdInBytes(SINGLE_BLOB_PUT_THRESHOLD_BYTES);
        options.setTimeoutIntervalInMs(timeout);
        options.setMaximumExecutionTimeInMs(Config.TIMEOUT_MAX);

        CloudBlockBlob blob = container.getBlockBlobReference(path);
        // Adding AccessCondition to contain lease ID
        AccessCondition blobAccessCondition = new AccessCondition();    
         
        while(!blob.exists()){

            System.out.println("\nBlob doesn't exists.");
            System.out.println("\nSetting up the content type and 0 byte on the blob.");
            
            try {
                blob.getProperties().setContentType(contentType);
                blobAccessCondition = AccessCondition.generateIfNoneMatchCondition("*");
                blob.upload(is, length, blobAccessCondition, options, null);
            } catch (final Exception e) {
                e.printStackTrace();
                if (e.getCause() instanceof StorageException) {
                StorageException storageException = (StorageException) e.getCause();
                System.out.println(storageException.getErrorCode());
                //Expected Error Code : ConditionNotMet
                System.out.println(storageException.getExtendedErrorInformation().getErrorMessage());
                //Expected Error Message : The condition specified using HTTP conditional header(s) is not met.
                System.out.println(storageException.getExtendedErrorInformation().getAdditionalDetails());
                continue;
                }           
            }
            Thread.sleep(1000);
        }
        System.out.println("The Blob exists.");              
    }

}
