/**
 * @format
 * @Author: Karelian_na
 */

import type { AxiosResponse } from "axios";

export type Result = {
	code: number;
	success: boolean;
	msg: string;
	data: any;

	response?: AxiosResponse<Result, any>;
};

export class StatusCode {
	static readonly ERROR_EMPTY_REQUEST = 600001;
	static readonly ERROR_ILLEGAL_ACCESS = 600002;
	static readonly ERROR_PERMISSION_NOT_FOUND = 600003;
	static readonly ERROR_PERMISSION_NOT_ALLOWED = 600004;
	static readonly ERROR_UN_LOGIN = 600005;
	static readonly ERROR_UN_AUTHORIZED = 600006;
	static readonly ERROR_INVALID_ARGUMENT = 600007;
}

export type DailyPoemResult = {
	status: boolean;
	data: {
		id: string;
		content: string;
		popularity: number;
		origin: {
			title: string;
			dynasty: string;
			author: string;
			content: string[];
			translate: string[];
		};
		matchTags: string[];
		recommendedReason: string;
		cacheAt: string;
	};
	token: string;
	ipAddress: string;
};
