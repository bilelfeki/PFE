package mss.pfe.fileIntegrator.entities;

public class CodeGenerated {
    private String repoCode;
    private String entityCode;

    private  SpringBatchCodeGenerated springBatchCode ;


    public CodeGenerated(String repoCode, String entityCode,SpringBatchCodeGenerated springBatchCode) {
        this.repoCode = repoCode;
        this.entityCode = entityCode;
        this.springBatchCode=springBatchCode;
    }

    public CodeGenerated() {

    }

    public String getRepoCode() {
        return repoCode;
    }

    public void setRepoCode(String repoCode) {
        this.repoCode = repoCode;
    }

    public String getEntityCode() {
        return entityCode;
    }

    public void setEntityCode(String entityCode) {
        this.entityCode = entityCode;
    }

    public SpringBatchCodeGenerated getSpringBatchCode() {
        return springBatchCode;
    }

    public void setSpringBatchCode(SpringBatchCodeGenerated springBatchCode) {
        this.springBatchCode = springBatchCode;
    }
}
