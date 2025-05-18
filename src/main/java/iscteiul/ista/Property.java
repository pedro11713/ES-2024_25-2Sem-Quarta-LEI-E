package iscteiul.ista;

import java.util.Objects;

/**
 * Representa uma propriedade geográfica com atributos como identificador,
 * localização administrativa, área, perímetro e geometria em formato String.
 * Esta classe serve como modelo de dados para representar terrenos e
 * propriedades, podendo ser usada para análise espacial e construção de grafos de vizinhança.
 */
public class Property {
    public int OBJECTID;
    public String PAR_ID;
    public String PAR_NUM;
    public double Shape_Length;
    public double Shape_Area;
    public String geometry; // Ainda como string; pode ser processado depois
    public String OWNER;
    public String Freguesia;
    public String Municipio;
    public String Ilha;

    /**
     * Construtor da classe Property que inicializa todos os atributos da propriedade.
     *
     * @param OBJECTID      Identificador único da propriedade.
     * @param PAR_ID        Identificador parcelar.
     * @param PAR_NUM       Número da parcela.
     * @param Shape_Length  Perímetro da propriedade.
     * @param Shape_Area    Área da propriedade.
     * @param geometry      Representação da geometria em formato string.
     * @param OWNER         Nome do proprietário.
     * @param Freguesia     Freguesia onde se localiza a propriedade.
     * @param Municipio     Município onde se localiza a propriedade.
     * @param Ilha          Ilha onde se localiza a propriedade.
     */
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

    /**
     * Devolve o identificador único da propriedade.
     *
     * @return O OBJECTID da propriedade.
     */
    int getOBJECTID() {
        return OBJECTID;
    }

    /**
     * Devolve o nome do proprietário da propriedade.
     *
     * @return O nome do proprietário.
     */
    String getOwner() {
        return OWNER;
    }

    /**
     * Devolve uma descrição textual do objeto Property, com os principais detalhes
     * como o identificador, número de parcela, área, perímetro e localização.
     *
     * @return Uma string com a informação detalhada da propriedade.
     */
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

    /**
     * Extrai os pontos de coordenadas de uma String no formato, removendo os
     * parênteses exteriores e devolvendo apenas os valores relevantes.
     *
     * @param polygonString A String que representa a geometria da propriedade.
     * @return Uma String com os pontos da geometria, ou uma String vazia caso o
     *         formato seja inválido.
     */
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

    /**
     * Compara esta propriedade com outra, considerando apenas o seu identificador
     * único (OBJECTID).
     *
     * @param o O objeto a comparar com esta instância.
     * @return true se ambas as propriedades tiverem o mesmo OBJECTID; false caso
     *         contrário.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Property))
            return false;
        Property property = (Property) o;
        return OBJECTID == property.OBJECTID;
    }

    /**
     * Gera um código de dispersão (hash) com base no identificador único da
     * propriedade.
     * Este método é útil para utilização em coleções como HashMap ou HashSet.
     *
     * @return O valor hash correspondente a esta propriedade.
     */
    @Override
    public int hashCode() {
        return Objects.hash(OBJECTID);
    }
}
