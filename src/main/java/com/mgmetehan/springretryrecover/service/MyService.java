package com.mgmetehan.springretryrecover.service;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@Slf4j
public class MyService {

    @Retryable(retryFor = SQLException.class)
    public void callExternalService(String sql) throws SQLException {
        if (StringUtils.isEmpty(sql)) {
            log.info("throw RuntimeException in method callExternalService()");
            throw new SQLException();
        }
    }

    @Recover
    public void recover(SQLException e, String sql) {
        log.info("In recover method");
    }
}
