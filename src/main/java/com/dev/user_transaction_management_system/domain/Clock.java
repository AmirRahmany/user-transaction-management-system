package com.dev.user_transaction_management_system.domain;

import java.time.LocalDateTime;

public interface Clock {

    LocalDateTime currentTime();
}
