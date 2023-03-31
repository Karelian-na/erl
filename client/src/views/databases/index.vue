<!-- @format -->

<script setup lang="ts">
	import type { ILoading } from "../main";
	import type { IFields, OperColumnButtonClickHandler } from "views/common";

	import TpManage from "./manage.vue";
	import IndexTemplate from "views/common/IndexTemplate.vue";

	import { inject, nextTick, ref } from "vue";
	import { axiosRequest, EmptyObject } from "common/utils";

	const props = defineProps<{
		url: string;
		head: string;
	}>();

	const pageLoading = inject<ILoading>("pageLoading")!;

	const fields = ref<IFields[""][]>();
	const indexTemplateIns = ref<InstanceType<typeof IndexTemplate>>(EmptyObject);

	const onOperColumnButtonClick: OperColumnButtonClickHandler = function (button, param, buttons) {
		switch (button.type) {
			case "edit":
				fields.value = undefined;
				pageLoading.value = true;
				indexTemplateIns.value.dialogProps.show = false;

				nextTick(async () => {
					const result = await axiosRequest({
						url: `${indexTemplateIns.value.parentUrl}edit`,
						method: "GET",
						params: {
							viewName: param!.view_name,
						},
					});
					pageLoading.value = false;
					if (result.success) {
						indexTemplateIns.value.dialogProps.show = true;
						const data = result.data as IFields[""][];
						const hidden = [] as typeof data;
						for (let idx = 0; idx < data.length; ) {
							const cur = data[idx];
							if (!cur.display) {
								hidden.push(data.splice(idx, 1)[0]);
							} else {
								++idx;
							}
						}
						fields.value = data.sort((l, r) => l.display_order - r.display_order).concat(hidden);
					}
				});
				break;
			case "details":
				fields.value = [];
				break;
			default:
				return false;
		}
		return true;
	};
</script>

<template>
	<IndexTemplate
		v-bind="props"
		no-pagination
		ref="indexTemplateIns"
		@oper-column-button-click="onOperColumnButtonClick"
	>
		<template #editContent="{ dialogProps, url, fieldsConfig }">
			<TpManage
				v-if="['edit', 'details'].includes(dialogProps.mode) && fields"
				:url="url"
				:fields="fields"
				:raw-data="dialogProps.data"
				:fields-config="fieldsConfig"
			/>
		</template>
	</IndexTemplate>
</template>

<style scoped lang="css">
	.index-template :deep(.el-dialog) {
		width: 1000px;
	}
</style>
