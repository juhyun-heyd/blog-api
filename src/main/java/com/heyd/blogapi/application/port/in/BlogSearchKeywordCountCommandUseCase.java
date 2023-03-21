package com.heyd.blogapi.application.port.in;

public interface BlogSearchKeywordCountCommandUseCase {

    void command(String keyword) throws InterruptedException;

}
