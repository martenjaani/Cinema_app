<template>
  <div class="seanss-item">
    <h3>Film: {{ seanss.film.film }}</h3>
    <p>Kell: {{ seanss.kell }}</p>
    <p>Saal: {{ seanss.saal }}</p>
  </div>
</template>

<script>
export default {
  name: 'Seanss',
  props: {
    seanss: {
      type: Object,
      required: true
    }
  }
}
</script>

<style>

.seanss-item {
  border: 1px solid #ccc;
  padding: 10px;
  border-radius: 5px;
}
</style>
