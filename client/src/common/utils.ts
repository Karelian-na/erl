/** @format */
import type { ILoading } from "src/views/main";
import type { EnumItem, KeyStringObject } from "views/common";

import Store from "store";
import { router } from "../router";
import { Result, StatusCode } from "./Result";
import axios, { AxiosRequestConfig } from "axios";
import {
	ElLoading,
	ElMessage,
	ElMessageBox,
	ElMessageBoxOptions,
	LoadingOptionsResolved,
	MessageOptions,
	UploadProps,
	UploadUserFile,
} from "element-plus";
import { isArray } from "element-plus/es/utils";

type AlertOptions = ElMessageBoxOptions & { content?: string };
type CustomMessageOptions = MessageOptions | AlertOptions;

export const EmptyObject = {} as any;

export const requiredRule = { required: true, message: "必填项不能为空!", trigger: "blur" };

export const imageBindProps: Partial<UploadProps> = {
	accept: "image/png, image/jpeg",
	limit: 1,
	showFileList: false,
	autoUpload: false,
};

export const AuditStatus: { [prop: number]: { label: string; class: string } } = {
	0: { label: "待审核", class: "waiting" },
	1: { label: "已通过", class: "passed" },
	2: { label: "未通过", class: "failed" },
};

export function getByValue<K, V>(map: Map<K, V>, searchValue: V, predict?: (fst: V, sec: V) => boolean): K {
	if (predict) {
		for (let [key, value] of map.entries()) {
			if (predict(value, searchValue)) return key;
		}
	} else {
		for (let [key, value] of map.entries()) {
			if (value === searchValue) return key;
		}
	}
	return null as K;
}

export function confirm(content: string, options?: AlertOptions) {
	return ElMessageBox.alert(
		content,
		options?.content ?? "警告",
		Object.assign({}, options, {
			type: "warning",
			showCancelButton: true,
		})
	);
}

export function success(type: "alert" | "msg", options?: CustomMessageOptions) {
	if (type == "alert") {
		const opt = options as AlertOptions | undefined;
		ElMessageBox.alert(
			opt?.content ?? "操作成功!",
			opt?.title ?? "信息",
			Object.assign(opt ?? {}, {
				type: "success",
				showCancelButton: false,
			} as ElMessageBoxOptions)
		);
	} else {
		const opt = options as MessageOptions | undefined;
		ElMessage.success(
			Object.assign(opt ?? {}, {
				message: opt?.message ?? "操作成功!",
				showClose: true,
				grouping: true,
			} as MessageOptions)
		);
	}
}

export function error(type: "alert" | "msg", options?: CustomMessageOptions) {
	if (type == "alert") {
		const opt = options as AlertOptions | undefined;
		ElMessageBox.alert(
			opt?.content ?? "操作失败!",
			opt?.title ?? "错误",
			Object.assign(opt ?? {}, {
				type: "error",
				showCancelButton: false,
			} as ElMessageBoxOptions)
		);
	} else {
		const opt = options as MessageOptions | undefined;
		ElMessage.error(
			Object.assign(opt ?? {}, {
				message: opt?.message ?? "操作失败!",
				showClose: true,
				grouping: true,
			} as MessageOptions)
		);
	}
}

export function info(type: "alert" | "msg", options: MessageOptions | (AlertOptions & { content: string })) {
	if (type == "alert") {
		const opt = options as AlertOptions & { content: string };
		ElMessageBox.alert(
			opt.content,
			opt?.title ?? "信息",
			Object.assign(opt, {
				type: "info",
				showCancelButton: false,
			} as ElMessageBoxOptions)
		);
	} else {
		const opt = options as MessageOptions;
		ElMessage.info(
			Object.assign(opt, {
				message: opt.message,
				showClose: true,
				grouping: true,
			} as MessageOptions)
		);
	}
}

export function showLoading(
	tip?: string,
	options?: Partial<
		Omit<LoadingOptionsResolved, "target" | "parent"> & {
			target: string | HTMLElement;
			body: boolean;
		}
	>
) {
	return ElLoading.service(
		Object.assign(
			{
				text: tip ?? "加载中...",
				background: "#ffffffcc",
			},
			options
		)
	);
}

export type AxiosRequestCallback = (result: Result) => boolean | void | Promise<boolean> | Promise<void>;

export async function axiosRequest(config: AxiosRequestConfig & { loading?: ILoading }, callback?: AxiosRequestCallback) {
	const loading = config.loading;
	delete config.loading;
	let result: Result;

	try {
		const response = await axios<Result>(config);
		result = response.data;
		result.response = response;
		if (result.code == StatusCode.ERROR_UN_LOGIN) {
			const handled = await callback?.(result);
			Store.namespace("cookie").each((val, key) => {
				Store.namespace("cookie").remove(key);
			});
			router.replace({ name: "login" });
			loading && (loading.value = false);
		}

		if (callback && !(await callback(result))) {
			if (result.success) {
				success("msg", { message: "操作成功!" });
			} else {
				error("msg", { message: `操作失败! ${result.msg ?? ""}` });
			}
		}
	} catch (reason: any) {
		result = {
			code: 0,
			success: false,
			msg: reason.message,
			data: null,
		};
		if (callback && !(await callback(result))) {
			error("msg", { message: `操作失败! ${result.msg}` });
		}
	}
	return result;
}

export function getDate(date?: Date) {
	if (!date) date = new Date();
	return date.toLocaleString("zh", {
		hour12: false,
		year: "numeric",
		month: "2-digit",
		day: "2-digit",
		hour: "2-digit",
		minute: "2-digit",
		second: "2-digit",
	});
}

export function mapEnumItem(items: (string | number)[]) {
	return items.reduce((prev, cur) => {
		prev.push({
			label: String(cur),
			value: cur,
		});
		return prev;
	}, [] as EnumItem[]);
}

let uploadedFilePaths = {} as Record<number, string>;
export async function uploadFile(files: UploadUserFile[], loading: ILoading): Promise<string> {
	const urls = [] as Array<string>;
	const formData = new FormData();
	const unUploadFiles = [] as UploadUserFile[];
	Object.values(files).forEach((file) => {
		if (!uploadedFilePaths[file.uid!]) {
			formData.append("files", new Blob([file.raw as File]), file.name);
			unUploadFiles.push(file);
		} else {
			urls.push(uploadedFilePaths[file.uid!]);
		}
	});

	if (unUploadFiles.length) {
		const result = await axiosRequest({
			url: "/upload",
			method: "POST",
			data: formData,
			onUploadProgress: (e) => {
				const percentage = (e.progress! * 100).toPrecision(3);
				loading.tip = "上传附件中..." + percentage + "%";
			},
		});

		if (result.success) {
			const paths = (result.data as string).split(";");
			unUploadFiles.forEach((file, idx) => {
				uploadedFilePaths[file.uid!] = paths[idx];
				urls.push(paths[idx]);
			});
		} else {
			return "";
		}
	}

	return urls.join(";");
}

export function getChangedAttributes(origin: KeyStringObject, changed: KeyStringObject) {
	const keys = Object.keys(changed);
	if (keys.length !== 0) {
		if (isArray(changed)) {
			const temp = JSON.stringify(changed);
			return JSON.stringify(origin) === temp ? undefined : JSON.parse(temp);
		} else {
			const res: KeyStringObject = {};
			keys.forEach((key) => {
				if (origin[key] !== undefined) {
					if (typeof changed[key] === "object" && origin[key] !== null) {
						const value = getChangedAttributes(origin[key], changed[key]);
						if (value) {
							res[key] = value;
						}
					} else if (origin[key] != changed[key]) {
						res[key] = changed[key];
					}
				} else if (origin[key] != changed[key]) {
					res[key] = changed[key];
				}
			});
			return Object.keys(res).length ? res : undefined;
		}
	} else if (Object.keys(origin).length !== 0) {
		return JSON.parse(JSON.stringify(changed));
	} else {
		return undefined;
	}
}

export function useFormData() {
	return window.templateFormData;
}
