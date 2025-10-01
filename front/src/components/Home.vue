<script setup lang="ts">
import { onMounted, ref } from 'vue';
import PokerHands from '../assets/hands.json';
import { getHands } from '../routes/hands';
import { getTournaments } from '../routes/tournaments';
import { getPlayers } from '../routes/players';
import dayjs from 'dayjs';

import type { Player } from '../types/Player';
import type { Tournament } from '../types/Tournament';

const pokerHands = ref<Record<string, { count: number; nbPossibility: number; win: number, rangePercentage: number }>>(PokerHands as any);

const allHands = ref<any>([]);
const hands = ref<any[]>([]);

const players = ref<Player[]>([]);
const tournaments = ref<Tournament[]>([]);
const positions = ref<string[]>(['BB', 'SB', 'BTN', 'CO', 'HJ', 'UTG+3', 'UTG+2', 'UTG+1', 'UTG']);
const selectedPlayer = ref<string | null>(null);
const selectedTournament = ref<string | null>(null);
const selectedPosition = ref<string | null>(null);

const pokerHandsCard = (hands: any) => {
  Object.keys(pokerHands.value).forEach((key) => {
    pokerHands.value[key].count = 0;
    pokerHands.value[key].win = 0;
  });

  hands.forEach((hand: any) => {
    const [highCard, lowCard] = [hand.cards[0], hand.cards[1]];
    if (highCard[0] === lowCard[0]) {
      pokerHands.value[highCard[0]+lowCard[0]].count += 1;
      pokerHands.value[highCard[0]+lowCard[0]].win += hand.win ? 1 : 0;
    } else if (highCard[1] === lowCard[1]) {
      pokerHands.value[highCard[0] + lowCard[0] + 's'].count += 1;
      pokerHands.value[highCard[0] + lowCard[0] + 's'].win += hand.win ? 1 : 0;
    } else if (highCard[1] !== lowCard[1]){
      pokerHands.value[highCard[0] + lowCard[0] + 'o'].count += 1;
      pokerHands.value[highCard[0] + lowCard[0] + 'o'].win += hand.win ? 1 : 0;
    }
  });
}

onMounted(async () => {
  //handsValue();
  players.value = await getPlayers();
  tournaments.value = await getTournaments();
  allHands.value = await getHands();
  hands.value = allHands.value;
  pokerHandsCard(hands.value);
  luckCalculator();
});

const ranks = ref<string[]>(['A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2']);
type PokerHand = string;

const grid = ref<PokerHand[][]>(
  ranks.value.map((rowRank, rowIndex) =>
    ranks.value.map((colRank, colIndex) => {
      if (rowIndex === colIndex) return rowRank + rowRank;
      if (rowIndex < colIndex) return rowRank + colRank + 's';
      return colRank + rowRank + 'o';
    }),
  ),
);

const filterHandsByTournament = (idTournament: string) => {
  hands.value = allHands.value.filter((hand: any) => hand.id_tournament == idTournament);
  pokerHandsCard(hands.value);
  luckCalculator();
};

const filterHandsByPosition = (position: string) => {
  hands.value = allHands.value.filter((hand: any) => hand.position === position);
  pokerHandsCard(hands.value);
  luckCalculator();
};

const filterHandsByPlayer = (pseudo: string) => {
  hands.value = allHands.value.filter((hand: any) => hand.pseudo === pseudo);
  pokerHandsCard(hands.value);
  luckCalculator();
}

const itemPropsTournament = (item: Tournament) => {
  return {
    title: item.name,
    subtitle: item.buy_in + '€' + ' - ' + dayjs(item.date).format('DD/MM/YY, HH:mm'),
    value: item._id,
  };
};

const itemPropsPlayer = (item: Player) => {
  return {
    title: item.pseudo,
    value: item.pseudo,
  };
};

const tab = ref<string>('three');

const luckResult = ref<number>(0);
const luckResultBase = ref<number>(0);

const luckCalculator = () => {
  luckResult.value = Object.keys(pokerHands.value).reduce((acc, key) =>  acc + pokerHands.value[key].count * pokerHands.value[key].rangePercentage, 0) / hands.value.length;
  luckResultBase.value = Object.keys(pokerHands.value).reduce((acc, key) =>  acc + pokerHands.value[key].nbPossibility * pokerHands.value[key].rangePercentage, 0) / 1326;
};
</script>

<template>
  <v-select
    label="Select player"
    :items="players"
    :item-props="itemPropsPlayer"
    v-model="selectedPlayer"
    @update:model-value="filterHandsByPlayer"
  />
  <v-select
    label="Select position"
    :items="positions"
    v-model="selectedPosition"
    @update:model-value="filterHandsByPosition"
  />
  <v-select
    label="Select player"
    :items="players"
    :item-props="itemPropsPlayer"
    v-model="selectedPlayer"
    @update:model-value="filterHandsByPlayer"
  />

  <v-autocomplete
    label="Select tournament"
    :items="tournaments"
    :item-props="itemPropsTournament"
    v-model="selectedTournament"
    @update:model-value="filterHandsByTournament"
    clearable
  >
  </v-autocomplete>
  <div>Nombre de mains jouées dans le tournoi : {{ hands.length }}</div>
  <div>Chance : {{ luckResult }}</div>
  <div>Chance : {{ luckResultBase }}</div>
  <!--<div v-if="hands" v-for="(obj, hand) in pokerHands" :key="hand">
    <p>{{ hand }}: <v-chip :color="obj.count > (obj.nbPossibility / 1326 * hands.length) ? 'green' : 'red'">{{ obj.count }}</v-chip>
      ({{ (obj.nbPossibility / 1326 * hands.length).toFixed(2) }})
    </p>
  </div>-->
  <v-card>
    <v-tabs
      v-model="tab"
      bg-color="primary"
    >
      <v-tab value="three">total compte par main</v-tab>
      <v-tab value="two">% gagné selon main</v-tab>
      <v-tab value="one">Chance sur les tournois</v-tab>
    </v-tabs>

    <v-card-text>
      <v-tabs-window v-model="tab">
        <v-tabs-window-item value="three">
          <v-row
            v-for="(row, i) in grid"
            :key="i"
            dense
            no-gutters
          >
            <v-col
              v-for="(cell, j) in row"
              :key="j"
              cols="auto"
              class="range-table align-center justify-center font-weight-medium"
              :class="{
                'pair': cell[0] === cell[1],
                'bg-green': pokerHands[cell].count > (pokerHands[cell].nbPossibility / 1326 * hands.length),
                'bg-red': pokerHands[cell].count <= (pokerHands[cell].nbPossibility / 1326 * hands.length),
              }"
            >
              <div>
                {{ cell }}
              </div>
                {{ pokerHands[cell].count || 0 }}
            </v-col>
          </v-row>
        </v-tabs-window-item>
        <v-tabs-window-item value="two">
          <v-row
            v-for="(row, i) in grid"
            :key="i"
            dense
            no-gutters
            class="mb-4"
          >
            <v-col
              v-for="(cell, j) in row"
              :key="j"
              cols="auto"
              class="range-table align-center justify-center font-weight-medium"
              :class="{
                'pair': cell[0] === cell[1],
                'bg-green': pokerHands[cell].win / pokerHands[cell].count >= 0.66,
                'bg-orange': pokerHands[cell].win / pokerHands[cell].count >= 0.33 && pokerHands[cell].win / pokerHands[cell].count < 0.66,
                'bg-red': pokerHands[cell].win / pokerHands[cell].count < 0.33,
              }"
            >
              <div>
                {{ cell }}
              </div>
                {{ Math.round(pokerHands[cell].win / pokerHands[cell].count * 100) }} ({{ pokerHands[cell].count }})
            </v-col>
          </v-row>
        </v-tabs-window-item>

        <v-tabs-window-item value="one">
          <v-row
            v-for="(row, i) in grid"
            :key="i"
            dense
            no-gutters
          >
            <v-col
              v-for="(cell, j) in row"
              :key="j"
              cols="auto"
              class="range-table align-center justify-center font-weight-medium"
              :class="{
                'bg-green': pokerHands[cell].handValue > 20,
                'bg-orange': pokerHands[cell].handValue > 10 && pokerHands[cell].handValue <= 20,
                'bg-red': pokerHands[cell].handValue <= 10,
              }"
            >
              <div>
                {{ cell }}
              </div>
                {{ pokerHands[cell].handValue || 0 }}
            </v-col>
          </v-row>
        </v-tabs-window-item>
      </v-tabs-window>
    </v-card-text>
  </v-card>
</template>

<style scoped>
.range-table {
  border: 1px solid #ccc;
  width: 50px;
  height: 50px;
  font-size: 12px;
  background-color: grey;
}
.range-table.bg-green {
  background-color: #4caf50 !important;
}
.range-table.bg-orange {
  background-color: #ff9800 !important;
}
.range-table.bg-red {
  background-color: #f44336 !important;
}
.range-table.pair {
  border: 3px solid #000;
}
</style>
