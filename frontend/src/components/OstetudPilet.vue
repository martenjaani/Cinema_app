<template>
  <div class="pilet-item">
    <h3>{{ ostetudPilet.film.film }}</h3>
    <p>Kuupäev: {{ ostetudPilet.kuupäev }}</p>
    <p>Kell: {{ ostetudPilet.kell }}</p>
    <p>Saal: {{ ostetudPilet.saal }}</p>
    <div class="seats-container">
    <div v-for="seat in seats" :key="seat" class="seat">
      Koht: {{ seat }}
    </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'OstenudPilet',
  props: {
    ostetudPilet: {
      type: Object,
      required: true
    },
    seats: {
      type: Array,
      default: () => []
    }
  },
  methods: {

  }
}
</script>

<style>
.pilet-item {
  border: 1px solid #ccc;
  padding: 10px;
  border-radius: 5px;
  width: 300px;
  height: 320px;
}

.seats-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
  max-height: 200px;
  overflow-y: auto;

}
.seats-container::-webkit-scrollbar {
  width: 10px;
}

.seats-container::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.seats-container::-webkit-scrollbar-thumb {
  background: #888;
}

.seats-container::-webkit-scrollbar-thumb:hover {
  background: #555;
}

.seat {
  width: 90%;
}
</style>
