package com.kazurayam.materials.aws;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class AppTest {

    @Test
    public void test_sayHelloTo() {
        App app = new App();
        String greeting = app.sayHelloTo("Ume-chan");
        assertEquals("greeting=" + greeting + " is not expected", "Hello, Ume-chan!", greeting);
    }

}
