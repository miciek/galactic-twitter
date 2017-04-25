package com.michalplachta.galactic.java.db;

import com.michalplachta.galactic.java.values.Citizen;
import javaslang.collection.List;
import javaslang.concurrent.Future;
import javaslang.concurrent.Promise;

public class DbClient {
    public static Future<Citizen> findCitizenByName(String name) {
        return FakeData.citizens.find(citizen -> citizen.getName().equals(name))
                .map(DbClient::simulateResponse)
                .getOrElse(simulateBadRequest("citizen with name $name couldn't be found"));
    }

    public static Future<List<Citizen>> getFollowers(Citizen citizen) {
        return simulateResponse(FakeData.citizens);
    }

    private static <R> Future<R> simulateResponse(R result) {
        Promise<R> promise = Promise.make();
        Future.run(() -> {
            Thread.sleep(4000);
            promise.success(result);
        });
        return promise.future();
    }

    private static <R> Future<R> simulateBadRequest(String error) {
        Promise<R> promise = Promise.make();
        Future.run(() -> {
            Thread.sleep(2000);
            promise.failure(new Exception("Bad Request: " + error));
        });
        return promise.future();
    }
}
