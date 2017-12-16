(function($) {
	! function() {
		function shimDataset() {
			var datasets = [];
			Object.defineProperty(HTMLElement.prototype, 'dataset', {
				get: function() {
					var dataset, ele = this;
					forEach(datasets, function(item) {
						if(item.ele === ele) {
							dataset = item.dataset;
							return false;
						}
					});
					if(!dataset) {
						dataset = {};
						datasets.push({
							ele: ele,
							dataset: dataset
						});
						forEach(ele.attributes, function(attr) {
							var name = attr.name;
							if(/^data-/.test(name)) {
								name = name.slice(5).replace(/-(\w)/g, function(m, g) {
									return g.toUpperCase();
								});
								dataset[name] = attr.value;
							}
						});
					}
					return dataset;
				},
			});
		}

		function shimClassList() {
			// IE 9- does not support classList
			function ClassList(ele) {
				this.ele = ele;
				this.add = this._add.bind(this);
				this.remove = this._remove.bind(this);
			}
			ClassList.prototype = {
				_get: function() {
					return this.ele.className.trim().split(/\s+/);
				},
				_set: function(list) {
					this.ele.className = list.join(' ');
				},
				_add: function(cls) {
					var list = this._get();
					var i = list.indexOf(cls);
					if(i < 0) list.push(cls);
					this._set(list);
				},
				_remove: function(cls) {
					var list = this._get();
					var i = list.indexOf(cls);
					if(~i) list.splice(i, 1);
					this._set(list);
				},
			};
			var classLists = [];
			Object.defineProperty(HTMLElement.prototype, 'classList', {
				get: function() {
					var classList, ele = this;
					forEach(classLists, function(item) {
						if(item.ele === ele) {
							classList = item.classList;
							return false;
						}
					});
					if(!classList) {
						classList = new ClassList(ele);
						classLists.push({
							ele: ele,
							classList: classList
						});
					}
					return classList;
				},
			});
		}
		if(!document.body.dataset) shimDataset();
		if(!document.body.classList) shimClassList();
	}();

	function forEach(arr, cb) {
		for(var i = 0; i < arr.length; i++)
			if(cb.call(arr, arr[i], i) === false) break;
	}

	function updateLogoTab() {
		forEach(logoTabs, function(ele) {
			ele.classList[ele.dataset.type === logoTab.type ? 'remove' : 'add']('hide');
		});
	}

	function updateLogoType(ele) {
		if(logoTab.head) logoTab.head.classList.remove('active');
		logoTab.head = ele;
		logoTab.type = ele.dataset.type;
		ele.classList.add('active');
		updateLogoTab();
	}
var code_divs = document.getElementsByName("sxlcode");
	for(var i=0;i<code_divs.length;i++) {
		var q = $('#' + code_divs[i].id);
		var canvas=null;
		var s = 0;
		var colorIn = '#000';
		var colorOut = '#000';
		var colorFore = '#000';
		var colorBack = '#fff';
		var so = $('#'+ code_divs[i].id+"text").value;
		var options = {
			cellSize: Number(5),
			foreground: [
				// foreground color
				{
					style: colorFore
				},
				// outer squares of the positioner
				{
					row: 0,
					rows: 7,
					col: 0,
					cols: 7,
					style: colorOut
				}, {
					row: -7,
					rows: 7,
					col: 0,
					cols: 7,
					style: colorOut
				}, {
					row: 0,
					rows: 7,
					col: -7,
					cols: 7,
					style: colorOut
				},
				// inner squares of the positioner
				{
					row: 2,
					rows: 3,
					col: 2,
					cols: 3,
					style: colorIn
				}, {
					row: -5,
					rows: 3,
					col: 2,
					cols: 3,
					style: colorIn
				}, {
					row: 2,
					rows: 3,
					col: -5,
					cols: 3,
					style: colorIn
				},
			],
			background: colorBack,
			data: so,
			typeNumber: Number(1),
		};
		if(s >= 0)
			options.effect = {
				key: 'round',
				value: s
			};
		else
			options.effect = {
				key: 'liquid',
				value: -s
			};
		options.reuseCanvas = canvas;
		canvas = qrgen.canvas(options);
		canvas.style.width = "108px";
		canvas.style.height = "108px";
		var src=canvas.toDataURL("image/png");
		document.getElementById(""+code_divs[i].id). innerHTML = '<img src="'+src+'" width="108px" height="108px" />';
	}
})(document.querySelector.bind(document));