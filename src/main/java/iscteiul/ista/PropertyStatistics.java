package iscteiul.ista;

import java.util.*;

/**
 * Classe que fornece funcionalidades estatísticas relacionadas com propriedades,
 * como o cálculo da área média numa determinada área administrativa.
 */
public class PropertyStatistics {

    /**
     * Calcula a área média simples das propriedades que pertencem a uma determinada área administrativa.
     *
     * @param properties Lista de propriedades a considerar.
     * @param tipoArea O tipo de área administrativa (por exemplo, "freguesia", "municipio" ou "ilha").
     * @param nomeArea O nome específico da área administrativa onde calcular a média.
     * @return A área média das propriedades nessa área administrativa, ou 0 se não existirem propriedades correspondentes.
     */
    public static double calcularAreaMedia(List<Property> properties, String tipoArea, String nomeArea) {
        double soma = 0;
        int contador = 0;

        for (Property p : properties) {
            boolean corresponde;
            switch (tipoArea.toLowerCase()) {
                case "freguesia":
                    corresponde = p.Freguesia.equalsIgnoreCase(nomeArea);
                    break;
                case "municipio":
                    corresponde = p.Municipio.equalsIgnoreCase(nomeArea);
                    break;
                case "ilha":
                    corresponde = p.Ilha.equalsIgnoreCase(nomeArea);
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de área inválido: " + tipoArea);
            }

            if (corresponde) {
                soma += p.Shape_Area;
                contador++;
            }
        }

        return (contador == 0) ? 0 : soma / contador;
    }
}
