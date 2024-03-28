<template>
  <div class="istmete-valimine" >
  <h2>Vali kohtade arv</h2>
  <br>

  <div class="seat-selector">
    <button @click="osta" class="osta-nupp">Osta Piletid</button>

    <div class="kohtadeValik">{{ istekohtadeArv }}</div>
    <button @click="vahenda" class="minus-button">-</button>
    <button @click="suurenda" class="plus-button">+</button>
  </div>


    <br>

    <SaaliPlaan :occupiedSeats="occupiedSeats" :selectedSeats="selectedSeats" :istekohtadeArv="istekohtadeArv" @update:selectedSeats="selectedSeats = $event"/>

  </div>
</template>

<script>
import SaaliPlaan from '@/components/SaaliPlaan.vue';
import {toRaw} from "vue";

export default {
  components: { SaaliPlaan },
  data() {
    return {
      occupiedSeats: [],
      istekohtadeArv: 1,
      selectedSeats: []
    };
  },
  methods: {
    fetchOccupiedSeats(seanssId) {
      fetch(`http://localhost:3000/hoivatud_kohad/${seanssId}`)
          .then(response => response.json()) // Make sure you're parsing the JSON response
          .then(data => {
            console.log('Occupied Seats:', data); // Log to check the received data
            this.occupiedSeats = data;
          })
          .catch(error => {
            console.error('Error fetching occupied seats:', error);
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
  }
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
  background-color: #36791a;
  color: white;
  cursor: pointer;
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
  margin-right: 50px;
  border: none;
  background-color: #36791a;
  cursor: pointer;
}
</style>
