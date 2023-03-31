/** @format */

import type { FieldsConfig } from "views/common";

import { requiredRule } from "common/utils";
import { commonFieldsConfig, reserveFieldsConfig } from "./CommonFieldsConfig";

const fieldsConfig: FieldsConfig = {
	number: {
		layoutSpan: 12,
		rule: requiredRule,
		type: "text",
	},
	name: {
		layoutSpan: 24,
		rule: requiredRule,
		type: "text",
	},
	addr: {
		layoutSpan: 24,
		rule: requiredRule,
		type: "text",
	},
	commander: {
		layoutSpan: 12,
		rule: requiredRule,
		type: "text",
	},
	capacity: {
		layoutSpan: 12,
		rule: requiredRule,
		type: "number",
	},
};

export const laboratoryFieldsConfig: FieldsConfig = Object.assign({}, fieldsConfig, commonFieldsConfig);

export const seperateLaboratoryReserveFieldsConfig: FieldsConfig = {
	day: {
		layoutSpan: 12,
		type: "date",
		rule: requiredRule,
		bindProps: {
			type: "date",
			valueFormat: "YYYY-MM-DD",
			disabledDate: (time: Date) => {
				return time.getTime() < Date.now();
			},
		},
	},
	start_time: {
		layoutSpan: 12,
		type: "time",
		rule: requiredRule,
		bindProps: {
			start: "08:20",
			step: "00:10",
			end: "17:00",
		},
	},
	end_time: {
		layoutSpan: 12,
		type: "time",
		rule: requiredRule,
		bindProps: {
			start: "08:30",
			step: "00:10",
			end: "18:00",
		},
	},
};

export const laboratoryReserveFieldsConfig: FieldsConfig = Object.assign({}, fieldsConfig, seperateLaboratoryReserveFieldsConfig, reserveFieldsConfig);
