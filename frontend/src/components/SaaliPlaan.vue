<template>
  <div class="cinema-plan">
    <div v-for="rowIndex in rows" :key="rowIndex" class="seat-row">
      <Iste
          v-for="seatIndex in seatsPerRow"
          :key="`row-${rowIndex}-seat-${seatIndex}`"
          :seatNumber="50 - (rowIndex * seatsPerRow) + seatIndex"
          :isOccupied="occupiedSeats.includes(50 - (rowIndex * seatsPerRow) + seatIndex)"
          @click="selectSeat(((rowIndex - 1) * seatsPerRow) + seatIndex)"
      />
    </div>
  </div>
</template>

<script>
import Iste from './Iste.vue';

export default {
  components: {
    Iste
  },
  props: {
    occupiedSeats: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      rows: 5, // 5 rows of seats
      seatsPerRow: 10, // 10 seats per row
      selectedSeats: []

    };
  },
  methods: {
    selectSeat(seatNumber) {
      if (!this.occupiedSeats.includes(seatNumber)) {
        if (this.selectedSeats.includes(seatNumber)) {
          this.selectedSeats = this.selectedSeats.filter(num => num !== seatNumber);
        } else {
          this.selectedSeats.push(seatNumber);
        }
      }
    }
  }
};
</script>

<style>
.cinema-plan {
  display: flex;
  flex-direction: column;
}
.seat-row {
  display: flex;
  justify-content: center;
}
</style>
