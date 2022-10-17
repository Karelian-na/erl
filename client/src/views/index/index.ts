export interface MenuItem {
	id: number,
	name: string,
	type: MenuType,
	icon: string,
	url: string,
	pid: number,
	children: MenuItem[] | null,
	status: number,
}

export enum MenuType {
	Menu = 1,
	Item = 2,
}