<!-- @format -->

<script setup lang="ts">
	import type { Result } from "common/Result";
	import type { DataChangedCallback, KeyStringObject, OperbarButtonClickHandler, OperColumnButtonClickHandler } from "views/common";

	import MatersUpload from "./upload.vue";
	import IndexTemplate from "views/common/IndexTemplate.vue";

	import { toUint8Array } from "js-base64";
	import { onBeforeMount, ref } from "vue";
	import { axiosRequest, EmptyObject, error } from "common/utils";

	const props = defineProps<{
		url: string;
		head: string;
	}>();

	const types: string[] = [];
	const parentUrl = props.url.substring(0, props.url.lastIndexOf("/") + 1);

	const fileNamePrefix = ref("");
	const indexTemplate = ref<InstanceType<typeof IndexTemplate>>(EmptyObject);

	onBeforeMount(() => {
		switch (props.head) {
			case "教学大纲及教学进程表":
				types.push("教学大纲", "教学进程表");
				break;
			case "毕业论文及实习报告":
				types.push("毕业论文材料", "毕业实习报告");
				break;
			case "教学安排":
				fileNamePrefix.value = "本科";
			default:
				types.push(props.head);
				break;
		}
	});

	const onOperColumnButtonClick: OperColumnButtonClickHandler = function (button, param) {
		const dialogProps = indexTemplate.value.dialogProps;
		switch (button.type) {
			case "download": {
				dialogProps.show = false;
				axiosRequest(
					{
						method: "GET",
						url: `${parentUrl}download`,
						params: {
							ids: param!.id,
						},
					},
					(result) => {
						if (result.success) {
							param!.download_times += 1;
							downloadFile(result);
							return true;
						}
						return false;
					}
				);
				break;
			}
			case "details":
				dialogProps.show = false;
				break;
			default:
				return false;
		}
		return true;
	};

	const onOperbarButtonClick: OperbarButtonClickHandler = function (button) {
		switch (button.type) {
			case "download": {
				const selectedRows = indexTemplate.value.getSelectedRows();
				if (!selectedRows.length) {
					error("msg", { message: "请先选择数据!" });
					break;
				} else if (selectedRows.length > 10) {
					error("msg", { message: "最多只能下载10个文件!" });
					break;
				}

				axiosRequest(
					{
						method: "GET",
						url: `${parentUrl}download`,
						params: {
							ids: selectedRows.map((val) => val.id).join(","),
						},
					},
					(result) => {
						if (result.success) {
							selectedRows.forEach((val) => {
								val!.download_times += 1;
							});
							downloadFile(result);
							return true;
						}
						return false;
					}
				);
				break;
			}
			default:
				return false;
		}
		return true;
	};

	const onDataChanged: DataChangedCallback = function (action, data, all) {
		switch (action) {
			case "edit":
				indexTemplate.value.dialogProps.data = all!.find((item) => item.id === data.id) ?? {};
				return false;
			default:
				return false;
		}
	};

	function downloadFile(response: Result) {
		const buffer = toUint8Array(response.data.content);

		const blob = new Blob([buffer], { type: "application/octet-stream" });
		const url = window.URL.createObjectURL(blob);
		const addrElement = document.createElement("a");
		addrElement.download = response.data.name;
		addrElement.href = url;
		addrElement.click();
		window.URL.revokeObjectURL(url);
	}

	function getFileName(data: KeyStringObject) {
		const fileNameInfos = (data.file_name as string).split("-");
		return fileNameInfos[1].concat(".", fileNameInfos[2].split(".")[1]);
	}
</script>

<template>
	<IndexTemplate
		ref="indexTemplate"
		:url="url"
		:head="head"
		@data-changed="onDataChanged"
		@operbar-button-click="onOperbarButtonClick"
		@oper-column-button-click="onOperColumnButtonClick"
	>
		<template #file_name="[_field, data]"> {{ getFileName(data) }} </template>
		<template #file_size="[_field, data]">
			<template v-if="data.file_size < 1024">{{ data.file_size }}B</template>
			<template v-else-if="data.file_size < 1048576">{{ (data.file_size / 1024).toFixed(1) }}KB</template>
			<template v-else>{{ (data.file_size / 1048576).toFixed(1) }}MB</template>
		</template>
		<template #dialogContent="{ dialogProps, onUpdatedData }">
			<MatersUpload
				v-if="dialogProps.mode == 'upload'"
				:types="types"
				:parent-url="parentUrl"
				:file-name-prefix="fileNamePrefix"
				@uploaded-file="onUpdatedData"
			></MatersUpload>
		</template>
	</IndexTemplate>
</template>
