package co.grtk.um;

import co.grtk.ual.dto.UserActivityLogDTO;
import co.grtk.um.service.KafkaPublisherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
public class DataLoaderApplication implements CommandLineRunner {
    private final KafkaPublisherService kafkaPublisherService;
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(DataLoaderApplication.class)
                .web(WebApplicationType.NONE)
                .run(args).close();
    }


    public void loadData() {
        log.info("DataLoaderApplication starting ...");

        Map<Integer, List<String>> data = new HashMap<>();

        try (FileInputStream file = new FileInputStream("/Users/Imre/IdeaProjects/grtk/um/src/main/resources/lt1.xlsx"); ReadableWorkbook wb = new ReadableWorkbook(file)) {
            Sheet sheet = wb.getFirstSheet();
            try (Stream<Row> rows = sheet.openStream()) {
                rows.forEach(r -> {

                    UserActivityLogDTO userActivityLogDTO = new UserActivityLogDTO();
                    String eventId = r.getOptionalCell(3)
                            .map(Cell::asString)
                            .orElse(UUID.randomUUID().toString());

                    userActivityLogDTO.setEventId(eventId);
                    String token = r.getOptionalCell(4)
                            .map(Cell::asString)
                            .orElse(UUID.randomUUID().toString());
                    userActivityLogDTO.setToken(token);


                    userActivityLogDTO.setClientId(r.getCell(5).asString());
                    String category = r.getOptionalCell(6)
                            .map(Cell::asString)
                            .orElse("");
                    userActivityLogDTO.setCategory(category);
                    userActivityLogDTO.setActivityCode(r.getCell(10).asString());
                    userActivityLogDTO.setResultCode(r.getCell(11).asString());
                    userActivityLogDTO.setTimeStamp(r.getCell(12).asDate());
                    String correlationId = r.getOptionalCell(13)
                            .map(Cell::asString)
                            .orElse(UUID.randomUUID().toString());
                    userActivityLogDTO.setCorrelationId(correlationId);


                    String logLevel = r.getOptionalCell(14)
                            .map(Cell::asString)
                            .orElse("");
                    userActivityLogDTO.setLogLevel(logLevel);
                    String textParam = r.getOptionalCell(16)
                            .map(Cell::asString)
                            .orElse("");
                    userActivityLogDTO.setTextParams(textParam);
                    userActivityLogDTO.setAppId("GEORGE");

                    data.put(r.getRowNum(), new ArrayList<>());

                    for (Cell cell : r) {
                        if(Objects.isNull(cell))
                            data.get(r.getRowNum()).add("");
                        else
                            data.get(r.getRowNum()).add(cell.getRawValue());
                    }

                    //log.info("{}", userActivityLogDTO);
                    kafkaPublisherService.logUserActivity(userActivityLogDTO);
                });
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        if(args.length > 0 && "loadData".equals(args[0]))
            loadData();
    }
}
