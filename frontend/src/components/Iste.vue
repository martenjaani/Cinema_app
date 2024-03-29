<template>
  <div
      class="seat"
      :class="{ 'occupied': isOccupied, 'selected': selected }"
  >
    {{ seatNumber }}
  </div>
</template>

<script>
export default {
  props: {
    seatNumber: Number,
    isOccupied: Boolean,
    selected: Boolean,
  }
};
</script>

<style>
.seat {
  margin: 5px;
  padding: 10px;
  border: 1px solid #ddd;
}

.occupied {
  background-color: #8f4545;
}

.selected {
  background-color: #257725;
}
</style>
