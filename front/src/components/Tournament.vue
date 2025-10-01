<script setup lang="ts">
import { onMounted, ref, computed } from 'vue';

import { getTournaments, getTournamentsBuyin } from '../routes/tournaments';
import { getPlayers } from '../routes/players';
import dayjs from 'dayjs';

const tournaments = ref<any[]>([]);
const players = ref<any[]>([]);
const buyin = ref<any[]>([]);

onMounted(async () => {
  players.value = await getPlayers();
  tournaments.value = await getTournaments();
  buyin.value = await getTournamentsBuyin();
});

const headers = computed(() => {
  const playerHeaders = players.value.map(player => ({
    title: player.pseudo,
    value: player.pseudo,
  }));

  return [
    { key: 'name', title: 'Name', value: 'name' },
    { key: 'buy_in', title: 'Buy-in', value: 'buy_in' },
    { key: 'speed', title: 'Speed', value: 'speed' },
    { key: 'date', title: 'Date', value: 'date' },
    { title: 'Result', children: playerHeaders },
  ];
});

const search = ref<string>('');
const value = ref<number[]>([0, 100]);
</script>

<template>
  <v-container>
    <v-card class="pt-8">
      <v-range-slider
        v-model="value"
        thumb-label="always"
      ></v-range-slider>
      <v-text-field
        v-model="search"
        label="Search"
        prepend-inner-icon="mdi-magnify"
        variant="outlined"
        hide-details
        single-line
      ></v-text-field>
      <v-data-table
        :items="tournaments"
        :headers="headers"
        :search="search"
      >
        <template v-slot:item.date="{ item }">
          {{ dayjs(item.date).format('HH:mm DD/MM/YYYY') }}
        </template>
        <template v-slot:item.Nakline="{ item }">
          {{ item.results.filter((result: any) => result.pseudo === 'Nakline')[0]?.place }}
        </template>
        <template v-slot:item.RideUnMax="{ item }">
          {{ item.results.filter((result: any) => result.pseudo === 'RideUnMax')[0]?.place }}
        </template>
      </v-data-table>
    </v-card>


  </v-container>
</template>

<style scoped>
</style>