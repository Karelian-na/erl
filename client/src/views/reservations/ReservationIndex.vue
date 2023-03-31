<!-- @format -->

<script setup lang="ts">
	import type { OperbarButtonClickHandler, OperColumnButtonClickHandler, PageInfoHandler } from "views/common";

	import UiTag from "components/UiTag.vue";
	import UiButton from "components/UiButton.vue";
	import AuditTemplate from "views/common/AuditTemplate.vue";
	import IndexTemplate from "views/common/IndexTemplate.vue";

	import { computed, provide, ref } from "vue";
	import { EmptyObject, error, AuditStatus } from "common/utils";

	const props = defineProps<{
		url: string;
		head: string;
		onOperColumnButtonClick?: OperColumnButtonClickHandler;
	}>();

	const selfMode = computed(() => props.url.endsWith("self/index"));
	const indexTemplateIns = ref<InstanceType<typeof IndexTemplate>>(EmptyObject);

	provide("selfMode", selfMode);

	defineExpose({ indexTemplateIns });

	const onPageInfoAccepted: PageInfoHandler = function (operButtons, _pageData) {
		if (selfMode.value) {
			operButtons.push({
				icon: "delete",
				oper_type: 2,
				type: "delete",
				title: "删除",
			});
		}
	};

	const onOperbarButtonClick: OperbarButtonClickHandler = function (button) {
		const dialogProps = indexTemplateIns.value.dialogProps;
		switch (button.type) {
			case "audit":
				const selectedRows = indexTemplateIns.value.getSelectedRows();
				if (!selectedRows.length) {
					error("msg", { message: "请先选择数据!" });
					break;
				}

				if (selectedRows.some((item) => item.audit_status != 0)) {
					error("msg", { message: "请选择未审核的数据!" });
					break;
				}
				dialogProps.data = selectedRows.length == 1 ? selectedRows[0] : selectedRows;
				return false;
			default:
				return false;
		}
		return true;
	};
</script>

<template>
	<IndexTemplate
		ref="indexTemplateIns"
		v-bind="props"
		@page-info-accepted="onPageInfoAccepted"
		@operbar-button-click="onOperbarButtonClick"
	>
		<template
			#[key]="scope"
			v-for="key in Object.keys($slots)"
		>
			<slot
				:name="key"
				v-bind="scope"
			/>
		</template>

		<template #audit_status="[_field, data]">
			<UiTag
				:label="AuditStatus[data.audit_status].label"
				:class="AuditStatus[data.audit_status].class"
			/>
		</template>

		<template
			v-if="!selfMode"
			#opers="{ data, clickHandler, buttons }"
		>
			<template v-if="buttons.audit">
				<UiButton
					v-if="data.audit_status == 0"
					:icon="buttons.audit.icon"
					:class="buttons.audit.type"
					@click="clickHandler(buttons.audit, data, buttons)"
					>{{ buttons.audit.title }}
				</UiButton>
			</template>
			<template v-if="buttons.delete">
				<UiButton
					:icon="buttons.delete.icon"
					@click="clickHandler(buttons.delete, data, buttons)"
					>{{ buttons.delete.title }}
				</UiButton>
			</template>
		</template>

		<template #editContent="scope">
			<slot
				v-if="$slots.editContent"
				name="editContent"
				v-bind="scope"
			/>
			<template v-else>
				<AuditTemplate
					v-if="['audit', 'details'].includes(scope.dialogProps.mode)"
					:head="head"
					:url="scope.url"
					:raw-data="scope.dialogProps.data!"
					:fields-config="scope.fieldsConfig"
				/>
			</template>
		</template>
	</IndexTemplate>
</template>

<style lang="css" scoped>
	.ui-tag.passed {
		background-color: var(--el-color-primary);
	}

	.ui-tag.waiting {
		background-color: orangered;
	}

	.ui-tag.failed {
		background-color: crimson;
	}
</style>
