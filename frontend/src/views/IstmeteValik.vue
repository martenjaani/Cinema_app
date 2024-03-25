<template>
  <div class="istmete-valimine" >
  <h2>Vali kohtade arv</h2>
  <br>
  <div class="seat-selector">
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
    }
  },
  created() {
    const seanssId = this.$route.params.id; // Assuming you're passing the session ID as a route param
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
</style>
