import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";

import { useAddPerson, useDeletePerson, useModifyPerson, usePerson, usePersons, useAddPersonTranslation, useModifyPersonTranslation, useDeletePersonTranslation } from "@/hooks/usePerson";

type PersonTranslation = {
    id: string;
    personId: string;
    language: string;
    name: string;
    createdAt?: string;
    modifiedAt?: string;
};

type Person = {
    id: string;
    code?: string;
    birthDate?: string;
    nationality?: string;
    createdAt?: string;
    modifiedAt?: string;
    translations?: PersonTranslation[];
};

function formatDateTime(iso?: string): string {
    if (!iso) {
        return "";
    }
    const d = new Date(iso);
    const pad = (n: number) => String(n).padStart(2, "0");

    const year = d.getFullYear();
    const month = pad(d.getMonth() + 1);
    const day = pad(d.getDate());
    const hours = pad(d.getHours());
    const minutes = pad(d.getMinutes());
    const seconds = pad(d.getSeconds());
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
}

function PersonPage() {
    const navigate = useNavigate();
    const { t } = useTranslation();

    const [page, setPage] = useState(1);
    const [size, setSize] = useState(5);
    const [code, setCode] = useState("");
    const [birthDate, setBirthDate] = useState("");
    const [nationality, setNationality] = useState("");

    const [selectedPerson, setSelectedPerson] = useState<Person | null>(null);
    const [isEditing, setIsEditing] = useState(false);
    const [editCode, setEditCode] = useState("");
    const [editBirthDate, setEditBirthDate] = useState("");
    const [editNationality, setEditNationality] = useState("");

    // translation form state
    const [transLanguage, setTransLanguage] = useState("KO");
    const [transName, setTransName] = useState("");
    const [editingTranslation, setEditingTranslation] = useState<PersonTranslation | null>(null);
    const [editTransLanguage, setEditTransLanguage] = useState("");
    const [editTransName, setEditTransName] = useState("");

    // -------------------- query ------------------------

    const personsQuery = usePersons(page, size);

    const personQuery = usePerson(selectedPerson?.id ?? "");

    useEffect(() => {
        const person = personQuery.data?.person;
        if (person) {
            setEditCode(person.code ?? "");
            setEditBirthDate(person.birthDate ?? "");
            setEditNationality(person.nationality ?? "");
        }
    }, [personQuery.data]);

    const translations: PersonTranslation[] = personQuery.data?.person?.translations ?? [];

    // -------------------- add person ------------------------
    const addPersonMutation = useAddPerson(() => {
        setCode("");
        setBirthDate("");
        setNationality("");
    });

    const handleAddPerson = () => {
        addPersonMutation.mutate({ code, birthDate, nationality });
    };

    // -------------------- modify person ------------------------
    const modifyPersonMutation = useModifyPerson(() => {
        setSelectedPerson(null);
        setIsEditing(false);
    });

    const handleSaveEdit = () => {
        if (!selectedPerson) {
            return;
        }
        modifyPersonMutation.mutate({
            id: selectedPerson.id,
            code: editCode,
            birthDate: editBirthDate || null!,
            nationality: editNationality || null!,
        });
    };

    // -------------------- delete person ------------------------
    const deletePersonMutation = useDeletePerson(() => {
        setSelectedPerson(null);
    });

    const handleDelete = () => {
        if (!selectedPerson) {
            return;
        }
        if (!confirm("정말 삭제하시겠습니까?")) {
            return;
        }
        deletePersonMutation.mutate(selectedPerson.id);
    };

    // -------------------- translation mutations ------------------------
    const addTransMutation = useAddPersonTranslation(() => {
        setTransLanguage("KO");
        setTransName("");
    });

    const modifyTransMutation = useModifyPersonTranslation(() => {
        setEditingTranslation(null);
        setEditTransLanguage("");
        setEditTransName("");
    });

    const deleteTransMutation = useDeletePersonTranslation(selectedPerson?.id ?? "");

    const handleAddTranslation = () => {
        if (!selectedPerson) {
            return;
        }
        addTransMutation.mutate({
            personId: selectedPerson.id,
            language: transLanguage,
            name: transName,
        });
    };

    const handleStartEditTranslation = (trans: PersonTranslation) => {
        setEditingTranslation(trans);
        setEditTransLanguage(trans.language);
        setEditTransName(trans.name);
    };

    const handleSaveTranslation = () => {
        if (!editingTranslation || !selectedPerson) {
            return;
        }
        modifyTransMutation.mutate({
            id: editingTranslation.id,
            personId: selectedPerson.id,
            language: editTransLanguage,
            name: editTransName,
        });
    };

    const handleCancelEditTranslation = () => {
        setEditingTranslation(null);
        setEditTransLanguage("");
        setEditTransName("");
    };

    const handleDeleteTranslation = (transId: string) => {
        if (!confirm("정말 삭제하시겠습니까?")) {
            return;
        }
        deleteTransMutation.mutate(transId);
    };

    // -------------------- handle ------------------------

    const handleRowClick = (person: Person) => {
        setSelectedPerson(person);
        setIsEditing(false);
        setEditingTranslation(null);
    };

    const handleEdit = () => {
        setIsEditing(true);
    };

    const handleCancelEdit = () => {
        setIsEditing(false);
        if (selectedPerson) {
            setEditCode(selectedPerson.code ?? "");
            setEditBirthDate(selectedPerson.birthDate ?? "");
            setEditNationality(selectedPerson.nationality ?? "");
        }
    };

    const handleCloseDetail = () => {
        setSelectedPerson(null);
        setIsEditing(false);
        setEditingTranslation(null);
    };

    return (
        <div className="person-page">
            <h1>Person Page</h1>

            <div className="button-container">
                <div>
                    <button className="go-button" onClick={() => navigate("/users")}>
                        Users
                    </button>
                </div>
            </div>

            <div className="list-container">
                {personsQuery.isLoading && <p>Loading...</p>}
                {personsQuery.isError && <p>Error: {personsQuery.error.message}</p>}
                <table>
                    <thead>
                        <tr>
                            <th>Code</th>
                            <th>Birth Date</th>
                            <th>Nationality</th>
                            <th>Created At</th>
                            <th>Modified At</th>
                        </tr>
                    </thead>
                    <tbody>
                        {personsQuery.data?.persons.items.map((person: Person) => (
                            <tr key={person.id} className={selectedPerson?.id === person.id ? "selected" : ""} onClick={() => handleRowClick(person)}>
                                <td>{person.code}</td>
                                <td>{person.birthDate}</td>
                                <td>{person.nationality}</td>
                                <td>{formatDateTime(person.createdAt)}</td>
                                <td>{formatDateTime(person.modifiedAt)}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                {personsQuery.data?.persons.items.length !== 0 && (
                    <div className="pagination-container">
                        <button onClick={() => setPage(page - 1)} disabled={page <= 1}>
                            Previous
                        </button>
                        {(() => {
                            const totalPages = personsQuery.data?.persons.pageInfo.totalPages ?? 0;
                            const start = Math.max(1, Math.min(page - 2, totalPages - 4));
                            const end = Math.min(start + 4, totalPages);
                            return Array.from({ length: end - start + 1 }, (_, i) => {
                                const p = start + i;
                                return (
                                    <button key={p} className={p === page ? "active" : ""} onClick={() => setPage(p)} disabled={p === page}>
                                        {p}
                                    </button>
                                );
                            });
                        })()}
                        <button onClick={() => setPage(page + 1)} disabled={page >= personsQuery.data?.persons.pageInfo.totalPages}>
                            Next
                        </button>
                    </div>
                )}
            </div>

            {selectedPerson && (
                <div className="detail-container">
                    <div className="detail-header">
                        <h2>Person Detail</h2>
                        <button className="close-button" onClick={handleCloseDetail}>
                            ✕
                        </button>
                    </div>
                    {isEditing ? (
                        <div className="detail-form">
                            <div className="detail-field">
                                <label>Code</label>
                                <input className="input-field" type="text" value={editCode} onChange={(e) => setEditCode(e.target.value)} />
                            </div>
                            <div className="detail-field">
                                <label>Birth Date</label>
                                <input className="input-field" type="text" value={editBirthDate} onChange={(e) => setEditBirthDate(e.target.value)} />
                            </div>
                            <div className="detail-field">
                                <label>Nationality</label>
                                <input className="input-field" type="text" value={editNationality} onChange={(e) => setEditNationality(e.target.value)} />
                            </div>
                            <div className="detail-actions">
                                <button className="confirm-button" onClick={handleSaveEdit}>
                                    Save
                                </button>
                                <button className="cancel-button" onClick={handleCancelEdit}>
                                    Cancel
                                </button>
                            </div>
                        </div>
                    ) : (
                        <div className="detail-form">
                            <div className="detail-field">
                                <label>Code</label>
                                <span>{selectedPerson.code}</span>
                            </div>
                            <div className="detail-field">
                                <label>Birth Date</label>
                                <span>{selectedPerson.birthDate ?? "-"}</span>
                            </div>
                            <div className="detail-field">
                                <label>Nationality</label>
                                <span>{selectedPerson.nationality ?? "-"}</span>
                            </div>
                            <div className="detail-field">
                                <label>Created At</label>
                                <span>{formatDateTime(selectedPerson.createdAt)}</span>
                            </div>
                            <div className="detail-field">
                                <label>Modified At</label>
                                <span>{formatDateTime(selectedPerson.modifiedAt)}</span>
                            </div>
                            <div className="detail-actions">
                                <button className="confirm-button" onClick={handleEdit}>
                                    Edit
                                </button>
                                <button className="cancel-button" onClick={handleDelete}>
                                    Delete
                                </button>
                            </div>
                        </div>
                    )}

                    {/* Translations */}
                    <div className="translation-section">
                        <h3>Translations</h3>
                        {personQuery.isLoading && <p>Loading translations...</p>}
                        <table className="translation-table">
                            <thead>
                                <tr>
                                    <th>Language</th>
                                    <th>Name</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                {translations.map((trans) =>
                                    editingTranslation?.id === trans.id ? (
                                        <tr key={trans.id}>
                                            <td>
                                                <select className="input-field" value={editTransLanguage} onChange={(e) => setEditTransLanguage(e.target.value)}>
                                                    <option value="KO">KO</option>
                                                    <option value="EN">EN</option>
                                                </select>
                                            </td>
                                            <td>
                                                <input className="input-field" type="text" value={editTransName} onChange={(e) => setEditTransName(e.target.value)} />
                                            </td>
                                            <td>
                                                <div className="translation-actions">
                                                    <button className="confirm-button" onClick={handleSaveTranslation}>
                                                        Save
                                                    </button>
                                                    <button className="cancel-button" onClick={handleCancelEditTranslation}>
                                                        Cancel
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    ) : (
                                        <tr key={trans.id}>
                                            <td>{trans.language}</td>
                                            <td>{trans.name}</td>
                                            <td>
                                                <div className="translation-actions">
                                                    <button className="confirm-button" onClick={() => handleStartEditTranslation(trans)}>
                                                        Edit
                                                    </button>
                                                    <button className="cancel-button" onClick={() => handleDeleteTranslation(trans.id)}>
                                                        Delete
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>
                                    ),
                                )}
                            </tbody>
                        </table>

                        <div className="translation-add-form">
                            <select className="input-field" value={transLanguage} onChange={(e) => setTransLanguage(e.target.value)}>
                                <option value="KO">KO</option>
                                <option value="EN">EN</option>
                            </select>
                            <input className="input-field" type="text" placeholder="Name" value={transName} onChange={(e) => setTransName(e.target.value)} />
                            <button className="confirm-button" onClick={handleAddTranslation} disabled={!transName.trim()}>
                                Add
                            </button>
                        </div>
                    </div>
                </div>
            )}

            <div className="add-person-container">
                <div className="add-person-form">
                    <div>
                        <input className="input-field" tabIndex={1} type="text" placeholder="Code" value={code} onChange={(e) => setCode(e.target.value)} />
                    </div>
                    <div>
                        <input className="input-field" tabIndex={2} type="text" placeholder="Birth Date" value={birthDate} onChange={(e) => setBirthDate(e.target.value)} />
                    </div>
                    <div>
                        <input className="input-field" tabIndex={3} type="text" placeholder="Nationality" value={nationality} onChange={(e) => setNationality(e.target.value)} />
                    </div>
                </div>
                <div>
                    <button className="confirm-button" onClick={handleAddPerson}>
                        Add Person
                    </button>
                </div>
            </div>
        </div>
    );
}

export default PersonPage;
