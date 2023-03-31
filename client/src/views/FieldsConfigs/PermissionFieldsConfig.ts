/** @format */

import type { FieldsConfig } from "views/common";
import type { InputProps, SwitchProps } from "element-plus";

import { requiredRule, useFormData } from "common/utils";
import { commonFieldsConfig } from "./CommonFieldsConfig";

import { permission } from "./static.json";
import { MenuType } from "../permissions";

export const permissionFieldsConfig: FieldsConfig = Object.assign(
	{
		icon: {
			layoutSpan: 16,
			type: "text",
			bindProps: {
				readonly: true,
			},
			columnBindProps: {
				className: "icon",
				width: "60px",
			},
		},
		type: {
			layoutSpan: 8,
			type: "enum",
			enumItems: permission.type,
			bindProps: {
				class: "type",
				teleported: false,
			},
			columnBindProps: {
				width: "100px",
			},
		},

		pid: {
			layoutSpan: 24,
			type: "enum",
			bindProps: {
				teleported: false,
			},
		},

		name: {
			layoutSpan: 24,
			type: "text",
			rule: requiredRule,
			columnBindProps: {
				className: "name",
				fixed: "left",
				align: "left",
				width: "300px",
			},
		},

		oper_type: {
			layoutSpan: 16,
			type: "enum",
			enumItems: permission.oper_type,
		},
		status: {
			layoutSpan: 8,
			type: "switch",
			enumItems: [
				{ value: true, label: "启用" },
				{ value: false, label: "禁用" },
			],
			bindProps: {
				"inline-prompt": true,
				"active-text": "启用",
				"inactive-text": "禁用",
			} as Partial<SwitchProps>,
			columnBindProps: {
				width: "80px",
			},
		},

		url: {
			layoutSpan: 24,
			type: "text",
			rule: [
				requiredRule,
				{
					validator(rule, value: string, callback, source, options) {
						const formData = useFormData();
						let regex: RegExp[] = [];
						const emptyReg = /#/g;
						const routeReg = /^\/[\w\/]{2,}\w\/?$/g;
						const urlReg = /^https?:\/\/([\w-]+\.)+[\w-]+(\/[\w-./?%&=]*)$/g;
						let msg: string;
						switch (formData.value.type) {
							case MenuType.Menu:
								regex.push(emptyReg, /^\/[\w\/]{2,}\w\/$/g);
								msg = "菜单类型权限的url必须时#或以 '/' 开头 '/' 结尾的路由地址";
								break;
							case MenuType.Item:
								regex.push(emptyReg, routeReg, urlReg);
								msg = "选项类型权限的url必须是#或外链或以 '/' 开头的路由地址";
								break;
							case MenuType.Page:
								regex.push(emptyReg, routeReg, urlReg);
								msg = "标签类型权限的url必须是#或外链或以 '/' 开头的路由地址";
								break;
							case MenuType.Oper:
								regex.push(/^\/[\w\/]{2,}\w$/g, urlReg);
								msg = "操作类型权限的url必须是外链或以 '/' 开头的路由地址";
								break;
							default:
								callback();
								return;
						}
						if (regex.every((item) => !item.test(value))) {
							callback(new Error(msg));
						} else {
							callback();
						}
					},
					trigger: "blur",
				},
			],
			columnBindProps: {
				align: "left",
				showOverflowTooltip: true,
				minWidth: "300px",
			},
		},

		descrip: {
			layoutSpan: 24,
			type: "text",
			rule: {
				max: 100,
				message: "长度不可超过100字!",
				trigger: "blur",
			},
			bindProps: {
				type: "textarea",
				rows: 3,
				resize: "none",
			} as Partial<InputProps>,
		},
	} as FieldsConfig,
	commonFieldsConfig
);
