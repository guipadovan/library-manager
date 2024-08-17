package com.guipadovan.librarymanager.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guipadovan.librarymanager.entities.Book;
import com.guipadovan.librarymanager.services.GoogleBooksService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link GoogleBooksService} interface.
 */
@Service
public class GoogleBooksServiceImpl implements GoogleBooksService {

    private static final String GOOGLE_BOOKS_API_URL_TEMPLATE = "https://www.googleapis.com/books/v1/volumes?q=intitle:%s&key=%s";

    @Value("${google.books.api-key}")
    private String apiKey;

    @Override
    public List<Book> searchBooksByTitle(String title) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(GOOGLE_BOOKS_API_URL_TEMPLATE, title, apiKey);
        String jsonResponse = restTemplate.getForObject(url, String.class);

        return mapBooks(jsonResponse);
    }

    /**
     * Maps the JSON response from the Google Books API to a list of Book objects.
     *
     * @param jsonResponse the JSON response from the API
     *
     * @return a list of Book objects
     *
     * @throws IOException if there is an error during JSON parsing
     */
    private List<Book> mapBooks(String jsonResponse) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonResponse);
        JsonNode itemsNode = rootNode.path("items");

        List<Book> books = new ArrayList<>();
        if (itemsNode.isArray()) {
            for (JsonNode itemNode : itemsNode) {
                JsonNode volumeInfo = itemNode.path("volumeInfo");

                // Mapeia os dados do volumeInfo para o objeto Book
                String title = volumeInfo.path("title").asText();
                String author = volumeInfo.path("authors").isArray()
                        ? volumeInfo.path("authors").get(0).asText()
                        : "";
                String isbn = volumeInfo.path("industryIdentifiers").isArray()
                        ? volumeInfo.path("industryIdentifiers").get(0).path("identifier").asText()
                        : "";
                String publicationDate = volumeInfo.path("publishedDate").asText();
                String category = volumeInfo.path("categories").isArray()
                        ? volumeInfo.path("categories").get(0).asText()
                        : "";

                // A API não aceita valores vazios, então se algum campo estiver vazio, ignora o book
                if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || publicationDate.isEmpty() || category.isEmpty()) {
                    continue;
                }

                books.add(new Book(title, author, isbn, parseDate(publicationDate), category));
            }
        }

        return books;
    }

    /**
     * Google Books API returns publication dates in various formats, such as "2002-01-18" or "2002".
     * Parses a date string in the format "YYYY-MM-DD", "YYYY-MM", or "YYYY".
     *
     * @param dateStr the date string to parse
     *
     * @return the parsed LocalDate object, or null if the date string is invalid
     */
    private LocalDate parseDate(String dateStr) {
        Optional<LocalDate> parsedDate = Optional.empty();

        // Tenta  dar parse da data como ISO "YYYY-MM-DD"
        try {
            parsedDate = Optional.of(LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE));
        } catch (DateTimeParseException ignored) {
        }

        // Tenta dar parse da data como "YYYY-MM" acrescentando "-01"
        if (parsedDate.isEmpty()) {
            try {
                parsedDate = Optional.of(LocalDate.parse(dateStr + "-01", DateTimeFormatter.ofPattern("yyyy-MM")));
            } catch (DateTimeParseException ignored) {
            }
        }

        // Tenta dar parse da data como "YYYY" com o mês e dia fixados em 1
        if (parsedDate.isEmpty()) {
            try {
                parsedDate = Optional.of(LocalDate.of(Integer.parseInt(dateStr), 1, 1));
            } catch (NumberFormatException ignored) {
            }
        }

        // Se nenhum dos casos de parse for bem sucedido, retorna null
        return parsedDate.orElse(null);
    }
}
