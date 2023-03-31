<!-- @format -->
<script setup lang="ts">
	import type { ILoading } from "../main";
	import type { BeforeSubmitHandler, DataChangedCallback, IDialogProps, KeyStringObject, UserInfo } from "views/common";

	import InfoBind from "./InfoBind.vue";
	import RevisePwd from "./revisepwd.vue";
	import IconFont from "components/IconFont.vue";
	import EditItem from "views/common/EditItem.vue";
	import EditTemplate from "views/common/EditTemplate.vue";
	import { axiosRequest, EmptyObject, error, uploadFile } from "common/utils";

	import { userFields, securityOptions } from ".";
	import { userFieldsConfig } from "../FieldsConfigs/UserFieldsConfig";
	import { inject, nextTick, onBeforeMount, ref, provide, reactive, Ref } from "vue";
	import { ElSpace, ElCard, ElButton, ElContainer, ElAside, ElMain, ElCol, ElRow, ElDialog } from "element-plus";

	const userInfo = inject<Ref<UserInfo>>("userInfo")!;
	const pageLoading = inject<ILoading>("pageLoading")!;

	const infoMode = ref("");
	const rawData = ref<KeyStringObject>();
	const editTemplateIns = ref<InstanceType<typeof EditTemplate>>(EmptyObject);
	const dialogProps = reactive<Partial<IDialogProps>>({
		show: false,
		mode: "details",
	});

	provide("dialogProps", dialogProps);

	onBeforeMount(async () => {
		pageLoading.value = true;
		await nextTick();

		const result = await axiosRequest({
			method: "GET",
			url: "/Users/self/index",
		});

		if (!result.success) {
			error("msg", { message: "获取信息失败! 请尝试刷新页面!" });
			return;
		}

		rawData.value = result.data;

		pageLoading.value = false;
	});

	const onBeforeSubmit: BeforeSubmitHandler = async function (postData) {
		if (postData.avatar) {
			if (Object.keys(postData.avatar).length === 0) {
				postData.avatar = "";
				return true;
			}

			const value = await uploadFile(postData.avatar, pageLoading);

			if (value === "") {
				postData = {};
				return false;
			}
			postData.avatar = value;
		}

		return true;
	};

	const onUpdatedData: DataChangedCallback = function (action, data) {
		switch (action) {
			case "edit":
				dialogProps.mode = "details";
				(["name", "avatar"] as (keyof UserInfo)[]).forEach((item) => {
					if (data[item]) {
						userInfo.value[item] = data[item];
					}
				});
				return true;
			default:
				break;
		}
		return false;
	};

	function onMsgOperButtonClick() {
		if (dialogProps.mode === "details") {
			dialogProps.mode = "edit";
		} else {
			dialogProps.mode = "details";
		}
	}

	function onSecurityButtonClick(mode: string) {
		infoMode.value = mode;
		dialogProps.show = true;
		switch (mode) {
			case "pwd":
				dialogProps.operLabel = "密码修改";
				break;
			default:
				dialogProps.operLabel = securityOptions[mode].title + "绑定";
				break;
		}
	}
</script>

<template>
	<EditTemplate
		v-if="rawData"
		mode="edit"
		title="修改"
		ref="editTemplateIns"
		url="/Users/self/edit"
		:raw-data="rawData"
		:fields="(userFields as any)"
		:fields-config="userFieldsConfig"
		:class="{ details: dialogProps.mode === 'details' }"
		@updated-data="onUpdatedData"
		@before-submit="onBeforeSubmit"
	>
		<template #layouts="{ formData }">
			<ElSpace direction="vertical">
				<EditItem
					v-model="formData.avatar"
					:field="userFields.avatar"
					:field-config="userFieldsConfig.avatar"
					:bind-props="{ label: '', labelWidth: '0', class: 'msg' }"
				>
					<template #upload="{ src }">
						<img :src="src" />
					</template>
					<template #extra>
						<div class="info">
							<EditItem
								v-for="fieldName in ['name', 'id', 'profile']"
								v-model="formData[fieldName]"
								:field="userFields[fieldName]"
								:field-config="userFieldsConfig[fieldName]"
								:bind-props="{ class: fieldName, labelWidth: 'max-content' }"
							></EditItem>
						</div>
					</template>
				</EditItem>
				<ElCard>
					<template #header>
						<span>基本信息</span>
						<span class="operation">
							<ElButton @click="onMsgOperButtonClick">{{ dialogProps.mode === "details" ? "编辑" : "退出" }}</ElButton>
							<ElButton
								type="primary"
								v-if="dialogProps.mode === 'edit'"
								@click="editTemplateIns.submit"
								>立即修改
							</ElButton>
						</span>
					</template>
					<EditItem
						v-for="fieldName in ['age', 'gender', 'clan', 'political_status', 'phone', 'email', 'roles', 'add_time']"
						v-model="formData[fieldName]"
						:field="userFields[fieldName]"
						:field-config="userFieldsConfig[fieldName]"
					></EditItem>
				</ElCard>
				<ElCard class="security">
					<template #header>账号安全</template>
					<ElContainer v-for="key in Object.keys(securityOptions).filter((item) => item !== 'pwd')">
						<ElAside>
							<IconFont :value="securityOptions[key].icon"></IconFont>
						</ElAside>
						<ElMain>
							<ElRow>
								<ElCol
									:span="8"
									class="tip"
								>
									<p class="name">{{ securityOptions[key].title }}绑定</p>
									<p class="">绑定{{ securityOptions[key].title }}后可使用{{ securityOptions[key].title }}登录</p>
									<p
										class="state"
										:class="formData[key] ? 'has-value' : ''"
										>{{ formData[key] ? "当前已绑定" : "暂未绑定" }}
									</p>
								</ElCol>
								<ElCol
									:span="12"
									class="value"
								>
									{{ formData[key] }}
								</ElCol>
								<ElCol
									:span="4"
									class="operation"
								>
									<ElButton @click="onSecurityButtonClick(key)">去{{ formData[key] ? "修改" : "绑定" }}</ElButton>
								</ElCol>
							</ElRow>
						</ElMain>
					</ElContainer>
					<ElContainer>
						<!-- 密码 -->
						<ElAside>
							<IconFont value="pwd"></IconFont>
						</ElAside>
						<ElMain>
							<ElRow>
								<ElCol
									:span="8"
									class="tip"
								>
									<p class="name">密码安全</p>
									<p>设置更复杂的密码可提高账号安全性</p>
								</ElCol>
								<ElCol :span="12"></ElCol>
								<ElCol
									:span="4"
									class="operation"
								>
									<ElButton @click="onSecurityButtonClick('pwd')">去修改</ElButton>
								</ElCol>
							</ElRow>
						</ElMain>
					</ElContainer>
					<ElDialog
						draggable
						destroy-on-close
						width="max-content"
						v-model="dialogProps.show"
						:close-on-click-modal="false"
						:title="`账号安全-${dialogProps.operLabel}`"
					>
						<InfoBind
							v-if="infoMode !== 'pwd'"
							:type="infoMode"
							v-model:secret-serial="editTemplateIns.formData[infoMode]"
						/>
						<RevisePwd
							v-else
							mode="revise"
						/>
					</ElDialog>
				</ElCard>
			</ElSpace>
		</template>

		<template #operations></template>
	</EditTemplate>
</template>

<style scoped lang="css">
	.edit {
		padding: 0 10vw;
		padding-bottom: 4em;
	}

	.edit .el-space,
	.edit .el-space :deep(.el-space__item) {
		width: 100%;
	}

	.el-space :deep(.msg > .el-form-item__content) {
		justify-content: center;
	}

	.el-space :deep(.msg .image) {
		height: 200px;
		width: 200px;
		flex-shrink: 0;
	}
	.el-space :deep(.msg .info) {
		height: inherit;
		padding-left: 2em;
		width: 25em;
		transition: 0.5s cubic-bezier(0.075, 0.82, 0.165, 1);
	}
	.edit:not(.details) .el-space :deep(.msg .info) {
		flex-grow: 1;
	}

	.el-space :deep(.image .el-upload) {
		border-radius: 50%;
	}
	.el-space :deep(.image .el-upload img) {
		object-fit: cover;
	}

	.info .name {
		font-size: 25px;
		line-height: 2em;
	}
	.info .id {
		font-size: 18px;
	}
	.info .profile {
		line-height: 1.5em;
	}

	.name :deep(.el-input) {
		height: 2em;
	}
	.profile :deep(.el-form-item__content) {
		height: 5.5em;
		overflow: auto;
	}
	.profile :deep(.el-form-item__content::-webkit-scrollbar) {
		display: none;
	}

	.el-space .el-card .operation {
		float: right;
	}

	.el-card .operation .el-button {
		margin-left: 2em;
	}

	.el-card .el-aside {
		padding: 20px;
		display: flex;
		align-items: center;
		width: max-content;
		padding-right: 0;
	}

	.el-card .el-main .name {
		font-size: 20px;
	}

	.el-card.security .el-aside .iconfont {
		font-size: 72px;
	}
	.el-card.security .el-main .tip p {
		line-height: 1.5;
	}
	.el-card.security .el-main .tip .state {
		color: var(--el-color-danger);
	}
	.el-card.security .el-main .tip .state.has-value {
		color: var(--el-color-primary);
	}
	.el-card.security .el-main .value {
		display: flex;
		align-items: center;
		justify-content: center;
	}
	.el-card.security .el-main .operation {
		display: flex;
		align-items: center;
		justify-content: flex-end;
	}
</style>
