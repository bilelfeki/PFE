package mss.pfe.fileIntegrator.entities;

public class Entity {
    private String entityName;
    private Champ[] columns;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Champ[] getColumns() {
        return columns;
    }

    public void setColumns(Champ[] columns) {
        this.columns = columns;
    }
}
