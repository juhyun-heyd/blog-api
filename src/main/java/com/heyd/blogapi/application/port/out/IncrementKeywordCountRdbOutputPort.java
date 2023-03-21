package com.heyd.blogapi.application.port.out;

public interface IncrementKeywordCountRdbOutputPort {

    long insertOrUpdateKeyword(String keyword);

}
