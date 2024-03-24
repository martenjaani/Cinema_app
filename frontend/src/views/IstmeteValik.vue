<template>
  <div>
    <h2>Vali oma kohad</h2>
    <SaaliPlaan :occupiedSeats="occupiedSeats" />
  </div>
</template>

<script>
import SaaliPlaan from '@/components/SaaliPlaan.vue';

export default {
  components: { SaaliPlaan },
  data() {
    return {
      occupiedSeats: []
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
    }
  },
  created() {
    const seanssId = this.$route.params.id; // Assuming you're passing the session ID as a route param
    this.fetchOccupiedSeats(seanssId);
  }
};
</script>
