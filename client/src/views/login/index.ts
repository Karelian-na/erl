/** @format */

import type { IFields, FieldsConfig } from "views/common";

export const loginFields: IFields = {
	account: {
		display_name: "ID",
		field_name: "account",
		editable: true,
	},
	pwd: {
		display_name: "密码",
		field_name: "pwd",
		editable: true,
	},
	remember: {
		display_name: "记住密码",
		field_name: "remember",
		editable: true,
	},
} as Record<string, Partial<IFields[""]>> as any;

export const loginFieldsConfig: FieldsConfig = {
	account: {
		type: "text",
		rule: [
			{ required: true, message: "请输入账户!", trigger: "blur" },
			{
				validator(rule, value: String, callback, source, options) {
					if (value.match(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/)) {
						callback();
					} else if (value.match(/^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/)) {
						callback();
					} else if (value.match(/^\d{6}|\d{8}|$\d{12}$/)) {
						callback();
					} else {
						if (value.match(/^\d*$/)) {
							if (value.length === 11) {
								callback("请输入正确的手机号!");
							} else if (![6, 8, 12].includes(value.length)) {
								callback("请输入正确的用户名!");
							} else {
								callback();
							}
						} else {
							callback("请输入正确的邮箱!");
						}
					}
				},
				trigger: "blur",
			},
		],
		bindProps: {
			placeholder: "用户名/手机号/邮箱",
		},
	},
	pwd: {
		type: "text",
		bindProps: {
			type: "password",
			showPassword: true,
		},
	},
	remember: {
		type: "checkbox",
	},
} as Record<string, Partial<FieldsConfig[""]>> as any;
