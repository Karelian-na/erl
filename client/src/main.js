/** @format */

import { createApp } from "vue";
import ElementPlus from "element-plus";
import axios from "axios";
import "element-plus/dist/index.css";
import App from "./App.vue";

const app = createApp(App);

axios.defaults.baseURL = "http://erl.wust.edu.cn:8181/";
axios.defaults.withCredentials = true;

import { router } from "./router/index.js";

app.use(router);

app.mount("#app");
