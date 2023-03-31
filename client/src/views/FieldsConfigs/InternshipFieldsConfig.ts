/** @format */

import type { FieldsConfig } from "views/common";

import { requiredRule } from "common/utils";
import { commonFieldsConfig } from "./CommonFieldsConfig";

import { application, internship } from "./static.json";

export const fieldsConfig: FieldsConfig = {
	name: {
		layoutSpan: 24,
		type: "text",
		rule: requiredRule,
	},

	unit_name: {
		layoutSpan: 24,
		type: "text",
		rule: requiredRule,
	},

	unit_descrip: {
		layoutSpan: 24,
		type: "text",
		rule: requiredRule,
	},

	start_date: {
		layoutSpan: 12,
		type: "date",
		rule: requiredRule,
		bindProps: {
			valueFormat: "YYYY-MM-DD",
		},
	},
	days: {
		layoutSpan: 12,
		type: "number",
		rule: requiredRule,
	},

	max_app_num: {
		layoutSpan: 12,
		type: "number",
		rule: requiredRule,
	},
	deadline: {
		layoutSpan: 12,
		type: "date",
		rule: requiredRule,
		bindProps: {
			valueFormat: "YYYY-MM-DD",
		},
	},

	status: {
		layoutSpan: 24,
		type: "radio",
		enumItems: internship.status,
	},

	descrip: {
		layoutSpan: 24,
		type: "text",
		bindProps: {
			type: "textarea",
			rows: 3,
			resize: "none",
		},
	},
};

export const internshipFieldsConfig: FieldsConfig = Object.assign({}, fieldsConfig, commonFieldsConfig);

export const intershipManageFieldsConfig: FieldsConfig = Object.assign({
	app_users: {
		layoutSpan: 24,
		type: "custom",
	},
} as FieldsConfig);

export const applicationFieldsConfig: FieldsConfig = Object.assign({}, commonFieldsConfig, {
	add_uid: commonFieldsConfig.add_uid,
	add_user: commonFieldsConfig.add_user,
	roles: {
		layoutSpan: 12,
		type: "text",
	},
	app_status: {
		layoutSpan: 12,
		type: "radio",
		enumItems: application.status,
	},
	oper_time: {
		layoutSpan: 12,
		type: "text",
	},
	reason: {
		layoutSpan: 24,
		type: "text",
	},
} as FieldsConfig);
