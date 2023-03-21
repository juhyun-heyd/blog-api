package com.heyd.blogapi.application.port.out;

public interface IncrementKeywordCountRedisOutputPort {

    double incrementScore(String key, String value, double delta);

}
