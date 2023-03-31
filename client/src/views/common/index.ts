/** @format */

import type { ComputedRef, Ref } from "vue";
import type { FormRules, Table, TableColumnInstance } from "element-plus";

/**
 * true or Function will ask to leave;
 * false will not leave
 */
export type BeforeLeaveResult = Function | boolean;

export type UserInfo = {
	name: string;
	avatar: string;
};

export type DataOperAction = "add" | "edit" | "delete";

export type PageInfoHandler = (operButtons: IOperButton[], pageData: IPageData) => void;

export type RefreshedDataCallback = (pageData: IPageData) => void;

export type DataChangedCallback = (action: string, data: KeyStringObject, all?: KeyStringObject[]) => boolean;

export type OperbarButtonClickHandler = (button: IOperButton, buttons: Record<string, IOperButton>) => boolean;

export type OperColumnButtonClickHandler = (button: IOperButton, param: KeyStringObject | null, buttons: Record<string, IOperButton>) => boolean;

export type UpdatingFormDataHandler = (formData: Ref<KeyStringObject>) => void | Promise<void>;

export type CollectingPostDataHandler = (postData: Ref<KeyStringObject>) => string | void;

export type PreparedCallback = (formData: Ref<KeyStringObject>, fields: Ref<IFields>, fieldsConfig: FieldsConfig, mode?: string) => void;

export type BeforeLeaveCallback = (formData: KeyStringObject, base: Function) => BeforeLeaveResult;

export type BeforeSubmitHandler = (postData: KeyStringObject) => Promise<boolean> | boolean;

export type KeyStringObject = {
	[prop: string]: any;
} & Object;

export interface IDialogProps {
	show: boolean;
	mode: string;
	operLabel: string;
	loading: boolean;
	loadingTip: String;
	operAction?: DataOperAction;
	data: KeyStringObject | KeyStringObject[];

	askIfNeedToLeave?(): BeforeLeaveResult;
}

export interface ITab {
	id: number;
	name: string;
	url: string;
	icon?: string;
}

export interface IFields {
	[name: string]: {
		field_name: string;
		display: boolean;
		display_name: string;
		display_order: number;
		searchable: boolean;
		editable: boolean;
	};
}

export interface IOperButton {
	type: string;
	icon: string;
	title: string;
	oper_type: number;
}

export interface IPageData {
	totalCount: number;
	curPageIdx: number;
	data: KeyStringObject[];
}

export interface IPageInfo {
	fields: IFields[""][];
	operButtons: IOperButton[];
	pageData: IPageData;
}

export type PreviewDialogProps = {
	show: boolean;
	src: string;
};

export type EnumItem = {
	value: string | number | boolean;
	label: string;
	disabled?: boolean | ComputedRef<boolean>;
	[props: string]: any;
};

export const CollectEnd = "end";

export type FieldShowCallback = (fieldName: string) => boolean;

export type EditItemType = "text" | "number" | "date" | "time" | "enum" | "image" | "file" | "radio" | "switch" | "custom" | "checkbox";

export type FieldConfig = {
	bindProps?: any;
	layoutSpan: number;
	type: EditItemType;
	rule?: FormRules[""];
	enumItems?: EnumItem[];
	children?: FieldsConfig;
	show?: FieldShowCallback;
	editableWhenAdd?: boolean;
	columnBindProps?: TableColumnInstance["$props"];
};

export type FieldsConfig = Record<string, FieldConfig>;

export const detailButton: IOperButton = {
	icon: "details",
	oper_type: 2,
	title: "详情",
	type: "details",
};

export function UnShowField() {
	return false;
}
