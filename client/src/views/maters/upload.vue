<!-- @format -->

<script setup lang="ts">
	import type { MaterUploadFile } from ".";
	import type { GetNavItemFunction } from "../main";
	import type { DataChangedCallback, IDialogProps, KeyStringObject } from "views/common";

	import IconFont from "components/IconFont.vue";
	import { ElButton, ElUpload, UploadInstance, ElCard, ElRadioGroup, ElRadio, ElProgress, genFileId, UploadProgressEvent } from "element-plus";

	import { inject, onBeforeMount, ref } from "vue";
	import { axiosRequest, confirm, EmptyObject, error, success } from "common/utils";

	const props = defineProps<{
		types: string[];
		parentUrl: string;
		fileNamePrefix: string;
		onUploadedFile?: DataChangedCallback;
	}>();

	const dialogProps = inject<IDialogProps>("dialogProps")!;
	const getNavItem = inject<GetNavItemFunction>("getNavItem")!;

	let exists = false;

	let accept = ref<string>();
	const uploadIns = ref<UploadInstance>(EmptyObject);
	const fileList = ref<(MaterUploadFile & { category: string })[]>([]);

	dialogProps.askIfNeedToLeave = () => {
		if (fileList.value.length != 0) {
			const left = fileList.value.filter((item) => item.status == "ready" || item.status == "uploading");

			if (left.length) {
				return () => {
					left.forEach((item) => {
						if (item.status == "uploading") {
							uploadIns.value.abort(item);
						}
					});
					uploadIns.value.clearFiles();
				};
			} else {
				return false;
			}
		}
		return false;
	};

	const onUpload: UploadInstance["httpRequest"] = async function (options) {
		let group = props.fileNamePrefix;
		if (!group) {
			group = getNavItem()!.parent.name;
		}

		const rawFile = options.file;
		const uploadFile = fileList.value[0];
		const extensions = rawFile.name.substring(rawFile.name.lastIndexOf("."));
		const fileName = group + uploadFile.category + extensions;

		const result = await axiosRequest({
			method: "GET",
			url: `${props.parentUrl}index`,
			params: {
				searchKey: fileName,
				one: true,
			},
		});

		if (!result.success) {
			return Promise.reject(new Error(result.msg));
		}

		const submitFunc = async () => {
			const formData = new FormData();
			formData.append("file", new Blob([rawFile]), fileName);

			const result = await axiosRequest({
				method: options.method,
				url: options.action,
				data: formData,
				onUploadProgress(e) {
					options.onProgress(
						Object.assign({
							...new ProgressEvent("progress", {
								total: e.total,
								cancelable: true,
							}),
							percent: e.progress! * 100,
						} as UploadProgressEvent)
					);
				},
			});

			if (result.success) {
				return result;
			} else {
				return Promise.reject(new Error(result.msg));
			}
		};

		if (result.data) {
			exists = true;
			try {
				const res = await confirm("服务器已存在今日上传的该文件! 是否覆盖此文件?");

				if (res === "confirm") {
					return (await submitFunc()).response;
				}
			} catch (error) {}
			return Promise.reject(new Error("用户已取消!"));
		}
		return (await submitFunc()).response;
	};

	const onChange: UploadInstance["onChange"] = function (file, files) {
		switch (file.status) {
			case "ready":
				(file as typeof fileList.value[0]).category = props.types[0];
				break;
			case "success":
				success("msg", { message: "上传成功!" });
				props.onUploadedFile?.(exists ? "edit" : "add", (file.response as any).data.data);
				break;
			default:
				break;
		}
	};

	const onExceed: UploadInstance["onExceed"] = function (files) {
		if (uploadIns.value.limit && uploadIns.value.limit > 1) {
			error("msg", { message: `最多只能上传${uploadIns.value.limit}个文件!` });
		}
		uploadIns.value.clearFiles();
		const file = files[0] as unknown as MaterUploadFile;
		file.uid = genFileId();
		uploadIns.value.handleStart(file as any);
	};

	const onError: UploadInstance["onError"] = function (err) {
		error("msg", { message: `上传失败! ${err.message}` });
	};

	const onRemoveFile: UploadInstance["onRemove"] = function (file) {
		uploadIns.value.handleRemove(file);
	};

	onBeforeMount(() => {
		const category = props.parentUrl.substring(props.parentUrl.lastIndexOf("/", props.parentUrl.length - 2) + 1, props.parentUrl.length - 1);
		switch (category) {
			case "Syllabus":
			case "Exam":
				accept.value = ".doc,.docx,.pdf";
				break;
			case "Handout":
				accept.value = ".ppt,.pptx,.pdf";
				break;
			default:
				break;
		}
	});

	function onSubmit() {
		if (fileList.value.length == 0) {
			error("msg", { message: "请先选择文件!" });
			return;
		}

		confirm("确定要上传吗?", {
			callback: (action, _ins) => {
				if (action != "confirm") return;

				uploadIns.value.submit();
			},
		});
	}
</script>

<template>
	<div class="upload">
		<ElUpload
			ref="uploadIns"
			method="POST"
			v-model:file-list="fileList"
			:limit="1"
			:drag="true"
			:accept="accept"
			:auto-upload="false"
			:show-file-list="false"
			:http-request="onUpload"
			:action="`${parentUrl}upload`"
			@error="onError"
			@exceed="onExceed"
			@change="onChange"
		>
			<div class="files">
				<ElCard
					class="item"
					@click.stop=""
					v-for="item in fileList"
				>
					<IconFont
						v-if="item.name.match(/doc(x)?$/)"
						value="word"
						type="svg"
					/>
					<IconFont
						v-else-if="item.name.match(/xls(x)?$/)"
						value="excel"
						type="svg"
					/>
					<IconFont
						v-else-if="item.name.match(/ppt(x)?$/)"
						value="ppt"
						type="svg"
					/>
					<IconFont
						v-else
						value="file"
						type="svg"
					/>
					<div class="info">
						<div class="base">
							<p class="name">{{ item.name }}</p>
							<p class="size">
								<template v-if="item.raw!.size < 1024">{{ item.raw!.size }}B</template>
								<template v-else-if="item.raw!.size < 1048576">{{ (item.raw!.size / 1024).toFixed(1) }}KB</template>
								<template v-else>{{ (item.raw!.size / 1048576).toFixed(1) }}MB</template>
							</p>
						</div>
						<ElRadioGroup
							v-model="item.category"
							class="type"
						>
							<ElRadio
								v-for="curType in types"
								:label="curType"
								>{{ curType }}</ElRadio
							>
						</ElRadioGroup>
						<div class="progress">
							<ElProgress
								v-if="item.status == 'uploading'"
								:percentage="item.percentage"
								:show-text="false"
							></ElProgress>
							<span
								v-else-if="item.status == 'success'"
								class="success"
								>上传成功</span
							>
							<span v-else-if="item.status == 'fail'">{{ item.response!.data.msg }}</span>
							<span v-else>待上传</span>
						</div>
					</div>
					<IconFont
						value="close"
						@click.stop.prevent="onRemoveFile(item, fileList)"
					/>
				</ElCard>
			</div>
			<div class="tips position-center">
				<IconFont value="upload"></IconFont>
				<div>拖拽文件至此处或 <em>点击选择文件</em></div>
			</div>
		</ElUpload>
		<div class="operations">
			<ElButton
				type="primary"
				@click="onSubmit"
				>立即上传
			</ElButton>
		</div>
	</div>
</template>

<style scoped>
	.upload :deep(.el-upload .el-upload-dragger) {
		padding: 10px;
		overflow: hidden;
	}

	.tips {
		position: absolute;
		height: max-content;
		font-weight: bold;
	}
	.icon-upload {
		font-size: 80px;
	}

	.files {
		display: flex;
		flex-direction: column;
		height: 300px;
		overflow: auto;
	}

	.item {
		flex-shrink: 0;
		border: none;
		font-size: 16px;
		line-height: 1em;
		z-index: 2;
	}

	.item :deep(.el-card__body) {
		position: relative;
		display: flex;
		padding: 10px;
		margin: 5px;
		border: 1px solid #cdcdcd;
		border-radius: 5px;
	}

	.item .icon {
		width: 3em;
		height: 3em;
		margin-right: 20px;
		flex-shrink: 0;
	}
	.item .info {
		flex-grow: 1;
		line-height: 1.5em;
		overflow: hidden;
		text-align: left;
	}
	.item .info .base {
		justify-content: space-between;
		display: flex;
		overflow: hidden;
	}
	.item .base .name {
		overflow: hidden;
		text-overflow: ellipsis;
		flex-grow: 1;
		white-space: nowrap;
	}
	.info .base .size {
		width: max-content;
		flex-shrink: 0;
		margin-left: 1em;
	}
	.info .type :deep(.el-radio) {
		font-size: 16px;
		height: 1.5em;
	}

	.info .progress {
		float: right;
	}
	.info .progress :deep(.el-progress) {
		width: 10em;
		display: inline-flex;
		height: 24px;
	}
	.info .progress :deep(.success) {
		color: var(--el-color-success);
	}
	.info .progress :deep(.success) {
		color: var(--el-color-error);
	}

	.item :deep(.icon-close) {
		position: absolute;
		right: 0;
		top: 0;
		border-radius: 50%;
		transform: translate(50%, -50%);
	}
	.item :deep(.icon-close:hover) {
		opacity: 0.7;
	}

	.operations {
		padding-top: 20px;
	}
</style>
