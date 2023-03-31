<!-- @format -->

<script setup lang="ts">
	import IconFont from "components/IconFont.vue";
	import {
		ElFormItem,
		ElInput,
		ElSelect,
		ElOption,
		ElInputNumber,
		ElDatePicker,
		ElUpload,
		genFileId,
		ElRadioGroup,
		ElRadio,
		ElSwitch,
		ElTimeSelect,
		ElButton,
		FormItemInstance,
		ElCheckbox,
	} from "element-plus";

	import { EmptyObject, error } from "common/utils";
	import { computed, inject, onBeforeMount, ref, watch } from "vue";
	import type { UploadUserFile, UploadProps } from "element-plus";
	import type { FieldsConfig, IDialogProps, IFields, PreviewDialogProps } from ".";
	import { isArray } from "@vue/shared";

	const props = defineProps<{
		field: IFields[""];
		fieldConfig: FieldsConfig[""];
		bindProps?: FormItemInstance["$props"];
		modelValue?: string | number | boolean | UploadUserFile[];
	}>();

	const emits = defineEmits<{
		(e: "update:modelValue", value?: string | number | boolean | UploadUserFile[]): void;
	}>();

	const dialogProps = inject<IDialogProps | null>("dialogProps", null);
	const preview = inject<PreviewDialogProps>("preview")!;

	const disabled = computed(() => {
		if (dialogProps?.mode === "details") {
			return true;
		}

		if (props.field.editable) {
			return false;
		}

		if (dialogProps?.mode === "add") {
			return !props.fieldConfig.editableWhenAdd;
		}

		return true;
	});

	const tempImageSrc = ref<string>("");
	const fileList = ref<UploadUserFile[]>();
	const uploadIns = ref<InstanceType<typeof ElUpload>>(EmptyObject);

	const onUploadExceed: UploadProps["onExceed"] = function (files: File[], uploadFiles: UploadUserFile[]) {
		if (uploadIns.value.limit === 1) {
			uploadIns.value.clearFiles();
			const file = files[0] as unknown as UploadUserFile;
			file.uid = genFileId();
			uploadIns.value.handleStart(file as any);
		} else {
			error("msg", { message: "最多只能上传3个附件!" });
		}
	};

	const onUploadChange: UploadProps["onChange"] = function (uploadFile, uploadFiles) {
		if (props.fieldConfig.type == "file") {
			return;
		}

		if (uploadFile.status == "ready" && props.fieldConfig.type == "image") {
			if (tempImageSrc.value && tempImageSrc.value.startsWith("blob")) {
				URL.revokeObjectURL(tempImageSrc.value);
			}
			tempImageSrc.value = URL.createObjectURL(uploadFile.raw as File);
		}
	};

	watch(
		() => props.modelValue,
		() => emits("update:modelValue", props.modelValue)
	);

	watch(
		() => fileList.value,
		() => emits("update:modelValue", fileList.value)
	);

	onBeforeMount(() => {
		if (props.fieldConfig.type === "image" && typeof props.modelValue === "string") {
			tempImageSrc.value = props.modelValue;
		}

		if (disabled.value) {
			if (props.fieldConfig.bindProps?.placeholder) {
				delete props.fieldConfig.bindProps.placeholder;
			}
			return;
		}

		if (!props.fieldConfig.bindProps) {
			props.fieldConfig.bindProps = {};
		}
		const bindProps = props.fieldConfig.bindProps;
		switch (props.fieldConfig.type) {
			case "enum":
			case "date":
				if (!bindProps.placeholder) bindProps.placeholder = `请选择${props.field.display_name}`;
				break;
			case "text":
			case "number":
				if (!bindProps.placeholder) bindProps.placeholder = `请输入${props.field.display_name}`;
				break;
			case "checkbox":
				bindProps.label = props.field.display_name;
			default:
				break;
		}
	});

	function onImagePreview() {
		preview.src = tempImageSrc.value!;
		preview.show = true;
	}

	function onDeleteImage() {
		if (isArray(props.modelValue)) {
			(props.modelValue as Array<UploadUserFile>).splice(0);
		} else {
			emits("update:modelValue", []);
		}

		if (tempImageSrc.value!.startsWith("blob")) {
			URL.revokeObjectURL(tempImageSrc.value!);
		}
		tempImageSrc.value = "";
	}
</script>

<template>
	<ElFormItem
		class="edit-item"
		:prop="field.field_name"
		:label="fieldConfig.type === 'checkbox' ? '' : field.display_name + ':'"
		v-bind="bindProps"
	>
		<slot
			:disabled="disabled"
			:fieldConfig="fieldConfig"
		>
			<template v-if="disabled && !['image', 'file'].includes(fieldConfig.type)">
				<span v-if="['enum', 'radio', 'switch'].includes(fieldConfig.type)">
					{{ fieldConfig.enumItems?.find((item) => item.value == modelValue)?.label ?? modelValue ?? "暂未设置" }}
				</span>
				<span v-else-if="field.editable">{{ modelValue ?? "暂未设置" }}</span>
				<span v-else-if="modelValue">{{ modelValue }}</span>
			</template>
			<template v-else>
				<ElInput
					v-if="fieldConfig.type == 'text'"
					v-model="modelValue"
					:disabled="disabled"
					v-bind="fieldConfig.bindProps"
				>
					<template
						v-if="$slots.prepend"
						#prepend
					>
						<slot name="prepend"></slot>
					</template>
				</ElInput>
				<ElInputNumber
					v-else-if="fieldConfig.type == 'number'"
					v-model="modelValue"
					:disabled="disabled"
					v-bind="fieldConfig.bindProps"
				/>
				<ElCheckbox
					v-else-if="fieldConfig.type == 'checkbox'"
					v-model="(modelValue as boolean)"
					:disabled="disabled"
					v-bind="fieldConfig.bindProps"
				></ElCheckbox>
				<ElSelect
					v-else-if="fieldConfig.type == 'enum'"
					v-model="modelValue"
					:disabled="disabled"
					v-bind="fieldConfig.bindProps"
				>
					<template v-if="fieldConfig.enumItems && fieldConfig.enumItems.length">
						<ElOption
							v-for="item in fieldConfig.enumItems"
							:label="item.label"
							:value="item.value"
							:disabled="(item.disabled as any)"
						>
							<slot
								name="options"
								v-bind="item"
							></slot>
						</ElOption>
					</template>
				</ElSelect>
				<ElRadioGroup
					v-else-if="fieldConfig.type == 'radio'"
					v-model="(modelValue as any)"
					:disabled="disabled"
					v-bind="fieldConfig.bindProps"
				>
					<template v-if="fieldConfig.enumItems && fieldConfig.enumItems.length">
						<ElRadio
							v-for="item in fieldConfig.enumItems"
							:label="item.value"
							>{{ item.label }}
						</ElRadio>
					</template>
				</ElRadioGroup>
				<ElSwitch
					v-else-if="fieldConfig.type == 'switch'"
					v-model="modelValue"
					:disabled="disabled"
					v-bind="fieldConfig.bindProps"
				/>
				<ElDatePicker
					v-else-if="fieldConfig.type == 'date'"
					v-model="modelValue"
					:disabled="disabled"
					v-bind="fieldConfig.bindProps"
				/>
				<ElTimeSelect
					v-else-if="fieldConfig.type == 'time'"
					v-model="modelValue"
					:disabled="disabled"
					v-bind="fieldConfig.bindProps"
				/>
				<div
					v-else-if="fieldConfig.type == 'image' || fieldConfig.type == 'file'"
					class="upload-item"
					:class="fieldConfig.type"
				>
					<ElUpload
						ref="uploadIns"
						class="upload"
						v-model:file-list="fileList"
						:disabled="disabled"
						:on-change="onUploadChange"
						:on-exceed="onUploadExceed"
						v-bind="fieldConfig.bindProps"
						:show-file-list="!disabled && fieldConfig.bindProps?.showFileList"
					>
						<template v-if="fieldConfig.type == 'image'">
							<template v-if="tempImageSrc">
								<img :src="tempImageSrc" />
								<span
									class="actions"
									@click.stop="null"
								>
									<IconFont
										value="enlarge"
										@click="onImagePreview"
									/>
									<IconFont
										v-if="!disabled"
										value="delete"
										@click="onDeleteImage"
									/>
								</span>
							</template>
							<span v-else-if="disabled">暂无数据</span>
							<IconFont
								v-else
								value="add"
							/>
						</template>
						<template v-else>
							<template v-if="disabled">
								<span v-if="!modelValue">暂无</span>
								<template v-else>
									<a
										v-for="item in (modelValue as string).split(';')"
										target="__blank"
										:href="item"
										>{{ item.substring(item.lastIndexOf("/") + 1) }}
									</a>
								</template>
							</template>
							<ElButton
								v-else
								type="primary"
								>选择文件
							</ElButton>
						</template>
					</ElUpload>
				</div>
			</template>
		</slot>
		<slot name="extra"></slot>
	</ElFormItem>
</template>

<style scoped lang="css">
	.upload-item.image {
		height: 150px;
	}

	.upload-item .upload {
		height: 100%;
	}
	.upload-item.image :deep(.el-upload) {
		border: 1px dashed var(--el-border-color);
		border-radius: 6px;
		cursor: pointer;
		position: relative;
		overflow: hidden;
		width: 100%;
		height: 100%;
		transition: var(--el-transition-duration-fast);
	}
	.upload-item.image :deep(.el-upload:hover) {
		border-color: var(--el-color-primary);
	}
	.upload-item.file :deep(.el-upload) {
		justify-content: flex-start;
		flex-wrap: wrap;
	}

	.upload-item.file .el-button {
		height: 2.5em;
	}

	.upload-item.file span,
	.upload-item.file a {
		color: var(--el-color-primary);
		line-height: 2.5em;
		width: max-content !important;
	}

	.upload .icon-add {
		font-size: 25px;
		color: #8c939d;
		text-align: center;
	}

	.upload-item .actions {
		position: absolute;
		width: 100%;
		height: 100%;
		left: 0;
		top: 0;
		cursor: default;
		display: inline-flex;
		justify-content: space-evenly;
		align-items: center;
		color: #fff;
		opacity: 0;
		font-size: 20px;
		background-color: var(--el-overlay-color-lighter);
		transition: opacity var(--el-transition-duration);
	}
	.upload-item .actions:hover {
		opacity: 1;
	}

	.upload-item .actions .iconfont {
		cursor: pointer;
	}

	.upload-item img {
		width: 100%;
		height: 100%;
		position: absolute;
		top: 0;
		bottom: 0;
		margin: auto;
	}
</style>
