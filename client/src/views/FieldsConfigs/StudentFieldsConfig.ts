/** @format */

import type { FieldsConfig } from "views/common";
import { commonFieldsConfig } from "./CommonFieldsConfig";
import { commonUserFieldsConfig } from "./UserFieldsConfig";

import { student } from "./static.json";

export const studentFieldsConfig: FieldsConfig = Object.assign(
	{},
	commonUserFieldsConfig,
	{
		major: {
			layoutSpan: 12,
			type: "enum",
		},
		degree: {
			layoutSpan: 12,
			type: "enum",
			enumItems: Object.values(student.degree),
		},

		session: {
			layoutSpan: 12,
			type: "text",
		},
		class: {
			layoutSpan: 12,
			type: "text",
		},
	} as FieldsConfig,
	commonFieldsConfig
);
