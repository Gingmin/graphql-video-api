package com.example.graphql.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.user.application.UserService;
import com.example.graphql.user.UserMutationController;
import com.example.graphql.user.UserQueryController;
import com.example.graphql.user.UserGql;
import com.example.user.domain.User;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@GraphQlTest({UserQueryController.class, UserMutationController.class})
public class UserTest {
    @Autowired GraphQlTester graphQlTester;

    @MockitoBean UserService userService;

    private static final DateTimeFormatter ISO_INSTANT = DateTimeFormatter.ISO_INSTANT.withZone(ZoneOffset.UTC);

    @Test
    void signUp() {
        given(userService.signUp("mk", "mk@example.com", "1234"))
            .willReturn(
                new User(1L, "mk", "mk@example.com", null, Instant.EPOCH, Instant.EPOCH, Instant.EPOCH)
            );
        
        graphQlTester
            .document(
                """
                   mutation SignUp($name: String!, $email: String!, $password: String!) {
                    signUp(name: $name, email: $email, password: $password) {
                        id name email latestLoginIp lastLoginDate createdAt modifiedAt
                    }
                   }     
                """
            )
            .variable("name", "mk")
            .variable("email", "mk@example.com")
            .variable("password", "1234")
            .execute()
            .path("signUp")
            .entity(UserGql.class)
            .satisfies(v -> {
                assertThat(v.id()).isEqualTo("1");
                assertThat(v.name()).isEqualTo("mk");
                assertThat(v.email()).isEqualTo("mk@example.com");
                assertThat(v.latestLoginIp()).isNull();
                assertThat(v.lastLoginDate()).isEqualTo(ISO_INSTANT.format(Instant.EPOCH));
                assertThat(v.createdAt()).isEqualTo(ISO_INSTANT.format(Instant.EPOCH));
                assertThat(v.modifiedAt()).isEqualTo(ISO_INSTANT.format(Instant.EPOCH));
            });
    }

    @Test
    void login() {
        given(userService.login("mk@example.com", "1234", null))
            .willReturn(
                new UserService.AuthResult(
                    new User(1L, "mk", "mk@example.com", null, Instant.EPOCH, Instant.EPOCH, Instant.EPOCH), "token-1"
                )
            );
        
            graphQlTester
                .document(
                    """
                        mutation Login($email: String!, $password: String!) {
                            login(email: $email, password: $password) {
                                id name email latestLoginIp lastLoginDate createdAt modifiedAt
                            }
                        }
                    """
                )
                .variable("email", "mk@example.com")
                .variable("password", "1234")
                .execute()
                .path("login")
                .entity(UserGql.class)
                .satisfies(v -> {
                    assertThat(v.id()).isEqualTo("1");
                    assertThat(v.name()).isEqualTo("mk");
                    assertThat(v.email()).isEqualTo("mk@example.com");
                    assertThat(v.latestLoginIp()).isNull();
                    assertThat(v.lastLoginDate()).isEqualTo(ISO_INSTANT.format(Instant.EPOCH));
                    assertThat(v.createdAt()).isEqualTo(ISO_INSTANT.format(Instant.EPOCH));
                    assertThat(v.modifiedAt()).isEqualTo(ISO_INSTANT.format(Instant.EPOCH));
                });
    }

    @Test
    void me_withoutToken_returnsNull() {
        graphQlTester
            .document(
                "query Me { me { id name email } }"
            )
            .execute()
            .path("me")
            .valueIsNull();
    }
}
