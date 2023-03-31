<!-- @format -->

<script setup lang="ts">
	import type { UserInfo } from "views/common";

	import TabBar from "./TabBar.vue";
	import SideBar from "./SideBar.vue";
	import MenuBar from "./MenuBar.vue";
	import IconFont from "components/IconFont.vue";
	import { TabPaneName, ElTabs, ElTabPane, vLoading } from "element-plus";

	import Store from "store";
	import { error, getByValue } from "common/utils";
	import { RouterView, useRouter } from "vue-router";
	import { handleMenus, IMenuItem } from "../permissions";
	import { onBeforeMount, provide, ref, nextTick, inject, shallowRef, reactive, KeepAlive } from "vue";
	import {
		specialTabs,
		ITabProps,
		ItemTabMapType,
		TabPageMapType,
		PageInPageTabMapType,
		IInPageProps,
		specialInPageProps,
		SpecialTabName,
	} from ".";

	const rawItems = inject<IMenuItem[]>("rawItems")!;

	let timeid: number;
	const router = useRouter();
	const treeItems = handleMenus(rawItems);
	const cookieStore = Store.namespace("cookie");

	const tabMapPage = ref<TabPageMapType>(new Map());
	const itemMapTab = ref<ItemTabMapType>(new Map());
	const sidebar = ref<InstanceType<typeof SideBar>>();
	const autoShrink = ref(cookieStore.get("autoShrink") ?? true);
	const pageMapInPageProps = shallowRef<PageInPageTabMapType>(new Map());
	const shrinked = ref(cookieStore.get("shrinked") ?? document.body.clientWidth <= 1000);
	const pageLoading = reactive({
		value: false,
		set tip(v: string) {
			const tipElement = document.querySelector(`#page-loading .el-loading-text`);
			if (tipElement) {
				tipElement.textContent = v;
			}
		},
	});

	const curNavItem = ref<number>(0);
	const curPage = ref<string>("home");
	const curTab = ref<ITabProps>(specialTabs.home);
	const curInPageProps = ref<IInPageProps>(specialInPageProps.home);

	provide("curTab", curTab);
	provide("shrinked", shrinked);
	provide("createTab", createTab);
	provide("itemMapTab", itemMapTab);
	provide("tabMapPage", tabMapPage);
	provide("switchPage", switchPage);
	provide("curNavItem", curNavItem);
	provide("getNavItem", getNavItem);
	provide("autoShrink", autoShrink);
	provide("pageLoading", pageLoading);
	provide("curInPageProps", curInPageProps);

	onBeforeMount(() => {
		document.body.onresize = () => {
			if (timeid) {
				clearTimeout(timeid);
			}
			if (autoShrink.value) {
				timeid = setTimeout(() => {
					if (!shrinked.value && document.body.clientWidth <= 1000) {
						shrinked.value = true;
					} else if (shrinked && document.body.clientWidth > 1000) {
						shrinked.value = false;
					}
				}, 100);
			}
		};
		window.onbeforeunload = () => {
			if (cookieStore.get("value")) {
				cookieStore.set("curPage", curPage.value);
				cookieStore.set("shrinked", shrinked.value);
				cookieStore.set("autoShrink", autoShrink.value);
				cookieStore.set(
					"tabProps",
					Array.from(tabMapPage.value.keys()).map((item) => {
						return {
							name: item.name,
							url: item.url,
						};
					})
				);
			}
		};

		tabMapPage.value.set(curTab.value, curTab.value.name);
		pageMapInPageProps.value.set(curTab.value.name, specialInPageProps.home);
		pageMapInPageProps.value.set(specialTabs.personal.name, specialInPageProps.personal);

		const tabProps = cookieStore.get("tabProps") as ITabProps[] | null;
		if (tabProps) {
			tabProps.shift();
			tabProps.forEach((tab) => {
				const matches = tab.name.match(/r(\d{1,})/);
				if (matches) {
					const navId = parseInt(matches[1]);
					const navItem = getNavItem(navId);
					if (navItem) {
						const newTab = createTab(navItem, tab.url);
						itemMapTab.value.set(navId, newTab);
					}
				} else {
					const newTab = specialTabs[tab.name as SpecialTabName];
					newTab.url = tab.url;

					const inPageProps = specialInPageProps[tab.name as SpecialTabName];
					inPageProps.curTab = (inPageProps.tabs.find((item) => item.url == tab.url) ?? inPageProps.tabs[0]).id;

					tabMapPage.value.set(newTab, newTab.name);
					pageMapInPageProps.value.set(newTab.name, inPageProps);
				}
			});

			let storedCurPage = (cookieStore.get("curPage") as string) ?? specialTabs.home.name;
			switchPage(getByValue(tabMapPage.value, storedCurPage), false);
		} else {
			router.replace("/home");
		}
	});

	function getNavItem(id?: number) {
		if (id === undefined) {
			id = curNavItem.value;
		}

		if (id == 0) {
			return null;
		}
		return rawItems.find((val) => val.id == id) ?? null;
	}

	function createTab(navItem: IMenuItem, inPageUrl?: string) {
		const name = `r${navItem.id}`;

		let title = "";
		let iter = navItem;
		while (iter) {
			title = iter.name + "-" + title;
			iter = iter.parent;
		}

		const newTab: ITabProps = {
			name: name,
			title: title.substring(0, title.length - 1),
			url: "",
		};

		const inPageProps: IInPageProps = {
			tabs: [],
			curTab: 0,
			key: new Date().getTime(),
		};
		if (navItem.children) {
			navItem.children.forEach((child) => {
				inPageProps.tabs.push({
					id: child.id,
					name: child.name,
					url: child.url,
					icon: child.icon,
				});
			});
			if (inPageUrl && navItem.children.find((item) => item.url == inPageUrl)) {
				newTab.url = inPageUrl;
				inPageProps.curTab = (navItem.children.find((item) => item.url == inPageUrl) ?? navItem.children[0]).id;
			} else {
				newTab.url = navItem.children[0].url;
				inPageProps.curTab = navItem.children[0].id;
			}
		} else {
			inPageProps.tabs.push({
				id: navItem.id,
				name: navItem.name,
				url: navItem.url,
			});
			newTab.url = navItem.url;
			inPageProps.curTab = navItem.id;
		}

		tabMapPage.value.set(newTab, name);
		pageMapInPageProps.value.set(name, inPageProps);
		return newTab;
	}

	function switchPage(tabProps: ITabProps, itemClick: boolean, replaceRouter: boolean = true) {
		try {
			if (tabProps.name != curTab.value.name) {
				curTab.value = tabProps;
				curNavItem.value = getByValue(itemMapTab.value, tabProps, (fst, sec) => fst.name == sec.name);
				nextTick(() => {
					if ((!itemClick && curNavItem.value) || !replaceRouter) {
						sidebar.value!.onLocateCurNavItem();
					}
					setTimeout(() => {
						curTab.value.element?.scrollIntoView({
							behavior: "smooth",
						});
					}, 100);
				});
				curPage.value = tabMapPage.value.get(tabProps)!;
				curInPageProps.value = pageMapInPageProps.value.get(curPage.value)!;
			} else {
				sidebar.value?.onLocateCurNavItem();
			}
			if (replaceRouter) {
				router.replace(tabProps.url);
			} else {
				router.push(tabProps.url);
			}
		} catch (e) {
			error("alert", { content: e as string });
		}
		return false;
	}

	function onInPageTabChange(tab: TabPaneName) {
		const url = curInPageProps.value.tabs.find((val) => val.id == tab)!.url;
		curTab.value.url = url;
		router.push(url);
	}
</script>

<template>
	<div class="main">
		<SideBar
			ref="sidebar"
			:items="treeItems"
			:class="{ shrinked: shrinked }"
		></SideBar>
		<div class="content">
			<div class="wrapper">
				<MenuBar></MenuBar>
				<TabBar></TabBar>
				<ElTabs
					v-if="curInPageProps.tabs.length > 1"
					v-model="curInPageProps.curTab"
					@tab-change="onInPageTabChange"
				>
					<ElTabPane
						v-for="tab in curInPageProps.tabs"
						:key="tab.id"
						:label="tab.name"
						:name="tab.id"
					>
						<template #label>
							<IconFont
								v-if="tab.icon"
								:value="tab.icon"
							></IconFont>
							<span>{{ tab.name }}</span>
						</template>
					</ElTabPane>
				</ElTabs>
				<div
					class="page"
					id="page-loading"
					v-loading="pageLoading.value"
					element-loading-text="处理中..."
				>
					<RouterView
						v-for="item in pageMapInPageProps"
						:name="item[0]"
						:key="item[1].key"
					>
						<KeepAlive>
							<component
								:is="$route.matched[$route.matched.length - 1].components![item[0]]"
								v-bind="$route.matched[$route.matched.length - 1].props![item[0]]"
							></component>
						</KeepAlive>
					</RouterView>
				</div>
			</div>
		</div>
	</div>
</template>

<style scoped lang="css">
	.main {
		height: 100%;
		display: flex;
		overflow: hidden;
	}

	.sidebar {
		/* Layout */
		width: 20em;
		display: inline-flex;
		flex-direction: column;
		flex-shrink: 0;

		/* Text */
		line-height: 3em;

		/* Appearance */
		background-color: #11101c;

		/* Animation */
		transition: width 0.3s ease-in-out;
	}
	.sidebar.shrinked {
		/* Layout */
		width: 3em;
	}
	.content {
		/* Layout */
		flex-grow: 1;
		overflow: auto;

		/* Appearance */
		color: #595959;
		background-color: white;

		/* Animation */
		transition: width 0.3s ease-in-out;
	}

	.content .wrapper {
		/* Text */
		display: inline-flex;
		flex-direction: column;
		overflow: hidden;
		width: 100%;
		height: 100%;
		min-width: 680px;
	}

	.wrapper .menubar,
	.wrapper .tabbar,
	.wrapper .page {
		line-height: 2.5em;
	}
	.wrapper .menubar {
		vertical-align: top;
	}
	.wrapper .page {
		/* Layout */
		position: relative;
		flex-grow: 1;
		overflow: auto;
	}
	.wrapper .el-tabs {
		padding: 0 20px;
	}

	.menubar :deep(.iconfont),
	.tabbar :deep(.iconfont) {
		width: 2.5em;
		cursor: pointer;
		vertical-align: top;
		transition: color 0.3s ease-in-out;
	}
	.menubar :deep(.iconfont:hover),
	.tabbar :deep(.iconfont:hover) {
		color: black;
		font-weight: bold;
	}

	.el-tabs .iconfont {
		margin-right: 5px;
	}
	.el-tabs :deep(.el-tabs__item) {
		padding: 0 10px;
	}
</style>
