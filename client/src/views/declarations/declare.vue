<!-- @format -->
<script setup lang="ts">
	import DeclarationDetail from "./detail.vue";
	import type { ILoading } from "../main";
	import type { Result } from "common/Result";
	import type {
		BeforeSubmitHandler,
		CollectingPostDataHandler,
		DataChangedCallback,
		FieldsConfig,
		IFields,
		KeyStringObject,
		UpdatingFormDataHandler,
		UserInfo,
	} from "views/common";

	import { getFields, IAuthor } from ".";
	import { computed, inject, nextTick, onBeforeMount, Ref, ref, watch } from "vue";
	import { axiosRequest, EmptyObject, info, requiredRule, uploadFile } from "common/utils";
	import { declarationDetailFieldsConfig } from "../FieldsConfigs/DeclarationFieldsConfig";

	import { paper, patent, award } from "../FieldsConfigs/static.json";

	const props = defineProps<{
		url: string;
		head: string;
	}>();

	const userInfo = inject<Ref<UserInfo>>("userInfo")!;
	const pageLoading = inject<ILoading>("pageLoading")!;

	const fieldsOfEachDeclType: Record<string, IFields> = {};

	const fields = ref<IFields>();
	const awardType = ref("Teaching");
	const fieldsConfig = ref<FieldsConfig>();
	const detailIns = ref<InstanceType<typeof DeclarationDetail>>(EmptyObject);
	const declarationType = computed(() => props.url.match("/Declarations/(.*)/declare")![1]);

	onBeforeMount(refresh);

	watch(() => awardType.value, refresh);
	watch(() => declarationType.value, refresh);

	const onBeforeSubmit: BeforeSubmitHandler = async function (postData) {
		if (postData.enclosures) {
			if (postData.enclosures.length == 0) {
				postData.enclosures = "";
				return true;
			}

			const value = await uploadFile(postData.enclosures, pageLoading);

			if (value === "") {
				postData = {};
				return false;
			}
			postData.enclosures = value;
		}

		return true;
	};

	const onUpdatingFormData: UpdatingFormDataHandler = function (formData) {
		switch (declarationType.value) {
			case "Conference":
				formData.value.parti_name = userInfo.value.name;
				break;
			case "Paper":
				formData.value.authors = [
					{
						name: userInfo.value.name,
						order: paper.authorOrder[1].value,
					},
				];
			default:
				formData.value.author = userInfo.value.name;
				break;
		}
	};

	const onCollectingPostData: CollectingPostDataHandler = function (postData) {
		if (!Object.keys(postData.value).length) {
			return;
		}

		const newPostData = {} as KeyStringObject;
		switch (declarationType.value) {
			case "Award":
				if (awardType.value !== "Teaching") {
					postData.value.role = award.competition.role[awardType.value == "Competition" ? "postGraduate" : "teacher"].value;
				}
				newPostData.awardType = awardType.value;
				break;
			case "Conference":
				postData.value.location = postData.value.location.join(",");
				break;
			case "Paper":
				(postData.value.authors as IAuthor[]).forEach((item, idx) => {
					if (item.order === Object.keys(paper.authorOrder).length) {
						item.order = idx + 1;
						item.is_corresponding = true;
					}
				});
				break;
			case "Project":
				postData.value.end_date = postData.value.start_date[1];
				postData.value.start_date = postData.value.start_date[0];
				break;
			default:
				break;
		}

		["message", "enclosures"].forEach((name) => {
			if (postData.value[name]) {
				newPostData[name] = postData.value[name];
				delete postData.value[name];
			}
		});

		newPostData.jsonData = JSON.stringify(postData.value);

		postData.value = newPostData;
		return;
	};

	const onUpdatedData: DataChangedCallback = function (action, data, all) {
		detailIns.value.editTemplateIns.formIns.resetFields();
		return true;
	};

	async function refresh() {
		fields.value = undefined;
		fieldsConfig.value = undefined;
		pageLoading.value = true;
		await nextTick();

		const group = declarationType.value === "Award" ? awardType.value : declarationType.value;
		fieldsConfig.value = declarationDetailFieldsConfig[group];

		let result: Result;
		if (!fieldsOfEachDeclType[group]) {
			result = await axiosRequest({
				method: "GET",
				url: props.url,
			});

			if (result.success) {
				switch (declarationType.value) {
					case "Award":
						Object.entries(result.data).forEach((entry) => {
							const fields = entry[1] as IFields[""][];
							fields.forEach((item) => (item.editable = true));
							fieldsOfEachDeclType[entry[0]] = getFields(fields);
						});
						break;
					case "Paper":
						fieldsConfig.value.disp.enumItems = result.data.disciplines.map((item: any) => ({ value: item.id, label: item.name }));
						fieldsConfig.value.authors.children!.uid.bindProps = {
							onBlur: async (e: FocusEvent) => {
								const input = e.target as HTMLInputElement;

								let value = input.value;
								if (!value || !(value = value.trim()) || value.length < 6) {
									return;
								}

								const result = await axiosRequest({
									url: "/Users/paperAuthor",
									method: "GET",
									params: {
										id: input.value,
									},
								});

								if (result.success) {
									const authors = detailIns.value.editTemplateIns.formData.authors as IAuthor[];
									const idx = authors.findIndex((item) => item.uid === input.value);
									if (idx !== -1) {
										Object.assign(authors[idx], result.data);
									}
								} else {
									info("msg", { message: `未找到ID为${input.value}的用户的信息!` });
								}
							},
						};
						result.data = result.data.fields;
						break;
					case "Project":
						const fields = getFields(result.data);
						Object.values(fields).forEach((item) => (item.editable = true));
						fieldsConfig.value.start_date.bindProps.startPlaceholder = "请选择" + fields.start_date.display_name;
						fieldsConfig.value.start_date.bindProps.endPlaceholder = "请选择" + fields.end_date.display_name;
						fields.start_date.display_name = "项目起止时间";
						fieldsOfEachDeclType[declarationType.value] = fields;
						break;
					case "Patent":
						fieldsConfig.value.auth_date.bindProps.disabled = computed(() => {
							const formData = detailIns.value.editTemplateIns?.formData;
							if (!formData?.status) {
								return true;
							}
							return formData.status < patent.status.empower.value;
						});
						fieldsConfig.value.auth_date.rule = {
							validator(rule, value, callback, source, options) {
								const auth_date = fieldsConfig.value?.auth_date;
								if (!auth_date) {
									callback();
								} else if (!auth_date.bindProps.disabled) {
									callback(new Error(requiredRule.message));
								} else {
									callback();
								}
							},
						};
						break;
					default:
						break;
				}

				if (!["Award", "Project"].includes(declarationType.value)) {
					const fields = result.data as IFields[""][];
					fields.forEach((item) => (item.editable = true));
					fieldsOfEachDeclType[declarationType.value] = getFields(result.data);
				}
			}
		}

		fields.value = fieldsOfEachDeclType[group] ?? EmptyObject;
		pageLoading.value = false;
	}
</script>

<template>
	<DeclarationDetail
		ref="detailIns"
		:template-props="{
			rawData: {},
			url: props.url,
			fields: fields,
			fieldsConfig: fieldsConfig!,
			onUpdatedData: onUpdatedData,
			onBeforeSubmit: onBeforeSubmit,
			onUpdatingFormData: onUpdatingFormData,
			onCollectingPostData: onCollectingPostData,
		}"
		v-model:award-type="awardType"
		:declaration-type="declarationType"
	/>
</template>

<style scoped lang="css">
	.edit-template.add {
		padding: 0 30px;
	}

	.el-row.author:not(:last-child) {
		margin-bottom: 1.5em;
	}

	.edit-template.add .ui-button {
		padding: 0 1em;
	}
	.ui-button.add {
		color: white;
		width: max-content;
		background-color: var(--el-color-primary);
		margin-bottom: 1em;
	}

	.ui-button.delete {
		margin-left: 0.5em;
		height: 2.5em;
		vertical-align: baseline;
		background-color: white;
		border: var(--el-border);
		padding: 0 5px;
	}

	.ui-button :deep(.icon-delete) {
		margin-right: 0;
	}
</style>
