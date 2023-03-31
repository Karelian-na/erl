/** @format */

export type SecurityOption = {
	name: string;
	icon: string;
	title: string;
	type?: number;
};

export type VerifyCodeSendingHandler = () => void;

export type VerifyCodeSentCallback = (pageTraceId: string) => void;
