/** @format */

import { FieldsConfig, UnShowField } from "views/common";

import { mapEnumItem, requiredRule } from "common/utils";
import { commonReservationFieldsConfig } from "./ReservationFieldsConfig";
import { commonFieldsConfig, reserveFieldsConfig } from "./CommonFieldsConfig";

import { award, conference, paper, patent, project } from "./static.json";

const enclosureConfig: FieldsConfig[""] = {
	layoutSpan: 24,
	type: "file",
	bindProps: {
		limit: 3,
		multiple: true,
		autoUpload: false,
		showFileList: true,
	},
};

export const awardTypeFieldConfig: FieldsConfig[""] = {
	layoutSpan: 24,
	type: "enum",
	enumItems: award.type,
};

export const declarationFieldsConfig: FieldsConfig = Object.assign({}, commonFieldsConfig, reserveFieldsConfig, commonReservationFieldsConfig);

export const teachingAwardFieldsConfig: FieldsConfig = {
	name: {
		layoutSpan: 24,
		type: "text",
		rule: requiredRule,
	},

	type: {
		layoutSpan: 12,
		type: "enum",
		rule: requiredRule,
		enumItems: award.teaching.type,
	},
	level: {
		layoutSpan: 12,
		type: "enum",
		rule: requiredRule,
		enumItems: award.teaching.level,
	},

	author: {
		layoutSpan: 12,
		type: "text",
		rule: requiredRule,
	},
	unit_sig_order: {
		layoutSpan: 12,
		type: "enum",
		rule: requiredRule,
		enumItems: award.teaching.signatureOrder,
	},

	comp_sig_order: {
		layoutSpan: 12,
		type: "enum",
		rule: requiredRule,
		enumItems: award.teaching.signatureOrder,
	},
	date: {
		layoutSpan: 12,
		type: "date",
		rule: requiredRule,
		bindProps: {
			valueFormat: "YYYY-MM-DD",
		},
	},

	enclosures: enclosureConfig,
};

export const competitionAwardFieldsConfig: FieldsConfig = {
	award_name: {
		layoutSpan: 24,
		type: "text",
		rule: requiredRule,
	},

	work_name: {
		layoutSpan: 24,
		type: "text",
		rule: requiredRule,
	},

	author: {
		layoutSpan: 12,
		type: "text",
		rule: requiredRule,
	},
	level: {
		layoutSpan: 12,
		type: "enum",
		rule: requiredRule,
		enumItems: award.competition.level,
	},

	org_name: {
		layoutSpan: 24,
		type: "text",
		rule: requiredRule,
	},

	org_type: {
		layoutSpan: 12,
		type: "enum",
		rule: requiredRule,
		enumItems: award.competition.orgType,
	},
	date: {
		layoutSpan: 12,
		type: "date",
		rule: requiredRule,
		bindProps: {
			valueFormat: "YYYY-MM-DD",
		},
	},

	enclosures: enclosureConfig,

	role: {
		layoutSpan: 12,
		type: "enum",
		rule: requiredRule,
		enumItems: Object.values(award.competition.role),
		show: UnShowField,
	},
};

export const conferenceFieldsConfig: FieldsConfig = {
	name: {
		layoutSpan: 24,
		type: "text",
		rule: requiredRule,
	},
	title: {
		layoutSpan: 24,
		type: "text",
		rule: requiredRule,
	},
	parti_name: {
		layoutSpan: 12,
		type: "text",
		rule: requiredRule,
	},
	date: {
		layoutSpan: 12,
		type: "date",
		rule: requiredRule,
		bindProps: {
			valueFormat: "YYYY-MM-DD",
		},
	},
	location: {
		layoutSpan: 24,
		type: "custom",
		bindProps: {
			options: conference.area,
		},
	},
	enclosures: enclosureConfig,
};

export const paperFieldsConfig: FieldsConfig = {
	title: {
		layoutSpan: 24,
		type: "text",
		rule: requiredRule,
	},

	pub_jour: {
		layoutSpan: 24,
		type: "text",
		rule: requiredRule,
	},

	pub_year: {
		layoutSpan: 12,
		type: "date",
		rule: requiredRule,
		bindProps: {
			type: "year",
			valueFormat: "YYYY",
		},
	},
	pub_vol: {
		layoutSpan: 12,
		type: "number",
		rule: requiredRule,
	},

	pub_term: {
		layoutSpan: 12,
		type: "number",
		rule: requiredRule,
	},
	inc: {
		layoutSpan: 12,
		type: "enum",
		rule: requiredRule,
		enumItems: paper.journalInclusion,
	},
	disp: {
		layoutSpan: 12,
		type: "enum",
		rule: requiredRule,
	},

	authors: {
		layoutSpan: 24,
		type: "custom",
		children: {
			uid: {
				layoutSpan: 6,
				type: "text",
			},
			name: {
				layoutSpan: 6,
				type: "text",
				rule: requiredRule,
			},
			role: {
				layoutSpan: 6,
				type: "enum",
				rule: requiredRule,
				enumItems: Object.values(paper.authorRole),
			},
			order: {
				layoutSpan: 6,
				type: "enum",
				rule: requiredRule,
				enumItems: Object.values(paper.authorOrder),
			},
		},
		columnBindProps: {
			showOverflowTooltip: false,
		},
	},

	enclosures: enclosureConfig,
};

export const patentFieldsConfig: FieldsConfig = {
	name: {
		layoutSpan: 24,
		type: "text",
		rule: requiredRule,
	},

	number: {
		layoutSpan: 12,
		type: "text",
		rule: requiredRule,
	},
	author: {
		layoutSpan: 12,
		type: "text",
		rule: requiredRule,
	},

	pub_date: {
		layoutSpan: 12,
		type: "date",
		rule: requiredRule,
		bindProps: {
			valueFormat: "YYYY-MM-DD",
		},
	},
	type: {
		layoutSpan: 12,
		type: "enum",
		rule: requiredRule,
		enumItems: patent.type,
	},

	status: {
		layoutSpan: 12,
		type: "enum",
		rule: requiredRule,
		enumItems: Object.values(patent.status),
	},
	auth_date: {
		layoutSpan: 12,
		type: "date",
		bindProps: {
			valueFormat: "YYYY-MM-DD",
		},
	},

	enclosures: enclosureConfig,
};

export const projectFieldsConfig: FieldsConfig = {
	name: {
		layoutSpan: 24,
		type: "text",
		rule: requiredRule,
	},

	author: {
		layoutSpan: 12,
		type: "text",
		rule: requiredRule,
	},
	auth_order: {
		layoutSpan: 12,
		type: "enum",
		rule: requiredRule,
		enumItems: project.order,
	},

	source: {
		layoutSpan: 24,
		type: "text",
		rule: requiredRule,
	},

	start_date: {
		layoutSpan: 24,
		type: "date",
		rule: requiredRule,
		bindProps: {
			type: "monthrange",
			rangeSeparator: "至",
			valueFormat: "YYYY-MM-DD",
		},
	},
	// end_date: {
	// 	layoutSpan: 12,
	// 	type: "date",
	// 	rule: requiredRule,
	// },

	tot_funding: {
		layoutSpan: 12,
		type: "number",
		rule: requiredRule,
	},
	rec_funding: {
		layoutSpan: 12,
		type: "number",
		rule: requiredRule,
	},

	type: {
		layoutSpan: 12,
		type: "text",
		rule: requiredRule,
	},
	category: {
		layoutSpan: 12,
		type: "enum",
		rule: requiredRule,
		enumItems: project.type,
	},

	enclosures: enclosureConfig,
};

export const declarationDetailFieldsConfig: Record<string, FieldsConfig> = {
	Teaching: Object.assign({}, teachingAwardFieldsConfig, declarationFieldsConfig),
	Competition: Object.assign({}, competitionAwardFieldsConfig, declarationFieldsConfig),
	Teacher: Object.assign({}, competitionAwardFieldsConfig, declarationFieldsConfig),
	Conference: Object.assign({}, conferenceFieldsConfig, declarationFieldsConfig),
	Paper: Object.assign({}, paperFieldsConfig, declarationFieldsConfig),
	Patent: Object.assign({}, patentFieldsConfig, declarationFieldsConfig),
	Project: Object.assign({}, projectFieldsConfig, declarationFieldsConfig),
};
