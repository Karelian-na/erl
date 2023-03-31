<!-- @format -->

<script setup lang="ts">
	import type { ILoading } from "../main";
	import type { Result } from "common/Result";
	import type { FieldsConfig, IFields } from "views/common";

	import EditItem from "views/common/EditItem.vue";
	import { ElCascader, ElRow, ElCol } from "element-plus";
	import AuditTemplate from "views/common/AuditTemplate.vue";
	import ReservationIndex from "../reservations/ReservationIndex.vue";

	import { isArray } from "@vue/shared";
	import { inject, nextTick, ref, watch } from "vue";
	import { axiosRequest, EmptyObject } from "common/utils";
	import { declarationDetailFieldsConfig } from "../FieldsConfigs/DeclarationFieldsConfig";
	import { getFields, IAuthor, authorPropNames } from ".";

	import { paper } from "../FieldsConfigs/static.json";
	import { commonReservationFieldsConfig } from "../FieldsConfigs/ReservationFieldsConfig";

	const props = defineProps<{
		url: string;
		head: string;
	}>();

	const pageLoading = inject<ILoading>("pageLoading")!;

	const fieldsOfEachDeclType: Record<string, IFields> = {};
	const categories = {
		Teaching: "教学成果奖",
		Competition: "竞赛获奖(研究生)",
		Teacher: "教师重要奖项",
		Patent: "专利",
		Paper: "论文",
		Project: "项目",
		Conference: "学术会议",
	};

	const fieldsConfig = ref<FieldsConfig>(EmptyObject);
	const reservationIndexIns = ref<InstanceType<typeof ReservationIndex>>(EmptyObject);

	const fields = ref<IFields>();
	const declarationType = ref<string>("");

	watch(
		() => reservationIndexIns.value?.indexTemplateIns?.dialogProps?.data,
		async (value) => {
			if (!value || !Object.keys(value).length) {
				return;
			}

			if (!isArray(value)) {
				const indexTemplateIns = reservationIndexIns.value.indexTemplateIns;
				const dialogProps = indexTemplateIns.dialogProps;

				fields.value = undefined;
				dialogProps.show = false;
				pageLoading.value = true;
				await nextTick();

				declarationType.value = ["Teaching", "Competition", "Teacher"].includes(value.group) ? "Award" : value.group;
				if (declarationType.value == "Award") {
					value.award_type = value.group;
				}
				fieldsConfig.value = declarationDetailFieldsConfig[value.group];

				const parentUrl = indexTemplateIns.parentUrl;
				let result: Result;
				if (!fieldsOfEachDeclType[value.group]) {
					result = await axiosRequest({
						method: "GET",
						url: `${parentUrl}${declarationType.value}/declare`,
					});

					if (result.success) {
						switch (declarationType.value) {
							case "Award":
								Object.entries(result.data).forEach((entry) => {
									fieldsOfEachDeclType[entry[0]] = getFields(entry[1] as any);
									Object.assign(fieldsOfEachDeclType[entry[0]], indexTemplateIns.allFields);
								});
								break;
							case "Paper":
								result.data = result.data.fields;
							default:
								fieldsOfEachDeclType[declarationType.value] = getFields(result.data);
								Object.assign(fieldsOfEachDeclType[declarationType.value], indexTemplateIns.allFields);
								break;
						}
					}
				}

				await nextTick();

				result = await axiosRequest({
					method: "GET",
					url: `${parentUrl}audit`,
					params: {
						id: value.ref_id,
						group: value.group,
					},
				});

				fields.value = fieldsOfEachDeclType[value.group] ?? EmptyObject;
				if (result.success) {
					delete result.data.id;

					switch (declarationType.value) {
						case "Conference":
							result.data.location = result.data.location.split(",");
							break;
						case "Project":
							result.data.start_date = [result.data.start_date, result.data.end_date];
							break;
						case "Paper":
							result.data.authors = (result.data.authors as string).split(",").map((item, idx) => {
								const [name, role] = item.split(":");
								const author = {} as IAuthor;
								if (name.endsWith(")")) {
									author.order = paper.authorOrder.corresponding.value;
									author.name = name.substring(0, name.indexOf("("));
								} else {
									author.order = idx + 1;
									author.name = name;
								}
								author.role = parseInt(role);
								return author;
							});
						default:
							break;
					}
					value = Object.assign(value, result.data);
				}

				dialogProps.show = true;
				pageLoading.value = false;
			} else {
				const injectFields = reservationIndexIns.value.indexTemplateIns.allFields;
				fields.value = {
					audit_status: injectFields.audit_status,
					comment: injectFields.comment,
				};
				fieldsConfig.value = commonReservationFieldsConfig;
			}
		}
	);
</script>

<template>
	<ReservationIndex
		v-bind="props"
		ref="reservationIndexIns"
	>
		<template #group="[_field, data]">
			<span>{{ categories[data.group as keyof typeof categories] }}</span>
		</template>

		<template #editContent="{ dialogProps, url }">
			<AuditTemplate
				v-if="['audit', 'details'].includes(dialogProps.mode) && fields"
				:url="url"
				:head="head"
				:fields="fields"
				:raw-data="dialogProps.data"
				:fields-config="fieldsConfig"
			>
				<template
					v-if="declarationType === 'Conference'"
					#location="{ fieldConfig, formData, disabled }"
				>
					<ElCascader
						v-model="formData.location"
						:disabled="disabled"
						v-bind="fieldConfig.bindProps"
					/>
				</template>

				<template
					v-if="declarationType === 'Paper'"
					#authors="{ fieldConfig, formData }"
				>
					<ElRow
						v-for="author in formData.authors"
						class="author"
					>
						<template v-for="config, key in fieldConfig.children!">
							<ElCol
								v-if="key !== 'uid'"
								:span="8"
							>
								<EditItem
									v-model="author[key]"
									:field="({
										display_name: authorPropNames[key],
										editable: false,
									} as any)"
									:bind-props="{ labelWidth: 'max-content' }"
									:field-config="config"
								/>
							</ElCol>
						</template>
					</ElRow>
				</template>
			</AuditTemplate>
		</template>
	</ReservationIndex>
</template>

<style scoped lang="css">
	.el-row.author:not(:last-child) {
		margin-bottom: 0.5em;
	}
	.el-row .el-col:not(:last-child) {
		padding-right: 1em;
	}
</style>
