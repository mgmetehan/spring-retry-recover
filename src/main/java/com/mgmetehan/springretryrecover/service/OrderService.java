package com.mgmetehan.springretryrecover.service;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
@Slf4j
public class OrderService {

    @Retryable(retryFor = SQLException.class)
    public void processOrder(String orderDetails) throws SQLException {
        if (StringUtils.isEmpty(orderDetails)) {
            log.info("Throwing SQLException in method processOrder()");
            throw new SQLException();
        }
    }

    @Recover
    public void recoverOrderProcess(SQLException e, String orderDetails) {
        log.info("In recoverOrderProcess method");
    }

    @Retryable(retryFor = SQLException.class, recover = "recoverOrderProcessWithBackoff", noRetryFor = RuntimeException.class, maxAttempts = 5,
            backoff = @Backoff(delay = 2000, multiplier = 2, maxDelay = 30000))
    public void processOrderWithBackoff(String orderDetails) throws SQLException {
        if (StringUtils.isEmpty(orderDetails)) {
            log.info("Throwing SQLException in method processOrderWithBackoff()");
            throw new SQLException();
        }
    }

    @Recover
    public void recoverOrderProcessWithBackoff(SQLException e, String orderDetails) {
        log.info("In recoverOrderProcessWithBackoff method");
    }

}
