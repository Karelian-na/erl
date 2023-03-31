<!-- @format -->

<script setup lang="ts">
	import { ElUpload, ElButton } from "element-plus";

	import { ref } from "vue";
	import type { UploadUserFile } from "element-plus";

	const props = defineProps<{
		parentUrl: string;
	}>();

	const fileList = ref<UploadUserFile[]>([]);

	function onExceed(files: File[], uploadFiles: UploadUserFile[]) {
		
	}
</script>

<template>
	<ElUpload
		v-model:file-list="fileList"
		list-type="text"
		accept=".xls,.xlsx"
		:limit="1"
		:action="`${parentUrl}add`"
		:on-exceed="onExceed"
	>
		<ElButton type="primary"> 选择文件 </ElButton>
	</ElUpload>
</template>
