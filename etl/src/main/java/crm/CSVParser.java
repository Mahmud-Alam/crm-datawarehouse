package crm;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {

    public List<String[]> readCSV(String filePath) throws IOException {

        List<String[]> rows = new ArrayList<>();

        try (
                FileReader reader = new FileReader(filePath);
                org.apache.commons.csv.CSVParser parser = CSVFormat.DEFAULT
                        .builder()
                        .setHeader()
                        .setSkipHeaderRecord(true)
                        .build()
                        .parse(reader)) {

            for (CSVRecord record : parser) {

                String[] row = new String[record.size()];

                for (int i = 0; i < record.size(); i++) {
                    row[i] = record.get(i);
                }

                rows.add(row);
            }
        }

        return rows;
    }
}