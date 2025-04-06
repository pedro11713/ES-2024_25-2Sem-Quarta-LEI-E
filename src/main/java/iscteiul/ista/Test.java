package iscteiul.ista;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        String filePath = "Madeira-Moodle-1.1.csv";
        List<Property> properties = CsvReaderUtil.loadPropertiesFromCSV(filePath);

        // Print the first and 2 property
        if (!properties.isEmpty()) {
            System.out.println(properties.get(0)); // Só para teste
            System.out.println(properties.get(1));
        }
    }

}
