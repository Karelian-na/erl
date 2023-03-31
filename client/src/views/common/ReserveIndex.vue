<!-- @format -->

<script setup lang="ts">
	import type { DataChangedCallback, OperColumnButtonClickHandler, PreparedCallback } from ".";

	import EditTemplate from "./EditTemplate.vue";
	import IndexTemplate from "./IndexTemplate.vue";

	import { computed, ref } from "vue";
	import { EmptyObject } from "common/utils";

	const props = defineProps<{
		url: string;
		head: string;
	}>();

	const indexTemplateIns = ref<InstanceType<typeof IndexTemplate>>(EmptyObject);

	const onOperColumnButtonClick: OperColumnButtonClickHandler = function (button, param, buttons) {
		const dialogProps = indexTemplateIns.value.dialogProps;
		switch (button.type) {
			case "details":
				dialogProps.operLabel = buttons.reserve.title;
				dialogProps.mode = "reserve";
			case "reserve":
				dialogProps.operAction = "add";
				break;
			default:
				return false;
		}
		return true;
	};

	const onDataChanged: DataChangedCallback = function (action, data, all) {
		switch (action) {
			case "reserve":
				break;
			default:
				return false;
		}
		return true;
	};

	const onTemplatePrepared: PreparedCallback = function (formData, fields, fieldsConfig) {
		if (props.url.startsWith("/Laboratories/")) {
			fieldsConfig.start_time.bindProps.maxTime = computed(() => formData.value.end_time);
			fieldsConfig.end_time.bindProps.minTime = computed(() => formData.value.start_time);
		}
	};
</script>

<template>
	<IndexTemplate
		ref="indexTemplateIns"
		v-bind="props"
		@data-changed="onDataChanged"
		@oper-column-button-click="onOperColumnButtonClick"
	>
		<!-- forward slots -->
		<template
			v-for="key in Object.keys($slots)"
			#[key]="scope"
		>
			<slot
				:name="key"
				v-bind="scope"
			/>
		</template>
		<template #editContent="{ dialogProps, fieldsConfig, url }">
			<EditTemplate
				v-if="dialogProps.mode == 'reserve'"
				:url="`${url}reserve`"
				:mode="dialogProps.mode"
				:raw-data="dialogProps.data!"
				:fields-config="fieldsConfig"
				:title="dialogProps.operLabel"
				@prepared="onTemplatePrepared"
			></EditTemplate>
		</template>
	</IndexTemplate>
</template>
