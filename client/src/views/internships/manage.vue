<!-- @format -->

<script setup lang="ts">
	import type {
		CollectingPostDataHandler,
		DataChangedCallback,
		FieldsConfig,
		IFields,
		KeyStringObject,
		OperColumnButtonClickHandler,
		PageInfoHandler,
		PreparedCallback,
	} from "views/common";

	import UiButton from "components/UiButton.vue";
	import { ElButton, ElMessageBox } from "element-plus";
	import EditTemplate from "views/common/EditTemplate.vue";
	import IndexTemplate from "views/common/IndexTemplate.vue";

	import { onBeforeUnmount, ref } from "vue";
	import { axiosRequest, EmptyObject } from "common/utils";
	import { internship, application } from "../FieldsConfigs/static.json";

	const props = defineProps<{
		url: string;
		rawData: KeyStringObject;
		fieldsConfig: FieldsConfig;
		onUpdatedData?: DataChangedCallback;
	}>();

	const manageUrl = `${props.url}manage?iid=${props.rawData.id}`;
	const editTemplateIns = ref<InstanceType<typeof EditTemplate>>(EmptyObject);
	const indexTemplateIns = ref<InstanceType<typeof IndexTemplate>>(EmptyObject);

	const onPageInfoAccepted: PageInfoHandler = function (operbuttons, pagedata) {
		operbuttons[0].title = "撤销"!;
	};

	const onTemplatePrepared: PreparedCallback = function (formData, fields, fieldsConfig) {
		if (!fields.value.app_users) {
			fields.value.app_users = {
				display_name: "申请人",
				field_name: "app_users",
			} as Partial<IFields[""]> as IFields[""];
		}
	};

	const onOperColumnButtonClick: OperColumnButtonClickHandler = function (button, param, buttons) {
		switch (button.type) {
			case "delete":
				indexTemplateIns.value.dialogProps.show = false;
				ElMessageBox.prompt("输入撤销原因", "申请管理-撤销", {
					inputType: "textarea",
					callback: async (res: any, _ins) => {
						if (res.action != "confirm") return;

						await axiosRequest(
							{
								method: "PUT",
								url: manageUrl,
								data: {
									appStatus: application.status[2].value,
									ids: [param!.id],
									reason: res.value,
								},
							},
							(result) => {
								if (result.success) {
									Object.assign(param!, result.data);
								}
								return false;
							}
						);
					},
				});

			default:
				break;
		}
		return true;
	};

	const onCollectingPostData: CollectingPostDataHandler = function (postData) {
		return `iid=${props.rawData.id}`;
	};

	async function onSubmit(status: number) {
		editTemplateIns.value.formData.status = status;
		editTemplateIns.value.submit();
	}
</script>

<template>
	<EditTemplate
		title="修改"
		mode="manage"
		v-bind="props"
		oper-action="edit"
		ref="editTemplateIns"
		@prepared="onTemplatePrepared"
		@collecting-post-data="onCollectingPostData"
	>
		<template #app_users>
			<IndexTemplate
				head="申请管理"
				:url="manageUrl"
				ref="indexTemplateIns"
				@page-info-accepted="onPageInfoAccepted"
				@oper-column-button-click="onOperColumnButtonClick"
			>
				<template #opers="{ buttons, clickHandler, data }">
					<UiButton
						v-if="props.rawData.status === 1 && data.app_status === 1"
						:icon="buttons.delete.icon"
						@click="clickHandler(buttons.delete, data, buttons)"
						>{{ buttons.delete.title }}
					</UiButton>
				</template>
			</IndexTemplate>
		</template>
		<template #operations>
			<template v-if="props.rawData.status === 1">
				<ElButton
					type="primary"
					@click="onSubmit(internship.status[1].value)"
					>开始该实习
				</ElButton>
			</template>
			<ElButton
				type="danger"
				@click="onSubmit(internship.status[2].value)"
				>取消该实习
			</ElButton>
		</template>
	</EditTemplate>
</template>

<style scoped lang="css">
	.index-template :deep(.data) {
		height: 400px;
	}

	.edit-template :deep(> .el-form > .el-form-item:first-child) {
		margin-bottom: 0;
	}

	.edit-template :deep(.el-form-item) {
		margin-bottom: 18px;
	}
</style>
