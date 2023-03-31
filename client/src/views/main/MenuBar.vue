<!-- @format -->

<script setup lang="ts">
	import type { UserInfo } from "views/common";
	import type { SwitchPageFunction, TabPageMapType } from ".";

	import { ElAvatar } from "element-plus";
	import IconFont from "components/IconFont.vue";

	import Store from "store";
	import { specialTabs } from ".";
	import { inject, Ref } from "vue";
	import { useRouter } from "vue-router";
	import { axiosRequest, confirm, success } from "common/utils";

	const router = useRouter();
	const userInfo = inject<Ref<UserInfo>>("userInfo")!;
	const switchPage = inject<SwitchPageFunction>("switchPage")!;
	const tabMapPage = inject<Ref<TabPageMapType>>("tabMapPage")!;

	function fullscreenOnclick() {
		document.documentElement.requestFullscreen();
	}
	function onRefreshPage() {
		const route = router.currentRoute.value.matched[router.currentRoute.value.matched.length - 1];
		const component = route.components?.[route.name as string];
		if (component) {
		}
	}

	function onPersonalTabClick() {
		if (!tabMapPage.value.get(specialTabs.personal)) {
			tabMapPage.value.set(specialTabs.personal, specialTabs.personal.name);
		}
		switchPage(specialTabs.personal, false);
	}
	function exitOnclick() {
		confirm("确定要退出?", {
			callback: (action, _ins) => {
				if (action != "confirm") return;

				axiosRequest(
					{
						method: "POST",
						url: "/logout",
					},
					() => {
						Store.namespace("cookie").each((val, key) => {
							Store.namespace("cookie").remove(key);
						});
						success("msg", { message: "退出成功!" });
						router.replace({
							name: "login",
						});
						return true;
					}
				);
			},
		});
	}
</script>

<template>
	<div class="menubar">
		<div class="operations">
			<IconFont
				title="刷新当前页面"
				value="refresh"
				@click="onRefreshPage"
			></IconFont>
			<IconFont
				title="全屏"
				value="fullscreen"
				@click="fullscreenOnclick"
			></IconFont>
		</div>
		<div class="personal">
			<div class="info">
				<ElAvatar
					class="avatar"
					:src="userInfo.avatar"
				></ElAvatar>
				<span class="name">{{ userInfo.name }}</span>
				<ul class="options">
					<li
						class="item"
						@click="onPersonalTabClick"
					>
						个人中心
					</li>
					<li
						class="item"
						@click="exitOnclick"
					>
						退出登陆
					</li>
				</ul>
			</div>
		</div>
	</div>
</template>

<style scoped>
	.menubar {
		white-space: nowrap;
		line-height: 2.5em;
	}

	.menubar .operations {
		/* Layout */
		display: inline-block;
	}

	.menubar .personal {
		/* Layout */
		float: right;
		padding-right: 1em;
	}
	.menubar .personal .info {
		/* Layout */
		position: relative;

		/* Appearance */
		cursor: pointer;
	}

	.personal .info .avatar {
		margin: 0.4em;
		width: 1.7em;
		height: 1.7em;
	}

	.personal .info .options {
		/* Layout */
		display: none;
		position: absolute;
		right: 0;
		top: 2.5em;
		z-index: 10;

		/* Appearance */
		border: 1px solid #aaaaaa;
		background-color: white;

		/* Other */
		border-radius: 5px;
	}

	.personal .info:hover .options {
		/* Layout */
		display: initial;

		/* Animation */
		animation: shrinked-display-animation 0.3s ease-in-out;
		transform-origin: 120px 0;
	}

	.info .options .item {
		/* Layout */
		margin: 0 1em;
		white-space: nowrap;

		/* Animation */
		transition: color 0.3s ease-in-out;
	}

	.info .options .item:hover {
		/* Appearance */
		color: black;
	}

	@keyframes shrinked-display-animation {
		from {
			opacity: 0%;
			transform: scale(0.5);
		}
		to {
			opacity: 100%;
			transform: scale(1);
		}
	}
</style>
