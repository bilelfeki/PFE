package mss.pfe.fileIntegrator.entities;

public class SpringBatchCodeGenerated {
    private String processor;
    private String writer;

    private String job;

    private String step;

    private String reader;

    public SpringBatchCodeGenerated(String processor, String writer, String job, String step, String reader) {
        this.processor = processor;
        this.writer = writer;
        this.job = job;
        this.step = step;
        this.reader = reader;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getReader() {
        return reader;
    }

    public void setReader(String reader) {
        this.reader = reader;
    }
}
