/** @format */

import { requiredRule } from "common/utils";
import type { SecurityOption } from "components/index";
import type { FormRules } from "element-plus";
import type { IFields, KeyStringObject } from "views/common";

export const userFields: IFields = {
	avatar: {
		field_name: "avatar",
		display_name: "头像",
		editable: true,
	},
	clan: {
		field_name: "clan",
		display_name: "民族",
		editable: true,
	},
	email: {
		field_name: "email",
		display_name: "电子邮箱",
		editable: true,
	},
	age: {
		field_name: "age",
		display_name: "年龄",
		editable: true,
	},
	gender: {
		field_name: "gender",
		display_name: "性别",
		editable: true,
	},
	id: {
		field_name: "id",
		display_name: "ID",
		editable: false,
	},
	name: {
		field_name: "name",
		display_name: "姓名",
		editable: true,
	},
	phone: {
		field_name: "phone",
		display_name: "联系方式",
		editable: true,
	},
	political_status: {
		field_name: "political_status",
		display_name: "政治面貌",
		editable: true,
	},
	add_time: {
		field_name: "add_time",
		display_name: "注册时间",
		editable: false,
	},
	roles: {
		field_name: "roles",
		display_name: "角色",
		editable: false,
	},
	profile: {
		field_name: "profile",
		display_name: "简介",
		editable: true,
	},
} as any;

export const securityOptions: Record<string, SecurityOption> = {
	pwd: {
		name: "pwd",
		icon: "pwd",
		title: "密码安全",
	},
	bind_email: {
		name: "bind_email",
		type: 0, // email
		icon: "email",
		title: "邮箱",
	},
	bind_phone: {
		name: "bind_phone",
		icon: "phone",
		type: 1, // phone
		title: "手机",
	},
};

export const emailRule: FormRules[""] = [
	requiredRule,
	{
		validator(rule, value: string, callback, source, options) {
			if (!value.match(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/)) {
				callback("邮箱格式错误!");
				return;
			}
			callback();
		},
		trigger: "blur",
	},
];

export const phoneRule: FormRules[""] = [
	requiredRule,
	{
		validator(rule, value: string, callback, source, options) {
			if (!value.match(/^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/)) {
				callback("手机号格式错误!");
				return;
			}
			callback();
		},
		trigger: "blur",
	},
];
