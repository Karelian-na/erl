/** @format */

import type { FieldsConfig } from "views/common";
import { commonFieldsConfig } from "./CommonFieldsConfig";
import { commonUserFieldsConfig } from "./UserFieldsConfig";

import { teacher } from "./static.json";

export const teacherFieldsConfig: FieldsConfig = Object.assign(
	{},
	commonUserFieldsConfig,
	{
		level: {
			layoutSpan: 12,
			type: "enum",
			enumItems: Object.values(teacher.level),
		},
		degree: {
			layoutSpan: 12,
			type: "enum",
			enumItems: Object.values(teacher.degree),
		},
		bachelor: {
			layoutSpan: 12,
			type: "enum",
			enumItems: Object.values(teacher.bachelor),
		},
		is_tutor: {
			layoutSpan: 12,
			type: "enum",
			enumItems: [
				{ label: "否", value: false },
				{ label: "是", value: true },
			],
		},
	} as FieldsConfig,
	commonFieldsConfig
);
