<!-- @format -->

<script setup lang="ts">
	import type { ILoading } from "../main";
	import type { TableColumnInstance } from "element-plus";
	import type {
		PageInfoHandler,
		KeyStringObject,
		RefreshedDataCallback,
		DataChangedCallback,
		OperColumnButtonClickHandler,
		OperbarButtonClickHandler,
	} from "views/common";

	import Edit from "./edit.vue";
	import UiTag from "components/UiTag.vue";
	import UiButton from "components/UiButton.vue";
	import IconFont from "components/IconFont.vue";
	import IndexTemplate from "views/common/IndexTemplate.vue";

	import { inject, ref } from "vue";
	import { EmptyObject } from "common/utils";
	import { handleMenus, MenuType, MenuTypeFields, MenuTypeNames, DetailMenuItem, Permission, toLinearArray, IMenuItem } from ".";

	const props = defineProps<{
		url: string;
		head: string;
	}>();

	const pageLoading = inject<ILoading>("pageLoading")!;

	let deleted = false;
	let allPermissionsSorted: DetailMenuItem[];
	let permissionsAllCanBeParent: DetailMenuItem[];
	let expandedRows: Map<number, DetailMenuItem> = new Map();

	const permissionsAllCanbeCurrentParent = ref<DetailMenuItem[]>([]);
	const indexTemplateIns = ref<InstanceType<typeof IndexTemplate>>(EmptyObject);

	const onPageInfoAccepted: PageInfoHandler = function (operButtons, pageData) {
		operButtons.push({
			type: "expand-all",
			icon: "expand-all",
			title: "全部展开/折叠",
			oper_type: 1,
		});
	};

	const onDataRefreshed: RefreshedDataCallback = function (pageData) {
		allPermissionsSorted = pageData.data as DetailMenuItem[];
		pageData.data = handleMenus(pageData.data as DetailMenuItem[]);
		permissionsAllCanBeParent = (toLinearArray(pageData.data as any) as DetailMenuItem[]).filter((value) => value.type != MenuType.Oper);
	};

	const onDataChanged: DataChangedCallback = function (action, data, all) {
		switch (action) {
			case "add": {
				if (data.pid == 0) {
					data.level = 1;
					all!.push(data as DetailMenuItem);
					allPermissionsSorted.push(data as DetailMenuItem);
				} else {
					let parentIdx = permissionsAllCanBeParent.findIndex((permi) => permi.id == data.pid);
					if (permissionsAllCanBeParent[parentIdx].children === undefined) {
						permissionsAllCanBeParent[parentIdx].children = [];
					}
					data.level = permissionsAllCanBeParent[parentIdx].level + 1;
					permissionsAllCanBeParent[parentIdx].children!.push(data as DetailMenuItem);

					if (data.type != MenuType.Oper) {
						permissionsAllCanBeParent.splice(parentIdx + 1, 0, data as DetailMenuItem);
					}

					parentIdx = allPermissionsSorted.findIndex((permi) => permi.id == data.pid);
					allPermissionsSorted.splice(parentIdx + 1, 0, data as DetailMenuItem);
				}
				break;
			}
			case "edit": {
				const oldValue = allPermissionsSorted.find((item) => item.id == data.id)!;
				if (data.pid !== oldValue.pid) {
					if (oldValue.pid != null) {
						const oldParentChildren = allPermissionsSorted.find((item) => item.id == oldValue.pid)!.children!;
						oldParentChildren.splice(
							oldParentChildren.findIndex((val) => val.id == data.id),
							1
						);
					} else {
						all!.splice(
							all!.findIndex((value) => value.id == oldValue.id),
							1
						);
					}
					if (data.pid != null) {
						const newParent = allPermissionsSorted.find((item) => item.id == data.pid)!;
						if (!newParent.children) {
							newParent.children = [];
						}
						newParent.children.push(oldValue);
					} else {
						all!.push(oldValue as DetailMenuItem);
					}
				}
				if (data.status !== undefined) {
					const func = (item: IMenuItem, _idx: number, _arr: IMenuItem[]) => {
						(item as DetailMenuItem).status = data.status;
						item.children?.forEach(func);
					};
					oldValue.children?.forEach(func);

					if (data.status) {
						let temp = oldValue.parent as DetailMenuItem;
						while (true) {
							if (!temp || temp.status) {
								break;
							}
							temp.status = true;
							temp = temp.parent as DetailMenuItem;
						}
					}
				}
				for (const key in data) {
					(oldValue as KeyStringObject)[key] = data[key];
				}
				break;
			}
			case "delete": {
				let idx = permissionsAllCanBeParent.findIndex((val) => val.id == data.id);
				if (idx != -1) {
					const nextIdx = idx + 1;
					while (permissionsAllCanBeParent[nextIdx]) {
						if (permissionsAllCanBeParent[nextIdx].level <= permissionsAllCanBeParent[idx].level) {
							break;
						}
						permissionsAllCanBeParent.splice(nextIdx, 1);
					}
					permissionsAllCanBeParent.splice(idx, 1);
				}

				const treeDelete = function (children: DetailMenuItem[], id: number) {
					for (let idx = 0; idx < children.length; idx++) {
						if (children[idx].id == id) {
							children.splice(idx, 1);
							deleted = true;
							break;
						} else if (children[idx].children) {
							if (deleted) {
								break;
							}
							treeDelete(children[idx].children as DetailMenuItem[], id);
						}
					}
				};
				treeDelete(all as DetailMenuItem[], data.id);
				deleted = false;
				break;
			}
			default:
				break;
		}
		return true;
	};

	const operbarButtonClick: OperbarButtonClickHandler = function (button, buttons) {
		switch (button.type) {
			case "search": {
				break;
			}
			case "refresh": {
				indexTemplateIns.value.refreshData();
				break;
			}
			case "add": {
				const dialogProps = indexTemplateIns.value.dialogProps;
				dialogProps.data = { id: null };
				return false;
			}
			case "expand-all": {
				if (expandedRows.size != 0) {
					expandedRows.forEach((item) => indexTemplateIns.value.table.toggleRowExpansion(item));
				} else {
					pageLoading.value = true;
					setTimeout(() => {
						permissionsAllCanBeParent.forEach((item) => indexTemplateIns.value.table.toggleRowExpansion(item));
						pageLoading.value = false;
					}, 250);
				}
				break;
			}
			default:
				return false;
		}
		return true;
	};

	const operColumnButtonClick: OperColumnButtonClickHandler = function (button, param) {
		const dialogProps = indexTemplateIns.value.dialogProps;
		switch (button.type) {
			case "add":
				dialogProps.data = new Permission();
				dialogProps.data.pid = param?.id;
				dialogProps.data.canBeParent = permissionsAllCanBeParent;
				return false;
			case "edit": {
				let temp: DetailMenuItem[] = [];
				switch (param!.type) {
					case MenuType.Menu:
					case MenuType.Item:
						temp = permissionsAllCanBeParent.filter((item) => item.type == MenuType.Menu);
						break;
					case MenuType.Page:
					case MenuType.Oper:
						temp = permissionsAllCanBeParent.filter((item) => item.type < param!.type);
						break;
					default:
						break;
				}
				const idx = temp.findIndex((value) => value.id == param!.id);
				if (idx != -1) {
					permissionsAllCanbeCurrentParent.value = temp.slice(0, idx);
					for (let start = idx + 1; start < temp.length; ++start) {
						if (temp[start].level <= temp[idx].level) {
							permissionsAllCanbeCurrentParent.value = permissionsAllCanbeCurrentParent.value.concat(temp.slice(start));
							break;
						}
					}
				} else {
					permissionsAllCanbeCurrentParent.value = temp;
				}
				(dialogProps.data as any).canBeParent = permissionsAllCanbeCurrentParent.value;
				break;
			}
			default:
				return false;
		}
		return true;
	};

	const onTableExpandChange = function (row: DetailMenuItem, expanded: boolean) {
		if (expandedRows.has(row.id)) {
			expandedRows.delete(row.id);
		} else {
			expandedRows.set(row.id, row);
		}
	};
</script>

<template>
	<IndexTemplate
		no-pagination
		show-index-column
		no-selection-column
		v-bind="props"
		class="permission"
		ref="indexTemplateIns"
		:table-props="{
			rowKey: 'id',
			'onExpand-change': onTableExpandChange,
		}"
		@data-changed="onDataChanged"
		@data-refreshed="onDataRefreshed"
		@page-info-accepted="onPageInfoAccepted"
		@operbar-button-click="operbarButtonClick"
		@oper-column-button-click="operColumnButtonClick"
	>
		<template #icon="[_field, data]">
			<IconFont
				v-if="data.icon"
				:value="data.icon"
			/>
		</template>

		<template #type="[_field, data]">
			<UiTag
				class="type"
				:class="MenuTypeFields[data.type]"
				:icon="MenuTypeFields[data.type]"
				:label="MenuTypeNames[data.type] ?? ''"
			/>
		</template>

		<template #status="[_field, data]">
			<UiTag
				class="status"
				:class="data.status ? 'on' : 'off'"
				:label="data.status ? '启用' : '禁用'"
			/>
		</template>

		<template #opers="{ data, buttons, clickHandler }">
			<UiButton
				v-for="button in Object.values(buttons).filter((item) => item.type != 'add')"
				:class="`button-${button.icon}`"
				:icon="button.icon"
				@click="clickHandler(button, data, buttons)"
				>{{ button.title }}
			</UiButton>
			<template v-if="buttons.add">
				<UiButton
					v-if="data.type != MenuType.Oper"
					:class="`button-${buttons.add.icon}`"
					:icon="buttons.add.icon"
					@click="clickHandler(buttons.add, data, buttons)"
					>{{ buttons.add.title }}
				</UiButton>
			</template>
		</template>

		<template #editContent="{ url, dialogProps, fieldsConfig }">
			<Edit
				v-if="['edit', 'add', 'details'].includes(dialogProps.mode)"
				:url="url"
				:fields-config="fieldsConfig"
				:raw-data="(dialogProps.data as any)"
				:parents="permissionsAllCanbeCurrentParent!"
			/>
		</template>
	</IndexTemplate>
</template>

<style scoped lang="css">
	.permission :deep(.el-table__placeholder) {
		display: none;
	}

	.permission :deep(.cell) {
		line-height: 2em;
	}

	.permission :deep(.name .cell) {
		vertical-align: baseline;
		white-space: nowrap;
	}

	.permission :deep(.ui-tag) {
		font-size: inherit;
	}

	.permission :deep(.type),
	.permission :deep(.status) {
		display: inline-block;
		padding: 0 5px;
		line-height: 1.7em;
		border-radius: 5px;
		color: white;
		vertical-align: baseline;
	}

	.permission :deep(.type.menu) {
		background-color: chocolate;
	}

	.permission :deep(.type.item) {
		background-color: cornflowerblue;
	}

	.permission :deep(.type.page) {
		background-color: dodgerblue;
	}

	.permission :deep(.type.oper) {
		background-color: darkgray;
	}

	.permission :deep(.status.off) {
		background-color: brown;
	}

	.permission :deep(.status.on) {
		background-color: var(--el-color-primary);
	}
</style>
