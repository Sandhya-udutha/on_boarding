package ug.daes.onboarding.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentResponse {
    @JsonProperty("datetime_created")
    private String datetimeCreated;
    private String description;
    @JsonProperty("document_change_type_url")
    private String documentChangeTypeUrl;
    @JsonProperty("document_type")
    private DocumentType documentType;
    @JsonProperty("file_list_url")
    private String fileListUrl;
    private int id;
    private String label;
    private String language;
    @JsonProperty("file_latest")
    private String fileLatest;
    private String url;
    private String uuid;
    @JsonProperty("version_active")
    private String versionActive;
    @JsonProperty("version_list_url")
    private String versionListUrl;

    public String getDatetimeCreated() {
        return datetimeCreated;
    }

    public void setDatetimeCreated(String datetimeCreated) {
        this.datetimeCreated = datetimeCreated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDocumentChangeTypeUrl() {
        return documentChangeTypeUrl;
    }

    public void setDocumentChangeTypeUrl(String documentChangeTypeUrl) {
        this.documentChangeTypeUrl = documentChangeTypeUrl;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getFileListUrl() {
        return fileListUrl;
    }

    public void setFileListUrl(String fileListUrl) {
        this.fileListUrl = fileListUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFileLatest() {
        return fileLatest;
    }

    public void setFileLatest(String fileLatest) {
        this.fileLatest = fileLatest;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getVersionActive() {
        return versionActive;
    }

    public void setVersionActive(String versionActive) {
        this.versionActive = versionActive;
    }

    public String getVersionListUrl() {
        return versionListUrl;
    }

    public void setVersionListUrl(String versionListUrl) {
        this.versionListUrl = versionListUrl;
    }
}
