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

1. upload() method for BlobClient class has been modified to aquire lease for the blob.
Some exceptions are given since the there will be multiple threads trying to write on the same blob.
Only one of these thread will be successful writing the whole block, and others will loop again.
2. UPLOAD_BYTES and NUM_OF_THREAD variables in Config.java has been increased.
3. CONNECTION_STRING variable in Config.java is reading from system variable. 


## Code Highlight
```sh
blob.getProperties().setContentType(contentType);
//Creating 0 byte empty blob, since eTag only created after the blob is created.
blob.upload(is, 0, null, options, null);
System.out.println("Etags after creating 0 byte blob : " + blob.getProperties().getEtag());
//The result is something like "0x8D779465B2AD7CB"
blobAccessCondition = AccessCondition.generateIfMatchCondition(blob.getProperties().getEtag());
System.out.println("Writing full content with known eTag.");
blob.upload(is, length, blobAccessCondition, options, null);
```
