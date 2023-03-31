<!-- @format -->

<script setup lang="ts">
	import type { CollectingPostDataHandler, IDialogProps, KeyStringObject, UpdatingFormDataHandler } from "views/common";

	import { ElTransfer, ElFormItem } from "element-plus";
	import EditTemplate from "views/common/EditTemplate.vue";
	import { inject, nextTick, onBeforeMount, ref } from "vue";
	import { axiosRequest } from "common/utils";
	import { isArray } from "@vue/shared";

	interface Option {
		key: number;
		label: string;
	}

	const props = defineProps<{
		url: string;
		title: string;
		rawData: KeyStringObject; // may be array
	}>();

	const dialogProps = inject<IDialogProps>("dialogProps")!;

	const roles = ref<Option[]>([]);
	const assignedRoles = ref<number[]>([]);

	const onUpdatingFormData: UpdatingFormDataHandler = async function (formData) {
		dialogProps.loading = true;
		await nextTick();
		const result = await axiosRequest({
			url: `${props.url}assign`,
			method: "GET",
			params: {
				ids: (props.rawData as Array<KeyStringObject>).map((item) => item.id).join(","),
			},
		});
		dialogProps.loading = false;

		if (result.success) {
			roles.value = (result.data.roles as Array<KeyStringObject>).map((item) => ({
				key: item.id,
				label: item.name,
			}));
			assignedRoles.value = result.data.common;
			formData.value = {
				roles: JSON.parse(JSON.stringify(result.data.common)),
			};
		}
	};

	const onCollectingPostData: CollectingPostDataHandler = function (postData) {
		const newPostData = {
			ids: isArray(props.rawData) ? props.rawData.map((item) => item.id) : [props.rawData.id],
			auths: {} as KeyStringObject,
		};
		const data = postData.value.roles as Array<number>;
		assignedRoles.value.forEach((item) => {
			const idx = data.indexOf(item);
			if (idx === -1) {
				newPostData.auths[item] = 0;
			} else {
				data.splice(idx, 1);
			}
		});
		data.forEach((item) => {
			newPostData.auths[item] = 1;
		});

		postData.value = newPostData;
	};
</script>

<template>
	<EditTemplate
		v-bind="props"
		title="修改"
		mode="assign"
		:fields-config="{}"
		@updating-form-data="onUpdatingFormData"
		@collecting-post-data="onCollectingPostData"
	>
		<template #layouts="{ fields, formData }">
			<ElFormItem>
				<ElTransfer
					v-model="formData.roles"
					:data="roles"
					:titles="['角色列表', '已赋予的角色']"
				></ElTransfer>
			</ElFormItem>
		</template>
	</EditTemplate>
</template>

<style scoped lang="css"></style>
