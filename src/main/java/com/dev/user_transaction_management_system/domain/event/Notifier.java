package com.dev.user_transaction_management_system.domain.event;

import com.dev.user_transaction_management_system.domain.user.Email;

public interface Notifier {

    void sendSimpleMessage(Subject subject, Message message, Email to);
}
