<template>
  <div
      class="seat"
      :class="{ 'occupied': isOccupied, 'selected': isSelected }"
      @click="select"
  >
    {{ seatNumber }}
  </div>
</template>

<script>
export default {
  props: {
    seatNumber: {
      type: Number,
      required: true
    },
    isOccupied: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      isSelected: false
    };
  },
  methods: {
    select() {
      if (!this.isOccupied) {
        this.isSelected = !this.isSelected;
        this.$emit('click');
      }
    }
  }
};
</script>

<style>
.seat {
  margin: 5px;
  padding: 10px;
  border: 1px solid #ddd;
  cursor: pointer;
}

.occupied {
  background-color: #8f4545;
}

.selected {
  background-color: #257725;
}
</style>
