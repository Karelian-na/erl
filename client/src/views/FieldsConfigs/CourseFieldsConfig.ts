/** @format */

import type { FieldsConfig } from "views/common";

import { requiredRule, useFormData } from "common/utils";
import { course } from "./static.json";
import type { FormRules } from "element-plus";

const hoursBindProps = {
	min: 0,
	max: 200,
};

const hoursRule: FormRules[""] = {
	validator(rule, value, callback, source, options) {
		const formData = useFormData().value;
		const total = ["theo_hours", "exp_hours", "comp_hours", "prac_hours"].reduce((prev, cur, idx) => {
			const val = formData[cur];
			if (isNaN(val)) {
				return prev;
			}
			return prev + val;
		}, 0);
		if (total === 0) {
			callback("总学时不可为0");
		} else {
			callback();
		}
	},
};

export const courseFieldsConfig: FieldsConfig = {
	number: {
		layoutSpan: 12,
		rule: requiredRule,
		type: "text",
	},
	category: {
		layoutSpan: 12,
		rule: requiredRule,
		type: "enum",
		enumItems: course.category,
	},

	name: {
		layoutSpan: 24,
		rule: requiredRule,
		type: "text",
	},

	unit: {
		layoutSpan: 12,
		rule: requiredRule,
		type: "text",
	},
	credit: {
		layoutSpan: 12,
		rule: requiredRule,
		type: "number",
		bindProps: {
			min: 0.5,
			step: 0.5,
			max: 20,
		},
	},

	theo_hours: {
		layoutSpan: 12,
		rule: hoursRule,
		type: "number",
		bindProps: hoursBindProps,
	},
	exp_hours: {
		layoutSpan: 12,
		rule: hoursRule,
		type: "number",
		bindProps: hoursBindProps,
	},
	comp_hours: {
		layoutSpan: 12,
		rule: hoursRule,
		type: "number",
		bindProps: hoursBindProps,
	},
	prac_hours: {
		layoutSpan: 12,
		rule: hoursRule,
		type: "number",
		bindProps: hoursBindProps,
	},

	ass_method: {
		layoutSpan: 12,
		type: "radio",
		rule: requiredRule,
		enumItems: course.assMethod,
	},
	nature: {
		layoutSpan: 12,
		type: "radio",
		rule: requiredRule,
		enumItems: course.nature,
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
