package ee.cinema.Backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import java.util.stream.Collectors;
@CrossOrigin(origins = "http://localhost:8080") // port kust frontend jookseb
@RestController
@SpringBootApplication
public class ServerController {
    public static void main(String[] args) {
        SpringApplication.run(ServerController.class, args);
    }


    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiUrl = "https://api.npoint.io/967c910691a90010739d";

    @GetMapping("/seansid")
    public List<Seanss> seansid() {
        // Fetch the JSON data from the API
        ExternalApiResponse response = restTemplate.getForObject(apiUrl, ExternalApiResponse.class);

        // Map the sessions to the Seanss structure and sort them by kuupäev and kell
        List<Seanss> seanssList = response.getSeansid().stream()
                .map(seans -> {
                    // Find the corresponding film information
                    ExternalApiFilm film = response.getFilmid().stream()
                            .filter(f -> f.getId() == seans.getFilm_id())
                            .findFirst()
                            .orElse(null);

                    if (film != null) {
                        return new Seanss(seans.getSeanss_id(), new Film(
                                film.getId(), film.getFilm(), film.getAge(), film.getLength(), film.getGenre()
                        ), seans.getKell(), Integer.parseInt(seans.getSaal()), seans.getKuupäev(), seans.getVabu_kohti());
                    }
                    return null;
                })
                .filter(Objects::nonNull) // filter out nulls in case there's no corresponding film
                .sorted(Comparator.comparing(Seanss::kuupäev).thenComparing(Seanss::kell))
                .collect(Collectors.toList());

        return seanssList;
    }

    @GetMapping("/hoivatud_kohad/{id}")
    public List<Integer> hoivatud_kohad(@PathVariable("id") Long seanssId){
        ExternalApiResponse response = restTemplate.getForObject(apiUrl, ExternalApiResponse.class);
        for (ExternalApiSeans externalApiSeans : response.getSeansid()) {
            if (externalApiSeans.getSeanss_id() == seanssId) {
                System.out.println(externalApiSeans.getHoivatud_kohad());
                return externalApiSeans.getHoivatud_kohad();
            }
        }
        return Collections.emptyList();
    }




    // Defineerin millisel kujul ma saan jsoni fetchiga kátte
    public static class ExternalApiResponse {
        private List<ExternalApiFilm> filmid;
        private List<ExternalApiSeans> seansid;

        public List<ExternalApiFilm> getFilmid() {
            return filmid;
        }

        public List<ExternalApiSeans> getSeansid() {
            return seansid;
        }

    }



    public static class ExternalApiFilm {
        private long id;
        private String film;
        private String age;
        private int length;
        private String genre;

        public long getId() {
            return id;
        }

        public String getFilm() {
            return film;
        }


        public String getAge() {
            return age;
        }

        public int getLength() {
            return length;
        }

        public String getGenre() {
            return genre;
        }

    }

    public static class ExternalApiSeans {
        private long film_id;

        @JsonProperty("id") // Ensure this matches the JSON key exactly
        private long id;
        private String kell;
        private String saal;
        private String kuupäev; // added new field for date
        private int vabu_kohti; // added new field for available seats

        @JsonProperty("hõivatud_kohad")
        private List<Integer> hõivatud_kohad;
        public List<Integer> getHoivatud_kohad() {
            return hõivatud_kohad;
        }

        // Getters (and setters if needed)
        public long getSeanss_id() {
            return id;
        }
        public long getFilm_id() {
            return film_id;
        }

        public String getKell() {
            return kell;
        }

        public String getSaal() {
            return saal;
        }

        public String getKuupäev() { return kuupäev; } // getter for date

        public int getVabu_kohti() { return vabu_kohti; } // getter for available seats
    }




    // Fetcitud jsoni teen all defineeritud recordite jargi objektideks
    public record Film(long id, String film, String age, int length, String genre) { }
    public record Seanss(long id, Film film, String kell, int saal, String kuupäev, int vabu_kohti) { }
}