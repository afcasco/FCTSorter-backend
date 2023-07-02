package dev.afcasco.fctsorterbackend.utils;


import dev.afcasco.fctsorterbackend.model.Company;
import dev.afcasco.fctsorterbackend.model.Status;
import dev.afcasco.fctsorterbackend.repository.CompanyRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Component
public class CsvParser {

    CompanyRepository repository;
    private final static int NAME_CIF = 0;
    private final static int ADDRESS = 2;
    private final static int CITY = 4;
    private final static int ZIP_CODE = 3;
    private final static int PHONE = 5;

    public CsvParser(CompanyRepository repository) {
        this.repository = repository;
    }

    public List<Company> readAllFromCsv() {
        try {
            Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
            Predicate<String> isNumeric = (input) -> pattern.matcher(input).matches();
            List<String> data = Files.readAllLines(Paths.get("src/main/resources/empreses.csv"));
            return data.stream().map(i -> i.split("\\|"))
                    .filter(row -> isNumeric.test(row[3]))
                    .map(i ->
                            new Company(
                                    0L,
                                    i[NAME_CIF].substring(i[NAME_CIF].indexOf("(")+1,i[NAME_CIF].indexOf(")")),
                                    i[NAME_CIF].substring(NAME_CIF,i[NAME_CIF].indexOf("(")-1),
                                    i[ADDRESS],
                                    i[CITY],
                                    i[ZIP_CODE],
                                    i[PHONE],
                                    Status.ACTIVE

                            )).peek(i-> {
                        if(i.getZipCode().length() == 4){
                            i.setZipCode("0"+i.getZipCode());
                        }
                    })
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
