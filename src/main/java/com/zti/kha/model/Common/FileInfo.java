

package com.zti.kha.model.Common;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.zti.kha.model.Base.BaseModel;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
        "id","fileName","fileType"

})
public class FileInfo extends BaseModel implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    long fileLength;

    @JsonProperty("fileName")
    protected String fileName;


    @JsonProperty("downloadUri")
    protected String downloadUri;

    @JsonProperty("fileType")
    protected String fileType;

    @JsonProperty("fileName")
    public String getFileName() {
        return fileName;
    }

    @JsonProperty("fileName")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }



    @JsonProperty("fileType")
    public String getFileType() {
        return fileType;
    }

    @JsonProperty("fileType")
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public String getDownloadUri() {
        return downloadUri;
    }

    public void setDownloadUri(String downloadUri) {
        this.downloadUri = downloadUri;
    }
}