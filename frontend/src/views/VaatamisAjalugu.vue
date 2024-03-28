<template>
  <div>
    <h1>Piletid</h1>
    <br>
  </div>
  <div class="piletid-konteiner">
    <div v-for="pilet in vaatamisajalugu" class="piletid-item">
      <OstetudPilet :ostetudPilet="findSeanssById(pilet.seanssId)" :seats="pilet.selectedSeats"/>      </div>

  </div>
</template>


<script>
import OstetudPilet from '../components/OstetudPilet.vue';

export default {
  components: {
    OstetudPilet
  },
  data() {
    return {
      vaatamisajalugu: [],
      seansid: []
    };
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
            this.seansid = data;
          })
          .catch(error => {
            console.error('Fetch error:', error);
          });
},

    fetchVaatamisajalugu() {
      fetch(`http://localhost:3000/ajalugu`)
          .then(response => {
            if (!response.ok) {
              throw new Error('Network response was not ok');
            }
            return response.json();
          })
          .then(data => {
            console.log('Vaatamisajalugu:', data);
            this.vaatamisajalugu = data;
          })
          .catch(error => {
            console.error('Error fetching vaatamisajalugu:', error);
          });
    },
    findSeanssById(seanssId) {
      return this.seansid.find(seans => seans.id === seanssId);
    }
  },
  mounted() {
    this.fetchVaatamisajalugu();
    this.fetchRecords();
  }
};


</script>


<style scoped>
.piletid-item{
  padding: 10px;

}
</style>