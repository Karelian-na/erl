<!-- @format -->

<script setup lang="ts">
	import StudentsImport from "./import.vue";
	import IndexTemplate from "views/common/IndexTemplate.vue";

	import { ref } from "vue";
	import { EmptyObject } from "common/utils";
	import type { PageInfoHandler, OperbarButtonClickHandler, OperColumnButtonClickHandler } from "views/common";

	const props = defineProps<{
		url: string;
		head: string;
	}>();

	const indexTemplateIns = ref<InstanceType<typeof IndexTemplate>>(EmptyObject);

	const onPageInfoAccepted: PageInfoHandler = function (operButtons, pageData) {
		if (operButtons.find((item) => item.type == "add")) {
			operButtons.push({
				icon: "import",
				type: "import",
				oper_type: 1,
				title: "导入",
			});
		}
	};

	const onOperbarButtonClick: OperbarButtonClickHandler = function (button, buttons) {
		switch (button.type) {
			case "import":
				const dialogProps = indexTemplateIns.value.dialogProps;
				dialogProps.show = true;
				dialogProps.mode = "import";
				dialogProps.operLabel = "导入";
				dialogProps.operAction = "add";
				return true;
			default:
				break;
		}
		return false;
	};
</script>

<template>
	<IndexTemplate
		ref="indexTemplateIns"
		v-bind="props"
		@page-info-accepted="onPageInfoAccepted"
		@operbar-button-click="onOperbarButtonClick"
	>
		<template #dialogContent="{ dialogProps, url }">
			<StudentsImport
				v-if="dialogProps.mode == 'import'"
				:parent-url="url"
			/>
		</template>
	</IndexTemplate>
</template>
