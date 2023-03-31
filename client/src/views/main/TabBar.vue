<!-- @format -->

<script setup lang="ts">
	import IconFont from "components/IconFont.vue";

	import { ref, inject, Ref } from "vue";
	import { error, getByValue } from "common/utils";
	import { ItemTabMapType, TabPageMapType, ITabProps, SwitchPageFunction, specialTabs } from ".";

	let timeId = 0;

	const optionDisplayed = ref(false);
	const tabContainer = ref<HTMLUListElement>();

	const curTab = inject<Ref<ITabProps>>("curTab")!;
	const switchPage = inject<SwitchPageFunction>("switchPage")!;
	const tabMapPage = inject<Ref<TabPageMapType>>("tabMapPage")!;
	const itemMapTab = inject<Ref<ItemTabMapType>>("itemMapTab")!;

	function onTabItemClose(tabProps: ITabProps) {
		if (tabProps.name != specialTabs.home.name) {
			if (curTab.value == tabProps) {
				const tabs = Array.from(tabMapPage.value.keys());
				const idx = tabs.findIndex((val) => val.name == tabProps.name) - 1;
				switchPage(tabs[idx], false);
			}
			itemMapTab.value.delete(getByValue(itemMapTab.value, tabProps, (fst, sec) => fst.name == sec.name));
			tabMapPage.value.delete(tabProps);
		}
	}

	function onTabLeftScrollMouseDown() {
		timeId = setInterval(() => {
			const distance = tabContainer.value!.scrollLeft;
			if (distance == 0) {
				return;
			} else if (distance < 100) {
				tabContainer.value!.scrollTo({
					left: 0,
					behavior: "smooth",
				});
			} else {
				tabContainer.value!.scrollTo({
					left: distance - 100,
					behavior: "smooth",
				});
			}
		}, 100);
	}

	function onTabRightScrollMouseDown() {
		timeId = setInterval(() => {
			const container = tabContainer.value as HTMLUListElement;
			var deltaDistance = container.scrollWidth - container.clientWidth - Math.floor(container.scrollLeft);
			if (deltaDistance == 0) {
				return;
			} else if (deltaDistance < 100) {
				container.scrollTo({
					left: container.scrollWidth - container.clientWidth,
					behavior: "smooth",
				});
			} else {
				container.scrollTo({
					left: container.scrollLeft + 100,
					behavior: "smooth",
				});
			}
		}, 100);
	}

	function onTabScrollMouseUp() {
		if (timeId != 0) {
			clearInterval(timeId);
			timeId = 0;
		}
	}

	function tabCloseAllOnclick() {
		const tabs = Array.from(tabMapPage.value.keys());
		for (let idx = 1; idx < tabs.length; ++idx) {
			itemMapTab.value.delete(getByValue(itemMapTab.value, tabs[idx], (fst, sec) => fst.name == sec.name));
			tabMapPage.value.delete(tabs[idx]);
		}
		switchPage(tabs[0], false);
		optionDisplayed.value = false;
	}

	function tabCloseOthersOnclick() {
		let tabs: ITabProps[];
		if (curTab.value.name == specialTabs.home.name) {
			tabs = Array.from(tabMapPage.value.keys());
		} else {
			tabs = Array.from(tabMapPage.value.keys()).filter((val) => val.name != curTab.value.name);
		}
		for (let idx = 1; idx < tabs.length; ++idx) {
			itemMapTab.value.delete(getByValue(itemMapTab.value, tabs[idx], (fst, sec) => fst.name == sec.name));
			tabMapPage.value.delete(tabs[idx]);
		}
		optionDisplayed.value = false;
	}

	function tabCloseCurrentOnclick() {
		if (curTab.value.name == specialTabs.home.name) {
			error("msg", { message: "不能关闭该标签!" });
			return;
		}
		onTabItemClose(curTab.value);
		optionDisplayed.value = false;
	}
</script>

<template>
	<div class="tabbar">
		<IconFont
			title="向前移动"
			value="double-arrow-left"
			@mousedown="onTabLeftScrollMouseDown"
			@mouseup="onTabScrollMouseUp"
		></IconFont>
		<ul
			ref="tabContainer"
			class="items"
		>
			<li
				class="item stable-home"
				:ref="(vnode) => (specialTabs.home.element = vnode as HTMLLIElement)"
				:class="{ 'tab-item-selected': curTab.name == specialTabs.home.name }"
				@click.stop.prevent="switchPage(specialTabs.home, false)"
			>
				<IconFont
					value="home"
					title="主页"
				></IconFont>
			</li>
			<li
				v-for="tab in Array.from(tabMapPage.keys()).filter((val) => val.name != 'home')"
				class="item"
				:ref="(vnode) => (tab.element = vnode as HTMLLIElement)"
				:key="tab.name"
				:class="{ 'tab-item-selected': curTab.name == tab.name }"
				@click="switchPage(tab, false)"
			>
				<span
					class="name"
					:title="tab.title"
					>{{ tab.title }}
				</span>
				<IconFont
					value="close"
					@click.stop.prevent="onTabItemClose(tab)"
				></IconFont>
			</li>
		</ul>
		<IconFont
			title="向后移动"
			value="double-arrow-right"
			@mousedown="onTabRightScrollMouseDown"
			@mouseup="onTabScrollMouseUp"
		></IconFont>
		<IconFont
			title="更多选项"
			value="more"
			@click="optionDisplayed = !optionDisplayed"
		></IconFont>
		<ul
			class="options"
			:class="{ display: optionDisplayed }"
		>
			<li
				class="item"
				@click="tabCloseAllOnclick"
				>关闭所有选项卡
			</li>
			<li
				class="item"
				@click="tabCloseCurrentOnclick"
			>
				关闭当前选项卡
			</li>
			<li
				class="item"
				@click="tabCloseOthersOnclick"
			>
				关闭其它选项卡
			</li>
		</ul>
	</div>
</template>

<style scoped lang="css">
	.tabbar {
		/* Layout */
		display: flex;
		position: relative;
	}

	.tabbar .icon-double-arrow-left,
	.tabbar .icon-double-arrow-right,
	.tabbar .icon-more {
		flex-shrink: 0;
	}

	.tabbar .items {
		/* Layout */
		flex-grow: 1;
		overflow: hidden;
		white-space: nowrap;
	}

	.items .item {
		display: inline-block;
		position: relative;
		cursor: pointer;
	}
	.items .item:not(:first-of-type) {
		padding: 0 5px;
	}
	.items .item:hover,
	.items .item.tab-item-selected {
		/* Appearance */
		color: black;
	}

	.items .item:hover::before,
	.items .item.tab-item-selected::before {
		/* Content Generater */
		content: "";

		/* Layout */
		position: absolute;
		top: 0px;
		left: 0px;
		width: 100%;
		height: 0.15em;

		/* Appearance */
		background-color: var(--el-color-primary);

		/* Animation */
		animation: tip-ani 0.3s ease-in-out;
	}

	.items .item .name {
		vertical-align: top;
		margin-right: 0.1em;
	}

	.items .item .icon-close {
		/* Layout */
		position: relative;
		width: 1em;
	}
	.items .item .icon-close:hover {
		/* Appearance */
		color: white;

		/* Animation */
		transition: color 0.3s ease-in-out;
	}
	/* make sure close icon is above background when hover */
	.items .item .icon-close::before {
		/* Layout */
		position: relative;
		z-index: 1;
		display: block;
		transform: scale(0.6);
	}
	.items .item .icon-close:hover::after {
		/* Content Generater */
		content: "";

		/* Layout */
		position: absolute;
		right: 0;
		top: 0;
		bottom: 0;
		margin: auto;
		width: 1em;
		height: 1em;

		/* Appearance */
		background-color: tomato;

		/* Animation */
		animation: bac-ani 0.3s;

		/* Other */
		border-radius: 50%;
	}

	.tabbar .icon-tab-right-scroll {
		/* Other */
		transform: rotate(0.5turn);
	}

	.tabbar .options {
		/* Layout */
		display: none;
		position: absolute;
		right: 0;
		top: 2.5em;
		margin: 5px;
		padding: 0 1em;
		z-index: 5;

		/* Appearance */
		border: 1px solid #aaaaaa;
		background-color: white;

		/* Other */
		border-radius: 5px;
	}
	.tabbar .options.display {
		/* Layout */
		display: initial;
	}

	.options .item {
		cursor: pointer;
	}
	.options .item:hover {
		color: black;
	}

	@keyframes tip-ani {
		from {
			margin: 0 50%;
			width: 0px;
		}
		to {
			margin: 0;
			width: 100%;
		}
	}

	@keyframes bac-ani {
		from {
			background-color: transparent;
		}
		to {
			background-color: tomato;
		}
	}
</style>
