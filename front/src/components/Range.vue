<script lang="ts" setup>
import { ref, computed } from 'vue';

const predefinedRanges = [
  {
    label: 'Open UTG',
    hands: ['AA', 'KK', 'QQ', 'AKs', 'AQs', 'JJ', 'TT', 'AKo'],
  },
  {
    label: '3Bet Value',
    hands: ['AA', 'KK', 'QQ', 'JJ', 'AKs', 'AKo'],
  },
  {
    label: 'Flat Call',
    hands: ['22', '33', '44', '55', 'AJs', 'KQs', 'QJs', 'JTs', 'T9s', '98s'],
  },
];

const selectedRange = ref<string | null>(null);

const applyRange = (rangeLabel: string | null): void => {
  if (!rangeLabel) return;
  const range = predefinedRanges.find((r) => r.label === rangeLabel);
  if (range) {
    selected.value = new Set(range.hands);
  }
};


type PokerHand = string;

const ranks: string[] = ['A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2'];

const grid = ref<PokerHand[][]>(
  ranks.map((rowRank, rowIndex) =>
    ranks.map((colRank, colIndex) => {
      if (rowIndex === colIndex) return rowRank + rowRank;
      if (rowIndex < colIndex) return rowRank + colRank + 's';
      return colRank + rowRank + 'o';
    }),
  ),
);

const selected = ref<Set<PokerHand>>(new Set());
const search = ref<string>('');

const totalCombos = 169;

const selectedCount = computed(() => selected.value.size);
const selectedPercent = computed(() => ((selectedCount.value / totalCombos) * 100).toFixed(1));

const toggleSelection = (hand: PokerHand): void => {
  if (selected.value.has(hand)) {
    selected.value.delete(hand);
  } else {
    selected.value.add(hand);
  }
};

const selectAll = (): void => {
  selected.value = new Set(grid.value.flat());
};

const clearAll = (): void => {
  selected.value.clear();
};

const isSelected = (hand: PokerHand): boolean => selected.value.has(hand);
const isSearched = (hand: PokerHand): boolean =>
  search.value.trim().toUpperCase() === hand.toUpperCase();

const getCombos = (hand: PokerHand): number => {
  if (/^[AKQJT98765432]{2}$/.test(hand)) return 6; // Paire
  if (hand.endsWith('s')) return 4;
  if (hand.endsWith('o')) return 12;
  return 0;
};

const exportSelected = (format: 'txt' | 'csv' | 'json'): void => {
  const hands = Array.from(selected.value).sort();

  let content = '';
  let mime = 'text/plain';
  const filename = `mains-selectionnees.${format}`;

  if (format === 'json') {
    const data = hands.map((h) => ({ hand: h, combos: getCombos(h) }));
    content = JSON.stringify(data, null, 2);
    mime = 'application/json';
  } else {
    const formatted = hands.map((h) => `${h} (${getCombos(h)} combos)`);
    content = formatted.join(format === 'csv' ? ',' : ', ');
    mime = format === 'csv' ? 'text/csv' : 'text/plain';
  }

  const blob = new Blob([content], { type: mime });
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url;
  a.download = filename;
  a.click();
  URL.revokeObjectURL(url);
};

const importSelected = async (e: Event): Promise<void> => {
  const input = e.target as HTMLInputElement;
  const file = input?.files?.[0];
  if (!file) return;

  const text = await file.text();
  let hands: string[] = [];

  if (file.name.endsWith('.json')) {
    try {
      const json = JSON.parse(text);
      hands = Array.isArray(json)
        ? json.map((h: any) => (typeof h === 'string' ? h : h.hand))
        : [];
    } catch {
      alert('Fichier JSON invalide');
      return;
    }
  } else {
    hands = text.split(/[,\s]+/).map((h) => h.trim().replace(/\s*\(.*?\)\s*/, ''));
  }

  const validHands = hands.filter((h) =>
    /^[AKQJT98765432]{1}[AKQJT98765432]{1}[os]?$/.test(h),
  );
  selected.value = new Set(validHands);
};
</script>

<template>
  <v-container class="pa-0 poker-grid">
    <v-text-field
      v-model="search"
      label="Rechercher une main (ex: AA, AKs, AJo...)"
      dense
      clearable
      hide-details
      class="mb-3"
    />

    <v-row v-for="(row, i) in grid" :key="i" dense no-gutters>
      <v-col
        v-for="(cell, j) in row"
        :key="j"
        cols="auto"
        class="cell"
        :class="{
          selected: isSelected(cell),
          searched: isSearched(cell),
        }"
        @click="toggleSelection(cell)"
      >
        {{ cell }}
      </v-col>
    </v-row>

    <v-row class="mt-4" dense>
      <v-col cols="auto">
        <v-btn color="green darken-1" @click="selectAll">Tout sélectionner</v-btn>
      </v-col>
      <v-col cols="auto">
        <v-btn color="red darken-1" @click="clearAll">Tout désélectionner</v-btn>
      </v-col>
      <v-col cols="auto" class="d-flex align-center">
        <span class="text-caption">
          {{ selectedCount }} / 169 mains sélectionnées ({{ selectedPercent }} %)
        </span>
      </v-col>
    </v-row>

    <v-row class="mb-3">
      <v-col cols="12" sm="6" md="4">
        <v-select
          v-model="selectedRange"
          :items="predefinedRanges.map(r => r.label)"
          label="Présets de ranges"
          clearable
          dense
          @update:modelValue="applyRange"
        />
      </v-col>
    </v-row>


    <v-row class="mt-3" dense>
      <v-col cols="auto">
        <v-btn color="primary" @click="exportSelected('txt')">Exporter .txt</v-btn>
      </v-col>
      <v-col cols="auto">
        <v-btn color="success" @click="exportSelected('csv')">Exporter .csv</v-btn>
      </v-col>
      <v-col cols="auto">
        <v-btn color="info" @click="exportSelected('json')">Exporter .json</v-btn>
      </v-col>
      <v-col cols="auto">
        <v-file-input
          label="Importer un fichier"
          accept=".txt,.csv,.json"
          dense
          hide-details
          @change="importSelected"
        />
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.poker-grid {
  max-width: 700px;
}

.cell {
  border: 1px solid #ccc;
  width: 40px;
  height: 40px;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.2s;
  user-select: none;
  background-color: gray;
}

.cell.selected {
  background-color: #d0ebff;
}

.cell.searched {
  background-color: #ffe066;
}
</style>