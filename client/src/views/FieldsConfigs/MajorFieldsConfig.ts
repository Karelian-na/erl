/** @format */

import { requiredRule } from "common/utils";
import type { FieldsConfig } from "views/common";
import { commonFieldsConfig } from "./CommonFieldsConfig";

export const majorFieldsConfig: FieldsConfig = Object.assign({}, commonFieldsConfig, {
	id: {
		layoutSpan: 12,
		type: "text",
	},
	number: {
		layoutSpan: 12,
		type: "text",
		rule: [requiredRule],
	},

	name: {
		layoutSpan: 24,
		type: "text",
		rule: [requiredRule],
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
} as FieldsConfig);
