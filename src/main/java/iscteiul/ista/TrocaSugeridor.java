package iscteiul.ista;

import java.util.*;

/**
 * Classe responsável por sugerir trocas duplas entre propriedades de diferentes proprietários,
 * com base em critérios de vizinhança e semelhança de área.
 */
public class TrocaSugeridor {

    /**
     * Gera sugestões de trocas duplas entre pares de proprietários, analisando propriedades vizinhas com áreas semelhantes.
     * As sugestões garantem que todas as propriedades envolvidas são distintas e que a troca resulta num ganho potencial para os envolvidos.
     *
     * @param properties Lista de todas as propriedades disponíveis.
     * @param propertyGraph Grafo que representa as vizinhanças entre propriedades.
     * @param ownerGraph Grafo que representa as relações entre proprietários.
     * @return Lista de strings com descrições detalhadas das trocas duplas sugeridas.
     */
    public static List<String> sugerirTrocasDuplas(List<Property> properties, PropertyGraph propertyGraph, OwnerGraph ownerGraph) {
        List<String> sugestoes = new ArrayList<>();
        Set<String> trocasRegistradas = new HashSet<>();

        Map<String, List<Property>> propsPorOwner = new HashMap<>();
        for (Property p : properties) {
            propsPorOwner.computeIfAbsent(p.OWNER, k -> new ArrayList<>()).add(p);
        }

        for (String owner1 : ownerGraph.getAllOwners()) {
            for (String owner2 : ownerGraph.getNeighbors(owner1)) {
                if (owner1.compareTo(owner2) >= 0) continue;

                List<Property> props1 = propsPorOwner.getOrDefault(owner1, Collections.emptyList());
                List<Property> props2 = propsPorOwner.getOrDefault(owner2, Collections.emptyList());

                List<TrocaSimples> trocasValidas = new ArrayList<>();

                for (Property p1 : props1) {
                    for (Property p2 : props2) {
                        if (!propertyGraph.areNeighbors(p1, p2)) continue;

                        double diff = Math.abs(p1.Shape_Area - p2.Shape_Area);
                        if (diff < 50.0) {
                            trocasValidas.add(new TrocaSimples(p1, p2));
                        }
                    }
                }

                for (int i = 0; i < trocasValidas.size(); i++) {
                    for (int j = i + 1; j < trocasValidas.size(); j++) {
                        TrocaSimples t1 = trocasValidas.get(i);
                        TrocaSimples t2 = trocasValidas.get(j);

                        Set<Integer> ids = new HashSet<>(Arrays.asList(
                                t1.p1.getOBJECTID(), t1.p2.getOBJECTID(),
                                t2.p1.getOBJECTID(), t2.p2.getOBJECTID()
                        ));

                        if (ids.size() < 4) continue;

                        String chave = generateTrocaKey(owner1, owner2, ids);
                        if (trocasRegistradas.contains(chave)) continue;
                        trocasRegistradas.add(chave);

                        // Calcular área inicial
                        double areaInicial1 = t1.p1.OWNER.equals(owner1) ? t1.p1.Shape_Area : t1.p2.Shape_Area;
                        areaInicial1 += t2.p1.OWNER.equals(owner1) ? t2.p1.Shape_Area : t2.p2.Shape_Area;

                        double areaInicial2 = t1.p1.OWNER.equals(owner2) ? t1.p1.Shape_Area : t1.p2.Shape_Area;
                        areaInicial2 += t2.p1.OWNER.equals(owner2) ? t2.p1.Shape_Area : t2.p2.Shape_Area;

                        // Calcular área final
                        double areaFinal1 = t1.p1.OWNER.equals(owner1) ? t2.p2.Shape_Area : t2.p1.Shape_Area;
                        areaFinal1 += t2.p1.OWNER.equals(owner1) ? t1.p2.Shape_Area : t1.p1.Shape_Area;

                        double areaFinal2 = t1.p1.OWNER.equals(owner2) ? t2.p2.Shape_Area : t2.p1.Shape_Area;
                        areaFinal2 += t2.p1.OWNER.equals(owner2) ? t1.p2.Shape_Area : t1.p1.Shape_Area;

                        double ganho1 = areaFinal1 - areaInicial1;
                        double ganho2 = areaFinal2 - areaInicial2;

                        String sugestao = String.format(
                                "Troca dupla sugerida entre %s e %s:\n" +
                                        "  1ª troca: %d (%.2f m²) ↔ %d (%.2f m²)\n" +
                                        "  2ª troca: %d (%.2f m²) ↔ %d (%.2f m²)\n" +
                                        "  Após troca:\n" +
                                        "    %s fica com %.2f m² (ganho líquido: %.2f m²)\n" +
                                        "    %s fica con %.2f m² (ganho líquido: %.2f m²)\n",
                                owner1, owner2,
                                t1.p1.getOBJECTID(), t1.p1.Shape_Area, t1.p2.getOBJECTID(), t1.p2.Shape_Area,
                                t2.p1.getOBJECTID(), t2.p1.Shape_Area, t2.p2.getOBJECTID(), t2.p2.Shape_Area,
                                owner1, areaFinal1, ganho1,
                                owner2, areaFinal2, ganho2
                        );

                        sugestoes.add(sugestao);
                    }
                }
            }
        }

        return sugestoes;
    }

    /**
     * Gera uma chave única para identificar uma troca dupla entre dois proprietários com base nos IDs das propriedades envolvidas.
     *
     * @param o1 Identificador do primeiro proprietário.
     * @param o2 Identificador do segundo proprietário.
     * @param ids Conjunto de IDs das propriedades envolvidas na troca.
     * @return String que representa de forma única a troca dupla.
     */
    private static String generateTrocaKey(String o1, String o2, Set<Integer> ids) {
        List<Integer> sortedIds = new ArrayList<>(ids);
        Collections.sort(sortedIds);
        return o1 + "-" + o2 + "-" + sortedIds.toString();
    }

    /**
     * Classe auxiliar que representa uma troca simples entre duas propriedades.
     */
    private static class TrocaSimples {
        Property p1, p2;

        public TrocaSimples(Property p1, Property p2) {
            this.p1 = p1;
            this.p2 = p2;
        }
    }

    /**
     * Classe pública que contém informação agregada sobre trocas duplas entre dois proprietários.
     */
    public static class TrocaDuplaInfo {
        public String owner1, owner2;
        public int totalTrocas;
        public double ganhoTotalOwner1;
        public double ganhoTotalOwner2;

        /**
         * Construtor para armazenar informações estatísticas sobre trocas duplas.
         *
         * @param owner1 Primeiro proprietário.
         * @param owner2 Segundo proprietário.
         * @param totalTrocas Número total de trocas entre os dois.
         * @param ganhoTotalOwner1 Ganho líquido total do primeiro proprietário.
         * @param ganhoTotalOwner2 Ganho líquido total do segundo proprietário.
         */
        public TrocaDuplaInfo(String owner1, String owner2, int totalTrocas,
                              double ganhoTotalOwner1, double ganhoTotalOwner2) {
            this.owner1 = owner1;
            this.owner2 = owner2;
            this.totalTrocas = totalTrocas;
            this.ganhoTotalOwner1 = ganhoTotalOwner1;
            this.ganhoTotalOwner2 = ganhoTotalOwner2;
        }
    }
}
