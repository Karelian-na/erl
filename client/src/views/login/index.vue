<script setup lang="ts">
	import { reactive, ref } from "vue";
	import axios from "axios";
	import { FormInstance, FormRules, ElMessage } from "element-plus";

	const formRef = ref<FormInstance>();
	const formData = reactive({
		name: "",
		pwd: "",
	});

	function onSubmit(formInstance: FormInstance | undefined) {
		if (formInstance) {
			formInstance.validate((valid) => {
				if (!valid) {
					return false;
				}
				axios.post("/users/login", formData).then(
					response => {
						ElMessage.info({
							message: "登陆成功! 2s后跳转",
							type: "success",
						});
					},
					error => {
						ElMessage.error({
							showClose: true,
							duration: 5000,
							message: `登陆失败! ${error}`,
						});
					}
				);
				return true;
			});
		}
	}

	const rules = reactive<FormRules>({
		name: [
			{ required: true, message: "请输入用户名!", trigger: "blur" },
			{ min: 6, max: 16, message: "用户名必须在6-16位之间", trigger: "blur" },
		],
		pwd: [{ required: true, message: "请输入密码!", trigger: "blur" }],
	});
</script>

<template>
	<div class="login-ui position-center">
		<h1 class="title">登录</h1>
		<el-form status-icon ref="formRef" size="large" id="login-ui-form" method="post" :model="formData" :rules="rules">
			<el-form-item prop="name">
				<el-input v-model="formData.name" placeholder="请输入用户名">
					<template #prepend>
						<i class="iconfont icon-user-line"></i>
					</template>
				</el-input>
			</el-form-item>
			<el-form-item prop="pwd">
				<el-input v-model="formData.pwd" type="password" show-password placeholder="请输入密码">
					<template #prepend>
						<i class="iconfont icon-password"></i>
					</template>
				</el-input>
			</el-form-item>
			<el-form-item>
				<el-checkbox label="记住密码"></el-checkbox>
			</el-form-item>
			<el-form-item>
				<el-button type="primary" @click="onSubmit(formRef)">登陆</el-button>
			</el-form-item>
		</el-form>
	</div>
</template>

<style lang="css" scoped>
	.login-ui {
		position: absolute;
		width: 400px;
		height: max-content;

		padding: 30px 40px;
		background-color: white;
		box-shadow: 0 0 20px #16b4f2;

		border-radius: 20px;
	}

	.title {
		font-size: 1.5em;
		text-align: center;
		color: #595959;
		margin-bottom: 30px;
	}

	.el-form-item {
		margin: 25px 0;
	}
	.el-button {
		width: 100%;
		font-size: 18px;
		height: 2em;
	}
</style>
