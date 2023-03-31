/** @format */

import type { FieldsConfig } from "views/common";

import { requiredRule } from "common/utils";
import { reserveFieldsConfig } from "./CommonFieldsConfig";
import { seperateBookReserveFieldsConfig } from "./BookFieldsConfig";
import { seperateLaboratoryReserveFieldsConfig } from "./LaboratoryFieldsConfig";

export const commonReservationFieldsConfig: FieldsConfig = Object.assign({}, reserveFieldsConfig, {
	audit_status: {
		layoutSpan: 12,
		type: "radio",
		rule: {
			validator(rule, value, callback, source, options) {
				if (!value || value > 2 || value < 1) {
					callback(new Error("请选择审核结果!"));
					return;
				}
				callback();
			},
		},
		enumItems: [
			{ value: 1, label: "通过" },
			{ value: 2, label: "驳回" },
		],
	},

	audit_time: {
		layoutSpan: 12,
		type: "date",
	},
	audit_user: {
		layoutSpan: 12,
		type: "text",
	},

	comment: {
		layoutSpan: 24,
		type: "text",
		bindProps: {
			rows: 3,
			resize: "none",
			type: "textarea",
		},
	},
} as FieldsConfig);

export const bookReservationFieldsConfig: FieldsConfig = Object.assign(
	{},
	{
		uid: {
			layoutSpan: 12,
			type: "text",
		},
		add_user: {
			layoutSpan: 12,
			type: "text",
		},

		book_name: {
			layoutSpan: 12,
			type: "text",
		},
		author: {
			layoutSpan: 12,
			type: "text",
		},

		cover: {
			layoutSpan: 12,
			type: "image",
		},
		price: {
			layoutSpan: 12,
			type: "number",
		},

		publisher: {
			layoutSpan: 12,
			type: "text",
		},
		add_time: {
			layoutSpan: 12,
			type: "date",
		},
	} as FieldsConfig,
	seperateBookReserveFieldsConfig,
	commonReservationFieldsConfig
);

export const laboratoryReservationFieldsConfig: FieldsConfig = Object.assign(
	{},
	{
		number: {
			layoutSpan: 12,
			rule: requiredRule,
			type: "text",
		},
		commander: {
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

		capacity: {
			layoutSpan: 12,
			rule: requiredRule,
			type: "number",
		},
		add_user: {
			layoutSpan: 12,
			type: "text",
		},
		add_time: {
			layoutSpan: 12,
			type: "date",
		},
	} as FieldsConfig,
	seperateLaboratoryReserveFieldsConfig,
	commonReservationFieldsConfig
);
