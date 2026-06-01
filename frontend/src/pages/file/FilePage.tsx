import { useState } from "react";
import { useNavigate } from "react-router-dom";

import { DateUtil } from "@/utils/date-util";
import { useFiles, useFile, useDeleteFile } from "@/hooks/useFile";

type FileItem = {
    id: string;
    name: string;
    originalName: string;
    path?: string;
    extension: string;
    mimeType: string;
    fileSize: string;
    createdAt?: string;
    modifiedAt?: string;
};

function formatFileSize(bytes: string): string {
    const size = Number(bytes);
    if (size < 1024) return `${size} B`;
    if (size < 1024 * 1024) return `${(size / 1024).toFixed(1)} KB`;
    if (size < 1024 * 1024 * 1024) return `${(size / (1024 * 1024)).toFixed(1)} MB`;
    return `${(size / (1024 * 1024 * 1024)).toFixed(2)} GB`;
}

export default function FilePage() {
    const navigate = useNavigate();

    const [page, setPage] = useState(1);
    const [size, setSize] = useState(10);

    const [selectedFile, setSelectedFile] = useState<FileItem | null>(null);

    const filesQuery = useFiles(page, size);
    const fileQuery = useFile(selectedFile?.id ?? "");

    const deleteFileMutation = useDeleteFile(() => {
        setSelectedFile(null);
    });

    const handleRowClick = (file: FileItem) => {
        setSelectedFile(file);
    };

    const handleDelete = () => {
        if (!selectedFile) return;
        if (!confirm("정말 삭제하시겠습니까?")) return;
        deleteFileMutation.mutate(selectedFile.id);
    };

    const handleCloseDetail = () => {
        setSelectedFile(null);
    };

    const detail: FileItem | null = fileQuery.data?.file ?? null;

    return (
        <div className="api-page">
            <h1>File Page</h1>

            <div className="button-container">
                <div>
                    <button className="go-button" onClick={() => navigate("/home")}>
                        Home
                    </button>
                    <button className="go-button" onClick={() => navigate("/upload")} style={{ marginLeft: 8 }}>
                        Upload
                    </button>
                </div>
            </div>

            <div className="list-container">
                {filesQuery.isLoading && <p>Loading...</p>}
                {filesQuery.isError && <p>Error: {filesQuery.error.message}</p>}
                <table>
                    <thead>
                        <tr>
                            <th>Original Name</th>
                            <th>Extension</th>
                            <th>Size</th>
                            <th>Created At</th>
                        </tr>
                    </thead>
                    <tbody>
                        {filesQuery.data?.files.items.map((file: FileItem) => (
                            <tr key={file.id} className={selectedFile?.id === file.id ? "selected" : ""} onClick={() => handleRowClick(file)}>
                                <td>{file.originalName}</td>
                                <td>{file.extension}</td>
                                <td>{formatFileSize(file.fileSize)}</td>
                                <td>{DateUtil.formatDateTime(file.createdAt)}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>

                {filesQuery.data?.files.items.length !== 0 && (
                    <div className="pagination-container">
                        <button onClick={() => setPage(page - 1)} disabled={page <= 1}>
                            Previous
                        </button>
                        {(() => {
                            const totalPages = filesQuery.data?.files.pageInfo.totalPages ?? 0;
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
                        <button onClick={() => setPage(page + 1)} disabled={page >= filesQuery.data?.files.pageInfo.totalPages}>
                            Next
                        </button>
                    </div>
                )}
            </div>

            {selectedFile && (
                <div className="detail-container">
                    <div className="detail-header">
                        <h2>File Detail</h2>
                        <button className="close-button" onClick={handleCloseDetail}>
                            ✕
                        </button>
                    </div>
                    <div className="detail-form">
                        <div className="detail-field">
                            <label>Original Name</label>
                            <span>{detail?.originalName}</span>
                        </div>
                        <div className="detail-field">
                            <label>Stored Name</label>
                            <span>{detail?.name}</span>
                        </div>
                        <div className="detail-field">
                            <label>Path</label>
                            <span>{detail?.path}</span>
                        </div>
                        <div className="detail-field">
                            <label>Extension</label>
                            <span>{detail?.extension}</span>
                        </div>
                        <div className="detail-field">
                            <label>MIME Type</label>
                            <span>{detail?.mimeType}</span>
                        </div>
                        <div className="detail-field">
                            <label>Size</label>
                            <span>{detail ? formatFileSize(detail.fileSize) : ""}</span>
                        </div>
                        <div className="detail-field">
                            <label>Created At</label>
                            <span>{DateUtil.formatDateTime(detail?.createdAt)}</span>
                        </div>
                        <div className="detail-field">
                            <label>Modified At</label>
                            <span>{DateUtil.formatDateTime(detail?.modifiedAt)}</span>
                        </div>
                        <div className="detail-actions">
                            <button className="cancel-button" onClick={handleDelete}>
                                Delete
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
