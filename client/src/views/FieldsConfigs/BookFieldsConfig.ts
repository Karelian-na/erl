/** @format */

import type { FieldsConfig } from "views/common";

import { imageBindProps, requiredRule } from "common/utils";
import { commonFieldsConfig, reserveFieldsConfig } from "./CommonFieldsConfig";

const fieldsConfig: FieldsConfig = {
	number: {
		layoutSpan: 12,
		rule: requiredRule,
		type: "text",
	},
	name: {
		layoutSpan: 12,
		rule: requiredRule,
		type: "text",
	},

	author: {
		layoutSpan: 12,
		rule: requiredRule,
		type: "text",
	},
	publisher: {
		layoutSpan: 12,
		rule: requiredRule,
		type: "text",
	},

	cover: {
		layoutSpan: 12,
		type: "image",
		bindProps: imageBindProps,
	},
	price: {
		layoutSpan: 12,
		type: "number",
	},

	type: {
		layoutSpan: 12,
		rule: requiredRule,
		type: "text",
	},
	isbn: {
		layoutSpan: 12,
		rule: requiredRule,
		type: "text",
	},
};

export const bookFieldsConfig: FieldsConfig = Object.assign({}, fieldsConfig, commonFieldsConfig);

export const seperateBookReserveFieldsConfig: FieldsConfig = {
	amount: {
		layoutSpan: 12,
		type: "number",
		rule: requiredRule,
		bindProps: {
			min: 1,
			max: 100,
		},
	},
};

export const bookReserveFieldsConfig: FieldsConfig = Object.assign({}, fieldsConfig, seperateBookReserveFieldsConfig, reserveFieldsConfig);
