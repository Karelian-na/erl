/** @format */

import type { FieldsConfig } from "views/common";

export const databaseFieldsConfig: FieldsConfig = {
	view_name: {
		layoutSpan: 12,
		type: "text",
	},
	comment: {
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
	update_user: {
		layoutSpan: 12,
		type: "text",
	},
};
