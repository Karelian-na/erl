<!-- @format -->

<script setup lang="ts">
	import { CollectEnd, CollectingPostDataHandler, UserInfo } from "views/common";

	import IconFont from "components/IconFont.vue";
	import EditItem from "views/common/EditItem.vue";
	import EditTemplate from "views/common/EditTemplate.vue";
	import { ElContainer, ElHeader, ElMain, ElButton } from "element-plus";

	import Store from "store";
	import { sha256 } from "js-sha256";
	import { useRouter } from "vue-router";
	import { inject, onBeforeMount, ref, Ref } from "vue";
	import { loginFields, loginFieldsConfig } from ".";
	import { axiosRequest, error, success } from "common/utils";

	const userInfo = inject<Ref<UserInfo | undefined>>("userInfo")!;

	const loginLoading = ref(false);

	const router = useRouter();
	const bindProps = { label: "", labelWidth: "0" };
	const formData = { account: "", pwd: "", remember: false };

	onBeforeMount(() => {
		userInfo.value = undefined;
		const infoStore = Store.namespace("info");
		formData.account = infoStore.get("account");
		formData.pwd = infoStore.get("pwd");
		formData.remember = infoStore.get("remember");
	});

	const onCollectingPostData: CollectingPostDataHandler = function (postData) {
		if (!Object.keys(postData.value).length) {
			return;
		}

		loginLoading.value = true;
		const pwd = postData.value.pwd;
		postData.value.pwd = sha256(postData.value.pwd);
		const data = Object.assign({}, postData.value);
		postData.value = {};
		setTimeout(async () => {
			const result = await axiosRequest({
				method: "POST",
				url: "/login",
				data: data,
			});

			loginLoading.value = false;

			if (result.success) {
				const infoStore = Store.namespace("info");
				infoStore.set("account", data.account);
				if (data.remember === true) {
					infoStore.set("pwd", pwd);
					infoStore.set("remember", data.remember);
				} else {
					infoStore.remove("pwd");
					infoStore.remove("remember");
				}

				Store.namespace("cookie").set("value", true);
				router.push("/index");
				success("msg", { message: "登陆成功!" });
			} else {
				error("msg", {
					message: `登陆失败! ${result.msg}`,
					duration: 5000,
				});
			}
		}, 10);
		return CollectEnd;
	};

	function onForgetPasswordClick() {
		if (!loginLoading.value) {
			router.push(`/retrieve?account=${formData.account}`);
		}
	}
</script>

<template>
	<div class="cover">
		<ElContainer class="position-center">
			<ElHeader>登陆</ElHeader>
			<ElMain>
				<EditTemplate
					url="/login"
					title="登陆"
					mode="login"
					oper-action="add"
					:raw-data="formData"
					:fields="loginFields"
					:fields-config="loginFieldsConfig"
					@collecting-post-data="onCollectingPostData"
				>
					<template #layouts="{ formData, fields }">
						<EditItem
							v-model="formData.account"
							:field="fields.account"
							:field-config="loginFieldsConfig.account"
							:bind-props="bindProps"
						>
							<template #prepend>
								<IconFont value="user-line"></IconFont>
							</template>
						</EditItem>
						<EditItem
							class="pwd"
							v-model="formData.pwd"
							:field="fields.pwd"
							:field-config="loginFieldsConfig.pwd"
							:bind-props="bindProps"
						>
							<template #prepend>
								<IconFont value="password"></IconFont>
							</template>
						</EditItem>
						<EditItem
							class="remember"
							v-model="formData.remember"
							:field="fields.remember"
							:field-config="loginFieldsConfig.remember"
							:bind-props="bindProps"
						></EditItem>
					</template>

					<template #operations="{ onSubmit }">
						<ElButton
							type="primary"
							:loading="loginLoading"
							@click="onSubmit"
							>立即登录
						</ElButton>
						<span
							class="link"
							@click="onForgetPasswordClick"
							>忘记密码 |
						</span>
						<span
							class="link"
							@click="onForgetPasswordClick"
						>
							&nbsp;注册账号
						</span>
					</template>
				</EditTemplate>
			</ElMain>
		</ElContainer>
	</div>
</template>
<style lang="css" scoped>
	div.cover {
		background-image: url("/src/assets/imgs/login.jpg");
		background-repeat: no-repeat;
		background-size: cover;
		background-position: center;
		min-width: 450px;
		position: relative;
		min-width: 45em;
		width: 100%;
		height: 100%;
	}

	.el-container {
		position: absolute;
		width: 450px;
		height: max-content;

		padding: 30px 40px;
		background-color: white;
		box-shadow: 0 0 20px #16b4f2;

		border-radius: 20px;
	}

	.el-header {
		font-size: 1.5em;
		text-align: center;
		color: #595959;
		margin-top: 20px;
		margin-bottom: 40px;
		height: max-content;
	}

	.el-main {
		padding: 0;
	}

	.el-main .edit-item:not(.operations) {
		margin-bottom: 2em;
	}

	.el-main .edit-item.pwd {
		margin-bottom: 5px;
	}

	.el-main :deep(.edit-item.operations) {
		margin-bottom: 0;
	}
	.el-main :deep(.edit-item.operations .el-form-item__content) {
		margin-left: 0 !important;
		justify-content: center;
	}
	.edit-item.operations .el-button {
		width: 100%;
		font-size: 20px;
		height: 2em;
	}
	.edit-item.operations .link {
		line-height: 1;
		width: max-content;
		margin-top: 15px;
		font-size: 14px;
		cursor: pointer;
	}
	.edit-item.operations .link:hover {
		color: var(--el-color-primary);
	}
</style>
