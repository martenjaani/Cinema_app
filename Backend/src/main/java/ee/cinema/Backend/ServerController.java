package ee.cinema.Backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.util.List;
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
    public Seansid seansid() {
        // Fetchin json andmed oma npoint binnist
        ExternalApiResponse response = restTemplate.getForObject(apiUrl, ExternalApiResponse.class);

        // See mappimine on tehtud AI abiga
        // Map the external response to your Seansid structure
        List<Seanss> seanssList = response.getFilmid().stream()
                .map(film -> {
                    // Find the corresponding session information
                    ExternalApiSeans seans = response.getSeansid().stream()
                            .filter(s -> s.getFilm_id() == film.getId())
                            .findFirst()
                            .orElse(null);

                    if (seans != null) {
                        return new Seanss(film.getId(), new Film(
                                film.getId(), film.getFilm(), film.getAge(), film.getLength(), film.getGenre()
                        ), seans.getKell(), Integer.parseInt(seans.getSaal()));
                    }
                    return null;
                })
                .filter(s -> s != null) // filter out nulls in case there's no corresponding session
                .collect(Collectors.toList());

        return new Seansid(seanssList);
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
        private String kell;
        private String saal;

        public long getFilm_id() {
            return film_id;
        }

        public String getKell() {
            return kell;
        }

        public String getSaal() {
            return saal;
        }

    }
    // Fetcitud jsoni teen all defineeritud recordite jargi objektideks
    public record Film(long id, String film, String age, int length, String genre) { }
    public record Seanss(long id, Film film , String kell, int saal) { }
    public record Seansid (List<Seanss>seansid){ }
}