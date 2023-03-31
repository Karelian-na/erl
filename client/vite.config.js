/** @format */

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

// https://vitejs.dev/config/
export default defineConfig({
	plugins: [vue()],
	server: {
		host: "erl.wust.edu.cn",
	},
	base: "/",
	resolve: {
		alias: [
			{
				find: "common",
				replacement: "/src/common/",
			},
			{
				find: "components",
				replacement: "/src/components/",
			},
			{
				find: "views/common",
				replacement: "/src/views/common/",
			},
			{
				find: "assets",
				replacement: "/src/assets/",
			},
			{
				find: "element-plus",
				replacement: "/node_modules/element-plus",
			},
			{
				find: "field-configs",
				replacement: "/src/views/FieldsConfigs",
			}
		],
	},
});
