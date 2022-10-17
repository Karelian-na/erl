<script setup lang="ts">
	import { MenuItem } from "./index";

	var expanded: Boolean = false;

	const props = defineProps<{
		navItem: MenuItem,
		selectedItem: HTMLLIElement | null,
	}>();

	const emits = defineEmits<{
		(e: "onChangeItem", value: HTMLLIElement): void,
	}>();

	function navItemOnClick(this: HTMLLIElement) {
		if (props.selectedItem != this) {
			emits("onChangeItem", this);
		}
	}

	function isSelected(this: HTMLLIElement) {
		return props.selectedItem == this;
	}
</script>

<template>
	<li v-if="navItem.children && navItem.children.length != 0" class="navigation-directory" :class="{ expanded: expanded}">
		<div @click="expanded = !expanded" class="navigation-directory-trigger">
			<i style="--level:{{ navItem.level }}" class="iconfont icon-{{ navItem.icon }}"></i>
			<a style="--level:{{ navItem.level }}" href="javascript: void(0);">{{ navItem.icon }}</a>
			<i class="iconfont icon-expand" :class="{ 'icon-expand': expanded }"></i>
		</div>
		<ul>
			<NavItem v-for="item in navItem.children" :navItem="item"></NavItem>
		</ul>
	</li>
	<li v-else-if="!navItem.pid" class="navigation-pseudo-directory navigation-directory-trigger">
		<i class="iconfont icon-{{ navItem.icon }}"></i>
		<ul>
			<li @click="navItemOnClick" id="item-{{ navItem.id }}" class="navigation-item" :class="{ 'link-selected': isSelected}">
				<a href="{{ navItem.url }}" style="--level:{{ navItem.level }}">{{ navItem.name }}</a>
			</li>
		</ul>
	</li>
	<li @click="navItemOnClick" v-else id="item-{{ navItem.id }}" class="navigation-item">
		<a href="{{ navItem.url }}" style="--level:{{ navItem.level }}">{{ navItem.name }}</a>
	</li>
</template>

<style lang="css" scoped>
	.navigation-directory {
		/* Layout */
		position: relative;
		overflow: hidden;
	}

	.navigation-directory:hover::before {
		/* Content Generater */
		content: "";

		/* Layout */
		position: absolute;
		height: 3em;
		width: 5px;

		/* Appearance */
		background-color: #008c8c;

		/* Animation */
		animation: height-ani 0.3s ease-in-out;
	}

	.navigation-directory ul {
		/* Layout */
		display: none;
	}

	.navigation-directory.expanded > ul {
		/* Layout */
		display: block;
	}

	.navigation-directory-trigger {
		/* Animation */
		transition: color 0.3s ease-in-out;
	}

	.navigation-directory-trigger,
	.navigation-item {
		/* Layout */
		position: relative;
		height: 3em;

		/* Appearance */
		color: #999999;
		cursor: pointer;
	}

	.navigation-directory-trigger:hover,
	.navigation-item:hover {
		/* Appearance */
		color: white;
	}

	.navigation-directory-trigger > .iconfont {
		/* Layout */
		left: calc((var(--level) - 1) * 20px);
		position: absolute;
		z-index: 10;
	}

	.navigation-item > .iconfont {
		/* Layout */
		width: 20px;
		left: calc(40px + (var(--level) - 2) * 20px);
		position: absolute;
		z-index: 10;
	}

	.navigation-directory-trigger .icon-expand {
		/* Layout */
		position: absolute;
		right: 0;

		/* Animation */
		transition: transform 0.1s ease-in-out;
	}

	.navigation-directory-trigger .icon-expand.expanded {
		/* Other */
		transform: rotate(0.5turn);
	}

	.navigation-item {
		/* Animation */
		transition: background-color 0.3s ease-in-out, color 0.3s ease-in-out;
	}

	.navigation-item:hover,
	.navigation-item.link-selected {
		/* Appearance */
		background-color: #008c8c;
		color: white;
	}

	a {
		/* Layout */
		display: inline-block;
		white-space: nowrap;
		margin-left: calc(30px + var(--level) * 20px);

		/* Appearance */
		color: inherit;

		/* Animation */
		transition: padding-left 0.3s ease-in-out;
	}

	.navigation-item:hover a,
	.navigation-item.link-selected a {
		/* Layout */
		padding-left: 5px;

		/* Text */
		font-size: 1.04em;

		/* Appearance */
		color: white;
	}
</style>
