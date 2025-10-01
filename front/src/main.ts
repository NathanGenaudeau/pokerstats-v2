import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import 'vuetify/styles'
import '@mdi/font/css/materialdesignicons.css'
import { createVuetify } from 'vuetify'
import { VFileUpload, VFileUploadItem } from 'vuetify/labs/components'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import router from './router/router'

const vuetify = createVuetify({
  icons: {
    defaultSet: 'mdi',
  },
  theme: {
    defaultTheme: 'dark'
  },
  components : {
    components,
    VFileUpload,
    VFileUploadItem
  },
  directives
});

const app = createApp(App);
app.use(vuetify);
app.use(router);
app.mount('#app');
