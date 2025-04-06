package iscteiul.ista;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

public class CsvReaderUtil {

    // Método genérico para ler linhas de CSV (com opção de ignorar cabeçalho)
    public static List<String[]> readCSV(String filePath, boolean skipHeader) {
        List<String[]> lines = new ArrayList<>();

        try {
            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';')
                    .build();

            CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                    .withCSVParser(parser)
                    .build();

            String[] line;
            boolean isFirstLine = true;

            while ((line = reader.readNext()) != null) {
                if (skipHeader && isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                lines.add(line);
            }

            reader.close();

        } catch (IOException | CsvValidationException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
            e.printStackTrace();
        }

        return lines;
    }

    // Método para criar a lista de propriedades a partir do CSV
    public static List<Property> loadPropertiesFromCSV(String filePath) {
        List<String[]> csvLines = readCSV(filePath, true);
        List<Property> properties = new ArrayList<>();

        for (String[] fields : csvLines) {
            Property property = new Property();
            property.setAttribute("OBJECTID", fields[0]);
            property.setAttribute("PAR_ID", fields[1]);
            property.setAttribute("PAR_NUM", fields[2]);
            property.setAttribute("Shape_Length", fields[3]);
            property.setAttribute("Shape_Area", fields[4]);
            property.setAttribute("geometry", fields[5]);
            property.setAttribute("OWNER", fields[6]);
            property.setAttribute("Parish", fields[7]);
            property.setAttribute("Municipality", fields[8]);
            property.setAttribute("Island", fields[9]);

            properties.add(property);
        }

        return properties;
    }
}
