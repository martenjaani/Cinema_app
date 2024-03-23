<template>
  <h3>Seansid</h3>
    <div v-for="seansselem in seansid" :key="seansselem.id" class="seanss">
      <Seanss :seanss="seansselem"></Seanss>
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
            this.seansid = data.seansid;
          })
          .catch(error => {
            console.error('Fetch error:', error);
          });
    }

  },
    mounted() {
      this.fetchRecords();
      console.log("mounted");
    }
  };
</script>
