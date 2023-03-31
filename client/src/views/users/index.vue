<!-- @format -->

<script setup lang="ts">
	import type { ILoading } from "../main";
	import type { OperbarButtonClickHandler, PreparedCallback } from "views/common";

	import AssignRole from "./assign.vue";
	import { ElAvatar, ElRow } from "element-plus";
	import UiButton from "components/UiButton.vue";
	import Authorize from "../permissions/authorize.vue";
	import IndexTemplate from "views/common/IndexTemplate.vue";

	import { inject, ref } from "vue";
	import { axiosRequest, confirm, EmptyObject, error } from "common/utils";

	const props = defineProps<{
		url: string;
		head: string;
	}>();

	const pageLoading = inject<ILoading>("pageLoading")!;

	const userMode = props.url.startsWith("/Users");

	const indexTemplateIns = ref<InstanceType<typeof IndexTemplate>>(EmptyObject);

	const onOperbarButtonClick: OperbarButtonClickHandler = function (button) {
		switch (button.type) {
			case "reset": {
				const selectedRows = indexTemplateIns.value.getSelectedRows();
				if (selectedRows.length === 0) {
					error("msg", { message: "请先选择数据!" });
					break;
				}
				const ids = selectedRows.map((value) => value.id);
				confirm("确定要重置所选用户的密码吗? 重置后密码为: 123456", {
					callback: (action, _ins) => {
						if (action != "confirm") return;

						pageLoading.value = true;
						axiosRequest(
							{
								method: "PUT",
								url: `/Users/reset`,
								data: {
									ids: ids,
								},
							},
							() => (pageLoading.value = false)
						);
					},
				});
				break;
			}
			case "assign":
				const selectedRows = indexTemplateIns.value.getSelectedRows();
				if (selectedRows.length === 0) {
					error("msg", { message: "请先选择数据!" });
					break;
				}
				indexTemplateIns.value.dialogProps.data = selectedRows;
				return false;
			default:
				return false;
		}
		return true;
	};

	const onEditTemplatePrepared: PreparedCallback = function (formData, fields, fieldsConfig, mode) {
		if (!userMode) {
			if (mode === "edit" && formData.value.id <= 2) {
				fields.value.name.editable = false;
				fields.value.level.editable = false;
			} else if (mode !== "details") {
				fields.value.name.editable = true;
				fields.value.level.editable = true;
			}
		}
	};
</script>
<template>
	<IndexTemplate
		v-bind="props"
		ref="indexTemplateIns"
		@operbar-button-click="onOperbarButtonClick"
		@edit-template-prepared="onEditTemplatePrepared"
	>
		<template
			v-if="userMode"
			#name="[_field, data]"
		>
			<ElRow class="name">
				<ElAvatar
					:size="50"
					:src="data.avatar"
				/>
				<div class="msg">
					<p>{{ data.id }}</p>
					<p>{{ data.name }}</p>
				</div>
			</ElRow>
		</template>

		<template #dialogContent="{ dialogProps, fieldsConfig, url }">
			<Authorize
				v-if="dialogProps.mode == 'authorize'"
				:url="url"
				:raw-data="dialogProps.data!"
				:fields-config="fieldsConfig"
			/>
			<AssignRole
				v-if="dialogProps.mode == 'assign'"
				:url="url"
				:raw-data="dialogProps.data"
				:title="dialogProps.operLabel"
			/>
		</template>

		<template #opers="{ buttons, clickHandler, data }">
			<template v-if="userMode">
				<UiButton
					v-for="button in buttons"
					:icon="button.icon"
					@click="clickHandler(button, data, buttons)"
					>{{ button.title }}
				</UiButton>
			</template>
			<template v-else>
				<template v-if="data.id <= 4">
					<UiButton
						v-if="buttons.edit"
						:icon="buttons.edit.icon"
						@click="clickHandler(buttons.edit, data, buttons)"
						>{{ buttons.edit.title }}
					</UiButton>
					<UiButton
						v-if="buttons.authorize && data.id != 1"
						:icon="buttons.authorize.icon"
						@click="clickHandler(buttons.authorize, data, buttons)"
						>{{ buttons.authorize.title }}
					</UiButton>
				</template>
				<template v-else>
					<UiButton
						v-for="button in buttons"
						:icon="button.icon"
						@click="clickHandler(button, data, buttons)"
						>{{ button.title }}
					</UiButton>
				</template>
			</template>
		</template>
	</IndexTemplate>
</template>

<style scoped lang="css">
	.el-row.name {
		flex-wrap: nowrap;
		flex-shrink: 0;
		justify-content: center;
	}
	.el-row.name .el-avatar {
		flex-shrink: 0;
	}
	.el-row.name .msg {
		text-align: left;
		margin-left: 1em;
		height: 50px;
		display: flex;
		flex-direction: column;
		justify-content: center;
	}

	.index-template :deep(.edit-item .upload-item) {
		width: 100px;
		height: 100px;
	}
	.index-template :deep(.edit-item .el-upload) {
		border-radius: 50%;
	}
</style>
