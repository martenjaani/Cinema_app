<template>
  <div class="istmete-valimine" >
    <div class="film-info">
      <p>{{ film }} | {{ genre }} | {{ kuupäev }} {{ kell }} | saal {{ saal }}</p>
    </div>

  <h2>Vali kohtade arv</h2>
  <br>


  <div class="seat-selector">

    <div class="kohtadeValik">{{ istekohtadeArv }}</div>
    <button @click="vahenda" class="minus-button">-</button>
    <button @click="suurenda" class="plus-button">+</button>
    <button @click="osta" class="osta-nupp">Osta</button>

  </div>



    <br>

    <SaaliPlaan
        ref="SaaliPlaan"
        :occupiedSeats="occupiedSeats"
        :selectedSeats="selectedSeats"
        :istekohtadeArv="istekohtadeArv"
        @update:selectedSeats="selectedSeats = $event"
    />

  </div>
</template>

<script>
import SaaliPlaan from '@/components/SaaliPlaan.vue';
import {toRaw} from "vue";
import saaliPlaan from "@/components/SaaliPlaan.vue";

export default {
  components: { SaaliPlaan },
  data() {
    return {
      occupiedSeats: [],
      istekohtadeArv: 1,
      selectedSeats: [],
      film: '',
      genre: '',
      kuupäev: '',
      kell: '',
      saal: '',

    };
  },
  methods: {
    fetchOccupiedSeats(seanssId) {
      fetch(`http://localhost:3000/hoivatud_kohad/${seanssId}`)
          .then(response => response.json()) // Make sure you're parsing the JSON response
          .then(data => {
            console.log('Occupied Seats:', data); // Log to check the received data
            this.occupiedSeats = data;
            this.$nextTick(() => {
              this.$refs.SaaliPlaan.selectSeats();
            });
          })
          .catch(error => {
            console.error('Error fetching occupied seats:', error);
          });
    },

    fetchSeanss(seanssId) {
      fetch(`http://localhost:3000/seanss/${seanssId}`)
          .then(response => response.json()) // Make sure you're parsing the JSON response
          .then(seanss => {
            console.log(seanss)
            this.film = seanss.film.film;
            this.genre = seanss.film.genre;
            this.kuupäev = seanss.kuupäev;
            this.kell = seanss.kell;
            this.saal = seanss.saal;
          })
          .catch(error => {
            console.error('Error fetching seanss:', error);
          });
    },

    suurenda() {
      if (this.istekohtadeArv < 50-this.occupiedSeats.length)
      this.istekohtadeArv++;
    },
    vahenda() {
      if (this.istekohtadeArv > 1) {
        this.istekohtadeArv--;
      }
    },
    osta() {
      const url = `http://localhost:3000/piletid/${this.$route.params.id}`;
      const data = {
        selectedSeats: toRaw(this.selectedSeats), // list
      };

      fetch(url, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
      })
          .then(response => {
            if (!response.ok) {
              throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
          })
          .then(data => {
            console.log('Response:', data);

            const genreUrl = `http://localhost:3000/genres/${this.genre}`;
            return fetch(genreUrl, {
              method: 'PUT',
              headers: {
                'Content-Type': 'application/json'
              },
              body: JSON.stringify({}) // send an empty object or any data required by the server
            });
          })
          .then(response => {
            if (!response.ok) {
              throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
          })
          .then(data => {
            console.log('Response:', data);
            this.$router.push('/');
          })
          .catch((error) => {
            console.error('Error:', error);
          });
    }
  },
  created() {
    const seanssId = this.$route.params.id;
    this.fetchOccupiedSeats(seanssId);
    this.fetchSeanss(seanssId);

  },

};
</script>
<style>
.seat-selector {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
}
.plus-button, .minus-button {
  width: 30px;
  height: 30px;
  font-size: 1.5rem;
  border-radius: 50%;
  border: none;
  background-color: #444944;;
  color: white;
  cursor: pointer;
}
.plus-button:hover, .minus-button:hover, .osta-nupp:hover {
  background-color: #2f6c17;
}
.plus-button{
  margin-right: 50px;
}
.kohtadeValik {

  text-align: center;
  font-size: x-large;

}
.osta-nupp{
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100px;
  height: 30px;
  font-size: 1.1rem;
  border-radius: 5%;
  border: none;
  background-color: #444944;
  cursor: pointer;
  color: whitesmoke;
}
h2{
  text-align: center;
}

.film-info {
  text-align: center;
  margin-bottom: 20px;
}

.film-info p {
  text-align: left;
  font-size: medium;
  margin: 0;
  padding: 5px;
}
</style>
