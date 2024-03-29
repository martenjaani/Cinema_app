<template>
  <header>
    <nav class="navbar">
      <router-link to="/vaatamisajalugu" class="nav-button">Ostetud Piletid</router-link>
      <router-link to="/" class="nav-button">Kinokava</router-link>
      <button @click="resetTickets" class="nav-button">Taasta lehekülg algseks</button>

    </nav>
  </header>
  <br>
  <router-view/>
</template>

<style>
.navbar {
  display: flex;
  justify-content: space-around;
  padding: 20px;
  background-color: #414346;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}



.nav-button:hover {
  background-color: #ced4da;
}
</style>

<script>
export default {
  methods: {
    resetTickets() {
      fetch('http://localhost:3000/reset', {
        method: 'POST',
      })
          .then(response => {
            if (!response.ok) {
              throw new Error('Network response was not ok');
            }

            this.$router.push('/');
          })
          .catch(error => {
            console.error('Error:', error);
          });
    }
  }
}
</script>