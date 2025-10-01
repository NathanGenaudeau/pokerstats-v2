<script setup lang="ts">
</script>

<template>
  <v-app class="app">
    <v-app-bar>
      <template v-slot:prepend>
        <v-btn icon="mdi mdi-home" to="/" />
        <v-btn to="/upload">Upload files</v-btn>
        <v-btn to="/range">Ranges</v-btn>
        <v-btn to="/tournament">Tournaments</v-btn>
        <v-btn to="/player">Players</v-btn>
      </template>
    </v-app-bar>

    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>


<style>
#app {
  width: 100vw;
  max-width: 100vw;
  padding: 0;
  margin: 0;
}
</style>
