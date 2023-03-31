<!-- @format -->

<script setup lang="ts">
	import Verify from "components/Verify.vue";
	import IconFont from "components/IconFont.vue";
	import {
		ElCard,
		ElForm,
		ElFormItem,
		ElSteps,
		ElStep,
		FormRules,
		ElInput,
		ElButton,
		ElContainer,
		ElAside,
		ElHeader,
		ElMain,
		FormInstance,
	} from "element-plus";

	import { sha256 } from "js-sha256";
	import { securityOptions } from ".";
	import { useRouter } from "vue-router";
	import { onMounted, reactive, ref } from "vue";
	import { axiosRequest, EmptyObject, requiredRule } from "common/utils";

	const props = defineProps<{
		account?: string;
		mode: "retrieve" | "revise";
	}>();

	const step = ref(0);
	const serial = ref("");
	const loading = ref(false);
	const formData = reactive({
		account: props.account,

		serial: "",
		code: "",
		traceId: "",

		old: "",
		pwd: "",
		confirm: "",
	});
	const verifyMethod = ref("");
	const formIns = ref<FormInstance>(EmptyObject);
	const verifyMethodsInfo = ref<Record<string, string>>({});

	const router = useRouter();
	const rules: FormRules = {
		account: [
			props.mode === "retrieve" ? requiredRule : {},
			{
				async asyncValidator(rule, value, callback, source, options) {
					if (!loading.value) {
						callback();
						return;
					}

					const result = await axiosRequest({
						method: "GET",
						url: "/Users/verifies",
						params: {
							account: value,
						},
					});

					if (result.success) {
						if (Object.keys(result.data).length === 0) {
							if (props.mode === "revise") {
								callback();
								setTimeout(() => {
									step.value = 2;
								}, 200);
							} else {
								callback("账号未包含安全验证! 请登录后更改密码或联系管理员!");
							}
							return;
						}
						verifyMethodsInfo.value = result.data;
						callback();
					} else {
						callback(result.msg);
					}
					return;
				},
				trigger: "blur",
			},
		],
		serial: [
			requiredRule,
			{
				validator(rule, value: string, callback, source, options) {
					switch (verifyMethod.value) {
						case "bind_email":
							if (!value.match(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/)) {
								callback("邮箱格式错误!");
								return;
							}
							break;
						case "bind_phone":
							if (!value.match(/^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/)) {
								callback("手机号格式错误!");
								return;
							}
							break;
						default:
							callback("验证方式有误!");
							return;
					}

					callback();
				},
				trigger: "blur",
			},
		],
		code: [
			requiredRule,
			{ min: 6, max: 6, message: "请输入正确格式的验证码" },
			{
				async asyncValidator(rule, value, callback, source, options) {
					if (!loading.value) {
						callback();
						return;
					}

					const result = await axiosRequest({
						method: "POST",
						url: "/Users/revisepwd/verify",
						data: {
							pageTraceId: formData.traceId,
							serial: formData.serial,
							code: formData.code,
						},
					});

					if (result.success) {
						callback();
					} else {
						callback(result.msg);
					}
					return;
				},
			},
		],
		old: requiredRule,
		pwd: [requiredRule, { min: 6, max: 16, message: "密码必须介于6~16个字符之间", trigger: "blur" }],
		confirm: [
			requiredRule,
			{
				validator(rule, value, callback, source, options) {
					if (value !== formData.pwd) {
						callback("密码输入不一致!");
						return;
					}

					if (!loading.value) {
						callback();
						return;
					}

					axiosRequest(
						{
							method: "PUT",
							url: "/Users/revisepwd",
							data: Object.assign(
								{ pwd: sha256(formData.pwd) },
								props.mode === "retrieve"
									? {
											account: props.account,
											pageTraceId: formData.traceId,
									  }
									: { old: sha256(formData.old) }
							),
						},
						async (result) => {
							if (result.success) {
								callback();
								await new Promise((resolve, reject) => {
									setTimeout(() => {
										resolve(0);
									}, 3000);
								});
							} else {
								callback(result.msg);
								return true;
							}
							return false;
						}
					);
				},
			},
		],
	};

	onMounted(() => {
		if (props.mode === "revise") {
			onNextStep();
		}
	});

	function onSelectVerifyMethod(method: string) {
		verifyMethod.value = method;
		serial.value = verifyMethodsInfo.value[method];
	}

	async function onNextStep() {
		if (step.value == 3) {
			router.push({ name: "login" });
			return;
		}
		loading.value = true;
		formIns.value.validate((isValid, error) => {
			loading.value = false;
			if (!isValid) {
				return;
			}
			++step.value;
		});
	}
</script>

<template>
	<ElContainer class="top position-center">
		<ElHeader>
			<ElSteps
				simple
				:space="200"
				:active="step"
			>
				<ElStep title="输入账号">
					<template #icon>
						<IconFont value="edit" />
					</template>
				</ElStep>
				<ElStep title="验证身份">
					<template #icon>
						<IconFont value="verify" />
					</template>
				</ElStep>
				<ElStep title="密码修改">
					<template #icon>
						<IconFont value="edit" />
					</template>
				</ElStep>
				<ElStep title="修改成功">
					<template #icon>
						<IconFont value="finish" />
					</template>
				</ElStep>
			</ElSteps>
		</ElHeader>
		<ElMain>
			<ElForm
				status-icon
				ref="formIns"
				label-position="top"
				:class="mode"
				:rules="rules"
				:model="formData"
			>
				<template v-if="step === 0">
					<ElFormItem
						prop="account"
						label="输入找回密码的账号:"
					>
						<ElInput v-model="formData.account"></ElInput>
					</ElFormItem>
				</template>
				<template v-if="step === 1">
					<Transition name="slide-fade">
						<div v-if="!verifyMethod">
							<ElCard
								class="method"
								v-for="(value, key) in verifyMethodsInfo"
								@click="onSelectVerifyMethod(key)"
							>
								<ElContainer>
									<ElAside>
										<IconFont :value="securityOptions[key].icon"></IconFont>
									</ElAside>
									<ElMain>
										<p class="title">{{ securityOptions[key].title }}验证</p>
										<p class="descrip">通过发送验证码至{{ value }}进行验证</p>
									</ElMain>
								</ElContainer>
							</ElCard>
						</div>
						<div v-else>
							<Verify
								url="/Users/revisepwd/verify/send"
								:formData="formData"
								:secret-serial="serial"
								:prop="['serial', 'code']"
								:option="securityOptions[verifyMethod]"
								@sending="loading = true"
								@sent="(v) => ((formData.traceId = v), (loading = false))"
							>
								<template #front>
									<p
										class="switch"
										@click="verifyMethod = ''"
										>切换验证方式
									</p>
								</template>
							</Verify>
						</div>
					</Transition>
				</template>
				<template v-else-if="step === 2">
					<ElFormItem
						v-if="mode === 'revise' && Object.keys(verifyMethodsInfo).length == 0"
						prop="old"
						label="输入旧密码:"
					>
						<ElInput
							show-password
							type="password"
							v-model="formData.old"
						></ElInput>
					</ElFormItem>
					<ElFormItem
						prop="pwd"
						label="输入新密码:"
					>
						<ElInput
							show-password
							type="password"
							v-model="formData.pwd"
						></ElInput>
					</ElFormItem>
					<ElFormItem
						prop="confirm"
						label="确认新密码:"
					>
						<ElInput
							type="password"
							v-model="formData.confirm"
						></ElInput>
					</ElFormItem>
				</template>
				<ElFormItem
					v-if="step !== 1 || verifyMethod !== ''"
					class="operations"
				>
					<ElButton
						type="primary"
						:loading="loading"
						@click="onNextStep"
						>{{ step === 3 ? "去登陆" : step === 2 ? "立即修改" : "下一步" }}
					</ElButton>
				</ElFormItem>
			</ElForm>
		</ElMain>
	</ElContainer>
</template>

<style scoped lang="css">
	.el-container.top {
		width: 800px;
		position: relative;
		top: 25%;
	}

	.el-steps {
		line-height: 1;
	}

	.el-form {
		position: relative;
		width: 60%;
		margin: auto;
	}
	.el-form-item {
		margin-bottom: 2em;
	}

	.el-card.method {
		cursor: pointer;
		margin-bottom: 1em;
	}
	.el-card.method .el-aside {
		padding: 20px;
		display: flex;
		align-items: center;
		width: max-content;
		padding-right: 0;
	}
	.el-card.method .el-aside .iconfont {
		font-size: 72px;
	}
	.el-card.method .el-main .title {
		font-size: 25px;
		margin-bottom: 10px;
	}
	.el-card.method .el-main .descrip {
		word-break: break-all;
		line-height: 1.2;
	}

	.verify .switch {
		cursor: pointer;
		margin-bottom: 10px;
		text-decoration: underline;
		color: var(--el-color-primary);
	}
	.el-form-item.operations .el-button {
		width: 100%;
		height: 2.5em;
	}

	.slide-fade-enter-active {
		transition: all 0.3s ease-out;
	}
	.slide-fade-leave-active {
		transition: all 0.3s cubic-bezier(1, 0.5, 0.8, 1);
	}
	.slide-fade-enter-from,
	.slide-fade-leave-to {
		transform: translateX(20px);
		opacity: 0;
	}
</style>
