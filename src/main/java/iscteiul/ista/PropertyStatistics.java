package iscteiul.ista;

import java.util.*;

public class PropertyStatistics {

    /**
     * Calcula a área média simples das propriedades numa área administrativa.
     */
    public static double calcularAreaMedia(List<Property> properties, String tipoArea, String nomeArea) {
        double soma = 0;
        int contador = 0;

        for (Property p : properties) {
            boolean corresponde = switch (tipoArea.toLowerCase()) {
                case "freguesia" -> p.Freguesia.equalsIgnoreCase(nomeArea);
                case "municipio" -> p.Municipio.equalsIgnoreCase(nomeArea);
                case "ilha" -> p.Ilha.equalsIgnoreCase(nomeArea);
                default -> throw new IllegalArgumentException("Tipo de área inválido: " + tipoArea);
            };

            if (corresponde) {
                soma += p.Shape_Area;
                contador++;
            }
        }

        return (contador == 0) ? 0 : soma / contador;
    }

 
}
