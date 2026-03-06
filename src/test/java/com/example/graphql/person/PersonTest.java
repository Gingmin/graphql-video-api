package com.example.graphql.person;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

import java.time.Instant;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import com.example.person.application.PersonService;
import com.example.graphql.user.UserMutationController;
import com.example.graphql.user.UserQueryController;
import com.example.person.domain.Person;
import com.example.person.application.PersonPage;

import com.example.graphql.CommonUtil;

@GraphQlTest({PersonQueryController.class, PersonMutationController.class})
public class PersonTest {
    @Autowired GraphQlTester graphQlTester;

    @MockitoBean PersonService personService;

    private static Person samplePerson(long id) {
        return new Person(
            id,
            "code-" + id,
            LocalDate.EPOCH,
            "nationality-" + id,
            Instant.EPOCH,
            Instant.EPOCH
        );
    }

    @Test
    void persons_returnsPage() {
        given(personService.persons(any(), any()))
            .willReturn(
                new PersonPage(
                    List.of(samplePerson(1)),
                    0,
                    20,
                    1L,
                    1,
                    false,
                    false
                )
            );
        graphQlTester
            .document(
                """
                    query Persons {
                        persons {
                            items { code }
                            pageInfo { page size totalElements totalPages hasNext hasPrev }
                        }
                    }
                """
            )
            .execute()
            .path("persons.items[0].code")
            .entity(String.class)
            .satisfies(v -> {
                assertThat(v).isEqualTo("code-1");
            });
    }
    
    @Test
    void persons_withPagingArgs_returnsPage() {
        given(personService.persons(1, 5))
            .willReturn(
                new PersonPage(
                    List.of(samplePerson(3)),
                    1, 5, 6L, 2, false, true
                )
            );
        graphQlTester
            .document(
                """
                    query Persons($page: Int!, $size: Int!) {
                        persons(page: $page, size: $size) {
                            items { id }
                            pageInfo { page size totalElements totalPages }
                        }
                    }
                """
            )
            .variable("page", 1)
            .variable("size", 5)
            .execute()
            .path("persons.items[0].id")
            .entity(String.class)
            .satisfies(v -> {
                assertThat(v).isEqualTo("3");
            });
    }

    @Test
    void person_returnOne() {
        given(personService.person(1L))
            .willReturn(samplePerson(1));
        graphQlTester
            .document(
                """
                    query Person($id: ID!) {
                        person(id: $id) {
                            id code birthDate nationality createdAt modifiedAt
                        }
                    }  
                """
            )
            .variable("id", 1L)
            .execute()
            .path("person.id")
            .entity(String.class)
            .satisfies(v -> {
                assertThat(v).isEqualTo("1");
            });
    }

    @Test
    void addPerson_returnPerson() {
        given(personService.addPerson("code-1", LocalDate.EPOCH, "nationality-1"))
            .willReturn(samplePerson(1));
        graphQlTester
            .document(
                """
                    mutation AddPerson($code: String!, $birthDate: String, $nationality: String) {
                        addPerson(code: $code, birthDate: $birthDate, nationality: $nationality) {
                            id code birthDate nationality createdAt modifiedAt
                        }
                    }
                """
            )
            .variable("code", "code-1")
            .variable("birthDate", "1970-01-01")
            .variable("nationality", "nationality-1")
            .execute()
            .path("addPerson.id")
            .entity(String.class)
            .satisfies(v -> {
                assertThat(v).isEqualTo("1");
            });
    }

    @Test
    void modifyPerson_returnPerson() {
        given(personService.modifyPerson(1L, "code-1", LocalDate.EPOCH, "nationality-1"))
            .willReturn(samplePerson(1));
        
        graphQlTester
            .document(
                """
                    mutation ModifyPerson($id: ID!,$code: String!, $birthDate: String, $nationality: String) {
                        modifyPerson(id: $id, code: $code, birthDate: $birthDate, nationality: $nationality) {
                            id code birthDate nationality createdAt modifiedAt
                        }
                    }
                """
            )
            .variable("id", 1L)
            .variable("code", "code-1")
            .variable("birthDate", "1970-01-01")
            .variable("nationality", "nationality-1")
            .execute()
            .path("modifyPerson.id")
            .entity(String.class)
            .satisfies(v -> {
                assertThat(v).isEqualTo("1");
            });
    }

    @Test
    void deletePerson_returnBoolean() {
        given(personService.deletePerson(1L)).willReturn(true);

        graphQlTester
            .document("mutation DeletePerson($id: ID!) { deletePerson(id: $id) }")
            .variable("id", 1)
            .execute()
            .path("deletePerson")
            .entity(Boolean.class)
            .satisfies(v -> assertThat(v).isTrue());
    }
}
