package ee.cinema.Backend;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
    private final String apiUrl = "https://api.jsonbin.io/v3/b/66048cdafe36e24a20a8ef8e?meta=false"; //lk kus mu json repo asi

    @GetMapping("/seansid")
    public List<Seanss> seansid() {
        // Fetch the JSON data from the API
        ExternalApiResponse response = restTemplate.getForObject(apiUrl, ExternalApiResponse.class);

        // Map the sessions to the Seanss structure JA sorteerin kuupäeva ja kella järgi
        List<Seanss> seanssList = response.getSeansid().stream()
                .map(seans -> {

                    ExternalApiFilm film = response.getFilmid().stream()
                            .filter(f -> f.getId() == seans.getFilm_id())
                            .findFirst()
                            .orElse(null);

                    if (film != null) {
                        return new Seanss(seans.getSeanss_id(), new Film(
                                film.getId(), film.getFilm(), film.getAge(), film.getLength(), film.getGenre()
                        ), seans.getKell(), Integer.parseInt(seans.getSaal()), seans.getKuupäev(), 50-seans.getHoivatud_kohad().size());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Seanss::kuupäev).thenComparing(Seanss::kell))
                .collect(Collectors.toList());

        return seanssList;
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetData() {
        String resetUrl = "https://api.jsonbin.io/v3/b/6606a70fe41b4d34e4dbec71?meta=false";

        // võtan algsed json andmed reset url-ist
        ExternalApiResponse resetData = restTemplate.getForObject(resetUrl, ExternalApiResponse.class);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Master-Key", "$2a$10$3jMY3eu7Dln/7A/8ObVOzujsc/4m33Bq4rI11HnA1wKMofwuGaF7u"); // replace with your jsonbin.io API key
        HttpEntity<ExternalApiResponse> entity = new HttpEntity<>(resetData, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.PUT, entity, String.class);

        return ResponseEntity.ok(responseEntity.getBody());
    }

    @GetMapping("/seanss/{id}")
    public Seanss getSeanssById(@PathVariable("id") Long seanssId) {

        ExternalApiResponse response = restTemplate.getForObject(apiUrl, ExternalApiResponse.class);

        // Map the sessions to the Seanss structure and filter by id
        Optional<Seanss> seanss = response.getSeansid().stream()
                .filter(seans -> seans.getSeanss_id() == seanssId)
                .map(seans -> {
                    ExternalApiFilm film = response.getFilmid().stream()
                            .filter(f -> f.getId() == seans.getFilm_id())
                            .findFirst()
                            .orElse(null);

                    if (film != null) {
                        return new Seanss(seans.getSeanss_id(), new Film(
                                film.getId(), film.getFilm(), film.getAge(), film.getLength(), film.getGenre()
                        ), seans.getKell(), Integer.parseInt(seans.getSaal()), seans.getKuupäev(), 50-seans.getHoivatud_kohad().size());
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .findFirst();

        return seanss.orElse(null);
    }

    @GetMapping("/hoivatud_kohad/{id}")
    public List<Integer> hoivatud_kohad(@PathVariable("id") Long seanssId) {
        ExternalApiResponse response = restTemplate.getForObject(apiUrl, ExternalApiResponse.class);
        for (ExternalApiSeans externalApiSeans : response.getSeansid()) {
            if (externalApiSeans.getSeanss_id() == seanssId) {
               // System.out.println(externalApiSeans.getHoivatud_kohad());
                return externalApiSeans.getHoivatud_kohad();
            }
        }
        return Collections.emptyList();
    }

    @GetMapping("/ajalugu")
    public List<ExternalApiAjalugu> ajalugu() {
        ExternalApiResponse response = restTemplate.getForObject(apiUrl, ExternalApiResponse.class);
        if (response == null) {
            return Collections.emptyList();
        }
        // Return the ajalugu list
        return response.getAjalugu();
    }

    @PutMapping("/piletid/{id}")
    public ResponseEntity<String> updateTicket(@PathVariable Long id, @RequestBody TicketRequest request) {
        // fetchin praeguse bin-i
        ExternalApiResponse response = restTemplate.getForObject(apiUrl, ExternalApiResponse.class);

        // uuendan seansi nii et lisan hõivatud kohad
        for (ExternalApiSeans seans : response.getSeansid()) {
            if (seans.getSeanss_id() == id) {
                seans.getHoivatud_kohad().addAll(request.getSelectedSeats());
                break;
            }
        }

        //lisame ajalugu listi info ostetud piletite kohta
        response.getAjalugu().add(new ExternalApiAjalugu(id, request.getSelectedSeats()));

        //saadame put requesti sinna bin-i oma andmetega
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Master-Key", "$2a$10$3jMY3eu7Dln/7A/8ObVOzujsc/4m33Bq4rI11HnA1wKMofwuGaF7u"); // replace with your jsonbin.io API key
        HttpEntity<ExternalApiResponse> entity = new HttpEntity<>(response, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.PUT, entity, String.class);

        return ResponseEntity.ok(responseEntity.getBody());
    }

    @GetMapping("/genres")
    public List<ExternalApiGenres> genres() {
        ExternalApiResponse response = restTemplate.getForObject(apiUrl, ExternalApiResponse.class);
        if (response == null) {
            return Collections.emptyList();
        }
        // Return the genres list
        return response.getGenres();
    }

    @PutMapping("/genres/{genre}")
    public ResponseEntity<String> updateGenre(@PathVariable String genre) {
        ExternalApiResponse response = restTemplate.getForObject(apiUrl, ExternalApiResponse.class);

        // vaatame kas genre on olemas
        Optional<ExternalApiGenres> existingGenre = response.getGenres().stream()
                .filter(g -> g.getGenre().equals(genre))
                .findFirst();
        //ut.println("genres: "+response);
        if (existingGenre.isPresent()) {
            // kui on siis suurendame counti
            existingGenre.get().setCount(existingGenre.get().getCount() + 1);
        } else {
            // kui ei ole siis lisame
            response.getGenres().add(new ExternalApiGenres(genre, 1));
        }

        // Send a PUT request to update the data
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Master-Key", "$2a$10$3jMY3eu7Dln/7A/8ObVOzujsc/4m33Bq4rI11HnA1wKMofwuGaF7u"); // replace with your jsonbin.io API key
        HttpEntity<ExternalApiResponse> entity = new HttpEntity<>(response, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.PUT, entity, String.class);
      //  System.out.println("response: "+response.getGenres());
       // System.out.println("responseEntity: "+responseEntity.getBody());
        return ResponseEntity.ok(responseEntity.getBody());
    }

    public static class TicketRequest {
        private List<Integer> selectedSeats;

        public List<Integer> getSelectedSeats() {
            return selectedSeats;
        }

    }


    public static class ExternalApiAjalugu {
        private long seanssId;
        private List<Integer> selectedSeats;

        public ExternalApiAjalugu(long seanssId, List<Integer> selectedSeats) {
            this.seanssId = seanssId;
            this.selectedSeats = selectedSeats;
        }

        public long getSeanssId() {
            return seanssId;
        }

        public List<Integer> getSelectedSeats() {
            return selectedSeats;
        }
    }

    public static class ExternalApiGenres {
        private String genre;
        private int count;

        public ExternalApiGenres(String genre, int count) {
            this.genre = genre;
            this.count = count;
        }
        public String getGenre() {
            return genre;
        }
        public int getCount() {
            return count;
        }
        public void setCount(int count) {
            this.count = count;
        }
    }


    // Defineerin millisel kujul ma saan jsoni fetchiga kátte
    public static class ExternalApiResponse {

        @JsonProperty("genres")
        private List<ExternalApiGenres> genres = new ArrayList<>();
        @JsonProperty("ajalugu")
        private List<ExternalApiAjalugu> ajalugu = new ArrayList<>();
        @JsonProperty("filmid")
        private List<ExternalApiFilm> filmid;
        @JsonProperty("seansid")
        private List<ExternalApiSeans> seansid;

        public List<ExternalApiFilm> getFilmid() {
            return filmid;
        }

        public List<ExternalApiSeans> getSeansid() {
            return seansid;
        }

        public List<ExternalApiAjalugu> getAjalugu() {
            return ajalugu;
        }

        public void setAjalugu(List<ExternalApiAjalugu> ajalugu) {
            this.ajalugu = ajalugu;
        }

        public void setFilmid(List<ExternalApiFilm> filmid) {
            this.filmid = filmid;
        }

        public void setSeansid(List<ExternalApiSeans> seansid) {
            this.seansid = seansid;
        }
        public List<ExternalApiGenres> getGenres() {
            return genres;
        }

        public void setGenres(List<ExternalApiGenres> genres) {
            this.genres = genres;
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

        @JsonProperty("id")
        private long id;
        private String kell;
        private String saal;
        private String kuupäev;
        private int vabu_kohti;

        @JsonProperty("hõivatud_kohad")
        private List<Integer> hõivatud_kohad = new ArrayList<>();

        public List<Integer> getHoivatud_kohad() {
            return hõivatud_kohad;
        }


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

        public String getKuupäev() {
            return kuupäev;
        }

        public int getVabu_kohti() {
            return vabu_kohti;
        }
    }


    // Fetcitud jsoni saan teha defineeritud recordite jargi isenditeks
    public record Film(long id, String film, String age, int length, String genre) {
    }

    public record Seanss(long id, Film film, String kell, int saal, String kuupäev, int vabu_kohti) {
    }

    public record Ajalugu(long seanssId, int selectedSeatsSize) {
    }
}