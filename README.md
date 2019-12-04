## javaconcurrentupload

To compile 
```sh 
mvn compile
```
To run 
```sh
sh ./run.sh
```
Actual command in run.sh
```sh
mvn exec:java -Dexec.mainClass="com.sec.scloud.file.App" -e
``` 

## What has been changed from the original file.
1. upload() method for BlobClient class has been modified to aquire lease for the blob.
Some exceptions are given since the there will be multiple thread trying to write on the same blob.
Only one of these thread will be successful writing the whole block, and others will loop again.
2. UPLOAD_BYTES and NUM_OF_THREAD variables in Config.java has been increased.    