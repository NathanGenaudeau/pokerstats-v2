<script setup lang="ts">
import { ref } from 'vue';
import { uploadFile } from '../routes/upload';

const files = ref<File[]>([]);

const upload = async () => {
  const summaryFiles = files.value.filter((file: any) => file.name.includes('summary'));
  const otherFiles = files.value.filter((file: any) => !file.name.includes('summary'));

  const uploadSequentially = async (fileList: any[]) => {
    for (const file of fileList) {
      try {
        console.log(`Uploading: ${file.name}`);
        await uploadFile(file);
        console.log(`Done: ${file.name}`);
      } catch (err) {
        console.error(`Error uploading ${file.name}`, err);
      }
    }
  };

  await uploadSequentially(summaryFiles);
  await uploadSequentially(otherFiles);
};
</script>

<template>
  <div>
    <v-file-upload density="compact" title="Drag and drop txt history files" v-model="files" multiple clearable show-size></v-file-upload>
    <v-btn color="green" @click="upload">Import</v-btn>
  </div>
</template>

<style scoped>
</style>
