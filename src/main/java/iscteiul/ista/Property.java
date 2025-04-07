package iscteiul.ista;

import java.util.Objects;

public class Property {
    public int OBJECTID;
    public String PAR_ID;
    public String PAR_NUM;
    public double Shape_Length;
    public double Shape_Area;
    public String geometry;  // Ainda como string; pode ser processado depois
    public String OWNER;
    public String Freguesia;
    public String Municipio;
    public String Ilha;

    public Property(int OBJECTID, String PAR_ID, String PAR_NUM, double Shape_Length, double Shape_Area,
                    String geometry, String OWNER, String Freguesia, String Municipio, String Ilha) {
        this.OBJECTID = OBJECTID;
        this.PAR_ID = PAR_ID;
        this.PAR_NUM = PAR_NUM;
        this.Shape_Length = Shape_Length;
        this.Shape_Area = Shape_Area;
        this.geometry = geometry;

        this.OWNER = OWNER;
        this.Freguesia = Freguesia;
        this.Municipio = Municipio;
        this.Ilha = Ilha;
    }

    @Override
    public String toString() {
        return "Property{" +
                "OBJECTID=" + OBJECTID +
                ", PAR_ID='" + PAR_ID + '\'' +
                ", PAR_NUM='" + PAR_NUM + '\'' +
                ", Shape_Length=" + Shape_Length +
                ", Shape_Area=" + Shape_Area +
                ", OWNER='" + OWNER + '\'' +
                ", Freguesia='" + Freguesia + '\'' +
                ", Municipio='" + Municipio + '\'' +
                ", Ilha='" + Ilha + '\'' +
                '}';
    }
    public String getGeometry(String polygonString) {
        // Encontra o índice do primeiro '(' mais interno
        int startIndex = polygonString.lastIndexOf("((");
        if (startIndex == -1) {
            startIndex = polygonString.lastIndexOf("(");
            if (startIndex == -1) {
                return ""; // Não encontrou parênteses
            }
        } else {
            startIndex += 1; // Ajusta para pegar após o segundo '('
        }

        // Encontra o índice do último ')' mais interno
        int endIndex = polygonString.indexOf("))");
        if (endIndex == -1) {
            endIndex = polygonString.indexOf(")");
            if (endIndex == -1) {
                return ""; // Não encontrou parênteses de fechamento
            }
        }

        // Extrai a substring entre os parênteses mais internos
        String result = polygonString.substring(startIndex + 1, endIndex).trim();

        // Remove possíveis parênteses extras que possam ter sobrado
        result = result.replace("(", "").replace(")", "").trim();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Property)) return false;
        Property property = (Property) o;
        return OBJECTID == property.OBJECTID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(OBJECTID);
    }
}
