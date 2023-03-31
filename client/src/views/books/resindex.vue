<!-- @format -->

<script setup lang="ts">
	import ReserveIndex from "views/common/ReserveIndex.vue";

	import { ElImage } from "element-plus";

	const props = defineProps<{
		url: string;
		head: string;
	}>();
</script>

<template>
	<ReserveIndex v-bind="props">
		<template #cover="[_field, data]">
			<ElImage
				fit="cover"
				style="width: 100px; height: 100px"
				:src="data.cover"
			></ElImage>
		</template>
	</ReserveIndex>
</template>
