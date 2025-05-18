package iscteiul.ista;

import java.util.*;

/**
 * Classe utilitária para cálculo de estatísticas sobre propriedades,
 * nomeadamente a área média detida por proprietário numa determinada região.
 */
public class PropertyAreaByOwner {

    /**
     * Construtor vazio da classe. Não realiza nenhuma operação.
     */
    public PropertyAreaByOwner() {
    }

    /**
     * Calcula a área média de propriedades por proprietário numa determinada
     * área geográfica (freguesia, município ou ilha).
     *
     * @param properties Lista de propriedades a analisar.
     * @param tipoArea   Tipo de área a considerar: "freguesia", "municipio" ou "ilha".
     * @param nomeArea   Nome da área geográfica a filtrar.
     * @return A área média de propriedades por proprietário nessa área.
     *         Devolve 0 caso não existam proprietários na área especificada.
     * @throws IllegalArgumentException Se o tipo de área fornecido for inválido.
     */
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
                // Soma a área à propriedade do respetivo proprietário
                areaPorOwner.put(
                        p.getOwner(),
                        areaPorOwner.getOrDefault(p.getOwner(), 0.0) + p.Shape_Area
                );
            }
        }

        // Calcula a média
        if (areaPorOwner.isEmpty()) return 0;

        double soma = 0;
        for (double area : areaPorOwner.values()) {
            soma += area;
        }

        return soma / areaPorOwner.size();
    }
}
