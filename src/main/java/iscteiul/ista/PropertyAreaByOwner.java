package iscteiul.ista;

import java.util.*;

public class PropertyAreaByOwner {

    public PropertyAreaByOwner() {
    }

    public static double calcularAreaMedia(List<Property> properties, String tipoArea, String nomeArea) {
        Map<String, Double> areaPorOwner = new HashMap<>();

        for (Property p : properties) {
            boolean corresponde = false;

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
                // Sumamos el área al dueño
                areaPorOwner.put(
                    p.getOwner(),
                    areaPorOwner.getOrDefault(p.getOwner(), 0.0) + p.Shape_Area
                );
            }
        }

        // Calcular la media
        if (areaPorOwner.isEmpty()) return 0;

        double soma = 0;
        for (double area : areaPorOwner.values()) {
            soma += area;
        }

        return soma / areaPorOwner.size();
    }
}
