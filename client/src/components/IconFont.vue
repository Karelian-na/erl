<!-- @format -->

<script setup lang="ts">
	defineProps<{
		value?: string;
		type?: "font" | "svg";
	}>();
</script>

<template>
	<svg
		v-if="type == 'svg'"
		class="icon"
		aria-hidden="true"
	>
		<use :xlink:href="`#icon-${value}`"></use>
	</svg>
	<i
		v-else-if="value"
		class="iconfont"
		:class="[value ? `icon-${value}` : '']"
	></i>
</template>

<style>
	.iconfont {
		display: inline-block;
		text-align: center;
		vertical-align: baseline;
		letter-spacing: 0;
		font-size: inherit;
		color: inherit;
	}

	.icon {
		width: 1em;
		height: 1em;
		vertical-align: -0.15em;
		fill: currentColor;
		overflow: hidden;
	}
</style>
