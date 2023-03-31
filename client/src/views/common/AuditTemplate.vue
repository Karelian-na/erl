<!-- @format -->

<script setup lang="ts">
	import type {
		CollectingPostDataHandler,
		DataChangedCallback,
		FieldsConfig,
		IDialogProps,
		IFields,
		KeyStringObject,
		PreparedCallback,
		UpdatingFormDataHandler,
	} from "views/common";

	import UiTag from "components/UiTag.vue";
	import EditTemplate from "views/common/EditTemplate.vue";

	import { inject } from "vue";
	import { isArray } from "@vue/shared";
	import { AuditStatus } from "common/utils";

	const props = defineProps<{
		url: string;
		head: string;
		fields?: IFields;
		rawData: KeyStringObject; // maybe array, multiMode audit
		fieldsConfig: FieldsConfig;
		onTemplatePrepared?: PreparedCallback;
		onUpdatingFormData?: UpdatingFormDataHandler;
		onCollectingPostData?: CollectingPostDataHandler;
	}>();

	const dialogProps = inject<IDialogProps>("dialogProps")!;

	const detailMode = dialogProps.mode == "details";
	const multiMode = isArray(props.rawData);

	const onTemplatePrepared: PreparedCallback = function (formData, fields, fieldsConfig) {
		if (multiMode) {
			Object.keys(fieldsConfig).forEach((item) => {
				fieldsConfig[item].show = () => false;
			});
			delete fieldsConfig.audit_status.show;
		} else if (fieldsConfig.comment.show) {
			Object.keys(fieldsConfig).forEach((item) => delete fieldsConfig[item].show);
		}

		["audit_time", "audit_user", "comment"].forEach((item) => {
			fieldsConfig[item].show = (fieldName) => {
				if (detailMode) {
					if (props.rawData.audit_status) {
						return true;
					}
				} else if (fieldName == "comment") {
					return true;
				}
				return false;
			};
		});

		props.onTemplatePrepared?.(formData, fields, fieldsConfig);
	};

	const onCollectingPostData: CollectingPostDataHandler = function (postData) {
		if (!Object.keys(postData.value).length) {
			return;
		}
		postData.value.ids = multiMode ? props.rawData.map((item) => item.id) : [props.rawData.id];
		return props.onCollectingPostData?.(postData);
	};

	function onBeforeLeave(formData: KeyStringObject) {
		if (detailMode) {
			return false;
		}

		if (formData.comment || formData.audit_status) {
			return true;
		}
		return false;
	}
</script>

<template>
	<EditTemplate
		v-bind="props"
		oper-action="edit"
		:mode="dialogProps.mode"
		:title="dialogProps.operLabel"
		@before-leave="onBeforeLeave"
		@prepared="onTemplatePrepared"
		@collecting-post-data="onCollectingPostData"
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

		<template
			v-if="detailMode"
			#audit_status="{ formData }"
		>
			<UiTag
				:label="AuditStatus[formData.audit_status].label"
				:class="AuditStatus[formData.audit_status].class"
			/>
		</template>
	</EditTemplate>
</template>

<style scoped>
	.ui-tag.passed,
	.ui-tag.waiting,
	.ui-tag.failed {
		width: max-content;
		padding: 0 1em;
		line-height: 2.5em;
	}
</style>
