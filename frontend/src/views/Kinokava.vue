<template>
  <h2>Seansid</h2>
  <br>
  <div class="filter-bar">
    <select v-model="filters.kuupäev">
      <option value="">Kõik kuupäevad</option>
      <option v-for="date in uniqueDates" :key="date" :value="date">{{ date }}</option>
    </select>

    <select v-model="filters.genre">
      <option value="">Kõik žanrid</option>
      <option v-for="genre in uniqueGenres" :key="genre" :value="genre">{{ genre }}</option>
    </select>

    <select v-model="filters.age">
      <option value="">Kõik vanusepiirangud</option>
      <option v-for="age in uniqueAges" :key="age" :value="age">{{ age }}</option>
    </select>
  </div>
  <br>
  <div class="seansid-container">
    <div v-for="seansselem in filteredSeansid" class="seansid">
      <Seanss :seanss="seansselem"></Seanss>
    </div>
  </div>
</template>

<script>
import Seanss from '../components/Seanss.vue';

export default {
  components: {
    Seanss
  },
  data() {
    return {
      sortedSeansid: [],
      uniqueDates: [],
      uniqueGenres: [],
      uniqueAges: [],
      filters: {
        kuupäev: '',
        genre: '',
        age: ''
      },
    };
  },
  computed: {
    filteredSeansid() {
      return this.sortedSeansid.filter(seans => {
        const matchesDate = !this.filters.kuupäev || seans.kuupäev === this.filters.kuupäev;
        const matchesGenre = !this.filters.genre || seans.film.genre === this.filters.genre;
        const matchesAge = !this.filters.age || seans.film.age === this.filters.age;
        return matchesDate && matchesGenre && matchesAge;
      });
    }
  },
  methods: {
    fetchRecords() {
      fetch(`http://localhost:3000/seansid`)
          .then(response => {
            if (!response.ok) {
              throw new Error('Network response was not ok');
            }
            return response.json();
          })
          .then(data => {
            this.sortedSeansid = data;
            this.populateFilters();
          })
          .catch(error => {
            console.error('Fetch error:', error);
          });
    },
    populateFilters() {
      this.uniqueDates = [...new Set(this.sortedSeansid.map(seans => seans.kuupäev))];
      this.uniqueGenres = [...new Set(this.sortedSeansid.map(seans => seans.film.genre))];
      this.uniqueAges = [...new Set(this.sortedSeansid.map(seans => seans.film.age))];
    }
  },
  mounted() {
    this.fetchRecords();
  }
};
</script>


<style>
.filter-bar select {
  margin-right: 10px;
}

.seansid-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

h2 {
  text-align: center;
}

/* Add the following styles */
body {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

h2, .filter-bar {
  flex: 0;
}

.seansid-container {
  flex: 1;
  overflow: auto;
}
</style>
