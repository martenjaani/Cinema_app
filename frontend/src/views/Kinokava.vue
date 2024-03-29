<template>
  <h1>Kinokava</h1>
  <br>

  <button :class="{ 'soovitus-active': vaatamisSoovitusNupp}" @click="fetchGenresAndSort">
    {{ vaatamisSoovitusNupp ? 'Ära soovita vaatamisajaloo põhjal' : 'Soovita vaatamisajaloo põhjal' }}
  </button><br><br>
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
      <Seanss :seanss="seansselem" @select-seanss="goToSeatSelection"></Seanss>
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
      vaatamisSoovitusNupp: false
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
    fetchGenresAndSort() {
      if(this.vaatamisSoovitusNupp){
        this.fetchRecords();
      } else {
        fetch('http://localhost:3000/genres')
            .then(response => response.json())
            .then(genres => {
              // Sort the genres by count in descending order
              const sortedGenres = genres.sort((a, b) => b.count - a.count);
              // Get the two genres with the highest counts
              const topGenres = sortedGenres.slice(0, 2).map(genre => genre.genre);
              // Filter the sortedSeansid array to only include movies that belong to the top genres
              this.sortedSeansid = this.sortedSeansid.filter(seans => topGenres.includes(seans.film.genre));
            })
            .catch(error => console.error('Error:', error));
      }
      this.vaatamisSoovitusNupp = !this.vaatamisSoovitusNupp;

    },
    populateFilters() {
      this.uniqueDates = [...new Set(this.sortedSeansid.map(seans => seans.kuupäev))];
      this.uniqueGenres = [...new Set(this.sortedSeansid.map(seans => seans.film.genre))];
      this.uniqueAges = [...new Set(this.sortedSeansid.map(seans => seans.film.age))];
    },
    goToSeatSelection(seanss) {
      this.$router.push({
        name: 'IstmeteValik',
        params: { id: seanss.id }
      });
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

h1 {
  text-align: center;
}

/* Add the following styles */
body {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

h1, .filter-bar {
  flex: 0;
}

.seansid-container {
  flex: 1;
  overflow: auto;
}

.vaatamisajalugu-button {
  display: inline-block;
  padding: 10px 20px;
  margin: 10px 0;
  background-color: #36791a;
  color: white;
  text-decoration: none;
  border-radius: 5px;
}

.soovitus-active {
  //background-color: #75af75; /* or any shade of green you prefer */
}
</style>
