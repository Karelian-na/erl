<!-- @format -->

<script setup lang="ts">
	import type { ILoading } from "../main";
	import type {
		DataChangedCallback,
		IDialogProps,
		IFields,
		IOperButton,
		IPageData,
		IPageInfo,
		KeyStringObject,
		OperbarButtonClickHandler,
		PageInfoHandler,
		RefreshedDataCallback,
		OperColumnButtonClickHandler,
		FieldsConfig,
		PreparedCallback,
	} from ".";

	import EditTemplate from "./EditTemplate.vue";
	import OperationBar from "./OperationBar.vue";
	import UiButton from "components/UiButton.vue";
	import UiPagination from "components/UiPagination.vue";
	import { ElDialog, ElTable, ElTableColumn, vLoading } from "element-plus";

	import { detailButton } from ".";
	import fieldConfigs from "field-configs";
	import { axiosRequest, error, confirm, success, EmptyObject } from "common/utils";
	import { ref, onBeforeMount, reactive, provide, nextTick, onActivated, watch, inject } from "vue";

	import { isArray } from "@vue/shared";

	const props = defineProps<{
		url: string;
		head: string;
		noPagination?: boolean;
		showIndexColumn?: boolean;
		noSelectionColumn?: boolean;
		onDataChanged?: DataChangedCallback;
		onPageInfoAccepted?: PageInfoHandler;
		onDataRefreshed?: RefreshedDataCallback;
		onEditTemplatePrepared?: PreparedCallback;
		onOperbarButtonClick?: OperbarButtonClickHandler;
		tableProps?: InstanceType<typeof ElTable>["$props"];
		onOperColumnButtonClick?: OperColumnButtonClickHandler;
	}>();

	const pageLoading = inject<ILoading>("pageLoading")!;

	let searchKey = "";
	let searchField = "";
	let indexUrl = "";

	const operColumnWidth = ref(0);
	const parentUrl = ref("");

	const allFields = ref<IFields>(EmptyObject);
	const displayFields = ref<IFields>(EmptyObject);
	const searchableFields = ref<IFields>(EmptyObject);
	const fieldsConfig = ref<FieldsConfig>(EmptyObject);

	const pageData = ref<IPageData>(EmptyObject);
	const operbarButtons = ref<Record<string, IOperButton>>({});
	const operColumnButtons = ref<Record<string, IOperButton>>({});
	const table = ref<InstanceType<typeof ElTable>>(EmptyObject);
	const operbar = ref<InstanceType<typeof OperationBar>>(EmptyObject);
	const pagination = ref<InstanceType<typeof UiPagination>>();
	const dialogProps = reactive<IDialogProps>({
		show: false,
		mode: "",
		operLabel: "",
		data: {},
		loading: false,
		loadingTip: "加载中...",
	});

	onBeforeMount(updateTemplate);
	onActivated(onPageChanged);

	watch(() => props.url, updateTemplate);

	defineExpose({ operbar, dialogProps, getSelectedRows, parentUrl, refreshData, table, fieldConfigs, allFields });

	const onUpdatedData: DataChangedCallback = function (action, data) {
		if (!props.onDataChanged?.(action, data, pageData.value.data)) {
			switch (action) {
				case "add":
					pageData.value.data.push(data);
					break;
				case "edit": {
					if (dialogProps.data.length > 1) {
						refreshData();
						return true;
					}
					delete data.ids;

					for (const key in data) {
						(dialogProps.data as KeyStringObject)[key] = data[key];
					}
				}
				default:
					break;
			}
		}
		return true;
	};

	const operbarButtonClick: OperbarButtonClickHandler = function (button) {
		if (!props.onOperbarButtonClick?.(button, operbarButtons.value)) {
			switch (button.type) {
				case "search":
					if (operbar.value!.searchKey != "" && operbar.value!.searchField == "") {
						error("msg", { message: "请选择查询字段!" });
					} else {
						props.onOperbarButtonClick!(button, operbarButtons.value);
					}
					break;
				case "refresh":
					refreshData();
					break;
				case "add":
					operColumnButtonClick(button, null, operbarButtons.value);
					break;
				case "delete":
					const rows = getSelectedRows();
					if (rows.length === 0) {
						error("msg", { message: "请选择要删除的数据!" });
						return true;
					}
					operColumnButtonClick(button, rows.length === 1 ? rows[0] : rows, operbarButtons.value);
					break;
				default:
					dialogProps.show = true;
					dialogProps.mode = button.type;
					if (!dialogProps.operLabel) {
						dialogProps.operLabel = button.title;
					}
					break;
			}
		}
		return true;
	};

	const operColumnButtonClick: OperColumnButtonClickHandler = function (button, param) {
		dialogProps.show = true;
		dialogProps.mode = button.type;
		dialogProps.operLabel = button.title;
		dialogProps.data = param!;

		switch (button.type) {
			case "edit":
				dialogProps.operLabel = "修改";
		}

		if (!props.onOperColumnButtonClick?.(button, param, operColumnButtons.value)) {
			switch (button.type) {
				case "add":
					if (!dialogProps.data) {
						dialogProps.data = {};
					}
					break;
				case "delete": {
					dialogProps.show = false;
					let warningText;
					let dataIds: any;
					if (isArray(param)) {
						dataIds = param.map((item) => item.id).join(",");
						warningText = "确定要删除选中数据吗?";
					} else {
						dataIds = param!.id;
						warningText = "确定要删除该条数据吗?";
					}

					confirm(warningText, {
						callback: (action, _ins) => {
							if (action != "confirm") return;

							pageLoading.value = true;
							axiosRequest(
								{
									method: "DELETE",
									url: `${parentUrl.value}delete`,
									params: { ids: dataIds },
								},
								(result) => {
									pageLoading.value = false;
									if (result.success) {
										if (!props.onDataChanged?.("delete", param!, pageData.value.data)) {
											if (isArray(param)) {
												refreshData();
											} else {
												const idx = pageData.value.data.indexOf(param!);
												idx != -1 && pageData.value.data.splice(idx, 1);
											}
										}
									}
								}
							);
						},
					});
					break;
				}
				default:
					break;
			}
		}
		return true;
	};

	provide("fields", allFields);
	provide("parentUrl", parentUrl);
	provide("dialogProps", dialogProps);
	provide("onUpdatedData", onUpdatedData);

	async function refreshData(pageIdx?: number, pageSize?: number) {
		let res = false;
		pageLoading.value = true;
		await nextTick();
		await axiosRequest(
			{
				method: "GET",
				url: indexUrl,
				params: pagination.value
					? {
							pageIdx: pageIdx ?? pagination.value.curPage,
							pageSize: pageSize ?? pagination.value.pageSize,
							searchKey: searchKey,
							searchField: searchField,
					  }
					: {},
			},
			(result) => {
				if (result.success) {
					res = true;
					success("msg", { message: "已加载数据!" });
					pageData.value = result.data;
					props.onDataRefreshed?.(pageData.value);
					return true;
				}
				return false;
			}
		);
		pageLoading.value = false;
		return res;
	}

	function updateTemplate() {
		operColumnWidth.value = 0;
		if (props.url.endsWith("/")) {
			indexUrl = props.url + "index";
			parentUrl.value = props.url;
		} else {
			indexUrl = props.url;
			parentUrl.value = props.url.substring(0, props.url.lastIndexOf("/") + 1);
		}
		const idx = props.url.indexOf("?");
		fieldsConfig.value = fieldConfigs[idx !== -1 ? props.url.substring(0, idx) : props.url];
		pageData.value = {} as any;
		pageLoading.value = true;
		nextTick(() => {
			axiosRequest(
				{
					method: "GET",
					url: indexUrl,
					params: {
						initPageSize: 20,
					},
				},
				(result) => {
					pageLoading.value = false;
					if (result.success) {
						allFields.value = {};
						displayFields.value = {};
						searchableFields.value = {};

						const info: IPageInfo = result.data;
						info.fields.forEach((field) => {
							const fieldName = field.field_name;
							allFields.value[fieldName] = field;
							if (field.display) {
								displayFields.value[fieldName] = field;
								if (field.searchable) {
									searchableFields.value[fieldName] = field;
								}
							}
						});
						pageData.value = info.pageData;
						operbarButtons.value = {
							search: { icon: "search", oper_type: 1, title: "搜索", type: "search" },
							refresh: { icon: "refresh", oper_type: 1, title: "刷新", type: "refresh" },
						};
						operColumnButtons.value = {};

						props.onPageInfoAccepted?.(info.operButtons, pageData.value);
						props.onDataRefreshed?.(pageData.value);
						info.operButtons.forEach((item) => {
							if (item.oper_type & 0x1) {
								operbarButtons.value[item.type] = item;
							}

							if (item.oper_type & 0x2) {
								operColumnButtons.value[item.type] = item;
								operColumnWidth.value += 20 + item.title.length * 20;
							}
						});
						operColumnWidth.value += 40;

						onPageChanged();
					} else {
						error("alert", {
							title: "错误!",
							content: `请求失败!原因:${result.msg}`,
						});
					}
					return true;
				}
			);
		});
	}

	function computeIndex(index: number) {
		if (pagination.value) {
			return (pagination.value.curPage - 1) * pagination.value.pageSize + index + 1;
		}
		return index + 1;
	}

	function getSelectedRows() {
		return table.value.getSelectionRows() as (KeyStringObject & { id: string | number })[];
	}

	function onPageChanged() {
		nextTick(() => {
			if (pagination.value) {
				const wrapperElement = document.querySelector<HTMLDivElement>(".content .wrapper")!;
				if (pagination.value.pageAmount > 1) {
					const minWidth = pagination.value.$el.clientWidth + 40;
					wrapperElement.style.setProperty("min-width", `${minWidth < 720 ? 720 : minWidth}px`);
				} else {
					wrapperElement.style.removeProperty("min-width");
				}
			}
		});
	}

	function onDialogBeforeClose(done: (cancel?: boolean) => void) {
		if (dialogProps.askIfNeedToLeave) {
			const leaveCall = dialogProps.askIfNeedToLeave();
			if (leaveCall) {
				confirm("确定要离开吗? 当前所作更改将不会保存!", {
					callback: (action, _ins) => {
						const cancel = action == "cancel";
						done(cancel);
						if (!cancel && leaveCall !== true) {
							leaveCall();
						}
					},
				});
				return;
			}
		}
		done();
	}

	function onDialogClosed() {
		dialogProps.mode = "";
		dialogProps.data = {};
		dialogProps.operLabel = "";
		dialogProps.operAction = undefined;
	}

	function mapDataValueToLabel(fieldName: string, value: any) {
		const enums = fieldsConfig.value[fieldName].enumItems;
		if (enums) {
			if (value > 0 && enums[value - 1]) {
				return enums[value - 1].label;
			}
		}
		return value;
	}
</script>

<template>
	<div class="index-template">
		<OperationBar
			ref="operbar"
			:oper-buttons="operbarButtons"
			:searchable-fields="searchableFields"
			@operbar-button-click="operbarButtonClick"
		/>
		<div class="data">
			<div class="table">
				<ElTable
					v-if="pageData.data"
					border
					stripe
					ref="table"
					height="100%"
					table-layout="auto"
					empty-text="未查询到数据"
					:data="pageData.data"
					v-bind="tableProps"
					@row-dblclick="(row) => operColumnButtonClick(detailButton, row, null as any)"
				>
					<ElTableColumn
						v-if="noSelectionColumn !== true"
						type="selection"
						align="center"
						width="39px"
					></ElTableColumn>
					<ElTableColumn
						v-if="showIndexColumn"
						fixed="left"
						label="序号"
						type="index"
						align="center"
						:index="computeIndex"
					></ElTableColumn>
					<slot name="layouts">
						<template v-for="field in Object.values(displayFields).sort((l, r) => l.display_order - r.display_order)">
							<ElTableColumn
								show-overflow-tooltip
								align="center"
								:prop="field.field_name"
								:label="field.display_name"
								v-bind="fieldsConfig?.[field.field_name]?.columnBindProps"
							>
								<template
									#default="{ row }"
									v-if="$slots[field.field_name]"
								>
									<slot
										:name="field.field_name"
										v-bind="[ field, row as KeyStringObject ]"
									/>
								</template>
								<template
									#default="{ row }"
									v-else-if="['enum', 'radio', 'switch'].includes(fieldsConfig?.[field.field_name]?.type)"
									>{{ mapDataValueToLabel(field.field_name, row[field.field_name]) }}
								</template>
							</ElTableColumn>
						</template>
						<ElTableColumn
							v-if="Object.keys(operColumnButtons).length || $slots.opers"
							fixed="right"
							label="操作"
							prop="operation"
							align="center"
							style="white-space: nowrap"
							class-name="oper"
							:width="`${operColumnWidth}px`"
						>
							<template #default="{ row }">
								<slot
									v-if="$slots.opers"
									name="opers"
									:data="row"
									:buttons="operColumnButtons"
									:clickHandler="operColumnButtonClick"
								/>
								<template v-else>
									<UiButton
										v-for="button in operColumnButtons"
										:icon="button.icon"
										@click="operColumnButtonClick(button, row, null as any)"
										>{{ button.title }}
									</UiButton>
								</template>
							</template>
						</ElTableColumn>
					</slot>
				</ElTable>
			</div>
			<UiPagination
				v-if="noPagination !== true && pageData.data"
				ref="pagination"
				:max-page-amount="7"
				:page-sizes="[20, 40, 100, 200]"
				:data-count="pageData.totalCount"
				@change="refreshData"
				@changed="onPageChanged"
			></UiPagination>
		</div>
		<ElDialog
			draggable
			width="850px"
			v-model="dialogProps.show"
			:title="`${head}-${dialogProps.operLabel}`"
			:align-center="true"
			:close-on-click-modal="false"
			:before-close="onDialogBeforeClose"
			@closed="onDialogClosed"
		>
			<div
				v-loading="dialogProps.loading"
				:element-loading-text="dialogProps.loadingTip"
			>
				<slot
					v-if="$slots.editContent"
					name="editContent"
					:url="parentUrl"
					:dialogProps="dialogProps"
					:fieldsConfig="fieldsConfig"
				>
				</slot>
				<template v-else>
					<EditTemplate
						v-if="['edit', 'add', 'details'].includes(dialogProps.mode)"
						:url="parentUrl"
						:fields="allFields"
						:mode="dialogProps.mode"
						:fields-config="fieldsConfig"
						:raw-data="dialogProps.data"
						:title="dialogProps.operLabel"
						@prepared="onEditTemplatePrepared"
					/>
				</template>
				<slot
					name="dialogContent"
					:url="parentUrl"
					:dialogProps="dialogProps"
					:fieldsConfig="fieldsConfig"
					:onUpdatedData="onUpdatedData"
				></slot>
			</div>
		</ElDialog>
	</div>
</template>

<style scoped lang="css">
	.index-template {
		/* Layout */
		width: 100%;
		height: 100%;
		display: flex;
		flex-direction: column;
		padding: 20px;
		padding-top: 0;
	}

	.index-template :deep(.el-dialog__header) {
		padding: 10px 20px;
	}

	.data {
		flex-grow: 1;
		display: flex;
		flex-direction: column;
		overflow: hidden;
	}

	.data > .table {
		flex-grow: 1;
		overflow: hidden;
		color: black;
	}

	.el-table :deep(.cell) {
		white-space: nowrap;
	}
	.el-table :deep(thead .cell) {
		text-align: center !important;
		font-weight: bold;
		color: black;
	}
	.el-table :deep(.oper .cell) {
		text-align: left;
		text-overflow: unset;
	}

	.el-table :deep(.oper .ui-button) {
		padding: 0;
		height: 2em;
		padding: 0 0.5em;
		color: white;
		letter-spacing: 0;
	}

	.index-template :deep(.el-dialog) {
		max-height: 80%;
		display: flex;
		flex-direction: column;
	}

	.index-template :deep(.el-dialog__body) {
		flex-grow: 1;
		overflow: auto;
	}

	.ui-pagination {
		margin-top: 10px;
	}
	.tips {
		line-height: 1.5em;
	}
</style>
