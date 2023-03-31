<!-- @format -->

<script setup lang="ts">
	import type { IMenuItem } from "../permissions";

	import NavItem from "./NavItem.vue";
	// import { ElInput } from "element-plus";
	import IconFont from "components/IconFont.vue";

	import { inject, Ref } from "vue";

	defineProps<{
		items: IMenuItem[];
	}>();

	const shrinked = inject<Ref<boolean>>("shrinked")!;
	const autoShrink = inject<Ref<boolean>>("autoShrink")!;
	const curNavItem = inject<Ref<number>>("curNavItem")!;

	defineExpose({ onLocateCurNavItem });

	function onLocateCurNavItem() {
		if (!shrinked.value) {
			if (curNavItem.value) {
				let curNavItemElement = document.getElementById(`item-${curNavItem.value}`)!;
				var parentDirList = new Array();
				var curNavItemParent = curNavItemElement.parentElement;
				while (curNavItemParent) {
					if (curNavItemParent.tagName == "NAV") {
						break;
					} else if (curNavItemParent.classList.contains("directory")) {
						if (!curNavItemParent.classList.contains("expanded")) {
							parentDirList.unshift(curNavItemParent.querySelector(".trigger") ?? curNavItemParent);
						}
					}
					curNavItemParent = curNavItemParent.parentElement;
				}
				parentDirList.forEach((item) => item.click());
				setTimeout(() => curNavItemElement.scrollIntoView({ behavior: "smooth" }), 500);
			}
		}
	}

	function onExpandAllNavDirectory() {
		const expandedDir = document.querySelectorAll<HTMLLIElement>(".sidebar .directory.expanded>.trigger");
		if (expandedDir.length == 0) {
			document.querySelectorAll<HTMLLIElement>(".sidebar .directory .trigger").forEach((item) => item.click());
		} else {
			expandedDir.forEach((item) => item.click());
		}
	}

	function onShrinkSidebar() {
		autoShrink.value = shrinked.value;
		shrinked.value = !shrinked.value;
	}
</script>

<template>
	<div class="sidebar">
		<header class="header">
			<IconFont value="bookline"></IconFont>
			<h1 class="title">数学与统计系电子资源库</h1>
		</header>
		<!-- <div class="search">
			<ElInput>
				<template #prepend>
					<IconFont value="search"></IconFont>
				</template>
			</ElInput>
		</div> -->
		<nav class="navigation">
			<ul>
				<NavItem
					v-for="item in items"
					:key="item.id"
					:nav-item="item"
				>
				</NavItem>
			</ul>
		</nav>
		<div class="navigation-option">
			<IconFont
				@click="onShrinkSidebar"
				title="伸缩侧边栏"
				value="shrink"
			></IconFont>
			<IconFont
				@click="onLocateCurNavItem"
				title="跳转到当前选中的菜单"
				value="locate"
			></IconFont>
			<IconFont
				@click="onExpandAllNavDirectory"
				title="展开或收起所有菜单"
				value="expand-all"
			></IconFont>
		</div>
	</div>
</template>

<style scoped lang="css">
	.sidebar :deep(.iconfont) {
		/* Layout */
		width: 3em;
	}
	/* header */

	.sidebar .header,
	.sidebar .search {
		/* couldn't wrap when shrink */
		white-space: nowrap;
		overflow: hidden;

		color: white;
	}

	.header .icon-bookline {
		/* Layout */
		width: 1.5em;

		/* Text */
		font-size: 2em;
		vertical-align: top;
	}

	.header .title {
		/* Layout */
		display: inline;

		/* Text */
		font: normal bold 1.2em/48px "consolas";

		/* Appearance */
		color: white;
	}

	.search {
		padding: 0 20px;
		background-color: transparent;
		color: white;
		transition: padding-left 0.3s ease-in-out;
	}
	.search .el-input :deep(.el-input-group__prepend),
	.search .el-input :deep(.el-input__wrapper) {
		background-color: transparent;
	}

	.search .el-input :deep(.el-input-group__prepend) {
		padding: 0;
	}

	/* navigation 开始  */
	.navigation {
		/* Layout */
		height: 400px;
		flex-grow: 1;
		overflow: hidden auto;
	}
	.navigation::-webkit-scrollbar {
		/* Layout */
		display: none;
	}

	.navigation-option {
		/* Layout */
		padding: 0 4em;
		white-space: nowrap;
		overflow: hidden;
		text-align: center;

		/* Animation */
		transition: padding-left 0.3s ease-in-out;
	}
	.navigation-option .iconfont {
		/* Text */
		width: 2em;
		font-size: 1.4em;
		margin-right: 1em;
		color: white;
		cursor: pointer;
		vertical-align: top;
	}
	.navigation-option .icon-shrink {
		/* Layout */
		transition: transform 0.3s ease-in-out;
	}

	.shrinked .header .title {
		/* Layout */
		display: none;
	}
	.shrinked .navigation {
		/* Layout */
		overflow: initial;
	}
	.shrinked .search {
		padding-left: 0;
	}
	.shrinked .search :deep(.el-input-group__prepend) {
		box-shadow: none;
	}
	.shrinked .navigation-option {
		/* Layout */
		padding-left: 0px;
	}
	.shrinked .navigation-option .icon-shrink {
		/* Layout */
		transform: rotate(0.5turn);
	}
</style>
