<!-- @format -->

<script setup lang="ts">
	import IconFont from "./IconFont.vue";

	defineProps<{
		icon?: string;
	}>();
</script>

<template>
	<button
		type="button"
		class="ui-button"
		:class="icon"
	>
		<IconFont
			v-if="icon"
			:value="icon"
		></IconFont>
		<slot></slot>
	</button>
</template>

<style>
	.ui-button {
		/* Layout */
		display: inline-block;
		height: 2.5em;
		padding: 0 1.5em;
		margin: 0 2.5px;

		/* Text */
		font-size: inherit;
		vertical-align: inherit;

		/* Appearance */
		outline: none;
		border-style: none;
		color: inherit;
		cursor: pointer;
		color: var(--el-text-color-regular);
		background-color: var(--el-color-primary);

		/* Animation */
		transition: opacity 0.3s ease-in-out;

		/* Other */
		border-radius: 5px;
		transition: opacity 0.3s ease-in-out;
	}

	.ui-button:hover {
		opacity: 0.7;
	}

	.ui-button .iconfont {
		margin-right: 5px;
	}

	.ui-button.delete {
		background-color: brown;
	}

	.ui-button.add,
	.ui-button.edit,
	.ui-button.upload {
		background-color: tomato;
	}

	.ui-button.export,
	.ui-button.download {
		background-color: dodgerblue;
	}
</style>
