<!-- @format -->

<script setup lang="ts">
	import type { IFields, IOperButton, OperbarButtonClickHandler } from ".";

	import UiButton from "components/UiButton.vue";
	import { ElForm, ElFormItem, ElInput, ElSelect, ElOption } from "element-plus";

	import { ref } from "vue";

	defineProps<{
		searchableFields: IFields;
		operButtons: Record<string, IOperButton>;
		onSearch?: () => void;
		onOperbarButtonClick?: OperbarButtonClickHandler;
	}>();

	const searchKey = ref("");
	const searchField = ref("");

	defineExpose({
		searchKey,
		searchField,
	});
</script>

<template>
	<div class="operationbar">
		<ElForm :inline="true">
			<ElFormItem label="查询内容:">
				<ElInput
					v-model="searchKey"
					placeholder="输入查询内容"
				>
				</ElInput>
			</ElFormItem>
			<ElFormItem label="查询字段:">
				<ElSelect
					v-model="searchField"
					placeholder="选择查询字段"
				>
					<ElOption
						value=""
						label="选择查询字段"
						>[选择查询字段]</ElOption
					>
					<ElOption
						v-for="field in Object.entries(searchableFields)"
						:key="field[0]"
						:value="field[0]"
						:label="field[1].display_name"
					></ElOption>
				</ElSelect>
			</ElFormItem>
			<ElFormItem v-for="button in operButtons">
				<UiButton
					:class="button.type"
					:icon="button.icon"
					@click="onOperbarButtonClick!(button, operButtons)"
					>{{ button.title }}</UiButton
				>
			</ElFormItem>
		</ElForm>
	</div>
</template>

<style scoped lang="css">
	.operationbar .el-form .el-form-item {
		margin-right: 0.5em;
		margin-bottom: 1em;
	}

	.operationbar .ui-button {
		color: white;
		padding: 0 10px;
		letter-spacing: 0;
		margin: 0;
	}
</style>
