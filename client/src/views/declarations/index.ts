/** @format */

import type { Ref } from "vue";
import type { FormRules } from "element-plus";
import type { CollectingPostDataHandler, IFields } from "views/common";

export type AcceptedFieldsCallback = (data: any, fields: Ref<IFields>) => void;

export type DeclareComponentExposes = {
	rules: FormRules;
	onAcceptedFields?: AcceptedFieldsCallback;
	onBeforeSubmit?: CollectingPostDataHandler;
};

export type AwardType = "Teaching" | "Competition" | "Teacher";

export interface IAuthor {
	uid?: string;
	role: number;
	name: string;
	order: number;
	is_corresponding?: boolean;
}

export const authorPropNames: Record<string, string> = {
	uid: "ID",
	name: "姓名",
	role: "角色",
	order: "署名次序",
};

export function getFields(raw: Array<IFields[""]>) {
	return raw.reduce((prev, cur) => {
		prev[cur.field_name] = cur;
		return prev;
	}, {} as IFields);
}
