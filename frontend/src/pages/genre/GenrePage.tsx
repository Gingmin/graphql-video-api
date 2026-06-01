import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import { DateUtil } from "@/utils/date-util";
import { useGenres, useGenre, useAddGenre, useModifyGenre, useDeleteGenre, useAddGenreTranslation, useModifyGenreTranslation, useDeleteGenreTranslation } from "@/hooks/useGenre";

type Translation = {
    id: string;
    targetId: string;
    language: string;
    name: string;
    createdAt?: string;
    modifiedAt?: string;
};

type Genre = {
    id: string;
    code: string;
    createdAt?: string;
    modifiedAt?: string;
    translations?: Translation[];
};

export default function TagPage() {
    const navigate = useNavigate();

    const [page, setPage] = useState(1);
    const [size, setSize] = useState(5);

    const [code, setCode] = useState("");

    const [selectedGenre, setSelectedGenre] = useState<Genre | null>(null);
    const [isEditing, setIsEditing] = useState(false);
    const [editCode, setEditCode] = useState("");

    // -------------------- translation form state ------------------------
    const [transLanguage, setTransLanguage] = useState("KO");
    const [transName, setTransName] = useState("");
    const [editingTranslation, setEditingTranslation] = useState<Translation | null>(null);
    const [editTransLanguage, setEditTransLanguage] = useState("");
    const [editTransName, setEditTransName] = useState("");

    // -------------------- query ------------------------

    const genresQuery = useGenres(page, size);

    const genreQuery = useGenre(selectedGenre?.id ?? "");

    useEffect(() => {
        const genre = genreQuery.data?.genre;
        if (genre) {
            setEditCode(genre.code ?? "");
        }
    }, [genreQuery.data]);

    const translations: Translation[] = genreQuery.data?.genre?.translations ?? [];

    // -------------------- add genre ------------------------
    const addGenreMutation = useAddGenre(() => {
        setCode("");
    });

    const handleAddGenre = () => {
        addGenreMutation.mutate({ code });
    };

    // -------------------- modify genre ------------------------
    const modifyGenreMutation = useModifyGenre(() => {
        setSelectedGenre(null);
        setIsEditing(false);
    });

    const handleSaveEdit = () => {
        if (!selectedGenre) {
            return;
        }
        modifyGenreMutation.mutate({
            id: selectedGenre.id,
            code: editCode,
        });
    };

    // -------------------- delete genre ------------------------
    const deleteGenreMutation = useDeleteGenre(() => {
        setSelectedGenre(null);
    });

    const handleDelete = () => {
        if (!selectedGenre) {
            return;
        }
        if (!confirm("정말 삭제하시겠습니까?")) {
            return;
        }
        deleteGenreMutation.mutate(selectedGenre.id);
    };

    // -------------------- translation mutations ------------------------
    const addTransMutation = useAddGenreTranslation(() => {
        setTransLanguage("KO");
        setTransName("");
    });

    const modifyTransMutation = useModifyGenreTranslation(() => {
        setEditingTranslation(null);
        setEditTransLanguage("");
        setEditTransName("");
    });

    const deleteTransMutation = useDeleteGenreTranslation(selectedGenre?.id ?? "");

    const handleAddTranslation = () => {
        if (!selectedGenre) {
            return;
        }
        addTransMutation.mutate({
            targetId: selectedGenre.id,
            language: transLanguage,
            name: transName,
        });
    };

    const handleStartEditTranslation = (trans: Translation) => {
        setEditingTranslation(trans);
        setEditTransLanguage(trans.language);
        setEditTransName(trans.name);
    };

    const handleSaveTranslation = () => {
        if (!editingTranslation || !selectedGenre) {
            return;
        }
        modifyTransMutation.mutate({
            id: editingTranslation.id,
            targetId: selectedGenre.id,
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
    const handleRowClick = (genre: Genre) => {
        setSelectedGenre(genre);
        setIsEditing(false);
        setEditingTranslation(null);
    };

    const handleEdit = () => {
        setIsEditing(true);
    };

    const handleCancelEdit = () => {
        setIsEditing(false);
        if (selectedGenre) {
            setEditCode(selectedGenre.code ?? "");
        }
    };

    const handleCloseDetail = () => {
        setSelectedGenre(null);
        setIsEditing(false);
        setEditingTranslation(null);
    };

    return (
        <div className="api-page">
            <h1>Genre Page</h1>

            <div className="button-container">
                <div>
                    <button className="go-button" onClick={() => navigate("/home")}>
                        Home
                    </button>
                </div>
            </div>

            <div className="list-container">
                {genresQuery.isLoading && <p>Loading...</p>}
                {genresQuery.isError && <p>Error: {genresQuery.error.message}</p>}
                <table>
                    <thead>
                        <tr>
                            <th>Code</th>
                            <th>Created At</th>
                            <th>Modified At</th>
                        </tr>
                    </thead>
                    <tbody>
                        {genresQuery.data?.genres.items.map((genre: Genre) => (
                            <tr key={genre.id} className={selectedGenre?.id === genre.id ? "selected" : ""} onClick={() => handleRowClick(genre)}>
                                <td>{genre.code}</td>
                                <td>{DateUtil.formatDateTime(genre.createdAt)}</td>
                                <td>{DateUtil.formatDateTime(genre.modifiedAt)}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>

                {genresQuery.data?.genres.items.length !== 0 && (
                    <div className="pagination-container">
                        <button onClick={() => setPage(page - 1)} disabled={page <= 1}>
                            Previous
                        </button>
                        {(() => {
                            const totalPages = genresQuery.data?.genres.pageInfo.totalPages ?? 0;
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
                        <button onClick={() => setPage(page + 1)} disabled={page >= genresQuery.data?.genres.pageInfo.totalPages}>
                            Next
                        </button>
                    </div>
                )}
            </div>

            {selectedGenre && (
                <div className="detail-container">
                    <div className="detail-header">
                        <h2>Genre Detail</h2>
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
                                <span>{selectedGenre.code}</span>
                            </div>
                            <div className="detail-field">
                                <label>Created At</label>
                                <span>{DateUtil.formatDateTime(selectedGenre.createdAt)}</span>
                            </div>
                            <div className="detail-field">
                                <label>Modified At</label>
                                <span>{DateUtil.formatDateTime(selectedGenre.modifiedAt)}</span>
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

                    <div className="translation-section">
                        <h3>Translations</h3>
                        {genreQuery.isLoading && <p>Loading translations...</p>}
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

            <div className="add-api-container">
                <div className="add-api-form">
                    <div>
                        <input className="input-field" tabIndex={1} type="text" placeholder="Code" value={code} onChange={(e) => setCode(e.target.value)} />
                    </div>
                    <div>
                        <button className="confirm-button" onClick={handleAddGenre}>
                            Add Genre
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}
