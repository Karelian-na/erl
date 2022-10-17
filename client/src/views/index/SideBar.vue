<script setup lang="ts">
	import type { MenuItem } from ".";
	import NavItem from "./NavItem.vue";

	var shrinked = false;
	var selectedItem: HTMLLIElement | null;
	defineProps<{
		items: MenuItem[],
	}>();

	function BtnPositionCurrentLink_OnClick() {
		if (!shrinked) {
			if (selectedItem) {
				var parentDirList = new Array();
				var selectedItemParent = selectedItem.parentElement;
				while (selectedItemParent) {
					if (selectedItemParent.tagName == "NAV") {
						break;
					} else if (selectedItemParent.classList.contains("navigation-directory")) {
						if (!selectedItemParent.classList.contains("expanded")) {
							parentDirList.unshift(selectedItemParent.querySelector(".navigation-directory-trigger"));
						}
					}
					selectedItemParent = selectedItemParent.parentElement;
				}
				parentDirList.forEach((item) => item.click());
				setTimeout(() => selectedItem?.scrollIntoView({ behavior: "smooth" }), 500);
			}
		}
	}

	function BtnExpandAll_OnClick() {
		const expandedDir = document.querySelectorAll<HTMLLIElement>(".sidebar .navigation-directory.expanded>.navigation-directory-trigger");
		if (expandedDir.length == 0) {
			document.querySelectorAll<HTMLLIElement>(".sidebar .navigation-directory .navigation-directory-trigger").forEach((item) => item.click());
		} else {
			expandedDir.forEach((item) => item.click());
		}
	}
</script>

<template>
	<div class="sidebar" :class="{ shrinked: shrinked }">
		<header class="header">
			<i class="iconfont icon-bookline"></i>
			<h1>数学与统计系电子资源库</h1>
		</header>
		<nav class="navigation">
			<ul>
				<NavItem v-for="item in items" 
					:navItem="item" 
					:selectedItem="selectedItem"
					@on-change-item="(value: HTMLLIElement) => selectedItem = value">
				</NavItem>
			</ul>
		</nav>
		<div class="navigation-option">
			<button @click="shrinked = !shrinked" id="button-shrink" class="ui-button" title="伸缩侧边栏">
				<i class="iconfont icon-shrink"></i>
			</button>
			<button @click="BtnPositionCurrentLink_OnClick" id="button-position" class="ui-button">
				<i class="iconfont icon-position"></i>
			</button>
			<button @click="BtnExpandAll_OnClick" id="button-expand" class="ui-button">
				<i class="iconfont icon-expand-all"></i>
			</button>
		</div>
	</div>
</template>

<style lang="css" scoped>
	.sidebar {
		/* Layout */
		width: 320px;
		display: flex;
		flex-direction: column;

		/* Text */
		line-height: 48px;

		/* Appearance */
		background-color: #11101c;

		/* Animation */
		transition: width 0.3s ease-in-out;
	}

	.sidebar .iconfont {
		/* Layout */
		width: 3em;
	}
	/* header */

	.sidebar .header {
		/* Layout */
		white-space: nowrap;
	}

	.sidebar .header .icon-bookline {
		/* Layout */
		width: 48px;

		/* Text */
		font-size: 30px;
		vertical-align: top;

		/* Appearance */
		color: white;
	}

	.sidebar .header h1 {
		/* Layout */
		display: inline;

		/* Text */
		font: normal bold 1.2em/48px "consolas";

		/* Appearance */
		color: white;
	}

	/*  */

	/* navigation 开始  */
	.sidebar .navigation {
		/* Layout */
		flex-grow: 1;
		overflow: hidden auto;
	}

	.sidebar .navigation::-webkit-scrollbar {
		/* Layout */
		display: none;
	}
</style>
