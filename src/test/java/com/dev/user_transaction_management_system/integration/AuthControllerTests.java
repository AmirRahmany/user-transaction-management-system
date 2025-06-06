package com.dev.user_transaction_management_system.integration;

import com.dev.user_transaction_management_system.domain.event.Message;
import com.dev.user_transaction_management_system.domain.event.Subject;
import com.dev.user_transaction_management_system.domain.event.Notifier;
import com.dev.user_transaction_management_system.domain.user.Email;
import com.dev.user_transaction_management_system.helper.UserAccountTestHelper;
import com.dev.user_transaction_management_system.use_case.authenticate_user.LoginRequest;
import com.dev.user_transaction_management_system.use_case.register_user_account.UserRegistrationRequest;
import com.dev.user_transaction_management_system.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.dev.user_transaction_management_system.test_builder.UserTestBuilder.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Tag("INTEGRATION")
class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAccountTestHelper accountTestUtil;

    @MockitoSpyBean
    @Qualifier("fakeEmailNotifier")
    private Notifier emailNotifier;


    @Test
    void register_user_successfully() throws Exception {
        final String email = "amir.ssofi@gmail.com";
        final UserRegistrationRequest userRegistrationRequest = aUser()
                .withFirstName("amir")
                .withLastName("sofi")
                .withEmail(email)
                .withPassword("@Abcd123")
                .withPhoneNumber("09214567845").buildDTO();

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationRequest)))
                .andExpect(status().isCreated());

        final boolean isUserExisted = userRepository.isUserAlreadyExists(Email.of(email));

        assertThat(isUserExisted).isTrue();
        then(emailNotifier).should(atLeastOnce()).sendSimpleMessage(any(Subject.class),any(Message.class),any(Email.class));
    }

    @Test
    void sign_in_user_successfully() throws Exception {
        final String username = "jacid20853@besibali.com";
        final String password = "@Abcd1234";
        accountTestUtil.havingRegistered(aUser().withEmail(username).withPassword(password));
        final LoginRequest loginRequest = new LoginRequest(username, password);

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value(username));
    }

}
