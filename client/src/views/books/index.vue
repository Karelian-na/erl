<!-- @format -->

<script setup lang="ts">
	import type { ILoading } from "../main";
	import type { BeforeSubmitHandler } from "views/common";

	import { ElImage, UploadUserFile } from "element-plus";
	import EditTemplate from "views/common/EditTemplate.vue";
	import IndexTemplate from "views/common/IndexTemplate.vue";

	import { inject, ref } from "vue";
	import { axiosRequest, EmptyObject } from "common/utils";

	const props = defineProps<{
		url: string;
		head: string;
	}>();

	const mainLoading = inject<ILoading>("mainLoading")!;

	let lastCoverUid = 0;
	let lastCoverPath = "";

	const editTemplateIns = ref<InstanceType<typeof EditTemplate>>(EmptyObject);

	const onBeforeSubmit: BeforeSubmitHandler = async function (postData) {
		if (postData.cover) {
			if (postData.cover.length == 0) {
				postData.cover = "";
				return true;
			}
			const file = postData.cover[0] as UploadUserFile;
			if (lastCoverUid != file.uid) {
				const formData = new FormData();
				formData.append("files", new Blob([file.raw as File]), file.name);

				const result = await axiosRequest({
					url: "/upload",
					method: "POST",
					data: formData,
					onUploadProgress: (e) => {
						const percentage = (e.progress! * 100).toPrecision(3);
						mainLoading.tip = "上传封面中..." + percentage + "%";
					},
				});

				mainLoading.tip = "处理中...";

				if (result.success) {
					lastCoverPath = result.data;
				} else {
					postData = {};
				}
				lastCoverUid = file.uid!;
			}
			postData.cover = lastCoverPath;
		}

		return true;
	};
</script>

<template>
	<IndexTemplate
		no-selection-column
		v-bind="props"
	>
		<template #cover="[_field, data]">
			<ElImage
				v-if="data.cover"
				fit="cover"
				style="width: 100px; height: 100px"
				:src="data.cover"
			></ElImage>
			<span v-else>暂无</span>
		</template>

		<template #editContent="{ dialogProps, fieldsConfig, url }">
			<EditTemplate
				v-if="['edit', 'add', 'details'].includes(dialogProps.mode)"
				ref="editTemplateIns"
				:url="url"
				:mode="dialogProps.mode"
				:raw-data="dialogProps.data"
				:fields-config="fieldsConfig"
				:title="dialogProps.operLabel"
				@before-submit="onBeforeSubmit"
			></EditTemplate>
		</template>
	</IndexTemplate>
</template>
