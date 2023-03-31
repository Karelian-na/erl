<!-- @format -->

<script setup lang="ts">
	import type { OperColumnButtonClickHandler } from "views/common";

	import AppManage from "./manage.vue";
	import UiButton from "components/UiButton.vue";
	import IndexTemplate from "views/common/IndexTemplate.vue";

	import { ref } from "vue";
	import { EmptyObject } from "common/utils";
	import { internship } from "../FieldsConfigs/static.json";
	import { intershipManageFieldsConfig } from "../FieldsConfigs/InternshipFieldsConfig";

	const props = defineProps<{
		url: string;
		head: string;
	}>();

	const indexTemplateIns = ref<InstanceType<typeof IndexTemplate>>(EmptyObject);

	const onOperColumnButtonClick: OperColumnButtonClickHandler = function (button, param, buttons) {
		switch (button.type) {
			case "manage":
				indexTemplateIns.value.dialogProps.operLabel = "申请管理";
				return false;
			default:
				return false;
		}
	};
</script>

<template>
	<IndexTemplate
		v-bind="props"
		ref="indexTemplateIns"
		@oper-column-button-click="onOperColumnButtonClick"
	>
		<template #dialogContent="{ dialogProps, url, onUpdatedData }">
			<AppManage
				v-if="['manage'].includes(dialogProps.mode)"
				:url="url"
				:raw-data="dialogProps.data"
				:fields-config="intershipManageFieldsConfig"
				@updated-data="onUpdatedData"
			/>
		</template>

		<template #opers="{ buttons, clickHandler, data }">
			<template v-if="data.status === internship.status[0].value">
				<UiButton
					v-if="buttons.delete"
					:icon="buttons.delete.icon"
					@click="clickHandler(buttons.delete, data, buttons)"
					>{{ buttons.delete.title }}
				</UiButton>
				<UiButton
					v-if="buttons.edit"
					:icon="buttons.edit.icon"
					@click="clickHandler(buttons.edit, data, buttons)"
					>{{ buttons.edit.title }}
				</UiButton>
			</template>
			<UiButton
				v-if="buttons.manage"
				:icon="buttons.manage.icon"
				@click="clickHandler(buttons.manage, data, buttons)"
				>{{ buttons.manage.title }}
			</UiButton>
		</template>
	</IndexTemplate>
</template>

<style scoped lang="css">
	.index-template :deep(.el-dialog) {
		width: 1050px;
	}
</style>
