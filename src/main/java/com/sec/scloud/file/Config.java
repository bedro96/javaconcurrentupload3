package com.sec.scloud.file;

public class Config {
    public static final int UPLOAD_BYTES = 1024 * 1024 * 20;
    public static final int NUM_OF_THREAD = 1;

    public static final int TIMEOUT_MAX = 120000;

    public static final String CONNECTION_STRING = "DefaultEndpointsProtocol=https;AccountName=javaconcurrentstg;AccountKey=sD3lNqoiIQEYUDKJH5VWttUvAnE7jaabGpdm79sFDWFHTWAzTyBUSWLYFhV+aaXI4pyV4eHPixQtid8RUBE1vQ==;EndpointSuffix=core.windows.net";
    public static final String CONTAINER_NAME = "javaconcurrentblockblob";
}
