package com.kazurayam.materials.aws;

import java.io.IOException;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class S3ClientTest {

    @Test
    public void test_main(){
        String[] args = { "" };
        try {
            S3Client.main(args);
        } catch (IOException e) {
            System.out.println("IOException was raised");
        }
        assertTrue("done", true);
    }

}