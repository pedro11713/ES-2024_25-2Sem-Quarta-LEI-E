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
