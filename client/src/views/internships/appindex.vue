<!-- @format -->

<script setup lang="ts">
	import type { OperColumnButtonClickHandler } from "views/common";

	import UiButton from "components/UiButton.vue";
	import IndexTemplate from "views/common/IndexTemplate.vue";

	import { ref } from "vue";
	import { axiosRequest, confirm, EmptyObject } from "common/utils";

	const props = defineProps<{
		url: string;
		head: string;
	}>();

	const indexTemplateIns = ref<InstanceType<typeof IndexTemplate>>(EmptyObject);

	const onOperColumnButtonClick: OperColumnButtonClickHandler = function (button, param, buttons) {
		switch (button.type) {
			case "apply":
				indexTemplateIns.value.dialogProps.show = false;
				confirm("确定要申请该实习岗位吗?", {
					callback: async (action, _ins) => {
						if (action != "confirm") return;

						axiosRequest(
							{
								url: `${indexTemplateIns.value.parentUrl}apply`,
								method: "POST",
								params: { id: param!.id },
							},
							(result) => {
								if (result.success) {
									param!.app_num = result.data;
								}
								return false;
							}
						);
					},
				});
				break;
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
		@oper-column-button-click="onOperColumnButtonClick"
	>
		<template #opers="{ data, buttons, clickHandler }">
			<template v-for="button in Object.values(buttons).filter((item) => item.type !== 'apply')">
				<UiButton
					v-if="data.app_num != data.max_app_num"
					:icon="button.icon"
					:class="button.type"
					@click="clickHandler(button, data, buttons)"
					>{{ button.title }}
				</UiButton>
			</template>
			<template v-if="buttons.apply">
				<UiButton
					v-if="data.app_num != data.max_app_num"
					:icon="buttons.apply.icon"
					:class="buttons.apply.type"
					@click="clickHandler(buttons.apply, data, buttons)"
					>{{ buttons.apply.title }}
				</UiButton>
			</template>
		</template>
	</IndexTemplate>
</template>
