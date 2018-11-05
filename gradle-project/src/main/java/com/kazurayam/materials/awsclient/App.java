package com.kazurayam.materials.awsclient;

public class App {

    public String sayHelloTo(String to) {
        return "Hello, " + to + "!";
    }

    public static void main(String[] args) {
        App app = new App();
        if (args.length > 0) {
            System.out.println(app.sayHelloTo(args[0]));
        }
    }

}