package com.kazurayam.materials.aws;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/*
 * Create your credentials file at ~/.aws/credentials (C:\Users\USER_NAME\.aws\credentials for Windows users)
 * and save the following lines after replacing the underlined values with your own.
 *
 * [default]
 * aws_access_key_id = YOUR_ACCESS_KEY_ID
 * aws_secret_access_key = YOUR_SECRET_ACCESS_KEY
 */
public class S3Client {

    private AmazonS3 s3_;
    private static final Region awsRegion_ = Region.getRegion(Regions.US_WEST_2);
    private static final String bucketName_ = "my-first-s3-bucket-" + UUID.randomUUID();
    private static final String key_ = "MyObjectKey";

    static Logger logger_ = LoggerFactory.getLogger(S3Client.class);

    public S3Client() {
        s3_ = new AmazonS3Client();
        s3_.setRegion(awsRegion_);

    }

    public void execute() throws IOException {
        logger_.info("=============================================");
        logger_.info("Getting Started with Amazon S3");
        logger_.info("=============================================\n");

        try {
            /*
             * Create a new S3 bucket
             */
            logger_.info("Creating bucket " + bucketName_);
            s3_.createBucket(bucketName_);

            /*
             * List the bucket in your account
             */
            logger_.info("Listing buckets");
            for (Bucket bucket : s3_.listBuckets()) {
                logger_.info(" - " + bucket.getName());
            }
            logger_.info("");

            /*
             * Upload and object to your bucket
             */
            logger_.info("Uploading a new object to S3 from a file");
            s3_.putObject(new PutObjectRequest(bucketName_, key_, createSampleFile()));

            /*
             * Download an object
             */
            logger_.info("Downloading an object");
            S3Object object = s3_.getObject(new GetObjectRequest(bucketName_, key_));
            logger_.info("Content-Type: " + object.getObjectMetadata().getContentType());
            displayTextInputStream(object.getObjectContent());

            /*
             * List objects in your bucket by prefix
             */
            logger_.info("Listing objects");
            ObjectListing objectListing =
                    s3_.listObjects(new ListObjectsRequest()
                            .withBucketName(bucketName_)
                            .withPrefix("My"));
            for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                logger_.info(" - " + objectSummary.getKey() + " " +
                        "(size = " + objectSummary.getSize() + ")");
            }
            logger_.info("");

            /*
             * Delete an object
             */
            logger_.info("Deleting an object");
            s3_.deleteObject(bucketName_, key_);

            /*
             * Delete a bucket
             */
            logger_.info("Deleting bucket " + bucketName_);
            s3_.deleteBucket(bucketName_);

        } catch (AmazonServiceException ase) {
            logger_.error("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            logger_.error("Error Message:    " + ase.getMessage());
            logger_.error("HTTP Status Code: " + ase.getStatusCode());
            logger_.error("AWS Error Code:   " + ase.getErrorCode());
            logger_.error("Error Type:       " + ase.getErrorType());
            logger_.error("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            logger_.error("Caught an AmazonClientException, which means the client encountered "
                    + "a searious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the netwoark.");
            logger_.error("Error Message: " + ace.getMessage());
        }

    }

    /**
     * Create a temporary file with text data to demonstrate uploading a file
     * to Amazon S3
     *
     * @return A newly created temporary file with text data.
     * @throws IOException
     */
    private static File createSampleFile() throws IOException {
        File file = File.createTempFile("aws-java-sdk-", ".txt");
        file.deleteOnExit();

        Writer writer = new OutputStreamWriter(new FileOutputStream(file));
        writer.write("abcdefghijklmnopqrstuvwxyz\n");
        writer.write("01234567890123456789012345\n");
        writer.write("!@#$%^&*()-=[]{};':',.<>/?\n");
        writer.write("01234567890123456789012345\n");
        writer.write("abcdefghijklmnopqrstuvwxyz\n");
        writer.close();
        return file;
    }

    private static void displayTextInputStream(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;
            logger_.info("    " + line);
        }
        logger_.info("");
    }

    public static void main(String[] args) throws IOException {
        S3Client s3client = new S3Client();
        s3client.execute();
    }
}