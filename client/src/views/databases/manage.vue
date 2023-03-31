<!-- @format -->

<script setup lang="ts">
	import type {
		BeforeLeaveCallback,
		BeforeSubmitHandler,
		CollectingPostDataHandler,
		DataChangedCallback,
		FieldsConfig,
		IDialogProps,
		IFields,
		KeyStringObject,
		UpdatingFormDataHandler,
	} from "views/common";

	import IconFont from "components/IconFont.vue";
	import EditTemplate from "views/common/EditTemplate.vue";
	import { ElFormItem, ElTable, ElTableColumn, ElCheckbox, ElInput } from "element-plus";

	import { EmptyObject } from "common/utils";
	import { inject, onMounted, ref } from "vue";

	const props = defineProps<{
		url: string;
		fields: IFields[""][];
		rawData: KeyStringObject;
		fieldsConfig: FieldsConfig;
	}>();

	const dialogProps = inject<IDialogProps>("dialogProps")!;

	let draggable = false;
	let draggingItem: HTMLTableRowElement;

	const tableIns = ref<InstanceType<typeof ElTable>>(EmptyObject);

	const onUpdatingFormData: UpdatingFormDataHandler = function (formData) {
		if (props.fields.length !== 0) {
			formData.value = {
				comment: props.rawData.comment,
				fields: props.fields.reduce((prev, cur, idx) => {
					prev[cur.field_name] = Object.assign({}, cur);
					return prev;
				}, {} as KeyStringObject),
			};
		}
	};

	const onCollectingPostData: CollectingPostDataHandler = function (postData) {
		if (!postData.value.fields) {
			postData.value.fields = {};
		}
		document.querySelectorAll<HTMLTableRowElement & { data: KeyStringObject }>(".edit-template tbody tr").forEach((item, idx) => {
			const curFieldName = item.data.field_name;
			const modelIdx = props.fields.findIndex((field) => field.field_name === curFieldName);
			if (postData.value.fields[curFieldName]) {
				if (postData.value.fields[curFieldName].display === false) {
					return;
				}
			} else if (!props.fields[modelIdx].display || item.rowIndex - 1 === modelIdx) {
				return;
			}

			if (!postData.value.fields[curFieldName]) {
				postData.value.fields[curFieldName] = {};
			}
			postData.value.fields[curFieldName].display_order = idx;
		});
		if (Object.keys(postData.value.fields).length === 0) {
			delete postData.value.fields;
		}
	};

	const onBeforeSubmit: BeforeSubmitHandler = function (postData) {
		delete postData.id;
		postData.viewName = props.rawData.view_name;
		return true;
	};

	const onBeforeLeave: BeforeLeaveCallback = function (formData, base) {
		const result = base();
		if (result) {
			return result;
		}

		const rows = document.querySelectorAll<HTMLTableRowElement & { data: KeyStringObject }>(".edit-template tbody tr");
		for (let idx = 0; idx < rows.length; idx++) {
			const item = rows[idx];
			if (item.data.display && item.rowIndex - 1 !== props.fields.findIndex((field) => field.field_name === item.data.field_name)) {
				return true;
			}
		}
		return false;
	};

	const onUpdatedData: DataChangedCallback = function (action, data, all) {
		switch (action) {
			case "edit":
				for (const key in data) {
					(dialogProps.data as KeyStringObject)[key] = data[key];
				}
				break;
			default:
				break;
		}
		return true;
	};

	onMounted(() => {
		document.querySelectorAll<HTMLTableRowElement & { data: KeyStringObject }>(".edit-template tbody tr").forEach((item, idx) => {
			item.draggable = true;
			item.data = props.fields[idx];
			item.ondragstart = (e) => {
				if (draggable) {
					draggingItem = item;
					setTimeout(() => {
						item.classList.add("dragging");
					}, 10);
				} else {
					e.preventDefault();
				}
			};

			item.ondragenter = (e) => {
				e.preventDefault();
				if (!draggingItem || draggingItem === e.currentTarget) {
					return;
				}
				if (item.rowIndex < draggingItem.rowIndex) {
					item.insertAdjacentElement("beforebegin", draggingItem);
				} else {
					item.insertAdjacentElement("afterend", draggingItem);
				}
			};

			item.ondragover = (e) => {
				e.dataTransfer!.dropEffect = "move";
			};

			item.ondragend = (e) => {
				item.classList.remove("dragging");
				e.preventDefault();
				draggable = false;
			};
		});
	});

	function onDragHandlerMouseDown() {
		draggable = true;
	}
</script>

<template>
	<EditTemplate
		mode="edit"
		title="修改"
		:url="url"
		:raw-data="dialogProps.data"
		:fields-config="fieldsConfig"
		@updated-data="onUpdatedData"
		@before-leave="onBeforeLeave"
		@before-submit="onBeforeSubmit"
		@updating-form-data="onUpdatingFormData"
		@collecting-post-data="onCollectingPostData"
	>
		<template
			v-if="dialogProps.mode === 'edit'"
			#front-extra="{ formData }"
		>
			<ElFormItem label="字段列表:">
				<ElTable
					ref="tableIns"
					:data="fields"
					:table-layout="'auto'"
				>
					<ElTableColumn width="36px">
						<IconFont
							value="movehandler"
							@mousedown="onDragHandlerMouseDown"
						/>
					</ElTableColumn>
					<ElTableColumn
						prop="field_name"
						label="字段名"
						align="center"
						class-name="field_name"
					/>
					<ElTableColumn
						prop="display_name"
						label="展示名"
						align="center"
					>
						<template #default="{ row }">
							<ElInput v-model="formData.fields[row.field_name].display_name" />
						</template>
					</ElTableColumn>
					<ElTableColumn
						prop="display"
						label="是否展示"
						align="center"
					>
						<template #default="{ row }">
							<ElCheckbox v-model="formData.fields[row.field_name].display" />
						</template>
					</ElTableColumn>
					<ElTableColumn
						prop="searchable"
						label="可检索"
						align="center"
					>
						<template #default="{ row }">
							<ElCheckbox
								v-model="formData.fields[row.field_name].searchable"
								:disabled="!formData.fields[row.field_name].display"
							/>
						</template>
					</ElTableColumn>
					<ElTableColumn
						prop="editable"
						label="可修改"
						align="center"
					>
						<template #default="{ row }">
							<ElCheckbox
								v-model="formData.fields[row.field_name].editable"
								:disabled="/((.*id)|.*(user|time))$/.test(row.field_name)"
							/>
						</template>
					</ElTableColumn>
				</ElTable>
			</ElFormItem>
		</template>
	</EditTemplate>
</template>

<style scoped lang="css">
	.icon-movehandler {
		cursor: move;
	}

	.edit-template :deep(tr) {
		background-color: white;
	}
	.edit-template :deep(tr.dragging) {
		background-color: #efefef;
	}
</style>
