import { createApp } from "vue";
import ElementPlus from "element-plus";
import 'element-plus/dist/index.css';
import Index from "./views/login/index.vue";

const app = createApp(Index);

app.use(ElementPlus);
app.mount("#app");