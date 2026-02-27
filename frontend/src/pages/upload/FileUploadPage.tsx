import React, { useCallback, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";

const IMAGE_ALLOWED_TYPES = ["image/jpeg", "image/png", "image/gif", "image/tiff", "image/bmp", "image/webp", "image/svg+xml"];
const VIDEO_ALLOWED_TYPES = ["video/mp4", "video/webm", "video/ogg", "video/webm", "video/quicktime", "video/x-msvideo"];
const MAX_FILE_SIZE = 10 * 1024 * 1024 * 1024; // 10GB

const MODE_IMAGE = "image";
const MODE_VIDEO = "video";

const STATUS_PENDING = "pending";
const STATUS_UPLOADING = "uploading";
const STATUS_DONE = "done";
const STATUS_ERROR = "error";

const API_BASE = "http://localhost:8080";

type FileItem = {
    file: File;
    status: typeof STATUS_PENDING | typeof STATUS_UPLOADING | typeof STATUS_DONE | typeof STATUS_ERROR;
    progress: number;
    error: string | null;
};

function formatBytes(bytes: number | string) {
    if (typeof bytes === "string") {
        bytes = parseFloat(bytes);
    }
    if (bytes === 0 || isNaN(bytes)) {
        return "0 B";
    }
    const k = 1024;
    const sizes = ["B", "KB", "MB", "GB"];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return `${parseFloat((bytes / Math.pow(k, i)).toFixed(2))} ${sizes[i]}`;
}

function Item({ item, mode, onRemove }: { item: FileItem; mode: typeof MODE_IMAGE | typeof MODE_VIDEO; onRemove: (name: string) => void }) {
    const isImage = mode === MODE_IMAGE;
    const preview = isImage ? URL.createObjectURL(item.file) : null;
    return (
        <div className="file-item">
            <div className="file-thumb">
                {isImage ? (
                    <img src={preview ?? ""} alt={item.file.name} onLoad={() => URL.revokeObjectURL(preview ?? "")} />
                ) : (
                    <div className="video-icon">
                        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
                            <polygon points="5,3 19,12 5,21" />
                        </svg>
                    </div>
                )}
            </div>
            <div className="file-info">
                <span className="file-name">{item.file.name}</span>
                <span className="file-meta">
                    {formatBytes(item.file.size)} · {item.file.type.split("/")[1].toUpperCase()}
                </span>

                {item.status === STATUS_UPLOADING && (
                    <div className="progress-bar">
                        <div className="progress-fill" style={{ width: `${item.progress}%` }}></div>
                    </div>
                )}
                {item.status === STATUS_DONE && <span className="status-done">✓ 업로드 완료</span>}
                {item.status === STATUS_ERROR && <span className="status-error">✗ {item.error}</span>}
                <div className="file-actions">
                    <button className="remove-btn" onClick={() => onRemove(item.file.name)}>
                        ×
                    </button>
                </div>
            </div>
        </div>
    );
}

function FileUploadPage() {
    const navigate = useNavigate();
    const [mode, setMode] = useState<typeof MODE_IMAGE | typeof MODE_VIDEO>(MODE_IMAGE);
    const [files, setFiles] = useState<FileItem[]>([]);
    const [isDragging, setIsDragging] = useState(false);
    const inputRef = useRef<HTMLInputElement>(null);

    const addFiles = useCallback(
        (newFiles: File[]) => {
            const filtered = filteredNewFiles(newFiles);
            settingFiles(filtered);
        },
        [mode],
    );

    const filteredNewFiles = useCallback(
        (newFiles: File[]) => {
            const filtered = Array.from(newFiles).filter((f) => {
                if (mode === MODE_IMAGE) {
                    return IMAGE_ALLOWED_TYPES.includes(f.type);
                } else if (mode === MODE_VIDEO) {
                    return VIDEO_ALLOWED_TYPES.includes(f.type);
                }
                if (f.size > MAX_FILE_SIZE) {
                    return false;
                }
                return false;
            });
            return filtered;
        },
        [mode],
    );

    const settingFiles = useCallback(
        (filtered: File[]) => {
            if (mode === MODE_VIDEO) {
                const single = filtered.slice(0, 1);
                setFiles(single.map((file) => ({ file, status: STATUS_PENDING, progress: 0, error: null })));
            } else {
                setFiles((prev) => {
                    const existingNames = new Set(prev.map((f) => f.file.name));
                    const toAdd = filtered.filter((f: File) => !existingNames.has(f.name)).map((file: File) => ({ file, status: STATUS_PENDING as typeof STATUS_PENDING, progress: 0, error: null }));
                    return [...prev, ...toAdd];
                });
            }
        },
        [mode],
    );

    const handleDrop = useCallback(
        (e: React.DragEvent<HTMLDivElement>) => {
            e.preventDefault();
            setIsDragging(false);
            addFiles(Array.from(e.dataTransfer.files));
        },
        [addFiles],
    );

    const handleDragOver = (e: React.DragEvent<HTMLDivElement>) => {
        e.preventDefault();
        setIsDragging(true);
    };

    const handleDragLeave = () => {
        setIsDragging(false);
    };

    const removeFile = (name: string) => {
        setFiles((prev) => prev.filter((f) => f.file.name !== name));
    };

    const uploadAll = () => {
        const pending = files.filter((f) => f.status === STATUS_PENDING);
        if (isCheckError(files) && isCheckPending(pending)) {
            roopForUpload(pending);
        }
    };

    const isCheckError = (files: FileItem[]) => {
        const error = files.filter((f) => f.status === STATUS_ERROR);
        if (error.length > 0) {
            return false;
        }
        return true;
    };

    const isCheckPending = (pending: FileItem[]) => {
        if (pending.length === 0) {
            return false;
        }
        return true;
    };

    const roopForUpload = (pending: FileItem[]) => {
        for (const item of pending) {
            changeStatusFileItemToUploading(item);
            const formData = createFormData(item);
            tryCatchSendXHR(item, formData);
        }
    };

    const changeStatusFileItemToUploading = (item: FileItem) => {
        setFiles((prev) => {
            return prev.map((f) => (f.file.name === item.file.name ? { ...f, status: STATUS_UPLOADING, progress: 0 } : f));
        });
    };

    const createFormData = (item: FileItem) => {
        const formData = new FormData();
        formData.append("file", item.file);
        formData.append("type", mode);
        return formData;
    };

    const tryCatchSendXHR = (item: FileItem, formData: FormData) => {
        try {
            sendXHR(item, formData);
        } catch (err: unknown) {
            const message = err instanceof Error ? err.message : "알 수 없는 오류";
            setFiles((prev) => {
                return prev.map((f) => (f.file.name === item.file.name ? { ...f, status: STATUS_ERROR, error: message } : f));
            });
        }
    };

    const sendXHR = async (item: FileItem, formData: FormData) => {
        await new Promise<void>((resolve, reject) => {
            const xhr = new XMLHttpRequest();
            xhr.open("POST", `${API_BASE}/upload`);

            xhr.upload.onprogress = (e) => {
                xhrOnProgress(e, item);
            };

            xhr.onload = () => {
                const isResolve = xhrOnLoad(xhr, item);
                if (isResolve) {
                    resolve();
                } else {
                    reject(new Error(`서버 오류: ${xhr.status}`));
                }
            };

            xhr.onerror = () => reject(new Error("네트워크 오류"));
            xhr.send(formData);
        });
    };

    const xhrOnProgress = (e: ProgressEvent, item: FileItem) => {
        if (e.lengthComputable) {
            const pct = Math.round((e.loaded / e.total) * 100);
            setFiles((prev) => {
                return prev.map((f) => (f.file.name === item.file.name ? { ...f, progress: pct } : f));
            });
        }
    };

    const xhrOnLoad = (xhr: XMLHttpRequest, item: FileItem) => {
        let isResolve = false;
        if (xhr.status >= 200 && xhr.status < 300) {
            setFiles((prev) => {
                return prev.map((f) => (f.file.name === item.file.name ? { ...f, status: STATUS_DONE, progress: 100 } : f));
            });
            isResolve = true;
        } else {
            isResolve = false;
        }
        return isResolve;
    };

    const onChangeFiles = (e: React.ChangeEvent<HTMLInputElement>) => {
        addFiles(Array.from(e.target.files ?? []));
        e.target.value = "";
    };

    const pendingCount = files.filter((f) => f.status === STATUS_PENDING).length;

    return (
        <div className="file-upload-page">
            <h1>FileUploadPage</h1>

            <button onClick={() => navigate("/users")}>Users</button>

            <div className="mode-toggle">
                <button
                    className={`mode-btn ${mode === MODE_IMAGE ? "active" : ""}`}
                    onClick={() => {
                        setMode(MODE_IMAGE);
                        setFiles([]);
                    }}
                >
                    Image
                </button>
                <button
                    className={`mode-btn ${mode === MODE_VIDEO ? "active" : ""}`}
                    onClick={() => {
                        setMode(MODE_VIDEO);
                        setFiles([]);
                    }}
                >
                    Video
                </button>
            </div>

            <div>
                <div className={`drop-zone ${isDragging ? "dragging" : ""}`} onDrop={handleDrop} onDragOver={handleDragOver} onDragLeave={handleDragLeave} onClick={() => inputRef.current?.click()}>
                    <svg className="drop-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.2">
                        <path d="M21 15v4a2 2 0 01-2 2H5a2 2 0 01-2-2v-4" />
                        <polyline points="17,8 12,3 7,8" />
                        <line x1="12" y1="3" x2="12" y2="15" />
                    </svg>
                    <div className="drop-label">
                        <strong>드래그하거나 클릭해서 업로드</strong>
                        <p className="hint">{mode === MODE_IMAGE ? "JPG, PNG, GIF, WEBP · 여러 개 가능" : "MP4, WEBM, MOV · 1개만 가능"}</p>
                    </div>
                    <input
                        ref={inputRef}
                        type="file"
                        accept={mode === MODE_IMAGE ? IMAGE_ALLOWED_TYPES.join(",") : VIDEO_ALLOWED_TYPES.join(",")}
                        multiple={mode === MODE_IMAGE}
                        onClick={(e) => e.stopPropagation()}
                        onChange={(e) => onChangeFiles(e)}
                        hidden
                    />
                </div>

                <div className="file-list">
                    {files.length > 0 && (
                        <div className="file-list-items">
                            {files.map((item: FileItem, index: number) => {
                                return <Item key={index + "_" + item.file.name} item={item} mode={mode} onRemove={removeFile} />;
                            })}
                        </div>
                    )}
                </div>

                <button className="upload-btn" disabled={pendingCount === 0} onClick={uploadAll}>
                    {pendingCount > 0 ? `업로드 (${pendingCount}개)` : "업로드할 파일을 선택하세요"}
                </button>
            </div>
        </div>
    );
}

export default FileUploadPage;
