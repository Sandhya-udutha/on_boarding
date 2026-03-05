package ug.daes.onboarding.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentType {
    @JsonProperty("delete_time_period")
    private String deleteTimePeriod;
    @JsonProperty("delete_time_unit")
    private String deleteTimeUnit;
    @JsonProperty("filename_generator_backend")
    private String filenameGeneratorBackend;
    @JsonProperty("filename_generator_backend_arguments")
    private String filenameGeneratorBackendArguments;
    private int id;
    private String label;
    @JsonProperty("quick_label_list_url")
    private String quickLabelListUrl;
    @JsonProperty("trash_time_period")
    private String trashTimePeriod;
    @JsonProperty("trash_time_unit")
    private String trashTimeUnit;
    private String url;


    public String getDeleteTimePeriod() {
        return deleteTimePeriod;
    }

    public void setDeleteTimePeriod(String deleteTimePeriod) {
        this.deleteTimePeriod = deleteTimePeriod;
    }

    public String getDeleteTimeUnit() {
        return deleteTimeUnit;
    }

    public void setDeleteTimeUnit(String deleteTimeUnit) {
        this.deleteTimeUnit = deleteTimeUnit;
    }

    public String getFilenameGeneratorBackend() {
        return filenameGeneratorBackend;
    }

    public void setFilenameGeneratorBackend(String filenameGeneratorBackend) {
        this.filenameGeneratorBackend = filenameGeneratorBackend;
    }

    public String getFilenameGeneratorBackendArguments() {
        return filenameGeneratorBackendArguments;
    }

    public void setFilenameGeneratorBackendArguments(String filenameGeneratorBackendArguments) {
        this.filenameGeneratorBackendArguments = filenameGeneratorBackendArguments;
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

    public String getQuickLabelListUrl() {
        return quickLabelListUrl;
    }

    public void setQuickLabelListUrl(String quickLabelListUrl) {
        this.quickLabelListUrl = quickLabelListUrl;
    }

    public String getTrashTimePeriod() {
        return trashTimePeriod;
    }

    public void setTrashTimePeriod(String trashTimePeriod) {
        this.trashTimePeriod = trashTimePeriod;
    }

    public String getTrashTimeUnit() {
        return trashTimeUnit;
    }

    public void setTrashTimeUnit(String trashTimeUnit) {
        this.trashTimeUnit = trashTimeUnit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "DocumentType{" +
                "deleteTimePeriod='" + deleteTimePeriod + '\'' +
                ", deleteTimeUnit='" + deleteTimeUnit + '\'' +
                ", filenameGeneratorBackend='" + filenameGeneratorBackend + '\'' +
                ", filenameGeneratorBackendArguments='" + filenameGeneratorBackendArguments + '\'' +
                ", id=" + id +
                ", label='" + label + '\'' +
                ", quickLabelListUrl='" + quickLabelListUrl + '\'' +
                ", trashTimePeriod='" + trashTimePeriod + '\'' +
                ", trashTimeUnit='" + trashTimeUnit + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
