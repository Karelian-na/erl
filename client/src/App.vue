<!-- @format -->

<script setup lang="ts">
	import type { UserInfo } from "views/common";
	import type { DailyPoemResult } from "common/Result";

	import Store from "store";
	import { zhCn } from "element-plus/es/locale";
	import { axiosRequest, error } from "common/utils";
	import { RouteRecordRaw, useRouter } from "vue-router";
	import { IMenuItem, MenuType } from "./views/permissions";
	import { ElConfigProvider, vLoading } from "element-plus";
	import { onBeforeMount, provide, reactive, ref } from "vue";

	let rawItems: IMenuItem[] = [];
	const router = useRouter();
	const mainLoading = reactive({
		value: false,
		set tip(v: string) {
			const tipElement = document.querySelector(`#main-loading .el-loading-text`);
			if (tipElement) {
				tipElement.textContent = v;
			}
		},
	});
	const userInfo = ref<UserInfo>();
	const dailyPoem = ref<DailyPoemResult>({} as any);
	const ComponentsMapping: Record<string, any> = {
		Common: () => import("./views/common/IndexTemplate.vue"),

		Maters: () => import("./views/maters/index.vue"),

		"Books/index": () => import("./views/books/index.vue"),
		"Books/resindex": () => import("./views/books/resindex.vue"),

		"Laboratories/resindex": () => import("./views/common/ReserveIndex.vue"),

		"Reservations/Book/index": () => import("./views/reservations/book/index.vue"),
		"Reservations/Book/self/index": () => import("./views/reservations/book/index.vue"),
		"Reservations/Laboratory/index": () => import("./views/reservations/ReservationIndex.vue"),
		"Reservations/Laboratory/self/index": () => import("./views/reservations/ReservationIndex.vue"),

		"Internships/index": () => import("./views/internships/index.vue"),
		"Internships/appindex": () => import("./views/internships/appindex.vue"),

		"Declarations/index": () => import("./views/declarations/index.vue"),
		declare: () => import("./views/declarations/declare.vue"),

		"Awards/Teaching/index": () => import("./views/declarations/display.vue"),
		"Awards/Teacher/index": () => import("./views/declarations/display.vue"),
		"Awards/Competition/index": () => import("./views/declarations/display.vue"),
		"Projects/Teaching/index": () => import("./views/declarations/display.vue"),
		"Projects/Research/index": () => import("./views/declarations/display.vue"),
		"Papers/index": () => import("./views/declarations/display.vue"),
		"Papers/Postgraduate/index": () => import("./views/declarations/display.vue"),
		"Patents/index": () => import("./views/declarations/display.vue"),
		"Conferences/index": () => import("./views/declarations/display.vue"),

		"Permissions/index": () => import("./views/permissions/index.vue"),
		"Roles/index": () => import("./views/users/index.vue"),
		"Users/index": () => import("./views/users/index.vue"),
		"Posts/index": () => import("./views/students/index.vue"),
		"Students/index": () => import("./views/students/index.vue"),
		"Databases/index": () => import("./views/databases/index.vue"),
	};

	provide("rawItems", rawItems);
	provide("userInfo", userInfo);
	provide("mainLoading", mainLoading);
	provide("dailyPoem", dailyPoem.value);

	onBeforeMount(async () => {
		const dailyPoemService = (window as any).jinrishici;
		if (dailyPoemService) {
			dailyPoemService.load(function (result: DailyPoemResult) {
				Object.assign(dailyPoem.value, result);
			});
		}
	});

	router.beforeEach((to, _from, next) => {
		const cookieStore = Store.namespace("cookie");
		if (!cookieStore.get("value")) {
			if (to.name != "login" && !["retrieve"].includes(to.name as string)) {
				next("login");
			} else {
				next();
			}
		} else if (userInfo.value) {
			next();
		} else {
			mainLoading.value = true;
			axiosRequest(
				{
					method: "GET",
					url: "/index",
					loading: mainLoading,
				},
				(result) => {
					if (!result.success) {
						mainLoading.value = false;
						error("alert", { content: "菜单获取失败!" });
					} else {
						cookieStore.set("requested", true);
						userInfo.value = result.data.userMsg;

						rawItems.splice(0, rawItems.length, ...result.data.menus);
						rawItems.forEach((item) => {
							if (item.url == "#" || item.type == MenuType.Menu) {
								return;
							}
							let viewName: string;
							let route = {} as RouteRecordRaw;

							route.name = `r${item.id}`;
							if (item.url[0] == "/") {
								route.path = item.url.substring(1);
								let idx = route.path.indexOf("?");
								if (idx != -1) {
									route.path = route.path.substring(0, idx);
								}
							} else {
								route.path = item.url;
							}
							if (route.path.endsWith("/")) {
								route.path = route.path + "index";
								item.url += "index";
							}

							if (item.type == MenuType.Item && !item.children) {
								viewName = `r${item.id}`;
							} else if (item.type != MenuType.Page) {
								return;
							} else {
								viewName = `r${item.pid}`;
							}

							route.props = {};
							route.components = {};
							if (ComponentsMapping[route.path] !== undefined) {
								route.components[viewName] = ComponentsMapping[route.path];
							} else {
								if (route.path.startsWith("Maters")) {
									route.components[viewName] = ComponentsMapping["Maters"];
								} else if (route.path.match(/Declarations\/(?!index)/)) {
									route.components[viewName] = ComponentsMapping["declare"];
								} else {
									route.components[viewName] = ComponentsMapping["Common"];
								}
							}
							route.props[viewName] = {
								url: item.url,
								head: item.name,
							};
							router.addRoute("main", route);
						});

						mainLoading.value = false;
						next({
							name: "index",
							replace: true,
						});
					}
					return true;
				}
			);
		}
	});
</script>

<template>
	<div
		id="main-loading"
		element-loading-text="处理中..."
		v-loading.fullscreen.lock="mainLoading.value"
	>
		<RouterView></RouterView>
	</div>
	<ElConfigProvider :locale="zhCn"> </ElConfigProvider>
</template>

<style scoped lang="css">
	div#main-loading {
		height: 100%;
		width: 100%;
	}
</style>
