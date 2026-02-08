var ddaccordion = {
	ajaxloadingmsg: '<img src="/WebResource/images/loading2.gif" /><br />Loading Content...',
	headergroup: {},
	contentgroup: {},
	preloadimages: function(a) {
		a.each(function() {
			var b = new Image();
			b.src = this.src
		})
	},
	expandone: function(b, a) {
		this.toggleone(b, a, "expand")
	},
	collapseone: function(b, a) {
		this.toggleone(b, a, "collapse")
	},
	expandall: function(b) {
		var a = this.headergroup[b];
		this.contentgroup[b].filter(":hidden").each(function() {
			a.eq(parseInt($(this).attr("contentindex"))).trigger("evt_accordion")
		})
	},
	collapseall: function(b) {
		var a = this.headergroup[b];
		this.contentgroup[b].filter(":visible").each(function() {
			a.eq(parseInt($(this).attr("contentindex"))).trigger("evt_accordion")
		})
	},
	toggleone: function(e, c, a) {
		var d = this.headergroup[e].eq(c);
		var b = this.contentgroup[e].eq(c);
		if (typeof a == "undefined" || a == "expand" && b.is(":hidden") || a == "collapse" && b.is(":visible")) {
			d.trigger("evt_accordion")
		}
	},
	ajaxloadcontent: function(f, a, b, e) {
		var c = f.data("ajaxinfo");
		function d(g) {
			if (g) {
				c.cacheddata = g;
				c.status = "cached";
				if (a.queue("fx").length == 0) {
					a.hide().html(g);
					c.status = "complete";
					e()
				}
			}
			if (c.status != "complete") {
				setTimeout(function() {
					d(c.cacheddata)
				},
				100)
			}
		}
		if (c.status == "none") {
			a.html(this.ajaxloadingmsg);
			a.slideDown(b.animatespeed);
			c.status = "loading";
			$.ajax({
				url: c.url,
				error: function(g) {
					d("Error fetching content. Server Response: " + g.responseText)
				},
				success: function(g) {
					g = (g == "") ? " ": g;
					d(g)
				}
			})
		} else {
			if (c.status == "loading") {
				d(c.cacheddata)
			}
		}
	},
	expandit: function(g, a, d, f, c, b) {
		var e = g.data("ajaxinfo");
		if (e) {
			if (e.status == "none" || e.status == "loading") {
				this.ajaxloadcontent(g, a, d,
				function() {
					ddaccordion.expandit(g, a, d, f, c)
				})
			} else {
				if (e.status == "cached") {
					a.html(e.cacheddata);
					e.cacheddata = null;
					e.status = "complete"
				}
			}
		}
		this.transformHeader(g, d, "expand");
		a.slideDown(b ? 0 : d.animatespeed,
		function() {
			d.onopenclose(g.get(0), parseInt(g.attr("headerindex")), a.css("display"), f);
			if (d.postreveal == "gotourl" && c) {
				var h = (g.is("a")) ? g.get(0) : g.find("a:eq(0)").get(0);
				if (h) {
					setTimeout(function() {
						location = h.href
					},
					200)
				}
			}
		})
	},
	collapseit: function(d, a, c, b) {
		this.transformHeader(d, c, "collapse");
		a.slideUp(c.animatespeed,
		function() {
			c.onopenclose(d.get(0), parseInt(d.attr("headerindex")), a.css("display"), b)
		})
	},
	transformHeader: function(c, a, b) {
		c.addClass((b == "expand") ? a.cssclass.expand: a.cssclass.collapse).removeClass((b == "expand") ? a.cssclass.collapse: a.cssclass.expand);
		if (a.htmlsetting.location == "src") {
			c = (c.is("img")) ? c: c.find("img").eq(0);
			c.attr("src", (b == "expand") ? a.htmlsetting.expand: a.htmlsetting.collapse)
		} else {
			if (a.htmlsetting.location == "prefix") {
				c.find(".accordprefix").html((b == "expand") ? a.htmlsetting.expand: a.htmlsetting.collapse)
			} else {
				if (a.htmlsetting.location == "suffix") {
					c.find(".accordsuffix").html((b == "expand") ? a.htmlsetting.expand: a.htmlsetting.collapse)
				}
			}
		}
	},
	urlparamselect: function(b) {
		var a = window.location.search.match(new RegExp(b + "=((\\d+)(,(\\d+))*)", "i"));
		if (a != null) {
			a = RegExp.$1.split(",")
		}
		return a
	},
	getCookie: function(a) {
		var b = new RegExp(a + "=[^;]+", "i");
		if (document.cookie.match(b)) {
			return document.cookie.match(b)[0].split("=")[1]
		}
		return null
	},
	setCookie: function(a, b) {
		document.cookie = a + "=" + b + "; path=/"
	},
	init: function(a) {
		document.write('<style type="text/css">\n');
		document.write("." + a.contentclass + "{display: none}\n");
		document.write("a.hiddenajaxlink{display: none}\n");
		document.write("</style>");
		jQuery(document).ready(function(g) {
			ddaccordion.urlparamselect(a.headerclass);
			var b = ddaccordion.getCookie(a.headerclass);
			ddaccordion.headergroup[a.headerclass] = g("." + a.headerclass);
			ddaccordion.contentgroup[a.headerclass] = g("." + a.contentclass);
			var e = ddaccordion.headergroup[a.headerclass];
			var d = ddaccordion.contentgroup[a.headerclass];
			a.cssclass = {
				collapse: a.toggleclass[0],
				expand: a.toggleclass[1]
			};
			a.revealtype = a.revealtype || "click";
			a.revealtype = a.revealtype.replace(/mouseover/i, "mouseenter");
			if (a.revealtype == "clickgo") {
				a.postreveal = "gotourl";
				a.revealtype = "click"
			}
			if (typeof a.togglehtml == "undefined") {
				a.htmlsetting = {
					location: "none"
				}
			} else {
				a.htmlsetting = {
					location: a.togglehtml[0],
					collapse: a.togglehtml[1],
					expand: a.togglehtml[2]
				}
			}
			a.oninit = (typeof a.oninit == "undefined") ?
			function() {}: a.oninit;
			a.onopenclose = (typeof a.onopenclose == "undefined") ?
			function() {}: a.onopenclose;
			var c = {};
			var f = ddaccordion.urlparamselect(a.headerclass) || ((a.persiststate && b != null) ? b: a.defaultexpanded);
			if (typeof f == "string") {
				f = f.replace(/c/ig, "").split(",")
			}
			if (f.length == 1 && f[0] == "-1") {
				f = []
			}
			if (a.collapseprev && f.length > 1) {
				f = [f.pop()]
			}
			if (a.onemustopen && f.length == 0) {
				f = [0]
			}
			e.each(function(j) {
				var h = g(this);
				if (/(prefix)|(suffix)/i.test(a.htmlsetting.location) && h.html() != "") {
					g('<span class="accordprefix"></span>').prependTo(this);
					g('<span class="accordsuffix"></span>').appendTo(this)
				}
				h.attr("headerindex", j + "h");
				d.eq(j).attr("contentindex", j + "c");
				var i = d.eq(j);
				var k = i.find("a.hiddenajaxlink:eq(0)");
				if (k.length == 1) {
					h.data("ajaxinfo", {
						url: k.attr("href"),
						cacheddata: null,
						status: "none"
					})
				}
				var l = (typeof f[0] == "number") ? j: j + "";
				if (jQuery.inArray(l, f) != -1) {
					ddaccordion.expandit(h, i, a, false, false, !a.animatedefault);
					c = {
						$header: h,
						$content: i
					}
				} else {
					i.hide();
					a.onopenclose(h.get(0), parseInt(h.attr("headerindex")), i.css("display"), false);
					ddaccordion.transformHeader(h, a, "collapse")
				}
			});
			e.bind("evt_accordion",
			function(j, i) {
				var h = d.eq(parseInt(g(this).attr("headerindex")));
				if (h.css("display") == "none") {
					ddaccordion.expandit(g(this), h, a, true, i);
					if (a.collapseprev && c.$header && g(this).get(0) != c.$header.get(0)) {
						ddaccordion.collapseit(c.$header, c.$content, a, true)
					}
					c = {
						$header: g(this),
						$content: h
					}
				} else {
					if (!a.onemustopen || a.onemustopen && c.$header && g(this).get(0) != c.$header.get(0)) {
						ddaccordion.collapseit(g(this), h, a, true)
					}
				}
			});
			e.bind(a.revealtype,
			function() {
				if (a.revealtype == "mouseenter") {
					clearTimeout(a.revealdelay);
					var h = parseInt(g(this).attr("headerindex"));
					a.revealdelay = setTimeout(function() {
						ddaccordion.expandone(a.headerclass, h)
					},
					a.mouseoverdelay || 0)
				} else {
					g(this).trigger("evt_accordion", [true]);
					return false
				}
			});
			e.bind("mouseleave",
			function() {
				clearTimeout(a.revealdelay)
			});
			a.oninit(e.get(), f);
			g(window).bind("unload",
			function() {
				e.unbind();
				var h = [];
				d.filter(":visible").each(function(i) {
					h.push(g(this).attr("contentindex"))
				});
				if (a.persiststate == true && e.length > 0) {
					h = (h.length == 0) ? "-1c": h;
					ddaccordion.setCookie(a.headerclass, h)
				}
			})
		})
	}
};
ddaccordion.preloadimages(jQuery(ddaccordion.ajaxloadingmsg).filter("img"));
ddaccordion.init({
	headerclass: "expandable",
	contentclass: "categoryitems",
	revealtype: "click",
	mouseoverdelay: 200,
	collapseprev: true,
	defaultexpanded: [],
	onemustopen: false,
	animatedefault: true,
	persiststate: false,
	toggleclass: ["", "openheader"],
	togglehtml: ["prefix", "", ""],
	animatespeed: "fast",
	oninit: function(b, a) {},
	onopenclose: function(d, b, c, a) {}
});
$(function() {
	$(".categoryitems li").each(function() {
		if ($(this).find("li").length > 0) {
			$(this).find("span").addClass("spanParentStyle");
			$(this).find("ul").hide();
			$(this).find("ul").find("ul").hide();
			if ($(this).parent().parent("li").length > 0) {
				$(this).find("a").eq(0).click(function() {
					$(this).parent().find("ul").slideDown()
				})
			} else {
				$(this).find("a").eq(0).click(function() {
					if ($(this).parent().find("ul").eq(0)[0].style.display == "none") {
						$(this).parents(".arrowlistmenu").find("li").find("ul").hide();
						$(this).parent().find("ul").eq(0).slideDown()
					} else {
						$(this).parent().find("ul").eq(0).slideUp()
					}
				})
			}
		} else {
			$(this).find("span").addClass("spanStyle")
		}
	});
	$(".categoryitems >li").each(function() {
		$(this).find("a").eq(0).click(function() {
			$(this).parents(".arrowlistmenu").find("li").find("a").removeClass("arrowlistmenuCurrent");
			$(this).addClass("arrowlistmenuCurrent")
		});
		$(this).find("li").find("a").click(function() {
			$(this).parents(".arrowlistmenu").find("li").find("a").removeClass("arrowlistmenuCurrent");
			$(this).addClass("arrowlistmenuCurrent")
		})
	})
});
$(function() {
	if ($(".nav_icon_v_item").length > 0) {
		var b = 0;
		var a = jQuery.jCookie("navIconVIndex");
		if (a != false) {
			b = parseInt(a)
		}
		$(".nav_icon_v_item >a").filter(":eq(" + b + ")").addClass("current");
		$(".nav_icon_v_item >a").each(function(c) {
			$(this).click(function() {
				$(".nav_icon_v_item >a").removeClass("current");
				$(this).addClass("current");
				jQuery.jCookie("navIconVIndex", c.toString())
			})
		})
	}
});