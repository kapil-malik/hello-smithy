package com.kmalik.smithy.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MyServiceCFImpl implements MyServiceGen<CompletableFuture> {

    private final Map<String, MyObject> _db = new HashMap<>();

    @Override
    public CompletableFuture<MyReadOutput> myRead(MyReadInput input) {
        return CompletableFuture.supplyAsync(() -> {
            if (!_db.containsKey(input.id())) {
                throw new RuntimeException("Object with id " + input.id() + " not found");
            }
            return new MyReadOutput(_db.get(input.id()));
        });
    }

    @Override
    public CompletableFuture<MyWriteOutput> myWrite(MyWriteInput input) {
        return CompletableFuture.supplyAsync(() -> {
            final MyObject inputObj = input.obj();
            final String id = inputObj.id();
            _db.put(id, inputObj);
            return new MyWriteOutput(true);
        });
    }
}
