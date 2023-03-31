<!-- @format -->

<script setup lang="ts">
	import { ElImage } from "element-plus";
	import ReservationIndex from "../ReservationIndex.vue";

	const props = defineProps<{
		url: string;
		head: string;
	}>();
</script>

<template>
	<ReservationIndex v-bind="props">
		<template #cover="[_field, data]">
			<ElImage
				fit="cover"
				style="width: 100px; height: 100px"
				:src="data.cover"
			></ElImage>
		</template>
	</ReservationIndex>
</template>
