<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';

import type { Player } from '../types/Player';

import { getPlayers } from '../routes/players';
import { getHands } from '../routes/hands';
import { getTournaments } from '../routes/tournaments';
import { getResults } from '../routes/results';

const players = ref<Player[]>([]);
const hands = ref<any[]>([]);
const tournaments = ref<any[]>([]);
const results = ref<any[]>([]);

const selectedPlayer = ref<string | null>(null);
const itemPropsPlayer = (item: Player) => {
  return {
    title: item.pseudo,
    value: item.pseudo,
  };
};
const filterHandsByPlayer = (player: string) => {
  selectedPlayer.value = player;
};

watch(selectedPlayer, async (newValue) => {
  if (newValue) {
    hands.value = await getHands(newValue);
    tournaments.value = await getTournaments(newValue);
    results.value = await getResults(newValue);
    console.log('Results:', results.value.length);
    displayStats();
  }
});

onMounted(async () => {
  players.value = await getPlayers();
});

const displayStats = () => {
  value.value = results.value.map((result: any) => {
    return result.place;
  });
}

const value = ref<number[]>([]);
</script>

<template>
  <v-container>
    <v-row>
        <v-select
          label="Select player"
          :items="players"
          :item-props="itemPropsPlayer"
          v-model="selectedPlayer"
          @update:model-value="filterHandsByPlayer"
        />
    </v-row>
    <v-row>
      <v-sparkline
        :model-value="value"
        line-width="1"
        color="rgba(255, 255, 255, .7)"
        height="100"
        padding="24"
        stroke-linecap="round"
        smooth
      >
        <template v-slot:label="item">
          {{ item.value }}
        </template>
      </v-sparkline>
    </v-row>
    <v-row>
      
    </v-row>
  </v-container>
</template>

<style scoped>
</style>