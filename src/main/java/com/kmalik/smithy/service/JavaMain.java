package com.kmalik.smithy.service;

import java.util.concurrent.CompletableFuture;

public class JavaMain {
    public static void main(String[] args) {
        final MyServiceGen<CompletableFuture> service = new MyServiceCFImpl();

        // Writing object with id 1
        handleOutput(service.myWrite(new MyWriteInput(new MyObject("1", "data_1"))));

        // Reading object with id 1
        handleOutput(service.myRead(new MyReadInput("1")));

        // Reading object with id 2 (should fail)
        handleOutput(service.myRead(new MyReadInput("2")));
    }

    private static void handleOutput(CompletableFuture output) {
        try {
            System.out.println("Output: " + output.get());
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }
}
