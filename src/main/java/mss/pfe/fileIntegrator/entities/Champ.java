package mss.pfe.fileIntegrator.entities;


import jakarta.persistence.Entity;

public class Champ  {
    private String valeur;
    private Integer valeur_min;
    private Integer valeur_max;
    private String champType;
    private String type;

    public String getChampType() {
        return champType;
    }

    public void setChampType(String champType) {
        this.champType = champType;
    }

    public Champ() {
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public Integer getValeur_min() {
        return valeur_min;
    }

    public void setValeur_min(Integer valeur_min) {
        this.valeur_min = valeur_min;
    }

    public Integer getValeur_max() {
        return valeur_max;
    }

    public void setValeur_max(Integer valeur_max) {
        this.valeur_max = valeur_max;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}