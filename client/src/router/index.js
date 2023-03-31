/** @format */

import { createRouter, createWebHistory } from "vue-router";

const routes = [
	{
		name: "login",
		path: "/login",
		component: () => import("../views/login/index.vue"),
	},
	{
		name: "retrieve",
		path: "/retrieve",
		component: () => import("../views/home/revisepwd.vue"),
		props: (route) => ({
			mode: "retrieve",
			account: route.query.account
		}),
	},
	{
		name: "index",
		path: "/index",
		redirect: "/",
	},
	{
		name: "main",
		path: "/",
		component: () => import("../views/main/main.vue"),
		children: [
			{
				name: "home",
				path: "home",
				components: {
					home: () => import("../views/home/home.vue"),
				},
			},
			{
				name: "personal",
				path: "personal",
				components: {
					personal: () => import("../views/home/personal.vue"),
				},
			},
		],
	},
	{
		path: "/:path(.*)",
		name: "404",
		component: () => import("../views/error/error.vue"),
	},
];

export const router = createRouter({
	history: createWebHistory(),
	routes,
});
