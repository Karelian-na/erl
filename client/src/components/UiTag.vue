<!-- @format -->

<script setup lang="ts">
	import IconFont from "./IconFont.vue";

	defineProps<{
		label: string;
		icon?: string;
	}>();
</script>

<template>
	<span class="ui-tag">
		<IconFont
			v-if="icon"
			:value="icon"
		></IconFont>
		{{ label }}
	</span>
</template>

<style>
	.ui-tag {
		padding: 5px;
		color: white;
		background-color: var(--el-color-primary);
		border-radius: 5px;
		font-size: 12px;
		vertical-align: initial;
	}
</style>
