/** @format */

import { FieldsConfig, UnShowField } from "views/common";

import { user } from "./static.json";
import { requiredRule } from "common/utils";
import { commonFieldsConfig } from "./CommonFieldsConfig";

export const commonUserFieldsConfig = {
	id: {
		layoutSpan: 12,
		type: "text",
		rule: requiredRule,
		editableWhenAdd: true,
	},
	name: {
		layoutSpan: 12,
		type: "text",
		rule: requiredRule,
		columnBindProps: {
			showOverflowTooltip: false,
		},
		editableWhenAdd: true,
	},

	gender: {
		layoutSpan: 12,
		type: "enum",
		rule: requiredRule,
		enumItems: [
			{ value: 1, label: "男" },
			{ value: 2, label: "女" },
		],
	},
	age: {
		layoutSpan: 12,
		type: "text",
		rule: requiredRule,
	},

	email: {
		layoutSpan: 12,
		type: "text",
	},
	phone: {
		layoutSpan: 12,
		type: "text",
	},
};

export const userFieldsConfig: FieldsConfig = Object.assign(
	{},
	commonUserFieldsConfig,
	{
		avatar: {
			layoutSpan: 12,
			type: "image",
			bindProps: {
				autoUpload: false,
				limit: 1,
				showFileList: false,
			},
		},

		roles: {
			layoutSpan: 12,
			type: "text",
			rule: requiredRule,
			show: UnShowField,
		},

		political_status: {
			layoutSpan: 12,
			type: "enum",
			rule: requiredRule,
			enumItems: user.politicalStatus,
		},

		clan: {
			layoutSpan: 12,
			type: "enum",
			rule: requiredRule,
			enumItems: user.clan,
		},

		add_time: {
			layoutSpan: 12,
			type: "text",
		},
		profile: {
			layoutSpan: 24,
			type: "text",
			bindProps: {
				type: "textarea",
				resize: "none",
				rows: 3,
			},
			show: UnShowField,
		},
	} as FieldsConfig,
	commonFieldsConfig
);
