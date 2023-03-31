<!-- @format -->

<script setup lang="ts">
	import type { ILoading } from "../main";
	import type { FormRules } from "element-plus";
	import {
		IDialogProps,
		KeyStringObject,
		BeforeLeaveCallback,
		IFields,
		UpdatingFormDataHandler,
		CollectingPostDataHandler,
		DataOperAction,
		FieldsConfig,
		DataChangedCallback,
		PreparedCallback,
		BeforeSubmitHandler,
		CollectEnd,
	} from ".";

	import EditItem from "./EditItem.vue";
	import { FormInstance, ElForm, ElButton, ElRow, ElCol, ElDialog, ElFormItem } from "element-plus";

	import { axiosRequest, confirm, EmptyObject, getChangedAttributes, info, success } from "common/utils";
	import { computed, inject, onBeforeMount, provide, reactive, Ref, ref, toRaw, watch } from "vue";

	const props = defineProps<{
		url: string;
		mode: string;
		title: string;
		rawData: KeyStringObject;
		fieldsConfig: FieldsConfig;
		operAction?: DataOperAction;

		fields?: IFields;

		onPrepared?: PreparedCallback;
		onUpdatedData?: DataChangedCallback;
		onBeforeLeave?: BeforeLeaveCallback;
		onBeforeSubmit?: BeforeSubmitHandler;
		onUpdatingFormData?: UpdatingFormDataHandler;
		onCollectingPostData?: CollectingPostDataHandler;
	}>();

	const mainLoading = inject<ILoading>("mainLoading")!;
	const injectFields = inject<Ref<IFields> | null>("fields", null);
	const dialogProps = inject<IDialogProps | null>("dialogProps", null);
	const injectOnUpdatedData = inject<DataChangedCallback | undefined>("onUpdatedData", undefined);

	const rules = ref<FormRules>();
	const initFormData: KeyStringObject = {};

	const labelWidth = ref(4);
	const layouts = ref<Array<string[]>>([]);
	const formData = ref<KeyStringObject>({});
	const formIns = ref<FormInstance>(EmptyObject);
	const preview = reactive({ show: false, src: "" });
	const fields = computed(() => props.fields ?? injectFields?.value ?? {});

	provide("preview", preview);

	onBeforeMount(() => {
		if (dialogProps) {
			dialogProps.askIfNeedToLeave = () => {
				if (dialogProps.mode === "details") {
					return false;
				}

				const func = () => {
					const diff = getChangedAttributes(initFormData, formData.value);
					if (diff) {
						return true;
					}
					return false;
				};
				if (props.onBeforeLeave) {
					return props.onBeforeLeave(formData.value, func);
				}
				return func();
			};
		}
		updateFormData();
		props.onPrepared?.(formData, fields, props.fieldsConfig, dialogProps?.mode);
		if (dialogProps?.mode !== "details") {
			rules.value = Object.entries(props.fieldsConfig).reduce((prev, cur, idx) => {
				if (cur[1].rule) {
					prev[cur[0]] = cur[1].rule;
				}
				return prev;
			}, {} as FormRules);
		}
		layoutFields();
		window.templateFormData = formData;
	});

	defineExpose({ updateFormData, formData, submit: onSubmit, formIns });
	watch(() => props.rawData, updateFormData.bind(null, undefined));
	watch(() => fields.value, layoutFields);

	function layoutFields() {
		if (!Object.keys(fields.value).length) {
			return;
		}

		let totalSpan = 0;
		let layout: string[] = [];
		layouts.value = new Array(layout);

		Object.entries(props.fieldsConfig).forEach(([name, info]) => {
			if (!fields.value[name] || (info.show && !info.show(name))) {
				return;
			}
			const field = fields.value[name];

			if (
				(dialogProps?.mode == "edit" && !field.editable) ||
				(dialogProps?.mode == "add" && ["add_time", "add_user"].includes(field.field_name))
			) {
				return;
			}

			if (field.display_name.length > labelWidth.value) {
				labelWidth.value = field.display_name.length;
			}

			if (totalSpan + info.layoutSpan > 24) {
				totalSpan = info.layoutSpan;
				layout = [field.field_name];
				layouts.value.push(layout);
			} else {
				totalSpan += info.layoutSpan;
				layout.push(field.field_name);
			}
		});
	}

	async function updateFormData(attrs?: KeyStringObject) {
		if (attrs) {
			Object.assign(formData.value, attrs);
		} else {
			if (Object.keys(props.rawData).length) {
				Object.assign(formData.value, props.rawData);
			}

			if (props.onUpdatingFormData) {
				await props.onUpdatingFormData(formData);
			}

			Object.assign(initFormData, JSON.parse(JSON.stringify(toRaw(formData.value))));
		}
	}

	function onSubmit() {
		if (dialogProps?.mode === "details") {
			return;
		}

		formIns.value!.validate(async (state, fields) => {
			if (!state) return;

			let reqUrl = props.url.endsWith("/") ? `${props.url}${props.mode}` : props.url;
			const reqMethod = (props.operAction ?? props.mode) == "add" ? "POST" : "PUT";

			let postData: Ref<KeyStringObject>;
			if ("PUT" == reqMethod) {
				postData = ref(getChangedAttributes(initFormData, formData.value) ?? {});
			} else {
				const temp = toRaw(formData.value);
				postData = ref(Object.assign({}, temp));
			}

			if (props.onCollectingPostData) {
				const temp = props.onCollectingPostData(postData);
				if (Object.keys(postData.value).length == 0) {
					if (temp !== CollectEnd) {
						info("msg", { message: "当前未作任何修改!" });
					}
					return;
				} else if (temp?.startsWith("/")) {
					reqUrl = temp;
				} else if (temp) {
					reqUrl = reqUrl + "?" + temp;
				}
			}

			if (props.mode != "add" && !postData.value.ids && !postData.value.id) {
				postData.value.id = props.rawData!.id;
			}

			confirm(`确定要${props.title}吗?`, {
				callback: async (action, _ins) => {
					if (action != "confirm") return;

					mainLoading.value = true;

					if (props.onBeforeSubmit && !(await props.onBeforeSubmit(postData.value))) {
						return;
					}

					axiosRequest(
						{
							url: reqUrl,
							method: reqMethod,
							data: toRaw(postData.value),
						},
						(result) => {
							mainLoading.value = false;

							if (!result.success) return false;

							success("alert", {
								callback: () => {
									dialogProps && (dialogProps.show = false);
									(props.onUpdatedData ?? injectOnUpdatedData)?.(props.operAction ?? props.mode, result.data ?? postData.value);
								},
							});
							return true;
						}
					);
				},
			});
		});
	}
</script>

<template>
	<div
		class="edit-template"
		:class="mode"
	>
		<ElForm
			ref="formIns"
			status-icon
			:rules="rules"
			:model="formData"
			:label-width="`${labelWidth + 3}em`"
		>
			<slot
				name="layouts"
				:formData="formData"
				:fields="fields"
			>
				<slot
					name="front-extra"
					:formData="formData"
				></slot>
				<template v-for="names in layouts">
					<template v-if="names.length == 1">
						<EditItem
							v-model="formData[names[0]]"
							:field="fields[names[0]]"
							:field-config="fieldsConfig[names[0]]"
						>
							<template
								v-if="$slots[names[0]]"
								#default="scope"
							>
								<slot
									:name="names[0]"
									v-bind="scope"
									:formData="formData"
								/>
							</template>
						</EditItem>
					</template>
					<ElRow
						v-else
						:gutter="40"
					>
						<ElCol
							v-for="name in names"
							:span="fieldsConfig[name].layoutSpan"
						>
							<EditItem
								v-model="formData[name]"
								:field="fields[name]"
								:field-config="fieldsConfig[name]"
							>
								<template
									v-if="$slots[name]"
									#default="scope"
								>
									<slot
										:name="name"
										v-bind="scope"
										:formData="formData"
									/>
								</template>
							</EditItem>
						</ElCol>
					</ElRow>
				</template>
			</slot>
			<template v-if="!mainLoading.value">
				<ElFormItem
					v-if="dialogProps?.mode != 'details'"
					class="edit-item operations"
				>
					<slot
						v-if="$slots.operations"
						name="operations"
						:onSubmit="onSubmit"
					/>
					<ElButton
						v-else
						type="primary"
						@click="onSubmit"
						>立即{{ props.title }}
					</ElButton>
				</ElFormItem>
				<div
					class="tips"
					v-if="dialogProps?.mode !== 'details'"
				>
					<p
						v-if="$slots.tips"
						class="title"
						>注意事项:
					</p>
					<slot name="tips"> </slot>
				</div>
			</template>
		</ElForm>
		<ElDialog
			class="image-preview"
			title="预览"
			v-model="preview.show"
		>
			<img :src="preview.src" />
		</ElDialog>
	</div>
</template>

<style scoped lang="css">
	.edit-template :deep(.image-preview) img {
		width: 100%;
		object-fit: contain;
	}

	.edit-template {
		padding-left: 10px;
		padding-right: 20px;
	}

	.el-form :deep(> .el-row) {
		margin-bottom: 5px;
	}

	.el-form :deep(> .el-form-item) {
		margin-bottom: 25px;
	}
	.edit-item.operations {
		margin-bottom: 0;
	}

	.edit-template.details .el-form :deep(> .el-row) {
		margin-bottom: -5px;
	}

	.edit-template.details .el-form :deep(> .el-form-item) {
		margin-bottom: 15px;
	}

	.edit-template :deep(.el-form-item__content > *) {
		width: 100%;
	}

	.operations :deep(.el-form-item__content) {
		margin-left: 0 !important;
		justify-content: center;
	}
	.edit-template :deep(.operations .el-button) {
		width: max-content;
	}

	.tips {
		margin: 10px 0;
		line-height: 1.5em;
	}

	.tips :deep(p:not(.title)) {
		margin-left: 1em;
	}
</style>
