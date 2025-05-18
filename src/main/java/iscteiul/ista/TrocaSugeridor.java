package iscteiul.ista;

import java.util.*;

public class TrocaSugeridor {

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

    private static String generateTrocaKey(String o1, String o2, Set<Integer> ids) {
        List<Integer> sortedIds = new ArrayList<>(ids);
        Collections.sort(sortedIds);
        return o1 + "-" + o2 + "-" + sortedIds.toString();
    }

    private static class TrocaSimples {
        Property p1, p2;

        public TrocaSimples(Property p1, Property p2) {
            this.p1 = p1;
            this.p2 = p2;
        }
    }
}
