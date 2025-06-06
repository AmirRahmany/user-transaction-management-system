package com.dev.user_transaction_management_system;

import com.dev.user_transaction_management_system.domain.user.User;
import com.dev.user_transaction_management_system.test_builder.UserTestBuilder;
import com.dev.user_transaction_management_system.helper.UserAccountTestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.dev.user_transaction_management_system.test_builder.UserTestBuilder.aUser;

@Component
public class UserAccountFixture {

    @Autowired
    private UserAccountTestHelper util;

    public UserAndToken havingRegisteredUserWithToken(String email, String password) {
        final UserTestBuilder userTestBuilder = aUser()
                .withEmail(email)
                .withPassword(password);

        return createUserWithToken(email, password, userTestBuilder);
    }

    public UserAndToken givenActivatedSignedInUserWithToken(){
        String username = "jacid20853@besibali.com";
        String password = "@Abcd137824";
        final UserTestBuilder userTestBuilder = aUser()
                .withEnabledStatus()
                .withEmail(username)
                .withPassword(password);

        return createUserWithToken(username,password, userTestBuilder);
    }

    private UserAndToken createUserWithToken(String email, String password, UserTestBuilder userTestBuilder) {
        final var user = util.havingRegistered(userTestBuilder);
        var token = util.signIn(email, password);
        return new UserAndToken(user, token);
    }

    public record UserAndToken(User user, String token) {}
}
