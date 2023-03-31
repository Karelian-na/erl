<!-- @format -->

<script setup lang="ts">
	import type { KeyStringObject } from "views/common";
	import type { SecurityOption, VerifyCodeSendingHandler, VerifyCodeSentCallback } from ".";

	import { ref, watch } from "vue";
	import { sha256 } from "js-sha256";
	import { axiosRequest, EmptyObject } from "common/utils";
	import { ElFormItem, ElInput, ElButton, FormItemInstance } from "element-plus";

	const props = defineProps<{
		url: string;
		secretSerial?: string;
		option: SecurityOption;
		prop: [string, string];
		formData: KeyStringObject;
		onSent?: VerifyCodeSentCallback;
		onSending?: VerifyCodeSendingHandler;
	}>();

	const countDown = ref(61);
	const sending = ref(false);
	const formItemSerialIns = ref<FormItemInstance>(EmptyObject);

	watch(
		() => formItemSerialIns.value.validateState,
		(value) => {
			if (countDown.value > 1 && countDown.value <= 60) {
				return;
			}
			switch (value) {
				case "error":
					countDown.value = 61;
					break;
				case "success":
					countDown.value = 0;
					break;
				default:
					break;
			}
		}
	);

	function onSendVerifyCode() {
		if (countDown.value === 0) {
			countDown.value = 61;

			formItemSerialIns.value.validate("blur", async (isValid) => {
				if (isValid) {
					props.onSending?.();

					sending.value = true;
					const traceId = sha256(window.location.pathname);
					let result = await axiosRequest({
						method: "POST",
						url: props.url,
						data: {
							pageTraceId: traceId,
							type: props.option.type,
							serial: props.formData[props.prop[0]],
						},
					});
					if (result.success) {
						props.onSent?.(traceId);
						countDown.value = 60;
						const id = window.setInterval(() => {
							--countDown.value;
							if (countDown.value <= 0) {
								window.clearInterval(id);
							}
						}, 1000);
					} else {
						props.onSent?.("");
						formItemSerialIns.value.validateState = "error";
						formItemSerialIns.value.validateMessage = result.msg;
					}
					sending.value = false;
				} else {
					countDown.value = 0;
				}
			});
		}
	}
</script>

<template>
	<div class="verify">
		<slot name="front"></slot>
		<p
			v-if="secretSerial"
			class="descrip"
		>
			请填写完整的{{ option.title }}
			<span class="secret">{{ secretSerial }}</span>
			以证明您的身份
		</p>
		<ElFormItem
			ref="formItemSerialIns"
			:prop="prop[0]"
		>
			<ElInput v-model="formData[prop[0]]">
				<template #prepend>输入{{ option.title }}</template>
			</ElInput>
		</ElFormItem>
		<ElFormItem
			:prop="prop[1]"
			class="code"
		>
			<ElInput
				v-model="formData[prop[1]]"
				placeholder="输入验证码"
			>
				<template #append>
					<ElButton
						:loading="sending"
						:disabled="countDown !== 0"
						@click="onSendVerifyCode"
						>{{ countDown > 0 && countDown <= 60 ? countDown + "s后重试" : "获取验证码" }}
					</ElButton>
				</template>
			</ElInput>
		</ElFormItem>
	</div>
</template>

<style scoped lang="css">
	.verify .el-form-item {
		margin-bottom: 2em;
	}

	.verify :deep(.descrip) {
		line-height: 1.5;
		margin-bottom: 10px;
	}
	.verify :deep(.descrip .secret) {
		color: var(--el-color-primary);
	}

	.verify .code .el-button {
		padding: 0 20px;
	}
</style>
