<!-- @format -->

<script setup lang="ts">
	import type { AwardType, IAuthor } from ".";
	import type { RefreshedDataCallback } from "views/common";

	import DeclarationDetail from "./detail.vue";
	import IndexTemplate from "views/common/IndexTemplate.vue";

	import { onBeforeMount, ref, watch } from "vue";
	import { paper } from "../FieldsConfigs/static.json";

	const props = defineProps<{
		url: string;
		head: string;
	}>();

	const declarationType = ref("");
	const awardType = ref<AwardType>("Teaching");

	let paperAuthorView = false;

	const onDataRefreshed: RefreshedDataCallback = function (pageData) {
		if (declarationType.value === "Paper") {
			if (!paperAuthorView) {
				pageData.data.forEach((item) => {
					item.authors = (item.authors as string).split(",").map((value, idx) => {
						const [name, role] = value.split(":");
						return {
							name: name.endsWith(")") ? name.substring(0, name.indexOf("(")) : name,
							role: parseInt(role),
							order: name.endsWith(")") ? paper.authorOrder.corresponding.value : idx + 1,
						} as IAuthor;
					});
				});
			}
		}
	};

	watch(() => props.url, refresh);

	onBeforeMount(refresh);

	function refresh() {
		const matches = props.url.match(/^\/([\w]+)\/([\w]+\/)*index$/);
		if (!matches) {
			return;
		}

		const group = matches[1];
		switch (group) {
			case "Awards":
				awardType.value = matches[2].substring(0, matches[2].length - 1) as any;
				break;
			case "Papers":
				paperAuthorView = matches[2] !== undefined;
				break;
			default:
				break;
		}

		declarationType.value = group.substring(0, group.length - 1);
	}
</script>

<template>
	<IndexTemplate
		ref="indexTemplateIns"
		v-bind="props"
		@data-refreshed="onDataRefreshed"
	>
		<!-- Paper -->
		<template
			v-if="declarationType === 'Paper'"
			#authors="[_field, data]"
		>
			<p v-for="item in (data.authors as IAuthor[])">
				<span>{{ item.name }} : </span>
				<span>{{ paper.authorRole[item.role as 1].label }}</span>
			</p>
		</template>

		<!-- paper author view -->
		<template
			v-if="declarationType === 'Paper'"
			#author="[_field, data]"
		>
			{{ data.author + `(第${data.order}作者${data.is_corresponding ? "/通讯作者" : ""})` }}
		</template>
		<template
			v-if="declarationType === 'Paper'"
			#role="[_field, data]"
		>
			{{ paper.authorRole[data.role as 1].label }}
		</template>

		<template
			v-if="declarationType === 'Paper'"
			#pub_year="[_field, data]"
		>
			{{ data.pub_year }}, {{ data.pub_term }}({{ data.pub_vol }})
		</template>

		<!-- common -->
		<template #enclosures="[_field, data]">
			<template v-if="data.enclosures">
				<p
					v-for="item in data.enclosures.split(';')"
					class="enclosure-item"
				>
					<a
						target="__blank"
						:href="item"
						>{{ item.substring(item.lastIndexOf("/") + 1) }}
					</a>
				</p>
			</template>
			<span
				v-else
				class="enclosure-item"
				>无附件
			</span>
		</template>

		<template #editContent="{ dialogProps, fieldsConfig }">
			<DeclarationDetail
				v-if="dialogProps.mode === 'details'"
				:template-props="{
					url: props.url,
					rawData: dialogProps.data,
					fieldsConfig: fieldsConfig,
				}"
				:award-type="awardType"
				:declaration-type="declarationType"
			/>
		</template>
	</IndexTemplate>
</template>

<style scoped lang="css">
	.enclosure-item {
		color: var(--el-color-primary);
		line-height: 2.5em;
		width: max-content !important;
	}
</style>
