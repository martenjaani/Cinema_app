<template>
  <div class="cinema-plan">
    <div v-for="rowIndex in rows" :key="rowIndex" class="seat-row">
      <Iste
          v-for="seatIndex in seatsPerRow"
          :key="50 - (rowIndex * seatsPerRow) + seatIndex"
          :seatNumber="50 - (rowIndex * seatsPerRow) + seatIndex"
          :isOccupied="occupiedSeats.includes(50 - (rowIndex * seatsPerRow) + seatIndex)"
          :selected="selectedSeats.includes(50 - (rowIndex * seatsPerRow) + seatIndex)"
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
      occupiedSeats: Array,
      istekohtadeArv: Number,

  },
  data() {
    return {
      rows: 5, // 5 rows of seats
      seatsPerRow: 10, // 10 seats per row
      selectedSeats: []
    };
  },
  methods: {
    isSeatAvailable(seatNumber) {
      return !(this.occupiedSeats.includes(seatNumber) || this.selectedSeats.includes(seatNumber));
    },
    calculateSeatDistance(seatNumber) {
      const centerSeat = 25;
      const row = Math.ceil(seatNumber / this.seatsPerRow);
      const col = ((seatNumber - 1) % this.seatsPerRow);
      const centerRow = Math.ceil(centerSeat / this.seatsPerRow);
      const centerCol = ((centerSeat - 1) % this.seatsPerRow);

      // Manhattan distance
      return Math.abs(centerRow - row) + Math.abs(centerCol - col);
    },
    findBestSeats(requiredSeats) {
      let bestSeatSequence = [];
      let lowestTotalDistance = Number.MAX_VALUE;

      for (let row = 1; row <= this.rows; row++) {
        for (let startCol = 1; startCol <= this.seatsPerRow; startCol++) {
          let currentSequence = [];
          let currentTotalDistance = 0;

          for (let i = 0; i < requiredSeats; i++) {
            if (startCol + i > this.seatsPerRow) break; //stay inbounds

            const seatNumber = (row - 1) * this.seatsPerRow + startCol + i;
            if (!this.isSeatAvailable(seatNumber)) break;

            currentSequence.push(seatNumber);
            currentTotalDistance += this.calculateSeatDistance(seatNumber);
          }

          if (currentSequence.length === requiredSeats && currentTotalDistance < lowestTotalDistance) {
            bestSeatSequence = currentSequence;
            lowestTotalDistance = currentTotalDistance;
          }

        }
      }

      return bestSeatSequence;
    },
    selectSeats() {
      let requiredSeats = this.istekohtadeArv;
      while (requiredSeats > 0) {
        console.log('Selecting', requiredSeats, 'seats');
        let bestSeats = this.findBestSeats(requiredSeats);
       //kui ei leia kohti kõrvuti olevaid kohti, siis proovima leida väiksema sequence
        let tempRequiredSeats = requiredSeats;
        while (bestSeats.length === 0 && tempRequiredSeats > 0) {
          tempRequiredSeats -= 1; // Reduce the seat requirement
          bestSeats = this.findBestSeats(tempRequiredSeats); // Try again with fewer seats
        }

        console.log('Best seats:', bestSeats);

        if (bestSeats.length === 0) {
          console.log("No available seats found. Stopping.");
          break; 
        }

        this.selectedSeats.push(...bestSeats); // Add found seats to the selected list
        this.$emit('update:selectedSeats', this.selectedSeats);
        requiredSeats -= bestSeats.length; // Reduce the remaining seat requirement
      }
    }
  },
  watch: {
    istekohtadeArv() {
      this.selectedSeats = [];
      this.selectSeats();
    }
 }
}
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
