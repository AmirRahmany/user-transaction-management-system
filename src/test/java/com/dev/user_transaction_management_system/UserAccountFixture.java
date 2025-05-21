package com.dev.user_transaction_management_system;

import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.fake.UserFakeBuilder;
import com.dev.user_transaction_management_system.helper.UserAccountTestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.dev.user_transaction_management_system.fake.UserFakeBuilder.aUser;

@Component
public class UserAccountFixture {
    @Autowired
    private UserAccountTestUtil util;

    public UserAndToken havingRegisteredUserWithToken(String email, String password) {
        final UserFakeBuilder userFakeBuilder = aUser()
                .withEmail(email)
                .withPassword(password);

        return createUserWithToken(email, password, userFakeBuilder);
    }

    public UserAndToken givenActivatedSignedInUserWithToken(){
        String username = "amirrahmani7017@gamial.com";
        String password = "@Abcd137824";
        final UserFakeBuilder userFakeBuilder = aUser()
                .withEnabledStatus()
                .withEmail(username)
                .withPassword(password);

        return createUserWithToken(username,password,userFakeBuilder);
    }

    private UserAndToken createUserWithToken(String email, String password, UserFakeBuilder userFakeBuilder) {
        final var user = util.havingRegistered(userFakeBuilder);
        var token = util.signIn(email, password);
        return new UserAndToken(user, token);
    }

    public record UserAndToken(User user, String token) {}
}
