package edu.eci.arsw.rmi;

import java.io.Serializable;

public class Equipment implements Serializable {
    private String code;
    private String name;
    private String laboratory;
    private boolean isAvailable;

    public Equipment(String code, String name, String laboratory) {
        this.code = code;
        this.name = name;
        this.laboratory = laboratory;
        this.isAvailable = true;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getLaboratory() {
        return laboratory;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return code + " - " + name + " (" + laboratory + ") - " + (isAvailable ? "DISPONIBLE" : "RESERVADO");
    }
}

