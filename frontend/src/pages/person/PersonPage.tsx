import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import { useQueryClient, useMutation, useQuery } from "@tanstack/react-query";
import { gqlClient } from "@/lib/graphql/client";
import { gql } from "graphql-request";

const ADD_PERSON = gql`
    mutation AddPerson($code: String!, $birthDate: String, $nationality: String) {
        addPerson(code: $code, birthDate: $birthDate, nationality: $nationality) {
            id
            code
            birthDate
            nationality
            createdAt
            modifiedAt
        }
    }
`;

const PERSONS = gql`
    query Persons($page: Int!, $size: Int!) {
        persons(page: $page, size: $size) {
            items {
                id
                code
                birthDate
                nationality
                createdAt
                modifiedAt
            }
            pageInfo {
                page
                size
                totalElements
                totalPages
                hasNext
                hasPrev
            }
        }
    }
`;

type Person = {
    id: string;
    code?: string;
    birthDate?: string;
    nationality?: string;
    createdAt?: string;
    modifiedAt?: string;
};

function formatDateTime(iso?: string): string {
    if (!iso) return "";
    const d = new Date(iso);
    const pad = (n: number) => String(n).padStart(2, "0");
    return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}

function PersonPage() {
    const navigate = useNavigate();
    const { t } = useTranslation();
    const queryClient = useQueryClient();

    const [page, setPage] = useState(0);
    const [size, setSize] = useState(5);
    const [code, setCode] = useState("");
    const [birthDate, setBirthDate] = useState("");
    const [nationality, setNationality] = useState("");

    const personsQuery = useQuery({
        queryKey: ["persons", { page, size }],
        queryFn: () => gqlClient.request(PERSONS, { page, size }),
        staleTime: 1000 * 60 * 5,
    });

    const addPersonMutation = useMutation({
        mutationFn: (vars: { code: string; birthDate: string; nationality: string }) => {
            return gqlClient.request(ADD_PERSON, vars);
        },
        onSuccess: (newData) => {
            // ---- 직접 데이터 업데이트
            // queryClient.setQueryData(["persons", { page, size }], (oldData: any) => {
            //     if (!oldData) {
            //         return oldData;
            //     }
            //     const newTotal = oldData.persons.pageInfo.totalElements + 1;
            //     return {
            //         persons: {
            //             ...oldData.persons,
            //             items: [newData.addPerson, ...oldData.persons.items],
            //             pageInfo: {
            //                 ...oldData.persons.pageInfo,
            //                 totalElements: newTotal,
            //                 totalPages: Math.ceil(newTotal / size),
            //             },
            //         },
            //     };
            // });

            // ---- 캐시 무효화
            queryClient.invalidateQueries({ queryKey: ["persons", { page, size }] });
        },
        onError: (error: any) => {
            const msg = error.response?.errors?.[0]?.message ?? "알 수 없는 오류";
            console.error(msg);
            alert(msg);
        },
    });

    const handleAddPerson = () => {
        addPersonMutation.mutate({ code, birthDate, nationality });
    };

    return (
        <div className="person-page">
            <h1>Person Page</h1>

            <div>
                <div>
                    <input type="text" placeholder="Code" value={code} onChange={(e) => setCode(e.target.value)} />
                </div>
                <div>
                    <input type="text" placeholder="Birth Date" value={birthDate} onChange={(e) => setBirthDate(e.target.value)} />
                </div>
                <div>
                    <input type="text" placeholder="Nationality" value={nationality} onChange={(e) => setNationality(e.target.value)} />
                </div>
                <div>
                    <button onClick={handleAddPerson}>Add Person</button>
                </div>
            </div>

            <div className="list-container">
                {personsQuery.isLoading && <p>Loading...</p>}
                {personsQuery.isError && <p>Error: {personsQuery.error.message}</p>}
                {personsQuery.data?.persons.items.map((person: Person) => (
                    <>
                        <div className="list-item" key={person.id}>
                            <span>{person.code}</span>
                            <span>{person.birthDate}</span>
                            <span>{person.nationality}</span>
                            <span>{formatDateTime(person.createdAt)}</span>
                            <span>{formatDateTime(person.modifiedAt)}</span>
                        </div>
                    </>
                ))}
                {personsQuery.data?.persons.items.length !== 0 && (
                    <div className="pagination-container">
                        <button onClick={() => setPage(page - 1)} disabled={page === 0 || page === 1}>
                            Previous
                        </button>
                        {(() => {
                            const totalPages = personsQuery.data?.persons.pageInfo.totalPages ?? 0;
                            const start = Math.max(0, Math.min(page - 2, totalPages - 5));
                            const end = Math.min(start + 5, totalPages);
                            return Array.from({ length: end - start }, (_, i) => {
                                const p = start + i;
                                return (
                                    <button key={p} className={p === page ? "active" : ""} onClick={() => setPage(p)} disabled={p === page}>
                                        {p + 1}
                                    </button>
                                );
                            });
                        })()}
                        <button onClick={() => setPage(page + 1)} disabled={page === personsQuery.data?.persons.pageInfo.totalPages - 1}>
                            Next
                        </button>
                    </div>
                )}
            </div>

            <button onClick={() => navigate("/users")}>Users</button>
        </div>
    );
}

export default PersonPage;
