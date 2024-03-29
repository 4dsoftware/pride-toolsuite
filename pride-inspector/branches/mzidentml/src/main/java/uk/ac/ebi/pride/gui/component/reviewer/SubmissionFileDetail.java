package uk.ac.ebi.pride.gui.component.reviewer;

import uk.ac.ebi.pride.archive.web.service.model.file.FileDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rui Wang
 * @version $Id$
 */
public class SubmissionFileDetail {

    private final List<SubmissionFileDetail> resultFileMappings;
    private final List<SubmissionFileDetail> sourceFileMappings;
    private FileDetail fileDetail;
    private boolean download;

    public SubmissionFileDetail() {
        this(null);
    }

    public SubmissionFileDetail(FileDetail fileDetail) {
        if (fileDetail == null) {
            this.fileDetail = new FileDetail();
        } else {
            this.fileDetail = fileDetail;
        }
        this.resultFileMappings = new ArrayList<SubmissionFileDetail>();
        this.sourceFileMappings = new ArrayList<SubmissionFileDetail>();
    }

//    public Long getId() {
//        return fileDetail.getId();
//    }

    public String getProjectAccession() {
        return fileDetail.getProjectAccession();
    }

    public void setProjectAccession(String projectAccession) {
        fileDetail.setProjectAccession(projectAccession);
    }

    public String getAsssayAccession() {
        return fileDetail.getAssayAccession();
    }

    public String getFileName() {
        return fileDetail.getFileName();
    }

    public void setFileSize(long fileSize) {
        fileDetail.setFileSize(fileSize);
    }

    public void setAssayAccession(String assayId) {
        fileDetail.setAssayAccession(assayId);
    }

//    public void setId(Long id) {
//        fileDetail.setId(id);
//    }

    public void setFileType(String fileType) {
        fileDetail.setFileType(fileType);
    }

    public String getFileType() {
        return fileDetail.getFileType();
    }

    public void setFileName(String fileName) {
        fileDetail.setFileName(fileName);
    }

    public long getFileSize() {
        return fileDetail.getFileSize();
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public boolean hasSourceFileMappings() {
        return sourceFileMappings.size() > 0;
    }

    public List<SubmissionFileDetail> getSourceFileMappings() {
        return sourceFileMappings;
    }

    public void addSourceFileMapping(SubmissionFileDetail fileMapping) {
        sourceFileMappings.add(fileMapping);
    }

    public boolean hasResultFileMappings() {
        return resultFileMappings.size() > 0;
    }

    public List<SubmissionFileDetail> getResultFileMappings() {
        return resultFileMappings;
    }

    public void addResultFileMapping(SubmissionFileDetail fileMapping) {
        resultFileMappings.add(fileMapping);
    }
}
