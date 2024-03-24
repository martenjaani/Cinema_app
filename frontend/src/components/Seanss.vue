<template>
  <div class="seanss-item">
    <h3>{{ seanss.film.film }}</h3>
    <p>Kuupäev: {{ seanss.kuupäev }}</p>
    <p>Kell: {{ seanss.kell }}</p>
    <p>Saal: {{ seanss.saal }}</p>
    <p>Vabu kohti: {{ seanss.vabu_kohti }}</p>
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
