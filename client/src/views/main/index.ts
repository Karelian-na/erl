/** @format */
import type { ITab } from "views/common";
import type { IMenuItem } from "../permissions";

export type IPageProps = string;
export type ITabProps = {
	name: string;
	url: string;
	title: string;
	element?: HTMLLIElement;
};

export type IInPageProps = {
	curTab: number;
	tabs: ITab[];
	key: number;
};

export type ILoading = {
	value: boolean;
	tip: string;
};

export type TabPageMapType = Map<ITabProps, IPageProps>;
export type ItemTabMapType = Map<number, ITabProps>;
export type PageInPageTabMapType = Map<IPageProps, IInPageProps>;

export type onChangeItemCallback = (value: HTMLLIElement) => void;
export type CreateTabFunction = (navItem: IMenuItem) => ITabProps;
export type SwitchPageFunction = (tabProps: ITabProps, itemClick: boolean) => void;
export type GetNavItemFunction = (navId?: number) => IMenuItem | null;

export type SpecialTabName = "home" | "personal";

export const topViewName = "main";

export const specialTabs: Record<SpecialTabName, ITabProps> = {
	home: {
		name: "home",
		url: "/home",
		title: "主页",
	},
	personal: {
		name: "personal",
		url: "/personal",
		title: "个人中心",
	},
};

export const specialInPageProps: Record<SpecialTabName, IInPageProps> = {
	home: {
		tabs: [
			{
				id: 0,
				name: "主页",
				url: "/home",
			},
		],
		curTab: 0,
		key: new Date().getTime(),
	},
	personal: {
		tabs: [
			{
				id: 0,
				name: "个人中心",
				url: "/personal",
			},
		],
		curTab: 0,
		key: new Date().getTime(),
	},
};
