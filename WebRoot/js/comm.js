var hpzlKvs = [ {
	key : "01",
	value : "大型汽车"
}, {
	key : "02",
	value : "小型汽车"
}, {
	key : "15",
	value : "挂车"
}, {
	key : "16",
	value : "教练汽车"
}

];

function getDictValueByKey(array, key) {
	if (!array)
		return "";
	var val = "";
	$.each(array, function(i, d) {
		if (d.key == key)
			val = d.value;
	});
	return val;
}

function addOption(view, key, value) {
	$("<option value='" + key + "'>" + value + "</option>").appendTo(view);
}

function reloadSelect(view, data, hasEmpty, excludes) {
	if (_.isUndefined(data) || _.isNull(data) || _.isNaN(data)) {
		return;
	}
	var map = {};
	view.empty();
	if (hasEmpty)
		addOption(view, '', '');
	if (data != undefined && _.isArray(data)) {
		_.map(data, function(d) {
			if (!excludes || !_.isArray(excludes)
					|| _.indexOf(excludes, d.key) < 0) {
				addOption(view, d.key, d.value);
				map[d.key] = d.value;
			}
		});
	} else {
		_.map(data, function(v, k) {
			if (!excludes || !_.isArray(excludes)
					|| _.indexOf(excludes, d.key) > -1) {
				addOption(view, k, v);
			}
		});
		map = data;
	}
	if (view.length > 0 && view.get(0).length > 0) {
		view.get(0).options[0].selected = true;
	}
	return map;
}

function ifNull(s) {
	if (isEmpty(s))
		return "";
	return s;
}
function isEmpty(s) {
	return !s || s.length == 0;
}

function getTimeByFloat(f) {
	var t = f * 60;
	var hour = Math.floor(t / 60);
	var min = Math.round(t % 60);
	var s = hour + "时";
	if (min != 0) {
		s += _.padLeft(min, 2, "0") + "分";
	}
	return s;
}

function getSjHuman(jxsjs) {
	var s = _.map(jxsjs, function(j) {
		return getTimeByFloat(j.stime) + '-' + getTimeByFloat(j.etime);
	});
	return s.join(',');
}