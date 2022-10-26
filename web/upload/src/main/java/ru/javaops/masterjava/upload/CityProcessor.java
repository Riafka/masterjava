package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class CityProcessor {
    private static final int NUMBER_THREADS = 4;

    private static final JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);
    private static final CityDao cityDao = DBIProvider.getDao(CityDao.class);

    private final ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_THREADS);

    public void process(final InputStream is, int chunkSize) throws XMLStreamException, JAXBException, IOException {
        log.info("Start processing cities with chunkSize=" + chunkSize);

        List<Future<List<String>>> chunkFutures = new ArrayList<>();
        List<City> chunk = new ArrayList<>(chunkSize);
        is.mark(0);
        val processor = new StaxStreamProcessor(is);
        val unmarshaller = jaxbParser.createUnmarshaller();

        while (processor.startElement("City", "Cities")) {
            ru.javaops.masterjava.xml.schema.CityType xmlCity = unmarshaller.unmarshal(processor.getReader(), ru.javaops.masterjava.xml.schema.CityType.class);
            final City city = new City(xmlCity.getId(), xmlCity.getValue());
            chunk.add(city);
            if (chunk.size() == chunkSize) {
                addChunkFutures(chunkFutures, chunk);
                chunk = new ArrayList<>(chunkSize);
            }
        }
        if (!chunk.isEmpty()) {
            addChunkFutures(chunkFutures, chunk);
        }

        chunkFutures.forEach((future) -> {
            try {
                List<String> futureResult = future.get();
                log.info("successfully process cities with name: {}", futureResult);
            } catch (InterruptedException | ExecutionException e) {
                log.error("cities process failed", e);
            }
        });

        is.reset();
    }

    private void addChunkFutures(List<Future<List<String>>> chunkFutures, List<City> chunk) {
        Future<List<String>> future = executorService.submit(() -> cityDao.insertAndGetLogs(chunk));
        chunkFutures.add(future);
        log.info("Submit chunk: " + chunk);
    }
}
