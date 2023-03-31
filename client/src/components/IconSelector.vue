<!-- @format -->

<script setup lang="ts">
	import IconFont from "./IconFont.vue";
	import { ElPopover } from "element-plus";

	import { ref } from "vue";
	import axios from "axios";
	import { EmptyObject } from "common/utils";

	const props = defineProps<{
		reqUrl?: string;
		modelValue?: string;
	}>();

	const container = ref<HTMLDivElement>(EmptyObject);
	const icons = ref<string[]>([]);

	const emits = defineEmits<{
		(e: "update:model-value", value: string): void;
	}>();

	if (props.reqUrl) {
		axios
			.get<{ glyphs: { font_class: string }[] }>(props.reqUrl, {
				withCredentials: false,
			})
			.then((result) => {
				const iconfonts = result.data;
				icons.value = iconfonts.glyphs.map((val) => val.font_class);
			});
	}

	function onAfterEnter() {
		container.value.querySelector(".selected")?.scrollIntoView();
	}

	function onSelect(val: string) {
		if (props.modelValue != val) {
			emits("update:model-value", val);
		}
	}
</script>

<template>
	<ElPopover
		placement="bottom-end"
		width="400px"
		trigger="click"
		:popper-style="{ height: '300px', 'overflow-y': 'hidden' }"
		@after-enter="onAfterEnter"
	>
		<template #reference>
			<slot></slot>
		</template>
		<div
			ref="container"
			class="icons-container"
		>
			<IconFont
				value=""
				:class="{ selected: '' == modelValue }"
				@click="onSelect('')"
			></IconFont>
			<IconFont
				v-for="icon in icons"
				:value="icon"
				:class="{ selected: icon == modelValue }"
				@click="onSelect(icon)"
			>
			</IconFont>
		</div>
	</ElPopover>
</template>

<style scoped>
	.icons-container {
		height: 100%;
		overflow: auto;
	}
	.iconfont {
		vertical-align: middle;
		font-size: 16px;
		width: 35px;
		height: 35px;
		line-height: 35px;
		cursor: pointer;
		border: 1px solid var(--el-border-color);
		border-radius: 5px;
		margin: 5px;
		transition: opacity 0.3s ease-in-out;
	}

	.iconfont:hover {
		opacity: 0.5;
	}

	.iconfont.selected {
		border-color: var(--el-color-primary);
		color: var(--el-color-primary);
	}
</style>
