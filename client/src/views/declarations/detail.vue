<!-- @format -->
<script setup lang="ts">
	import type { FieldsConfig, IFields } from "views/common";

	import UiButton from "components/UiButton.vue";
	import EditItem from "views/common/EditItem.vue";
	import { ElCascader, ElCol, ElRow } from "element-plus";
	import EditTemplate from "views/common/EditTemplate.vue";

	import { ref, watch } from "vue";
	import { IAuthor, authorPropNames } from ".";
	import { EmptyObject, info } from "common/utils";
	import { paper } from "../FieldsConfigs/static.json";
	import { awardTypeFieldConfig } from "../FieldsConfigs/DeclarationFieldsConfig";

	const props = defineProps<{
		awardType: string;
		declarationType: string;
		templateProps: Partial<InstanceType<typeof EditTemplate>["$props"]>;
	}>();

	const awardTypeField: IFields[""] = {
		field_name: "award_type",
		display: false,
		display_name: "奖项类型",
		display_order: 2,
		editable: true,
		searchable: false,
	};

	const editTemplateIns = ref<InstanceType<typeof EditTemplate>>(EmptyObject);

	defineExpose({ editTemplateIns });

	const emits = defineEmits<{
		(e: "update:awardType", value: string): void;
	}>();

	watch(
		() => props.awardType,
		(value) => {
			emits("update:awardType", value);
		}
	);

	function onAddAuthor(authors: IAuthor[]) {
		const maxLength = Object.keys(paper.authorOrder).length - 1;
		if (authors.length == maxLength) {
			info("msg", { message: `最多只能添加${maxLength}个作者!` });
			return;
		}

		authors.push({
			order: paper.authorOrder[(authors.length + 1) as 1].value,
		} as any);
	}

	function onDeleteAuthor(order: number) {
		const authors = editTemplateIns.value.formData.authors as IAuthor[];
		for (let idx = order + 1; idx < authors.length; idx++) {
			authors[idx].order = idx;
		}
		authors.splice(order, 1);
	}

	function getOrderFieldConfig(order: number, config: FieldsConfig[""]) {
		const newConfig = Object.assign({}, config);
		const last = config.enumItems![config.enumItems!.length - 1];
		newConfig.enumItems = [config.enumItems![order], last];
		return newConfig;
	}
</script>

<template>
	<EditTemplate
		v-if="templateProps.fieldsConfig !== undefined"
		mode="add"
		title="申报"
		ref="editTemplateIns"
		v-bind="(props.templateProps as any)"
	>
		<template
			v-if="declarationType === 'Conference'"
			#location="{ fieldConfig, formData, disabled }"
		>
			<ElCascader
				v-model="formData.location"
				:disabled="disabled"
				v-bind="fieldConfig.bindProps"
			></ElCascader>
		</template>

		<template
			v-if="declarationType === 'Paper'"
			#authors="{ fieldConfig, formData, disabled }"
		>
			<UiButton
				v-if="!disabled"
				icon="add"
				@click="onAddAuthor(formData.authors)"
				>添加论文作者
			</UiButton>
			<ElRow
				v-for="(author, order) in formData.authors"
				class="author"
				:gutter="20"
			>
				<ElCol :span="22">
					<ElRow :gutter="10">
						<ElCol
							v-for="config, key in fieldConfig.children!"
							:span="config.layoutSpan"
						>
							<EditItem
								v-model="author[key]"
								:field="({
									field_name: `authors.${order}.${key}`,
									display_name: authorPropNames[key],
									editable: true,
								} as any)"
								:bind-props="{
									labelWidth: '0',
									label: '',
									rules: config.rule,
								}"
								:field-config="key === 'order' ? getOrderFieldConfig(order, config) : config"
							/>
						</ElCol>
					</ElRow>
				</ElCol>
				<ElCol
					v-if="!disabled && order != 0"
					:span="2"
				>
					<UiButton
						icon="delete"
						class="delete"
						@click="onDeleteAuthor(order)"
					/>
				</ElCol>
			</ElRow>
		</template>

		<template
			v-if="declarationType === 'Award'"
			#front-extra
		>
			<EditItem
				v-model="awardType"
				:field="awardTypeField"
				:field-config="awardTypeFieldConfig"
			/>
		</template>
	</EditTemplate>
</template>

<style scoped lang="css">
	.edit-template.add {
		padding: 0 30px;
	}

	.el-row.author:not(:last-child) {
		margin-bottom: 1.5em;
	}

	.edit-template.add .ui-button {
		padding: 0 1em;
	}
	.ui-button.add {
		color: white;
		width: max-content;
		background-color: var(--el-color-primary);
		margin-bottom: 1em;
	}

	.ui-button.delete {
		margin-left: 0.5em;
		height: 2.5em;
		vertical-align: baseline;
		background-color: white;
		border: var(--el-border);
		padding: 0 5px;
	}

	.ui-button :deep(.icon-delete) {
		margin-right: 0;
	}
</style>
