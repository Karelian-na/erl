/** @format */

import type { FieldsConfig } from "views/common";

export const commonFieldsConfig = {
	add_uid: {
		layoutSpan: 12,
		type: "text",
	},
	add_user: {
		layoutSpan: 12,
		type: "text",
	},
	add_time: {
		layoutSpan: 12,
		type: "text",
	},
};

export const reserveFieldsConfig: FieldsConfig = {
	message: {
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
		},
	},
};
