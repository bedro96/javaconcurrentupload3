## java concurrent uploader

To compile 
```sh 
mvn compile
```
To run 
```sh
sh ./run.sh
```
Actual command in run.sh.
```sh
mvn exec:java -Dexec.mainClass="com.sec.scloud.file.App" -e
``` 
To run from jar file, execute the following command. 
```sh
java -jar target/blob-problem-0.1-jar-with-dependencies.jar
```
## What has been changed from the original file.

1. upload() method for BlobClient class has been modified to start the upload regardless of etag.
One exception, BlobAlreadyExists, is given since the there will be multiple threads trying to write on the same blob.
Only one of these thread will be successful writing the whole block. Rest of them will fail with BlobAlreadyExists error.
2. UPLOAD_BYTES and NUM_OF_THREAD variables in Config.java has been increased.
3. CONNECTION_STRING variable in Config.java is reading from system variable. 


## Code Highlight
```sh
blob.getProperties().setContentType(contentType);
blobAccessCondition = AccessCondition.generateIfNoneMatchCondition("*");
blob.upload(is, length, blobAccessCondition, options, null);
```
