/*
 * @Author: Karelian_na
 */

export type DropDownElement = HTMLElement & { timeID: number };

export function dropDown(element: DropDownElement, time: number, callback?: Function) {
	if (element.timeID) {
		clearInterval(element.timeID);
	}
	var endValue = 0;
	if (element.clientHeight == 0) {
		element.style.setProperty('display', 'block');
		element.style.setProperty('visibility', 'hidden');
		element.style.setProperty('position', 'absolute');
		endValue = element.clientHeight;
		element.style.removeProperty('visibility');
		element.style.removeProperty('position');
		element.style.setProperty('height', '0px');
	}

	element.timeID = setInterval(function () {
		var curValue = Math.ceil(parseFloat(getComputedStyle(element)['height']));
		if (isNaN(curValue)) {
			clearInterval(element.timeID);
			element.style.removeProperty('height');
			element.style.removeProperty('display');
			callback?.call(null);
		} else {
			var speed = (endValue - curValue) / 8;
			speed = speed > 0 ? Math.ceil(speed) : Math.floor(speed);
			if (speed == 0) {
				clearInterval(element.timeID);
				element.style.removeProperty('height');
				element.style.removeProperty('display');
				callback?.call(null);
			} else {
				element.style.setProperty('height', (curValue + speed) + 'px');
			}
		}
	}, time / 25);

}