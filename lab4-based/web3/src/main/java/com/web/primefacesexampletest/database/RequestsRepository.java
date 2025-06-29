package com.web.primefacesexampletest.database;

import java.util.List;
import java.util.stream.Stream;

public interface RequestsRepository {

    Stream<Request> findAll();

    void save(Request request);

    List<Request> findSome(int count);

    Stream<Request> findAllHited();

    Stream<Request> findAllHitAndR(Float r);
}
