<!-- @format -->

<script setup lang="ts">
	import type { IDialogProps, UpdatingFormDataHandler, CollectingPostDataHandler, FieldsConfig, EnumItem, PreparedCallback } from "views/common";

	import UiTag from "components/UiTag.vue";
	import IconFont from "components/IconFont.vue";
	import IconSelector from "components/IconSelector.vue";
	import EditTemplate from "views/common/EditTemplate.vue";
	import { ElInput, ElSelect, ElOption } from "element-plus";

	import { EmptyObject } from "common/utils";
	import { inject, ref, computed, nextTick } from "vue";
	import { MenuType, MenuTypeFields, MenuTypeNames, DetailMenuItem } from ".";

	const props = defineProps<{
		url: string;
		fieldsConfig: FieldsConfig;
		rawData: DetailMenuItem & { canBeParent: DetailMenuItem[] };
	}>();

	const dialogProps = inject<IDialogProps>("dialogProps")!;

	const editTemplate = ref<InstanceType<typeof EditTemplate>>(EmptyObject);

	let pType = 0;

	function isTypeDisabled(type: number) {
		if (props.rawData.id) {
			return true;
		}
		switch (pType) {
			case 0:
				return type >= MenuType.Page;
			case MenuType.Menu:
				return type == MenuType.Page;
			case MenuType.Item:
			case MenuType.Page:
				return type <= pType;
			default:
				return true;
		}
	}

	function onParentChange(pid: number) {
		if (!props.rawData.id) {
			let newUrl = "#";
			if (pid) {
				const permi = props.rawData.canBeParent.find((val) => val.id == pid)!;
				if (permi.type != pType) {
					pType = permi.type;
					props.fieldsConfig.type.enumItems?.forEach((item, idx) => {
						(item as EnumItem).disabled = isTypeDisabled(idx + 1);
					});
				}
				newUrl = permi.url;
			} else {
				pType = 0;
			}
			nextTick(() => {
				editTemplate.value.updateFormData({
					url: newUrl,
					type: pType + 1,
				});
			});
		}
	}

	function isParentDisabled(pType: number | null) {
		if (!props.rawData.id) {
			if (!props.rawData.pid) {
				return false; // add
			}
			return true; // add with parent
		}

		return props.rawData.type == MenuType.Page && pType == MenuType.Menu; // edit
	}

	const onTemplatePrepared: PreparedCallback = function (formData) {
		if (dialogProps.mode === "details") {
			return;
		}
		props.fieldsConfig.type.bindProps.disabled = Boolean(props.rawData.id);
		props.fieldsConfig.type.enumItems!.forEach((item, idx) => {
			(item as EnumItem).disabled = isTypeDisabled(idx + 1);
		});

		props.fieldsConfig.pid.bindProps.onChange = onParentChange;
		props.fieldsConfig.pid.bindProps.disabled = Boolean(!props.rawData.id && props.rawData.pid);
		props.fieldsConfig.pid.enumItems = [
			{
				value: 0,
				label: "无父权限",
				disabled: isParentDisabled(null),
			} as EnumItem,
		].concat(
			props.rawData.canBeParent.reduce((prev, cur) => {
				prev.push({
					value: cur.id,
					label: cur.name,
					disabled: isParentDisabled(cur.type),
					type: cur.type,
					level: cur.level,
				});
				return prev;
			}, [] as EnumItem[])
		);

		props.fieldsConfig.oper_type.bindProps = {
			disabled: computed(() => formData.value.type != MenuType.Oper),
		};
	};

	const onUpdatingFormData: UpdatingFormDataHandler = function (formData) {
		delete formData.value.children;
		delete formData.value.parent;
		delete formData.value.canBeParent;

		formData.value.oper_type = props.rawData.oper_type ?? 0;
		formData.value.pid = props.rawData.pid ?? 0;
		formData.value.type = props.rawData.type;

		if (!props.rawData.id && formData.value.pid) {
			const permi = props.rawData.canBeParent.find((val) => val.id == formData.value.pid)!;
			pType = permi.type;
			formData.value.url = permi.url;
			formData.value.type = pType + 1;
		}
	};

	const onCollectingPostData: CollectingPostDataHandler = function (postData) {
		if (!Object.keys(postData.value).length) {
			return;
		}

		if ("edit" == dialogProps.mode) {
			postData.value.pid === undefined && (postData.value.pid = props.rawData.pid);
			postData.value.id = props.rawData.id;
		}

		postData.value.pid === 0 && (postData.value.pid = null);
		postData.value.oper_type === 0 && (postData.value.oper_type = null);
	};
</script>

<template>
	<EditTemplate
		ref="editTemplate"
		v-bind="props"
		:mode="dialogProps.mode"
		:title="dialogProps.operLabel"
		@prepared="onTemplatePrepared"
		@updating-form-data="onUpdatingFormData"
		@collecting-post-data="onCollectingPostData"
	>
		<template #icon="{ disabled, fieldConfig, formData }">
			<p v-if="dialogProps.mode === 'details'">
				<IconFont :value="formData.icon" />
				<span>&nbsp;{{ formData.icon }}</span>
			</p>
			<IconSelector
				v-else
				v-model="formData.icon"
				req-url="https://at.alicdn.com/t/c/font_3165301_0wgsq9nl1nmb.json"
			>
				<ElInput
					v-model="formData.icon"
					:disabled="disabled"
					v-bind="fieldConfig.bindProps"
				>
					<template #prepend>
						<IconFont :value="formData.icon"></IconFont>
					</template>
				</ElInput>
			</IconSelector>
		</template>

		<template #type="{ disabled, fieldConfig, formData }">
			<p v-if="dialogProps.mode === 'details'">
				<UiTag
					class="type"
					:class="MenuTypeFields[formData.type]"
					:icon="MenuTypeFields[formData.type]"
					:label="fieldConfig.enumItems?.find((item) => item.value === formData.type)?.label ?? ''"
				/>
			</p>
			<ElSelect
				v-else
				v-model="formData.type"
				:disabled="disabled"
				v-bind="fieldConfig.bindProps"
			>
				<ElOption
					v-for="item in fieldConfig.enumItems"
					:label="item.label"
					:value="item.value"
					:disabled="(item.disabled as any)"
				>
					<UiTag
						class="type"
						:class="MenuTypeFields[item.value as number]"
						:icon="MenuTypeFields[item.value as number]"
						:label="item.label"
					/>
				</ElOption>
			</ElSelect>
		</template>

		<template #pid="{ disabled, fieldConfig, formData }">
			<template v-if="dialogProps.mode === 'details'">
				<span v-if="!formData.pid">无父权限</span>
				<span v-else>{{ fieldConfig.enumItems?.find((item) => item.value === formData.item)?.label }}</span>
			</template>
			<ElSelect
				v-else
				v-model="formData.pid"
				:disabled="disabled"
				v-bind="fieldConfig.bindProps"
			>
				<ElOption
					v-for="item in fieldConfig.enumItems"
					:label="item.label"
					:value="item.value"
					:disabled="(item.disabled as any)"
				>
					<template v-if="item.value != 0">
						<UiTag
							class="type"
							:class="MenuTypeFields[item.type]"
							:icon="MenuTypeFields[item.type]"
							:label="MenuTypeNames[item.type]"
						/>
						<span :style="`margin-left: ${23 * item.level}px`">{{ `|—${item.label}` }}</span>
					</template>
					<template v-else>无父权限</template>
				</ElOption>
			</ElSelect>
		</template>

		<template #tips>
			<p>1. 若当前权限类型为菜单或选项, 则其父权限类型必须为菜单或无;</p>
			<p>2. 若当前权限类型为标签, 则其父权限类型必须为选项;</p>
			<p>3. 若当前权限类型为操作, 则其父权限类型不能为操作;</p>
			<p>4. 当操作类型权限的父权限类型为菜单或选项时, 该权限对其所有兄弟权限生效.</p>
		</template>
	</EditTemplate>
</template>

<style scoped lang="css">
	.el-form-item :deep(.el-input-group__prepend .iconfont) {
		line-height: 1em;
	}

	.el-select.type {
		width: 8.5em;
	}
</style>
