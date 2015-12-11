package com.example.bookshelf.dependencies;

/**
 * Created by jls on 6/19/15.
 */
public interface QueryCollector {

    // INSERT QUERY
    int insert();

    // SELECT QUERY
    int select();

    // UPDATE QUERY
    int update();

    // DELETE QUERY
    int delete();
}
