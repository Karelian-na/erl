<!-- @format -->

<script setup lang="ts">
	import UiButton from "./UiButton.vue";
	import { ElInput, ElSelect, ElOption } from "element-plus";

	import { ref, watch } from "vue";
	import { error, info } from "common/utils";

	const props = defineProps<{
		pageSizes: number[];
		maxPageAmount: number;
		dataCount: number;
		onChange?: (pageIdx: number, pageSize: number) => Promise<boolean>;
		onChanged?(): void;
	}>();

	const pageSize = ref(20);
	const pageAmount = ref(Math.ceil(props.dataCount / pageSize.value));
	const curPage = ref(1);
	const startIdx = ref(1);
	const inputIdxValue = ref("");
	const endIdx = ref(pageAmount.value <= props.maxPageAmount ? pageAmount.value : props.maxPageAmount);

	let needToRefresh = true;
	watch(
		() => props.dataCount,
		() => needToRefresh && refresh()
	);

	defineExpose({ pageSize, curPage, pageAmount });

	function refresh() {
		const newValue = Math.ceil(props.dataCount / pageSize.value);
		if (newValue != pageAmount.value) {
			pageAmount.value = newValue;
		}
		if (curPage.value > pageAmount.value) {
			curPage.value = pageAmount.value;
		} else if (curPage.value == 0) {
			curPage.value = 1;
		}
		if (curPage.value < props.maxPageAmount || pageAmount.value <= props.maxPageAmount) {
			startIdx.value = 1;
			endIdx.value = pageAmount.value <= props.maxPageAmount ? pageAmount.value : props.maxPageAmount;
		} else if (pageAmount.value - curPage.value <= 3) {
			startIdx.value = pageAmount.value - 6;
			endIdx.value = pageAmount.value;
		} else if (curPage.value >= endIdx.value || curPage.value <= startIdx.value) {
			startIdx.value = curPage.value - 3;
			endIdx.value = curPage.value + 3;
		}
		props.onChanged?.();
	}

	async function changePage(idx: number) {
		if (curPage.value != idx) {
			if (idx == -1) {
				idx = curPage.value;
			}

			needToRefresh = false;
			if (props.onChange && !(await props.onChange?.(idx, pageSize.value))) {
				return;
			}
			curPage.value = idx;
			refresh();
			needToRefresh = true;
		}
	}

	function jumpPage() {
		let idx = parseInt(inputIdxValue.value);
		if (isNaN(idx)) {
			error("msg", { message: "输入页码有误!" });
		} else if (idx < 1 || idx > pageAmount.value) {
			error("msg", { message: "请输入合法的页码值" });
		} else if (idx == curPage.value) {
			info("msg", { message: "当前正在该页!" });
		} else {
			changePage(idx);
		}
	}
</script>

<template>
	<div
		v-show="pageAmount > 1"
		class="ui-pagination"
	>
		<UiButton
			v-if="startIdx != 1"
			@click="curPage != 1 && changePage(1)"
			>首页</UiButton
		>
		<UiButton
			:icon="'arrow-left'"
			@click="curPage > 1 && changePage(curPage - 1)"
		></UiButton>
		<div class="pager">
			<UiButton
				v-for="idx in new Array(endIdx - startIdx + 1).fill(0).map((value, index) => startIdx + index)"
				class="page-index"
				:key="idx"
				:class="{ current: idx == curPage }"
				@click="changePage(idx)"
				>{{ idx }}</UiButton
			>
		</div>
		<UiButton
			:icon="'arrow-right'"
			@click="curPage < pageAmount && changePage(curPage + 1)"
		></UiButton>
		<UiButton
			v-if="endIdx != pageAmount"
			@click="curPage != pageAmount && changePage(pageAmount)"
			>尾页</UiButton
		>
		<ElInput
			class="input-page"
			v-model="inputIdxValue"
			placeholder="输入页码"
			type="number"
		></ElInput>
		<UiButton
			id="jump"
			@click="jumpPage"
			>跳转</UiButton
		>
		<span>共 {{ dataCount }} 条</span>
		<ElSelect
			v-model="pageSize"
			@change="changePage(-1)"
		>
			<ElOption
				v-for="size in pageSizes"
				:value="size"
				:key="size"
				:label="`${size}条/每页`"
			></ElOption>
		</ElSelect>
	</div>
</template>

<style scoped lang="css">
	.ui-pagination {
		/* Layout */
		width: max-content;
		white-space: nowrap;

		/* Text */
		letter-spacing: -1em;
	}

	.ui-pagination > *:not(.pager) {
		/* Text */
		line-height: 2.5;
		vertical-align: top;
		margin-right: 5px;
	}

	.ui-pagination .ui-button {
		width: max-content;
		padding: 0 10px;
		letter-spacing: 0;
	}

	.ui-pagination .ui-button:not(#jump) {
		/* Layout */
		min-width: 3em;

		/* Appearance */
		border: 1px solid #cdcdcd;
		background-color: transparent;
		transition: background-color 0.5s ease-in-out, color 0.5s ease-in-out;
	}

	.ui-pagination .ui-button:not(#jump):hover,
	.ui-pagination .ui-button:not(#jump).current {
		/* Appearance */
		opacity: 1;
		color: white;
		background-color: var(--el-color-primary);
	}

	.ui-pagination .ui-button:not(#jump).current {
		cursor: not-allowed;
	}

	.ui-pagination .ui-button#jump {
		color: white;
	}

	.ui-pagination .pager {
		/* Layout */
		display: inline-block;
		white-space: nowrap;
	}

	.ui-pagination > .el-input {
		/* Layout */
		width: 8em;
	}

	.ui-pagination :deep(.el-input__inner) {
		/* Layout */
		text-align: center;
	}

	.ui-pagination > .el-select {
		/* Layout */
		width: 9em;
	}
</style>
