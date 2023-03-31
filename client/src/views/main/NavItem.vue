<!-- @format -->

<script setup lang="ts">
	import type { CreateTabFunction, ITabProps, ItemTabMapType, SwitchPageFunction } from ".";

	import IconFont from "components/IconFont.vue";

	import { ref, inject, Ref } from "vue";
	import { IMenuItem, MenuType } from "../permissions";
	import { dropDown, DropDownElement } from "common/Animation";

	const props = defineProps<{
		navItem: IMenuItem;
	}>();

	const expanded = ref(false);
	const curTab = inject<Ref<ITabProps>>("curTab")!;
	const shrinked = inject<Ref<boolean>>("shrinked")!;
	const curNavItem = inject<Ref<number>>("curNavItem")!;
	const createTab = inject<CreateTabFunction>("createTab")!;
	const switchPage = inject<SwitchPageFunction>("switchPage")!;
	const itemMapTab = inject<Ref<ItemTabMapType>>("itemMapTab")!;

	function onNavItemClick(nav: IMenuItem) {
		if (nav.url != "#" && !nav.url.startsWith("/")) {
			window.open(nav.url);
		} else {
			const navId = nav.id;
			if (curNavItem.value != props.navItem.id) {
				let findedTab = itemMapTab.value.get(navId);
				if (!findedTab) {
					findedTab = createTab(props.navItem);
					itemMapTab.value.set(navId, findedTab);
				}
				switchPage(findedTab, true);
				curNavItem.value = navId;
			} else {
				curTab.value.element?.scrollIntoView({ behavior: "smooth" });
			}
		}
	}

	function onNavDirectoryTriggerClick(event: Event) {
		if (!shrinked.value) {
			const directory: HTMLLIElement = (event.currentTarget as HTMLLIElement).parentElement as HTMLLIElement;
			if (directory.classList.contains("expanded")) {
				dropDown(directory.querySelector("ul") as unknown as DropDownElement, 100, () => {
					expanded.value = !expanded.value;
				});
			} else {
				dropDown(directory.querySelector("ul") as unknown as DropDownElement, 100);
				expanded.value = !expanded.value;
			}
		}
	}
</script>

<template>
	<template v-if="navItem.type <= MenuType.Item">
		<li
			v-if="navItem.type == MenuType.Menu && navItem.children!.length != 0"
			class="directory"
			:class="{ expanded: expanded }"
		>
			<div
				class="trigger"
				@click="onNavDirectoryTriggerClick"
			>
				<IconFont
					v-if="navItem.icon"
					:value="navItem.icon"
					:style="`--level:${navItem.level}`"
				/>
				<a
					href="javascript: void(0);"
					:style="`--level:${navItem.level}`"
					>{{ navItem.name }}
				</a>
				<IconFont value="expand" />
			</div>
			<ul>
				<NavItem
					v-for="item in navItem.children"
					:key="item.id"
					:navItem="item"
				/>
			</ul>
		</li>
		<li
			v-else-if="!navItem.pid"
			class="directory pseudo trigger"
			:class="{ selected: navItem.id == curNavItem }"
		>
			<IconFont
				v-if="navItem.icon"
				:value="navItem.icon"
			/>
			<ul>
				<li
					class="item"
					:id="`item-${navItem.id}`"
					@click="onNavItemClick(navItem)"
				>
					<a
						:href="navItem.url == '#' ? (navItem.children ? navItem.children[0].url : '#') : navItem.url"
						:style="`--level:${navItem.level}`"
						@click.stop.prevent="onNavItemClick(navItem)"
						>{{ navItem.name }}
					</a>
					<IconFont
						v-if="navItem.url != '#' && !navItem.url.startsWith('/')"
						value="external-link"
					/>
				</li>
			</ul>
		</li>
		<li
			v-else
			class="item"
			:id="`item-${navItem.id}`"
			:class="{ selected: navItem.id == curNavItem }"
			@click="onNavItemClick(navItem)"
		>
			<IconFont
				v-if="navItem.icon"
				:value="navItem.icon"
				:style="`--level:${navItem.level}`"
			/>
			<a
				:href="navItem.url == '#' ? (navItem.children ? navItem.children[0].url : '#') : navItem.url"
				:style="`--level:${navItem.level}`"
				@click.stop.prevent="onNavItemClick(navItem)"
				>{{ navItem.name }}</a
			>
			<IconFont
				v-if="navItem.url != '#' && !navItem.url.startsWith('/')"
				value="external-link"
			/>
		</li>
	</template>
</template>

<style lang="css" scoped>
	.directory {
		/* Layout */
		position: relative;
		overflow: hidden;
		color: #999999;
		background-color: #11101c;
	}
	.directory:not(.pseudo):hover::before {
		/* Content Generater */
		content: "";

		/* Layout */
		position: absolute;
		height: 3em;
		width: 5px;

		/* Appearance */
		background-color: var(--el-color-primary);

		/* Animation */
		animation: height-ani 0.3s ease-in-out;
	}

	.directory > ul {
		background-color: inherit;
	}
	.directory:not(.pseudo) > ul {
		/* Layout */
		display: none;
	}
	.directory.expanded > ul {
		/* Layout */
		display: block;
	}

	/* all menu icon */
	.trigger > .iconfont:first-child {
		/* Layout */
		left: calc((var(--level) - 1) * 20px);
		position: absolute;
		z-index: 10;
	}
	/* sub menu icon special */
	.directory > ul .iconfont:first-child {
		width: unset;
		left: calc(2.5em + (var(--level) - 2) * 1em);
		position: absolute;
		z-index: 10;
	}

	.trigger {
		white-space: nowrap;
		/* Animation */
		transition: color 0.3s ease-in-out;
	}
	.item,
	.trigger {
		/* Layout */
		height: 3em;
		cursor: pointer;
	}

	.item:hover,
	.trigger:hover {
		/* Appearance */
		color: white;
	}

	/* operation icon, like expand, external-link */
	.trigger .iconfont:last-child {
		/* Layout */
		right: 0;
		position: absolute;

		/* Animation */
		transition: transform 0.1s ease-in-out;
	}

	.directory.expanded > .trigger .icon-expand {
		/* Other */
		transform: rotate(0.5turn);
	}

	.item {
		/* Animation */
		transition: background-color 0.3s ease-in-out, color 0.3s ease-in-out;
	}

	.item:hover,
	.item.selected,
	.directory.pseudo.selected {
		/* Appearance */
		background-color: var(--el-color-primary);
		color: white;
	}
	.directory.pseudo.selected .item {
		background-color: var(--el-color-primary);
	}

	a {
		/* Layout */
		display: inline-block;
		white-space: nowrap;
		margin-left: calc(2em + var(--level) * 1.25em);

		/* Appearance */
		color: inherit;

		/* Animation */
		transition: padding-left 0.3s ease-in-out;
	}

	.item:hover a,
	.item.selected a,
	.directory.pseudo.selected a {
		/* Layout */
		padding-left: 5px;

		/* Text */
		font-size: 1.04em;

		/* Appearance */
		color: white;
	}

	.shrinked .directory {
		/* Layout */
		overflow: initial;
	}
	.shrinked .directory.pseudo.selected {
		background-color: #11101c;
	}
	.shrinked .directory > ul > .directory:first-child:hover::before,
	.shrinked .directory > ul > .directory:last-child:hover::before {
		/* Layout */
		height: 2.5em;
		animation: height-ani-first 0.3s ease-in-out;
	}
	.shrinked .directory > ul > .directory:first-child:hover::before {
		top: 0.5em;
	}

	.shrinked .directory > ul,
	.shrinked .directory:not(.pseudo) a[style="--level:1;"],
	.shrinked a[style="--level:1;"] + .icon-expand {
		/* Layout */
		display: none;
	}

	/* all menu icon except top level when shrinked */
	.shrinked .directory > ul .iconfont:first-child {
		/* Layout */
		left: 1em;
	}

	.shrinked .directory.pseudo:hover::before {
		/* Content Generater */
		content: "";

		/* Layout */
		position: absolute;
		height: 3em;
		width: 5px;

		/* Appearance */
		background-color: var(--el-color-primary);

		/* Animation */
		animation: height-ani 0.3s;
	}

	.shrinked .trigger .icon-expand {
		/* Other */
		transform: rotate(0.75turn);
	}

	.shrinked .directory > ul {
		/* Layout */
		top: 0;
		left: 100%;
		z-index: 10;
		width: 280px;
		padding-left: 5px;
		position: absolute;

		/* Appearance */
		background-clip: content-box;

		/* Other */
		border-radius: 1em;
	}
	.shrinked .directory:hover > ul {
		/* Layout */
		display: initial;

		/* Animation */
		animation: shrinked-display-animation 0.3s ease-in-out;
		transform-origin: 0 0;
	}

	.shrinked .directory:not(.pseudo):last-child,
	.shrinked .item:last-child {
		/* Other */
		border-bottom-left-radius: 0.8em;
		border-bottom-right-radius: 1em;
	}
	.shrinked .directory:not(.pseudo):first-child,
	.shrinked .item:first-child {
		/* Other */
		border-top-left-radius: 0.8em;
		border-top-right-radius: 1em;
	}

	.shrinked .item a,
	.shrinked .trigger a {
		/* Layout */
		margin-left: 2.5em;
	}

	@keyframes height-ani {
		from {
			height: 0;
		}
		to {
			height: 3em;
		}
	}

	@keyframes height-ani-first {
		from {
			height: 0;
		}
		to {
			height: 40px;
		}
	}

	@keyframes shrinked-display-animation {
		from {
			opacity: 0%;
			transform: scale(0.5);
		}
		to {
			opacity: 100%;
			transform: scale(1);
		}
	}
</style>
