/** @format */

import { requiredRule } from "common/utils";
import type { FieldsConfig } from "views/common";
import { commonFieldsConfig } from "./CommonFieldsConfig";

export const roleFieldsConfig: FieldsConfig = Object.assign(
	{},
	{
		name: {
			layoutSpan: 12,
			type: "text",
			rule: requiredRule,
		},
		level: {
			layoutSpan: 12,
			type: "number",
			rule: [
				requiredRule,
				{
					validator(rule, value, callback, source, options) {
						if (value < 2 || value > 100) {
							callback("角色等级只能介于2-100之间!");
							return;
						}
						callback();
					},
					trigger: "blur",
				},
			],
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

		update_time: {
			layoutSpan: 12,
			type: "text",
		},
	} as FieldsConfig,
	commonFieldsConfig
);
