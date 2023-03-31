/** @format */

import { requiredRule } from "common/utils";
import { FieldsConfig } from "views/common";
import { commonFieldsConfig } from "./CommonFieldsConfig";

export const degreeFieldsConfig: FieldsConfig = Object.assign(
	{
		number: {
			layoutSpan: 24,
			type: "text",
			rule: requiredRule,
		},
		name: {
			layoutSpan: 24,
			type: "text",
			rule: requiredRule,
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
	},
	commonFieldsConfig
);
