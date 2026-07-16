var Drag = {
	obj: null,
	init: function(b, a, f) {
		if (f == null) {
			b.onmousedown = Drag.start
		}
		b.root = a;
		if (isNaN(parseInt(b.root.style.left))) {
			b.root.style.left = "0px"
		}
		if (isNaN(parseInt(b.root.style.top))) {
			b.root.style.top = "0px"
		}
		b.root.onDragStart = new Function();
		b.root.onDragEnd = new Function();
		b.root.onDrag = new Function();
		if (f != null) {
			var b = Drag.obj = b;
			f = Drag.fixe(f);
			var d = parseInt(b.root.style.top);
			var c = parseInt(b.root.style.left);
			b.root.onDragStart(c, d, f.pageX, f.pageY);
			b.lastMouseX = f.pageX;
			b.lastMouseY = f.pageY;
			document.onmousemove = Drag.drag;
			document.onmouseup = Drag.end
		}
	},
	start: function(d) {
		var a = Drag.obj = this;
		d = Drag.fixEvent(d);
		var c = parseInt(a.root.style.top);
		var b = parseInt(a.root.style.left);
		a.root.onDragStart(b, c, d.pageX, d.pageY);
		a.lastMouseX = d.pageX;
		a.lastMouseY = d.pageY;
		document.onmousemove = Drag.drag;
		document.onmouseup = Drag.end;
		return false
	},
	drag: function(i) {
		i = Drag.fixEvent(i);
		var f = Drag.obj;
		var b = i.pageY;
		var c = i.pageX;
		var h = parseInt(f.root.style.top);
		var g = parseInt(f.root.style.left);
		if (document.all) {
			Drag.obj.setCapture()
		} else {
			i.preventDefault()
		}
		var d, a;
		d = g + c - f.lastMouseX;
		a = h + (b - f.lastMouseY);
		f.root.style.left = d + "px";
		f.root.style.top = a + "px";
		f.lastMouseX = c;
		f.lastMouseY = b;
		f.root.onDrag(d, a, i.pageX, i.pageY);
		return false
	},
	end: function() {
		if (document.all) {
			Drag.obj.releaseCapture()
		}
		document.onmousemove = null;
		document.onmouseup = null;
		Drag.obj.root.onDragEnd(parseInt(Drag.obj.root.style.left), parseInt(Drag.obj.root.style.top));
		Drag.obj = null
	},
	fixEvent: function(c) {
		var a = Math.max(document.documentElement.scrollLeft, document.body.scrollLeft);
		var b = Math.max(document.documentElement.scrollTop, document.body.scrollTop);
		if (typeof c == "undefined") {
			c = window.event
		}
		if (typeof c.layerX == "undefined") {
			c.layerX = c.offsetX
		}
		if (typeof c.layerY == "undefined") {
			c.layerY = c.offsetY
		}
		if (typeof c.pageX == "undefined") {
			c.pageX = c.clientX + a - document.body.clientLeft
		}
		if (typeof c.pageY == "undefined") {
			c.pageY = c.clientY + b - document.body.clientTop
		}
		return c
	}
};