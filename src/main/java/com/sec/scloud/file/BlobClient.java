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
            // Adding lines from here 
            
            String leaseID = "";
            String leaseID2 = "";
            
            System.out.println("\n\tAcquiring a lease on the blog to prevent writes and deletes.");
            blob.breakLease(0);
            blob.upload(is, 0, null, options, null);
            // leaseID = blob.acquireLease(15,null);
            
            try {
                leaseID = blob.acquireLease(15,null);
                System.out.println(String.format("\t\tSuccessfully acquired a lease on blob %s. Lease state: %s, Lease id: %s.", blob.getName(), blob.getProperties().getLeaseStatus().toString(), leaseID));
                blob.getProperties().setContentType(contentType);
                blob.upload(is, length, null, options, null);
            } catch (Exception e) {
                e.printStackTrace();
                if(e.getCause() instanceof StorageException) {
                    StorageException storageException = (StorageException) e.getCause();
                    System.out.println("This is the errorcode");
                    System.out.println(storageException.getErrorCode());
                }
            }
            finally {
                blob.breakLease(0);
                System.out.println(String.format("\t\tSuccessfully broke the lease on blob %s. Lease state: %s.", blob.getName(), blob.getProperties().getLeaseStatus().toString()));
            }
            // leaseID2 = blob.acquireLease(15,null);
            
            // blob.breakLease(0);
            
 
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
