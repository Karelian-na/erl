<!-- @format -->

<script setup lang="ts">
	import type Node from "element-plus/es/components/tree/src/model/node";
	import type {
		BeforeLeaveCallback,
		CollectingPostDataHandler,
		FieldsConfig,
		IDialogProps,
		KeyStringObject,
		UpdatingFormDataHandler,
	} from "views/common";

	import { ElTree, ElRow, ElCol } from "element-plus";
	import IconFont from "components/IconFont.vue";
	import EditTemplate from "views/common/EditTemplate.vue";

	import { isArray } from "@vue/shared";
	import { handleMenus, IMenuItem } from ".";
	import { axiosRequest } from "common/utils";
	import { inject, nextTick, ref } from "vue";

	type ElTreeInstance = InstanceType<typeof ElTree>;

	const props = defineProps<{
		url: string;
		rawData: KeyStringObject;
		fieldsConfig: FieldsConfig;
	}>();

	const dialogProps = inject<IDialogProps>("dialogProps")!;

	interface TreeNodeData {
		id: number;
		name: string;
		type: number;
		pid: number;
		children?: TreeNodeData[];
		disabled: boolean;
		origin: boolean;
		force: boolean;
		totalSubAmount: number;
		checkedSubAmount: number;
		independent?: {
			totalSubAmount: number;
			checkedSubAmount: number;
			curSubAmount: number;
		};
	}

	const checkedIds = ref<number[]>([]);
	const userMode = props.url == "/Users/";
	const time = ref<number>(new Date().getTime());
	const permisTreeIns = ref<InstanceType<typeof ElTree>>();
	const changesTreeIns = ref<InstanceType<typeof ElTree>>();
	const allAuthorizedPermissions = ref<TreeNodeData[]>([]);

	const isPermis = ref(true);
	let IdValueMap = new Map<number, TreeNodeData>();

	const onUpdatingFormData: UpdatingFormDataHandler = function (formData) {
		checkedIds.value = [];
		dialogProps.loading = true;

		const params = { id: props.rawData.id, all: false };
		if (allAuthorizedPermissions.value.length == 0) {
			params.all = true;
		}
		axiosRequest(
			{
				method: "GET",
				url: `${props.url}authorize`,
				params: params,
			},
			(result) => {
				if (result.success) {
					const data: {
						all: TreeNodeData[] | null;
						auth: number[];
						independent: number[];
					} = result.data;

					// 更新所有权限数据
					if (data.all) {
						data.all.forEach((item) => {
							IdValueMap.set(item.id, item);
							item.origin = false;
							item.disabled = false;
							item.checkedSubAmount = 0;
						});
						allAuthorizedPermissions.value = handleMenus(data.all as unknown as IMenuItem[]) as unknown as TreeNodeData[];

						IdValueMap.forEach((value) => {
							value.totalSubAmount = value.children ? value.children.length : 0;
						});
					} else {
						if (userMode) {
							IdValueMap.forEach((value) => {
								value.origin = false;
								value.disabled = false;
								value.independent && delete value.independent;
								value.checkedSubAmount = 0;
							});
						} else {
							IdValueMap.forEach((value) => (value.origin = false));
						}
					}

					// 更新授权的权限数据
					data.auth.forEach((authId) => {
						const permi = IdValueMap.get(authId)!;
						permi.origin = true;
						if (permi.children === undefined) {
							checkedIds.value.push(authId);
						}
						if (permi.pid) {
							const parent = IdValueMap.get(permi.pid)!;
							++parent.checkedSubAmount;
						}
					});

					// 更新独立的权限数据
					if (userMode) {
						data.independent.forEach((independentId) => {
							const permi = IdValueMap.get(independentId)!;
							permi.independent = {
								curSubAmount: 0,
								checkedSubAmount: 0,
								totalSubAmount: 0,
							};
							const parent = IdValueMap.get(permi.pid)!;
							if (parent?.independent) {
								++parent.independent.curSubAmount;
								++parent.independent.totalSubAmount;
								if (permi.origin) {
									++parent.independent.checkedSubAmount;
								}
							}
						});
					}

					formData.value.auths = {};

					time.value = new Date().getTime();
					nextTick(() => (dialogProps.loading = false));
					return true;
				}
				dialogProps.loading = false;
				return false;
			}
		);
	};

	const onCollectingPostData: CollectingPostDataHandler = function (postData) {
		if (!Object.keys(postData.value).length) {
			return;
		}

		const changesElementTree = changesTreeIns.value!.$el as HTMLDivElement;
		const changesContainer = changesElementTree.parentElement!;

		if (userMode) {
			postData.value.auths = Array.from(changesContainer.querySelectorAll<HTMLDivElement>("div[changed]")).reduce((prev, cur) => {
				const key = parseInt(cur.getAttribute("data-key")!);
				const node = changesTreeIns.value!.getNode(key)!;
				const data = node.data as TreeNodeData;
				const value = node.checked || node.indeterminate;
				if (data.independent && data.origin == value) {
					prev[key] = 2;
					if (data.pid) {
						const parentData = changesTreeIns.value!.getNode(data.pid)!.data as TreeNodeData;
						if (prev[data.pid] === undefined && parentData.independent) {
							prev[data.pid] = 2;
						}
					}
				} else if (data.origin !== value) {
					prev[key] = value ? 1 : 0;
					if (value && data.independent && prev[data.pid] !== undefined && prev[data.pid] != 1) {
						prev[data.pid] = 2;
					}
				}
				return prev;
			}, {} as KeyStringObject);
		} else {
			postData.value.auths = Array.from(changesContainer.querySelectorAll<HTMLDivElement>("div[changed]")).reduce((prev, cur) => {
				const key = parseInt(cur.getAttribute("data-key")!);
				const node = changesTreeIns.value!.getNode(key)!;
				prev[key] = node.checked || node.indeterminate ? 1 : 0;
				return prev;
			}, {} as KeyStringObject);
		}

		if (Object.keys(postData.value.auths).length == 0) {
			delete postData.value.auths;
		} else {
			postData.value.id = props.rawData.id;
		}
	};

	const onBeforeLeave: BeforeLeaveCallback = function (formData) {
		const changesElementTree = changesTreeIns.value!.$el as HTMLDivElement;
		const changesContainer = changesElementTree.parentElement!;

		if (changesContainer.querySelector<HTMLDivElement>("div[changed]")) {
			return true;
		}
		IdValueMap.forEach((value) => {
			value.disabled = false;
		});
		return false;
	};

	function onScroll(event: Event, another: InstanceType<typeof ElTree>) {
		if ((another == changesTreeIns.value && isPermis.value) || (another == permisTreeIns.value && !isPermis.value)) {
			another.$el.scroll(0, (event.target as any)!.scrollTop);
		}
	}

	function treeExpandChange(another: InstanceType<typeof ElTree>, data: TreeNodeData) {
		if ((another == changesTreeIns.value && isPermis.value) || (another == permisTreeIns.value && !isPermis.value)) {
			const node2 = another.getNode(data)!;
			if (node2.expanded === true) {
				another.getNode(data)?.collapse();
			} else {
				another.getNode(data)?.expand();
			}
		}
	}

	function permisTreeCheckChange(data: TreeNodeData, curChecked: boolean) {
		if (isPermis.value) {
			const permisElementTree = permisTreeIns.value!.$el as HTMLDivElement;
			const changesElementTree = changesTreeIns.value!.$el as HTMLDivElement;

			const permiNode = permisTreeIns.value!.getNode(data);
			const changeNode = changesTreeIns.value!.getNode(data);
			changeNode.checked = curChecked;
			changeNode.indeterminate = permiNode.indeterminate;

			let element = changesElementTree.querySelector<HTMLDivElement>(`[data-key="${data.id}"]`)!;

			let checked = changeNode.checked || changeNode.indeterminate;

			const parentData = permiNode.parent.data as TreeNodeData;
			if (checked == data.origin) {
				element.removeAttribute("changed");
				data.independent && permisElementTree.querySelector(`[data-key="${data.id}"]`)!.removeAttribute("changed");
			} else {
				element.setAttribute("changed", "");
				data.independent && permisElementTree.querySelector(`[data-key="${data.id}"]`)!.setAttribute("changed", "");
			}
			// 当前为叶子节点更新父节点独立子权限状态
			if (userMode && permiNode.isLeaf) {
				if (parentData.independent && (data.independent || !data.origin)) {
					if (checked) {
						++parentData.independent.checkedSubAmount;
					} else {
						--parentData.independent.checkedSubAmount;
					}
				}
				let last: boolean = true;
				if (parentData.independent && parentData.independent.curSubAmount == parentData.independent.totalSubAmount) {
					last = false;
				}
				handleParent("refresh", permiNode.parent, last);
			}
		}
	}

	function changesTreeCheckChange(data: TreeNodeData, checked: boolean) {
		if (!isPermis.value) {
			const changesElementTree = changesTreeIns.value!.$el as HTMLDivElement;
			const permisElementTree = permisTreeIns.value!.$el as HTMLDivElement;

			const permiNode = permisTreeIns.value!.getNode(data);
			const changeNode = changesTreeIns.value!.getNode(data);
			permiNode.checked = checked;
			permiNode.indeterminate = changeNode.indeterminate;
			const element = changesElementTree.querySelector(`[data-key="${data.id}"]`)!;
			if (!element.hasAttribute("independent")) {
				if (element.hasAttribute("changed") && !(changeNode.checked || changeNode.indeterminate)) {
					element.removeAttribute("changed");
					permisElementTree.querySelector(`[data-key="${data.id}"]`)!.removeAttribute("changed");
				}
			}
		}
	}

	function getIndependentCheckedAmount(prev: number, cur: Node) {
		const data = cur.data as TreeNodeData;
		const checked = cur.checked || cur.indeterminate;
		if (
			!cur.disabled &&
			checked &&
			((data.independent && (cur.isLeaf || data.independent.checkedSubAmount != 0)) || (cur.isLeaf && !cur.data.origin))
		) {
			++prev;
		}
		return prev;
	}

	function handleElement(type: "delete" | "revoke" | "refresh", node: Node, last: boolean) {
		const changesElementTree = changesTreeIns.value!.$el as HTMLDivElement;
		const permisElementTree = permisTreeIns.value!.$el as HTMLDivElement;

		const element = changesElementTree.querySelector(`[data-key="${changesTreeIns.value!.getNodeKey(node)}"]`)!;

		const data = node.data as TreeNodeData;
		const parentData = node.parent.data as TreeNodeData;
		const checked = node.checked || node.indeterminate;
		const independCheckedAmount = node.childNodes.reduce(getIndependentCheckedAmount, 0);

		switch (type) {
			case "delete": {
				if (node.isLeaf && element.hasAttribute("changed") && !element.hasAttribute("independent")) return last;
				if (data.independent && independCheckedAmount == 0 && !element.hasAttribute("independent")) {
					element.setAttribute("changed", "");
					element.setAttribute("independent", "");
					if (parentData?.independent) {
						--parentData.independent.curSubAmount;
						checked && --parentData.independent.checkedSubAmount;
					}
				}
				break;
			}
			case "revoke": {
				if (element.hasAttribute("independent")) {
					element.removeAttribute("changed");
					element.removeAttribute("independent");
					if (parentData?.independent) {
						++parentData.independent.curSubAmount;
						checked && ++parentData.independent.checkedSubAmount;
					}
				}
				break;
			}
			case "refresh": {
				if (element.hasAttribute("changed")) {
					if (element.hasAttribute("independent")) {
						if (
							independCheckedAmount != 0 ||
							(data.independent!.curSubAmount == data.independent!.totalSubAmount &&
								!element.querySelector(":scope [changed] .icon-user-line"))
						) {
							element.removeAttribute("independent");
							element.removeAttribute("force");
							data.force = false;
							if (parentData?.independent) {
								++parentData.independent.curSubAmount;
								++parentData.independent.checkedSubAmount;
							}
							if (checked == data.origin) {
								element.removeAttribute("changed");
							}
						} else {
							element.toggleAttribute("force", !last);
							data.force = !last;
						}
					} else {
						if (checked == data.origin) {
							if (
								data.independent &&
								data.independent.checkedSubAmount == 0 &&
								data.independent.curSubAmount != data.independent.totalSubAmount
							) {
								element.setAttribute("independent", "");
								if (parentData?.independent) {
									--parentData.independent.curSubAmount;
									--parentData.independent.checkedSubAmount;
								}
							} else {
								element.removeAttribute("changed");
							}
						}
					}
				} else {
					if (data.independent && independCheckedAmount == 0) {
						element.toggleAttribute("force", !last);
						data.force = !last;
						element.setAttribute("changed", "");
						element.setAttribute("independent", "");

						if (parentData.independent) {
							--parentData.independent.curSubAmount;
							--parentData.independent.checkedSubAmount;
						}
						return last;
					} else {
					}
				}
			}
		}

		// 更新可用状态，及权限区的更改状态
		if (element.hasAttribute("independent")) {
			if (node.isLeaf || (data.independent!.curSubAmount == 0 && data.independent!.totalSubAmount == node.childNodes.length)) {
				data.disabled = true;
				permisElementTree.querySelector(`[data-key="${data.id}"]`)!.setAttribute("changed", "");
			}
		} else if (data.disabled) {
			data.disabled = false;
			permisElementTree.querySelector(`[data-key="${data.id}"]`)!.removeAttribute("changed");
		}
		return last;
	}

	function handleParent(type: "delete" | "revoke" | "refresh", node: Node, last: boolean) {
		while (true) {
			if (isArray(node.data)) {
				break;
			}
			last = handleElement(type, node, last);
			node = node.parent;
		}
	}

	function independentPermisClick(type: "delete" | "revoke", node: Node) {
		if (!node.data.force) {
			let force = true;
			const handleChildren = (type: "delete" | "revoke", node: Node) => {
				if (node.childNodes.length != 0) {
					node.childNodes.forEach((sub) => handleChildren(type, sub));
				}

				force = handleElement(type, node, force);
			};
			if (node.isLeaf) {
				handleElement(type, node, force);
				node = node.parent;
			} else {
				node.childNodes.forEach((sub) => handleChildren(type, sub));
			}
			if (node.data.independent && node.data.independent.curSubAmount == node.data.independent.totalSubAmount) {
				force = false;
			}
			handleParent("refresh", node, force);
		}
	}

	const treeProps = {
		nodeKey: "id",
		showCheckbox: true,
		defaultExpandAll: true,
		props: {
			label: "name",
			children: "children",
		},
		expandOnClickNode: false,
	} as ElTreeInstance["$props"];
</script>

<template>
	<EditTemplate
		v-bind="props"
		:mode="dialogProps.mode"
		:title="dialogProps.operLabel"
		@before-leave="onBeforeLeave"
		@updating-form-data="onUpdatingFormData"
		@collecting-post-data="onCollectingPostData"
	>
		<template #layouts="props">
			<ElRow class="content">
				<ElCol class="handle">
					<p>权限列表:</p>
					<ElTree
						v-bind="treeProps"
						ref="permisTreeIns"
						:key="time"
						:data="allAuthorizedPermissions"
						:defaultCheckedKeys="checkedIds"
						@check-change="permisTreeCheckChange"
						@mousemove.native="isPermis = true"
						@scroll.native="(e: Event) => onScroll(e, changesTreeIns!)"
						@node-expand="data => treeExpandChange(changesTreeIns!, data)"
						@node-collapse="data => treeExpandChange(changesTreeIns!, data)"
					>
						<template
							#default="{ node, data }"
							v-if="userMode"
						>
							<span>{{ node.label }}</span>
							<span v-if="data.independent">
								<IconFont value="user-line" />
								<IconFont
									value="delete"
									@click="independentPermisClick('delete', node)"
								/>
							</span>
						</template>
					</ElTree>
				</ElCol>
				<ElCol class="change">
					<p>更改列表:</p>
					<ElTree
						v-bind="treeProps"
						ref="changesTreeIns"
						:key="time"
						:data="allAuthorizedPermissions"
						:defaultCheckedKeys="checkedIds"
						@check-change="changesTreeCheckChange"
						@mousemove.native="isPermis = false"
						@scroll.native="(e: Event) => onScroll(e, permisTreeIns!)"
						@node-expand="data => treeExpandChange(changesTreeIns!, data)"
						@node-collapse="data => treeExpandChange(permisTreeIns!, data)"
					>
						<template
							v-if="userMode"
							#default="{ node, data }"
						>
							<span>{{ node.label }}</span>
							<span v-if="data.independent">
								<IconFont value="user-line"></IconFont>
								<IconFont
									value="backward"
									@click="independentPermisClick('revoke', node)"
								></IconFont>
							</span>
						</template>
					</ElTree>
				</ElCol>
			</ElRow>
		</template>
		<template
			v-if="userMode"
			#tips
		>
			<p>用户独立权限的优先级大于所属角色的权限; </p>
			<p><IconFont value="user-line" /> 表示该权限来自用户独立权限, 点击 <IconFont value="delete" /> 可删除该独立权限. </p>
		</template>
	</EditTemplate>
</template>

<style scoped lang="css">
	.authorize {
		flex-direction: column;
		display: flex;
		margin: 0 auto;
	}

	.authorize .content {
		height: 450px;
		display: flex;
		overflow: auto;
		margin-bottom: 2em;
		justify-content: space-between;
	}

	.infor > span {
		margin-right: 2em;
	}

	.content .handle,
	.content .change {
		border-radius: 5px;
		border: 1px solid #cdcdcd;
		height: 100%;
		flex: 0 0 49%;
		display: flex;
		flex-direction: column;
		padding-left: 20px;
	}

	.content .el-tree {
		overflow: auto;
	}

	.el-tree :deep(.iconfont) {
		display: inline-block;
		vertical-align: middle;
		margin-left: 0.5em;
	}

	.handle :deep(.el-tree-node[changed] .icon-delete) {
		display: none;
	}

	.change :deep(.el-tree-node) {
		visibility: hidden;
	}
	.change :deep(.el-tree-node[changed]) {
		visibility: initial;
	}

	.change :deep(.icon-backward) {
		display: none;
	}
	.change :deep(.el-tree-node[independent] .icon-backward) {
		display: initial;
	}
	.change :deep(.el-tree-node[force] .icon-backward) {
		cursor: not-allowed;
	}
</style>
