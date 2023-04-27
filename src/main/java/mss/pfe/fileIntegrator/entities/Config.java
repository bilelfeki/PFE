package mss.pfe.fileIntegrator.entities;

public class Config {
    private String database;
    private String schema;

    public Config(String database, String schema) {
        this.database = database;
        this.schema = schema;
    }

    public Config() {
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}
