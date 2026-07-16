if ($.fn.pagination) {
    $.fn.pagination.defaults.beforePageText = '第';
    $.fn.pagination.defaults.afterPageText = '共{pages}页';
    $.fn.pagination.defaults.displayMsg = '显示{from}到{to},共{total}记录';
}
if ($.fn.datagrid) {
    $.fn.datagrid.defaults.loadMsg = 'body_正在处理，请稍待……';
    $.fn.datagrid.defaults.pageList = [10, 20, 30, 40, 50, 100, 200, 500, 1000];
}
if ($.fn.treegrid && $.fn.datagrid) {
    $.fn.treegrid.defaults.loadMsg = $.fn.datagrid.defaults.loadMsg;
    $.fn.treegrid.defaults.pageList = $.fn.datagrid.defaults.pageList;
}
if ($.messager) {
    $.messager.defaults.ok = '确定';
    $.messager.defaults.cancel = '取消';
}

if ($.fn.validatebox) {
    $.fn.validatebox.defaults.missingMessage = '该输入项为必输项';
    $.fn.validatebox.defaults.rules.email.message = '请输入有效的电子邮件地址';
    $.fn.validatebox.defaults.rules.url.message = '请输入有效的URL地址';
    $.fn.validatebox.defaults.rules.length.message = '输入内容长度必须介于{0}和{1}之间';
    $.fn.validatebox.defaults.rules.remote.message = '请修正该字段';

    $.extend($.fn.validatebox.defaults.rules, {
        min_max_len: {
            validator: function (v, p) {
                p[4] = "";
                if (v.length > p[1]) { $(p[2]).val(v.substring(0, v.length - 1)); }
                if (v.length < p[0]) { p[4] = "输入值长度为" + p[0] + "-" + p[1] + "个字符, 已输入" + v.length + "个字符!"; }
                if (p[3] == true && !is_alpha(v)) { p[4] = p[4] + "输入了非法字符, 仅可输入字母,数字,下划线!"; return false; }
                return v.length >= p[0] && v.length <= p[1];
            },
            message: '{4}'
        },
        v_same: {
            validator: function (v, p) {
                return v == $(p[0]).val();
            },
            message: '两次输入密码不一致, 请重新输入!'
        },
        v_email: {
            validator: function (v, p) {
                return is_email(v);
            },
            message: '电子邮件格式错误!(格式: xxx@xxx.xxx)'
        },
        v_numint: {
            validator: function (v, p) {
                return is_numint(v);
            },
            message: '格式不正确!(格式如: 20、300)'
        },
        v_double: {
            validator: function (v, p) {
                return is_double(v);
            },
            message: '格式不正确!(格式如: 20.19、300.28)'
        },
        v_phone: {
            validator: function (v, p) {
                p[1] = "";
                if (v.length < p[0]) { p[1] = "手机号码长度为" + p[0] + "位数字, 已输入" + v.length + "位数字!<br />"; return false; }
                else if (!is_phone(v)) { p[1] = p[1] + "手机号码格式错误!(以13[0-9],15[0-3|5-9]或18[5-9]开头的号码段,长度" + p[0] + "位数字)"; return false; }
                return true;
            },
            message: '{1}'
        }
    });

    $.extend($.fn.validatebox.methods, {
        remove: function (jq, newposition) {
            return jq.each(function () {
                $(this).removeClass("validatebox-text validatebox-invalid").unbind('focus').unbind('blur');
            });
        },
        reduce: function (jq, newposition) {
            return jq.each(function () {
                var opt = $(this).data().validatebox.options;
                $(this).addClass("validatebox-text").validatebox(opt);
            });
        }
    });
}
function is_alpha(v) {
    return /(^[A-Za-z0-9_]+$)/.test(v);
}
function is_email(v) {
    return /^([\w]+)(.[\w]+)*@([\w-]+\.){1,5}([A-Za-z]){2,4}$/.test(v);
}
function is_numint(v) {
    return /^\d+$/.exec(v);
}
function is_double(v) {
    return /^\d+(?=\.{0,1}\d+$|$)/.exec(v);
}
function is_phone(v) {
    if (/^13\d{9}$/g.test(v) || (/^15[0-35-9]\d{8}$/g.test(v)) || (/^18[05-9]\d{8}$/g.test(v))) {
        return true;
    } else {
        return false;
    }
}

if ($.fn.numberbox) {
    $.fn.numberbox.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.combobox) {
    $.fn.combobox.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.combotree) {
    $.fn.combotree.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.combogrid) {
    $.fn.combogrid.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.numberspinner) {
    $.fn.numberspinner.defaults.missingMessage = '该输入项为必输项';
}
if ($.fn.calendar) {
    $.fn.calendar.defaults.weeks = ['日', '一', '二', '三', '四', '五', '六'];
    $.fn.calendar.defaults.months = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'];
}
if ($.fn.datebox) {
    $.fn.datebox.defaults.currentText = '今天';
    $.fn.datebox.defaults.closeText = '关闭';
    $.fn.datebox.defaults.okText = '确定';
    $.fn.datebox.defaults.resetText = '重置';
    $.fn.datebox.defaults.missingMessage = '该输入项为必输项';
    $.fn.datebox.defaults.formatter = function (date) {
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        var d = date.getDate();
        return y + '-' + (m < 10 ? ('0' + m) : m) + '-' + (d < 10 ? ('0' + d) : d);
    };
    $.fn.datebox.defaults.parser = function (s) {

        if (!s) {
            return new Date();
        }
        var ss = s.split('-');
        var y = parseInt(ss[0], 10);
        var m = parseInt(ss[1], 10);
        var d = parseInt(ss[2], 10);
        if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
            return new Date(y, m - 1, d);
        } else {
            return new Date();
        }
    };
}
if ($.fn.datetimebox && $.fn.datebox) {
    $.extend($.fn.datetimebox.defaults, {
        currentText: $.fn.datebox.defaults.currentText,
        closeText: $.fn.datebox.defaults.closeText,
        okText: $.fn.datebox.defaults.okText,
        resetText: $.fn.datebox.defaults.resetText,
        missingMessage: $.fn.datebox.defaults.missingMessage
    });
}
if ($.fn.window) {
    $.fn.window.defaults.draggable = false;
    $.fn.window.defaults.maximizable = false;
    $.fn.window.defaults.collapsible = false;
    $.fn.window.defaults.minimizable = false;
    $.fn.window.defaults.closable = false;
}
if ($.fn.tree) {
    $.extend($.fn.tree.methods, {
        getCheckedExt: function (jq) {
            var checked = $(jq).tree("getChecked");
            var checkbox2 = $(jq).find("span.tree-checkbox2").parent();
            $.each(checkbox2, function () {
                var node = $.extend({}, $.data(this, "tree-node"), {
                    target: this
                });
                checked.push(node);
            });
            return checked;
        },
        getSolidExt: function (jq) {
            var checked = [];
            var checkbox2 = $(jq).find("span.tree-checkbox2").parent();
            $.each(checkbox2, function () {
                var node = $.extend({}, $.data(this, "tree-node"), {
                    target: this
                });
                checked.push(node);
            });
            return checked;
        }
    });
    $.fn.tree.defaults.loadFilter = function (data, parent) {
        var opt = $(this).data().tree.options;
        var idFiled,
	textFiled,
	parentField;
        if (opt.parentField) {
            idFiled = opt.idFiled || 'id';
            textFiled = opt.textFiled || 'text';
            parentField = opt.parentField;
            var i,
		l,
		treeData = [],
		tmpMap = [];

            for (i = 0, l = data.length; i < l; i++) {
                tmpMap[data[i][idFiled]] = data[i];
            }
            for (i = 0, l = data.length; i < l; i++) {
                if (tmpMap[data[i][parentField]] && data[i][idFiled] != data[i][parentField]) {
                    if (!tmpMap[data[i][parentField]]['children'])
                        tmpMap[data[i][parentField]]['children'] = [];
                    data[i]['text'] = data[i][textFiled];
                    tmpMap[data[i][parentField]]['children'].push(data[i]);
                } else {
                    data[i]['text'] = data[i][textFiled];
                    treeData.push(data[i]);
                }
            }
            return treeData;
        }
        return data;
    };
}

if ($.fn.panel) {
    $.fn.panel.defaults = $.extend({}, $.fn.panel.defaults, { onBeforeDestroy: function () {
        var frame = $('iframe', this);
        if (frame.length > 0) {
            frame[0].contentWindow.document.write('');
            frame[0].contentWindow.close();
            frame.remove();
            if ($.browser.msie) {
                CollectGarbage();
            }
        }
    }
    });

}
if ($.fn.datagrid) {
    //扩展的方法
    $.extend($.fn.datagrid.methods, {
        keyCtr: function (jq) {
            return jq.each(function () {
                var grid = $(this);
                grid.datagrid('getPanel').panel('panel').attr('tabindex', 1).bind('keydown', function (e) {
                    switch (e.keyCode) {
                        case 38: // up
                            var selected = grid.datagrid('getSelected');
                            if (selected) {
                                var index = grid.datagrid('getRowIndex', selected);
                                grid.datagrid('selectRow', index - 1);
                            } else {
                                var rows = grid.datagrid('getRows');
                                grid.datagrid('selectRow', rows.length - 1);
                            }
                            break;
                        case 40: // down
                            var selected = grid.datagrid('getSelected');
                            if (selected) {
                                var index = grid.datagrid('getRowIndex', selected);
                                grid.datagrid('selectRow', index + 1);
                            } else {
                                grid.datagrid('selectRow', 0);
                            }
                            break;
                    }
                });
            });
        }
    });
}
//$("#id").datagrid({}).datagrid("keyCtr");
if ($.fn.validatebox) {
    $.extend($.fn.validatebox.methods, {
        remove: function (jq, newposition) {
            return jq.each(function () {
                $(this).removeClass("validatebox-text validatebox-invalid").unbind('focus').unbind('blur');
            });
        },

        reduce: function (jq, newposition) {
            return jq.each(function () {
                var opt = $(this).data().validatebox.options;
                $(this).addClass("validatebox-text").validatebox(opt);
            });
        }
    });
}
//$('#id').validatebox('remove');
//$('#id').validatebox('reduce');
/**JQuery扩展**/
$.fn.W = function (_e) {
    return this.each(function () {
        if (!$.boxModel && $.browser.msie) {
            $(this).width(_e);
        } else {
            $(this).width(_e - ($(this).outerWidth() - $(this).width()));
        }
    });
};
$.fn.H = function (_f) {
    return this.each(function () {
        if (!$.boxModel && $.browser.msie) {
            $(this).height(_f);
        } else {
            $(this).height(_f - ($(this).outerHeight() - $(this).height()));
        }
    });
};
/**表单数据转Json**/
(function ($) {
    $.fn.extend({
        form2json: function () {
            var obj = {};
            var count = 0;
            $.each(this.serializeArray(), function (i, o) {
                var n = o.name, v = o.value;
                count++;
                obj[n] = obj[n] === undefined ? v : ($.isArray(obj[n]) ? obj[n].concat(v) : [obj[n], v]);
            });
            obj.CertKey = url_val('cey');
            return obj;
        }
    });

})(jQuery);
/**Json转Url**/
$.json2par = function (json) {
    var tmps = [];
    for (var key in json) {
        tmps.push(key + '=' + json[key]);
    }
    return tmps.join('&');
};
$.fn.setCursorPosition = function (position) {
    if (this.lengh == 0) {
        return this;
    }
    return $(this).setSelection(position, position);
}
$.fn.setSelection = function (start, end) {
    if (this.lengh == 0) return this;
    input = this[0];
    if (input.createTextRange) {
        var range = input.createTextRange();
        range.collapse(true);
        range.moveEnd('character', end);
        range.moveStart('character', start);
        range.select();
    } else if (input.setSelectionRange) {
        input.focus();
        input.setSelectionRange(start, end);
    }
    return this;
}

$.fn.FE = function () {
    this.setCursorPosition(this.val().length);
}


Date.prototype.format = function (format) {
    var o = {
        "M+": this.getMonth() + 1, ////月份   
        "d+": this.getDate(),    //日   
        "h+": this.getHours(),   //小时   
        "m+": this.getMinutes(), //分   
        "s+": this.getSeconds(), //秒   
        "q+": Math.floor((this.getMonth() + 3) / 3),  //季度   
        "S": this.getMilliseconds() //毫秒   
    };
    if (/(y+)/.test(format)) format = format.replace(RegExp.$1,
        (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o) if (new RegExp("(" + k + ")").test(format))
        format = format.replace(RegExp.$1,
        RegExp.$1.length == 1 ? o[k] :
("00" + o[k]).substr(("" + o[k]).length));
    return format;
}
Date.prototype.addDays = function (d) {
    this.setDate(this.getDate() + d);
};
Date.prototype.addWeeks = function (w) {
    this.addDays(w * 7);
};
Date.prototype.addMonths = function (m) {
    var d = this.getDate();
    this.setMonth(this.getMonth() + m);

    if (this.getDate() < d)
        this.setDate(0);
};
Date.prototype.addYears = function (y) {
    var m = this.getMonth();
    this.setFullYear(this.getFullYear() + y);

    if (m < this.getMonth()) {
        this.setDate(0);
    }
};
Date.prototype.addSeconds = function (s) {
    var d = this.getDate();
    this.setSeconds(this.getSeconds() + s);
    if (this.getDate() < d) {
        this.setDate(0);
    }
}
Date.prototype.getWeek = function () {

    var arr_week = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
    return arr_week[this.getDay()];
}
function formateDate(value, rec) {
    if (value.length > 19) {
        return value.substring(0, 19);
    }
    return value;
}
function Class() { };
Class.prototype.imgpath = "../";
Class.prototype.errmsg = "抱歉，无法处理您的请求！";
Class.prototype.errimg = "Images/Tools/no.png";
Class.prototype.sesmsg = "抱歉，您已登录超时，请重新登录！";
Class.prototype.undefined = "undefined";
Class.prototype.url = "../Service/AjaxService.ashx";
/*主子表，子表是否全部显示*/
Class.prototype.isAll = true;
Class.prototype.cknull = function cknull(val, defval) {
    return ((val == this.undefined || val == '' || typeof val == this.undefined) ? defval : val);
}
/*取当前域名*/
Class.prototype.GR = function GR() {
    var _pN = location.pathname.substring(1);
    var webName = _pN == "" ? "" : _pN.substring(0, _pN.indexOf("/"));
    return location.protocol + "//" + location.host + "/" + webName + "/";
};
/**弹出提示信息**/
Class.prototype.Alert = function Alert(title, msgString, msgType) {
    $.messager.alert(title, msgString, msgType);
};

/**提示信息，本地调试专用，远程访问不提示**/
Class.prototype.show = function show(msg) {
    if (this.GR().indexOf('localhost') != -1) {
        this.Alert('提示信息', msg, '');
    }
}
/**自动提示信息**/
Class.prototype.Msg = function Msg(cf) {
    if (parent == window) {
        util.AMsg(cf);
    } else {

        var p = window.top;
        if (p) {
            p.util.AMsg(cf);
        }
    }
};
Class.prototype.nMsg = function nMsg(cf) {
    if (parent == window) {
        util.AMsg(cf);
    } else {
        var p = parent;
        if (p) {
            p.util.AMsg(cf);
        }
    }
};
Class.prototype.DF = function DF(id) {
    if (id != '') {
        var _37c = $('#' + id);
        if (_37c) {
            if (_37c.attr('class')) {
                if (_37c.attr('class').indexOf("easyui-combotree") != -1) {
                    _37c.combotree('textbox').focus();
                } else if (_37c.attr('class').indexOf("easyui-combobox") != -1) {
                    _37c.combobox('textbox').focus();
                } else
                    _37c.FE();
            } else {
                _37c.FE();
            }
        }
    }
};
Class.prototype.pW = function pW(cc) {
    var h = $(window).height();
    if (cc) { h = cc.height(); }
    var w = $(document.body).outerWidth(true);
    if (cc) { w = cc.width(); }
    $("<div class=\"datagrid-mask\"></div>").css({ display: "block", width: "100%", height: h }).appendTo(cc || "body");
    $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候……").appendTo(cc || "body").css({ display: "block", left: (w - 190) / 2, top: (h - 45) / 2 });
}
Class.prototype.cPW = function cPW() {
    $(".datagrid-mask").remove();
    $(".datagrid-mask-msg").remove();
};
//可以自动关闭的提示
Class.prototype.AMsg = function AMsg(cf) {
    $(".dz").remove();
    if (!cf.msg) {
        cf.msg = "正在处理，请稍候……";
    }
    if (cf.img == "-1") {
        cf.img = this.imgpath + 'Images/Tools/loading.gif';
    }
    if (cf.img != "") {
        cf.msg = "<img src=" + this.imgpath + cf.img + " align=absmiddle>&nbsp;" + cf.msg;
    }
    var style = "position: absolute;display: none;z-index:11000;";
    switch (cf.model) {
        case "1":
            style += "padding:5px 15px 5px 5px;border: 2px solid #F9F2B7;color: #F00;background-color:#FDF8E8;";
            break;
        case "2":
            cf.msg = "<div style=\"" + "background-color:#FFF;border:solid 1px #3E8EB3;line-height:18px;margin:5px;padding:10px;" + "\">" + cf.msg + '</div>';
            style += "background-color:#74cbed;border:solid 1px #000;color:#F00;text-align:center;z-index:10001";
            break;
        case "0":
        default:
            style += "padding:5px 15px 5px 5px;border: 2px solid #6593CF;color: #222;background-color:#FFF;";
            break;
    }
    var _d = $("<div class=\"dz\"  style=\"" + style + "\">" + cf.msg + "</div>").appendTo($("body"));
    var w = $(document.body).outerWidth(true) - _d.outerWidth(true);

    var h = $(document.body).H($(document).height()).height() - _d.outerHeight(true);

    function g(r, v) {
        if (typeof (r) == "undefined") {
            r = parseInt(v) / 2;
        } else {
            if (r < 1) {
                r = parseInt(v * r);
            }
        }
        return parseInt(r);
    }
    _d.css("left", g(cf.left, w));
    _d.css("top", g(cf.top, h));
    _d.show();
    if (typeof (cf.click) != false) {
        _d.bind("click", function (e) {
            $(".dz").remove();
            if (typeof (cf.fun) != "undefined") {
                cf.fun.call(this, cf);
            }
        });
    }
    if (parseInt(cf.time) != -1) {
        if (typeof (cf.fun) != "undefined") {
            setTimeout(function () {
                cf.fun.call(this, cf);
            }, ((typeof (cf.time) == "undefined") ? 1500 : parseInt(cf.time) / 2));
        }
        //默认3秒后清除提示
        setTimeout(function () {
            //如果动画结束则删除节点
            if (!$(".dz").is(":animated")) {
                $(".dz").remove();
            }
            $(".dz").fadeOut(500);
        }, ((typeof (cf.time) == "undefined") ? 3000 : parseInt(cf.time)));
    }
}

Class.prototype.ajax = function ajax(cf) {
    cf.url = cf.url || this.url;
    cf.top = cf.top || 35;
    cf.dataType = cf.dataType || "json";

    if (cf.msg) {
        this.Msg({ top: cf.top, msg: cf.msg, img: 'images/Tools/loading.gif', time: -1 });
    }
    if (cf.param) {
        cf.param.CertKey = url_val("cey");
    }
    $.ajax({
        url: encodeURI(cf.url),
        async: true,
        data: cf.param,
        dataType: cf.dataType,
        type: "post",
        success: function (r) {
            if (r.slay == -1) {
                location.href = "LoginOut.aspx";
            }
            else if (r.slay == 0) {
                util.Msg({ msg: r.msg, img: util.errimg, top: cf.top });
            }
            else {
                cf.callback(r, cf.param);
            }
        },
        error: function (x, t, e) {
            util.Msg({ msg: util.errmsg, img: util.errimg, top: cf.top });
        }
    });
};
Class.prototype.aJson = function aJson(cf) {
    cf.url = cf.url || this.url;
    cf.async = cf.async || false;
    cf.dataType = cf.dataType || "json";
    if (cf.param) {
        if (cf.notcey != true) {
            cf.param.CertKey = url_val("cey");
        }
    }
    $.ajax({
        url: encodeURI(cf.url),
        async: cf.async,
        data: cf.param,
        dataType: cf.dataType,
        type: "post",
        success: function (r) {
            if (r) {
                if (r.slay == -1) {
                    location.href = "LoginOut.aspx";
                }
                else {
                    cf.callback(r, cf.param);
                }
            }
        }
    });
};

Class.prototype.callback = function callback(data, back) {
    if (typeof data == "string") {
        eval('data=' + data);
    }
    if (data.slay == -1) {
        parent.logOut('');
    } else if (typeof data.error != this.undefined) {
        // util.cT();

    } else if (typeof back != this.undefined) {
        back(data);
    }
};
Class.prototype.del = function del(cf) {
    var ks = "";
    cf.id = cf.id || "gridTable";
    cf.url = cf.url || this.url;
    var ch = false;
    var ct = (typeof (cf.ct) == this.undefined) ? '删除' : cf.ct;
    cf.m = (typeof (cf.m) == this.undefined) ? '记录' : cf.m
    var msg;
    var KeyCount = 0;
    if (typeof cf.index != this.undefined && cf.index != -1) {
        var row = this.getRow(cf.index, '#' + cf.id);
        ks = row['KeyNum'];
        var m = row[cf.cname];
        KeyCount = 1;
        m = (typeof m == this.undefined || m == '') ? cf.m : cf.m + '[' + m + ']';
        msg = "您确定要" + ct + m + "吗?";
    } else {
        var sc = $('#' + cf.id).datagrid("getSelections");
        if (sc.length == 0) {
            this.Alert('提示', '请先选中要' + ct + '的' + cf.m + '。', 'warning');
            return;
        } else {
            msg = "您确定要将选中" + cf.m + ct + "吗?";
            for (var j = 0; j < sc.length; j++) {
                if (ks != "") {
                    ks += ",";
                }
                ks += sc[j].KeyNum;
                KeyCount++;
            }
        }
    }
    $.messager.confirm('提示', msg, function (r) {
        if (r) {
            cf.param.KeyID = ks;
            cf.param.KeyCount = KeyCount;
            util.ajax(cf);
        }
    });
};
Class.prototype.bat = function bat(cf) {
    var ks = "";
    cf.url = cf.url || this.url;
    cf.index = cf.index || -1;
    cf.TableId = cf.TableId || "gridTable";
    cf.TipTitle = cf.TipTitle || "记录";
    cf.TipColumn = cf.TipColumn || "KeyNum";
    cf.Mode = cf.Mode || "删除";
    cf.Confirm = cf.Confirm || "您确定要" + cf.Mode + "吗?";
    cf.SelectAll = cf.SelectAll || "选中";
    cf.RangeData = cf.RangeData || "getSelections";
    cf.AfterMsg = cf.AfterMsg || "";
    var ch = false;
    if (cf.index != -1) {
        var m = '';
        if (cf.index == -2) {
            ks = cf.param.KeyNum;
            m = cf.param[cf.TipColumn] || "";
        } else {
            var row = this.getRow(cf.index, '#' + cf.TableId);
            ks = row['KeyNum'];
            m = row[cf.TipColumn] || "";
        }
        cf.Confirm = "您确定要" + cf.Mode + ((m == "") ? cf.TipTitle : (cf.TipTitle + "[" + m + "]")) + "吗?" + cf.AfterMsg;
    } else {
        var sc = $('#' + cf.TableId).datagrid(cf.RangeData);
        if (sc.length == 0) {
            this.Alert('提示', '请先' + cf.SelectAll + cf.TipTitle + '。', 'warning');
            return;
        } else {
            cf.Confirm = ("您确定要将" + cf.SelectAll + cf.TipTitle + cf.Mode + "吗?" + cf.AfterMsg);
            for (var j = 0; j < sc.length; j++) {
                if (ks != "") {
                    ks += ",";
                }
                ks += sc[j].KeyNum;
            }
        }
    }
    $.messager.confirm('提示', cf.Confirm, function (r) {
        if (r) {
            cf.param.KeyID = ks;
            util.ajax(cf);
        }
    });
};
Class.prototype.spage = function spage(divId, divName, pid) {
    pid = util.cknull(pid, "#pagelist");
    divName = util.cknull(divName, "pagelist_");
    var zDivCount = $(pid).children("div").length
    $(pid).children("div").each(function () {
        var id = $(this).prop("id");
        if (id.replace(divName, "") != divId) {
            $(this).hide();
        }
    });
    $("#" + divName + divId).show();
}
Class.prototype.getRow = function getRow(rowIndex, id) {
    var rows = $(typeof (id) == "undefined" ? '#gridTable' : id).datagrid("getRows");
    return rows[rowIndex];
}
Class.prototype.getAjaxRow = function getAjaxRow(cf) {
    cf.url = this.cknull(cf.url, this.url); //远程数据地址
    if (cf.param) {
        cf.param.time = new Date().toString();
    }
    $.ajax({
        url: cf.url,
        data: cf.param,
        dataType: "JSON",
        async: false,
        type: "POST",
        success: function (r) {
            cf.callback(r, cf);
        }
    });
}
Class.prototype.GI = function GI(val) {
    return parseInt(val);
}
Class.prototype.FW = function FW(p, min, max) {
    var w = this.GI(document.body.clientWidth * p);
    if (typeof (min) != "undefined") {
        w = parseInt(min) > w ? min : w;
    }
    if (typeof (max) != "undefined") {
        w = parseInt(max) < w ? max : w;
    }
    return w;
}
Class.prototype.FH = function FH(p, min, max) {
    var w = this.GI(this.BH(0) * p);
    if (typeof (min) != "undefined") {
        w = parseInt(min) > w ? min : w;
    }
    if (typeof (max) != "undefined") {
        w = parseInt(max) < w ? max : w;
    }
    return w;
}
Class.prototype.WR = function WR(w, h) {
    return { width: w, height: h, left: parseInt((util.BW() - w) / 2), top: parseInt((util.BH() - h) / 2) };
}
Class.prototype.GF = function GF(i, c, ix) {
    var sr = "";
    var as = c.split("|");
    if (typeof (ix) == "undefined") {
        ix = '';
    }
    for (var s = 0; s < as.length; s++) {
        if (as[s] == "s")
            sr += '<a href="#" onclick="show' + ix + '(' + i + ')">查看</a>';
        if (as[s] == "e")
            sr += '<a href="#" onclick="edit' + ix + '(' + i + ')">修改</a>';
        if (as[s] == "d")
            sr += '<a href="#" onclick="del' + ix + '(' + i + ')">删除</a>';
        if (s < as.length - 1)
            sr += " | ";
    }
    return sr;
}
Class.prototype.GP = function GP(id, param) {
    if ($(id).length > 0) {
        var ary = $(id).combobox('getData');
        if (ary) {
            $.each(ary, function (i, n) {
                var qx = n["value"];
                if (qx == param.qt) {
                    param[qx] = param.qv;
                } else {
                    param[qx] = "";
                }
            });
        }
    }
    return param;
}
Class.prototype.BH = function BH(x) {
    return $(document.body).H($(document).height()).height() - ((x) ? x : 0);
}
Class.prototype.BW = function BW(x) {
    return $(document.body).W($(document).width()).width() - ((x) ? x : 0);
}
Class.prototype.ajaxbox = function ajaxbox(cf) {
    cf.url = this.cknull(cf.url, this.url); //远程数据地址
    cf.txt = this.cknull(cf.txt, 'text'); //显示值
    cf.key = this.cknull(cf.key, 'id'); //隐藏值
    cf.cid = this.cknull(cf.cid, ''); //控件id
    cf.add = this.cknull(cf.add, false); //是否增加项
    cf.avl = this.cknull(cf.avl, ''); //增加项显示值
    cf.svl = this.cknull(cf.svl, ''); //增加项隐藏值,默认选择值
    cf.google = this.cknull(cf.google, false); //自动补全
    cf.required = cf.required || false;
    cf.sindex = this.cknull(cf.svl, this.cknull(cf.sindex, '0')); //默认选取

    if (cf.param) {
        cf.param.time = new Date().toString();
        if (cf.notcey != true) {
            cf.param.CertKey = url_val("cey");
        }
    }
    if (cf.cid != '') {
        var dc1 = $("#" + cf.cid);
        var dary = new Array();
        if (typeof cf.url != this.undefined) {
            if (cf.param) {
                cf.param.time = new Date().toString();
            }
            $.ajax({
                url: cf.url,
                data: cf.param,
                dataType: "JSON",
                async: false,
                type: "POST",
                success: function (ary) {
                    if (ary.length > 0) {
                        dary = ary;
                    }
                }
            });
        }
        //if (dary) {
        if (cf.add) {
            var ru = {};
            eval("ru={ " + cf.key + ": '" + cf.avl + "', " + cf.txt + ": '" + cf.avl + "' }");
            dary.unshift(ru);
        }

        if (cf.google) {
            dc1.combobox({ valueField: cf.key, textField: cf.txt, multiple: cf.multiple, editable: cf.google, panelHeight: (dary.length < 10 ? 'auto' : 200) });
            var myKeyHandler = $.extend({}, $.fn.combobox.defaults.keyHandler, {
                query: function (q) {
                    if (cf.param) {
                        cf.param.time = new Date().toString();
                    }
                    cf.param[cf.txt] = q;
                    $.ajax({
                        url: cf.url,
                        data: cf.param,
                        dataType: "JSON",
                        async: false,
                        type: "POST",
                        success: function (ary) {
                            if (ary) {
                                dc1.combobox("setValue", q);
                                dc1.combobox("loadData", ary);
                            }
                        }
                    });
                }
            });
            dc1.combobox({
                keyHandler: myKeyHandler
            });
        }
        else {
            var df = "";
            if (dary) {
                if (typeof cf.svl == "string") {
                    df = cf.svl;
                } else {
                    if (parseInt(cf.sindex) != -1) {
                        df = dary[parseInt(cf.sindex)][cf.key];
                    } else { df = ""; }
                }
            }
            dc1.combobox({ data: dary, valueField: cf.key, textField: cf.txt, multiple: cf.multiple, editable: cf.editable, panelHeight: (dary.length < 10 ? 'auto' : 200), value: df });
        }
    }
}
Class.prototype.SC = function SC(cc) {
    var er = 0;
    var c = null;
    if (cc) {
        c = cc;
    }
    $(".edittable[no!='1']", c).find("tr").each(function () {
        if ($(this).attr("no") != "1") {
            $(this).css("background-color", (er % 2 == 0) ? "#F0FAFF" : "#FFFFFF");
            er++;
        }
    });
}
Class.prototype.getShowRow = function getShowRow(show, row) {
    var html = "";
    if (show != "") {
        var fig = {};
        if (show.indexOf(':') == -1) {
            eval("fig={ name:'" + show + "'}");
        } else {
            eval("fig={ " + show + "}");
        }
        var begHtml = fig.bhtml || '';
        var endHtml = fig.ehtml || '';
        var val = '';
        if (fig.name.indexOf("+") != -1) {
            var ary = fig.name.split("+");
            for (var i = 0; i < ary.length; i++) {
                var na = ary[i];
                if (na.indexOf("STRING") == -1) {
                    val += row[na] || '';
                } else {
                    val += na.rAll('STRING', "");
                }
            }
        } else {
            val = row[fig.name] || '';
        }
        if (val) {
            switch (fig.type) {
                case "bool":
                    html = (val == "1" ? "是" : "否");
                    break;
                case "boolStr":
                    html = (val == "1" ? fig.yes : fig.no);
                    break;
                case "date":
                    html = (val == "" ? "" : val.toTime().format("yyyy-MM-dd"));
                    break;
                case "time":
                    html = (val == "" ? "" : val.toTime().format("yyyy-MM-dd hh:mm:ss"));
                    break;
                case "date10":
                    html = (val == "" ? "" : val.toTime().format("yyyy-MM-dd hh"));
                    break;
                case "date12":
                    html = (val == "" ? "" : val.toTime().format("yyyy-MM-dd hh:mm"));
                    break;
                default:
                    html = val.rAll('\r\n', "<br/>");
                    break;
            }
        }
        html = begHtml + html + endHtml;
        //$(showId).html(html);
        fig = null;
    }
    return html;
}
function showInfo(show, row, s) {

}

var util = new Class();
$(document).ready(function () {
    $('body').bind('keydown', onBodyKeyDown);
    $('body').css('visibility', 'visible');
    parent.util.cPW();
});
onload = function () {
    parent.util.cPW();
}
//处理回车返回问题
function onBodyKeyDown(event) {
    var srcElement = event.srcElement ? event.srcElement : event.target;
    if (event.keyCode == 13) {
        if (srcElement.tagName != "TEXTAREA") {

        }
    } else {
        if (event.keyCode != 8
		|| (srcElement.tagName == "TEXTAREA" && ($(srcElement).attr("readonly") == false || typeof ($(srcElement).attr("readonly")) == util.undefined))
		|| (srcElement.tagName == "INPUT" && ($(srcElement).attr("readonly") == false || typeof ($(srcElement).attr("readonly")) == util.undefined) && (srcElement.type.toLocaleLowerCase() == "text" || srcElement.type.toLocaleLowerCase() == "password"))) {
            return true;
        }
        return false;
    }
}


/**自定义方法**/
String.prototype.replaceAll = function (s1, s2) {
    return this.replace(new RegExp(s1, "gm"), s2);
}
String.prototype.rAll = function (s1, s2) {
    return this.replace(new RegExp(s1, "gm"), s2);
}
String.prototype.toTime = function () {
    return new Date(this.rAll('-', '/'));
}
function getAllPathKeyNum(id, ztree) {
    var tree = ztree ? ztree : $('#' + id).combotree('tree');
    var node = tree.tree('getSelected');
    var str = '';
    if (node) {
        str = node.id;
        while (node.attributes.ParentKeyNum != url_val('cey')) {
            node = tree.tree('getParent', node.target);
            if (node) {
                if (node.id != '') {
                    str = node.id + "→" + str;
                }
            } else {
                break;
            }

        }
    }
    return str;
}
function getAllPathText(id, ztree) {
    var tree = ztree ? ztree : $('#' + id).combotree('tree');
    var node = tree.tree('getSelected');
    var str = '';
    if (node) {
        str = node.text;
        while (node && node.attributes.ParentKeyNum != url_val('cey')) {
            node = tree.tree('getParent', node.target);
            if (node) {
                if (node.text != '') {
                    str = node.text + "→" + str;
                }
            } else {
                break;
            }
        }
    }

    return str;
}
function getFeeTypeStr(v) {
    var rv = "月付";
    switch (v.toString()) {
        case "3": rv = "季付"; break;
        case "6": rv = "半年付"; break;
        case "12": rv = "年付"; break;
        default:
            rv = "月付";
            break;
    }
    return rv;
}
function logOut(lv) {
    if (lv == '' || lv == "{\"slay\":-1,\"msg\":\"您已经登录超时，请重新登录！\"}") {
        $.messager.alert('提示', '您以登陆超时，请重新登陆！', 'warning', function () {
            if (parent == window) {
                location.href = "LoginOut.aspx";
            } else {
                parent.location.href = "LoginOut.aspx";
            }
        });
        lv = '';
    }
    return lv;
}
/**得到最后一个字母位置**/
function GLL(str) {
    var len = str.length;
    for (var i = 0; i < len; i++) {
        var s = str.substr(len - 1 - i, 1);
        var c = s.charCodeAt();
        if ((c > 64 && c < 91) || (c > 96 && c < 123)) {
            return len - 1 - i;
        }
    }
}

/**取URl参数**/
function url_val(p) {
    var returnVal = "";
    try {
        var paramUrl = window.location.search;
        //处理长度   
        if (paramUrl.length > 0) {
            paramUrl = paramUrl.substring(1, paramUrl.length);
            var paramUrlArray = paramUrl.split("&");
            for (var i = 0; i < paramUrlArray.length; i++) {
                if (paramUrlArray[i].toLowerCase().indexOf(p.toLowerCase()) != -1) {
                    var temp = paramUrlArray[i].split("=");
                    if (temp[0].toLowerCase() == p.toLowerCase()) {
                        returnVal = decodeURI(temp[1]);
                        break;
                    }
                }
            }
        }
    }
    catch (e) { }
    return returnVal;
}
function cookie(name) {
    var cookieArray = document.cookie.split("; "); //得到分割的cookie名值对    
    var cookie = new Object();
    for (var i = 0; i < cookieArray.length; i++) {
        var arr = cookieArray[i].split("=");       //将名和值分开
        if (arr[0] == name) {
            return unescape(arr[1]); //如果是指定的cookie，则返回它的值   
        }
    }
    return "";
}
function delCookie(name)//删除cookie
{
    document.cookie = name + "=;expires=" + (new Date(0)).toGMTString();
}
function getCookie(objName) {
    //获取指定名称的cookie的值
    var arrStr = document.cookie.split("; ");
    for (var i = 0; i < arrStr.length; i++) {
        var temp = arrStr[i].split("=");
        if (temp[0] == objName) {
            return unescape(temp[1]);
        }
    }
}
//添加cookie
function addCookie(objName, objValue, objHours) {
    var str = objName + "=" + escape(objValue);
    if (objHours > 0) {
        //为时不设定过期时间，浏览器关闭时cookie自动消失
        var date = new Date();
        var ms = objHours * 3600 * 1000;
        date.setTime(date.getTime() + ms);
        str += "; expires=" + date.toGMTString();
    }
    document.cookie = str;
}
//两个参数，一个是cookie的名子，一个是值
function SetCookie(name, value) {
    var Days = 30; //此 cookie 将被保存 30 天
    var exp = new Date();    //new Date("December 31, 9998");
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}
function getCookie(name)//取cookies函数        
{
    var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
    if (arr != null) {
        return unescape(arr[2]);
    }
    return "";
}
//删除cookie
function delCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null) {
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
    }
}

/**
* EasyUI DataGrid根据字段动态合并单元格
* @param fldList 要合并table的id
* @param fldList 要合并的列,用逗号分隔(例如："name,department,office");
*/
function MergeCells(tableID, colList) {
    var ColArray = colList.split(",");
    var tTable = $('#' + tableID);
    var TableRowCnts = tTable.datagrid("getRows").length;
    var tmpA;
    var tmpB;
    var PerTxt = "";
    var CurTxt = "";
    var alertStr = "";
    for (j = ColArray.length - 1; j >= 0; j--) {
        PerTxt = "";
        tmpA = 1;
        tmpB = 0;
        for (i = 0; i <= TableRowCnts; i++) {
            if (i == TableRowCnts) {
                CurTxt = "";
            }
            else {
                CurTxt = tTable.datagrid("getRows")[i][ColArray[j]];
            }
            if (PerTxt == CurTxt) {
                tmpA += 1;
            }
            else {
                tmpB += tmpA;
                tTable.datagrid('mergeCells', {
                    index: i - tmpA,
                    field: ColArray[j],
                    rowspan: tmpA,
                    colspan: null
                });
                tmpA = 1;
            }
            PerTxt = CurTxt;
        }
    }
}
function MergeCells1(tableID, fldList) {
    var Arr = fldList.split(",");
    var dg = $('#' + tableID);
    var fldName;
    var RowCount = dg.datagrid("getRows").length;
    var span;
    var PerValue = "";
    var CurValue = "";
    var length = Arr.length - 1;
    for (i = length; i >= 0; i--) {
        fldName = Arr[i];
        PerValue = "";
        span = 1;
        for (row = 0; row <= RowCount; row++) {
            if (row == RowCount) {
                CurValue = "";
            }
            else {
                CurValue = dg.datagrid("getRows")[row][fldName];
            }
            if (PerValue == CurValue) {
                span += 1;
            }
            else {
                var index = row - span;
                if (span > 1) {
                    dg.datagrid('mergeCells', {
                        index: index,
                        field: fldName,
                        rowspan: span,
                        colspan: null
                    });
                }
                span = 1;
                PerValue = CurValue;
            }
        }
    }
}

function showUrl(url) {
    $(".dzxx").remove();
    var p = $("body");
    var div = $("<div class='dzxx' style='padding: 0px;margin: 0px;position: absolute;display: none;z-index:11000;'><div>").appendTo(p);
    var w = $(document.body).W($(document).width()).width();
    var h = $(document.body).H($(document).height()).height();
    div.css('height', h);
    div.css('width', w);
    div.css('left', 0);
    div.css('top', 0);
    var ifr = $('<iframe scrolling="auto" style="width: 100%; height:100%; padding: 0px;margin: 0px;visibility: inherit; margin: 0px; padding: 0px;" frameborder="0" src="' + encodeURI(url) + '"></iframe>' + top.ininMsg()).appendTo(div);
    if ($.browser.msie && ($.browser.version == "6.0") && !$.support.style) {
        ifr.attr("src", encodeURI(url));
    }
    div.show();
}
function hideUrl() {
    $(".dzxx").remove();
}
function removeRepater(array, rt) {
    array = array || [];
    var a = {};
    for (var i = 0; i < array.length; i++) {
        var v = array[i];
        if (a[v] == undefined) {
            a[v] = 1;
        }
    }
    array.length = 0;
    var str = '';
    for (var i in a) {
        array[array.length] = i;
        str += (str == '') ? i : ('' + i);
    }
    if (rt == '1') {
        return array;
    } else {
        return str;
    }
}
function checkT(startTime, endTime, inTime) {
    //alert(startTime + '|' + endTime + '|' + inTime);
    if (startTime.length > 0 && endTime.length > 0 && inTime.length > 0) {
        var startTmp = startTime.split("-");
        var endTmp = endTime.split("-");
        var inTmp = inTime.split("-");

        var sd = startTime.toTime(); //new Date(startTmp[0], startTmp[1], startTmp[2]);
        var ed = endTime.toTime(); // new Date(endTmp[0], endTmp[1], endTmp[2]);
        var id = inTime.toTime(inTime); // new Date(inTmp[0], inTmp[1], inTmp[2]);

        //alert(sd.getTime() + '|' + id.getTime() + '|' + ed.getTime());
        if (sd.getTime() <= id.getTime() && id.getTime() <= ed.getTime()) {
            return true;
        }
    }
    return false;
}

jQuery.fn.rowspan = function (colIdx) { //封装的一个JQuery小插件 
    return this.each(function () {
        var that;
        $('tr', this).each(function (row) {
            $('td:eq(' + colIdx + ')', this).filter(':visible').each(function (col) {
                if (that != null && $(this).html() == $(that).html() && $(this).html() != "&nbsp;") {
                    rowspan = $(that).attr("rowSpan") || 1;
                    if (rowspan == undefined) {
                        $(that).attr("rowSpan", 1);
                        rowspan = $(that).attr("rowSpan");
                    }
                    rowspan = Number(rowspan) + 1;
                    $(that).attr("rowSpan", rowspan);
                    $(this).hide();

                } else {
                    that = this;
                }
            });
        });
    });
}

function aic(ary, i, cName, v) {
    return ary[i][cName] || (v || "&nbsp;");
}

function correctPNG() {
    for (var i = 0; i < document.images.length; i++) {
        var img = document.images[i]
        var imgName = img.src.toUpperCase()
        if (imgName.substring(imgName.length - 3, imgName.length) == "PNG") {
            var imgID = (img.id) ? "id='" + img.id + "' " : ""
            var imgClass = (img.className) ? "class='" + img.className + "' " : ""
            var imgTitle = (img.title) ? "title='" + img.title + "' " : "title='" + img.alt + "' "
            var imgStyle = "display:inline-block;" + img.style.cssText
            if (img.align == "left") imgStyle = "float:left;" + imgStyle
            if (img.align == "right") imgStyle = "float:right;" + imgStyle
            if (img.parentElement.href) imgStyle = "cursor:hand;" + imgStyle
            var strNewHTML = "<span " + imgID + imgClass + imgTitle
                        + " style=\"" + "width:" + img.width + "px; height:" + img.height + "px;" + imgStyle + ";"
                        + "filter:progid:DXImageTransform.Microsoft.AlphaImageLoader"
                        + "(src=\'" + img.src + "\', sizingMethod='scale');\"></span>"
            img.outerHTML = strNewHTML;
            i = i - 1;
        }
    }
}
function abg() {
    var rslt = navigator.appVersion.match(/MSIE (d+.d+)/, '');
    var itsAllGood = (rslt != null && Number(rslt[1]) >= 5.5);
    for (i = 0; i < document.all.length; i++) {
        var bg = document.all[i].currentStyle.backgroundImage;
        if (bg) {
            if (bg.match(/.png/i) != null) {
                var mypng = bg.substring(5, bg.length - 2);
                document.all[i].style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + mypng + "', sizingMethod='crop')";
                document.all[i].style.backgroundImage = "url('')";
            }
        }
    }
}
if ($.browser.msie && ($.browser.version == "6.0") && !$.support.style) {
    if (navigator.platform == "Win32" && navigator.appName == "Microsoft Internet Explorer" && window.attachEvent) {
        window.attachEvent("onload", cPng);
        window.attachEvent("onload", abg);
    }
}
function getRootPath() {
    var pathName = window.location.pathname.substring(1);
    var webName = pathName == '' ? '' : pathName.substring(0, pathName.indexOf('/'));
    return window.location.protocol + '//' + window.location.host + '/' + webName + '';
}
function checkedAll(name, form, val) {
    var rr = $("input[name=\"" + name + "\"][type=radio], input[name=\""
					+ name + "\"][type=checkbox]", form);
    $.fn.prop ? rr.prop("checked", false) : rr.attr("checked", false);
    rr.each(function () {
        var f = $(this);
        var ary = val.split(",");
        for (var i = 0; i < ary.length; i++) {
            if (f.val() == ary[i]) {
                $.fn.prop ? f.prop("checked", true) : f.attr("checked", true);
            }
        }
    });
    return rr;
}
var IncomingCellPhone = '';
var mswindow;
function showCell(v) {
    IncomingCellPhone = getCookie("IncomingCellPhone");
    SetCookie("IncomingCellPhone", v);
    var mswin = $.messager.show({
        title: '来电提醒',
        msg: "正在叫入电话号码：" + v + "</br>" + '</br><div style=" text-align:center"><input type="button" value="接听" onclick="link_url();"/></div>',
        timeout: 0,
        showType: 'fade',
        closable: true
    });
    mswindow = mswin;
}

function showCell(v) {
    var areaCode = parent._AreaCode;
    var cityName = '';
    var cvr = v.substring(0, 2);
    var cvr1 = v.substring(0, 1);
    if (cvr == '01' || cvr == '02') {
        if (v.length == 11) {
            areaCode = v.substring(0, 3);
            cityName = parent.GetCityName(areaCode);
            SetCookie("PhoneCityName", cityName);
        }
        if (v.length == 12) {
            searchMobilePhoneGuiSuArea(v);
        }
    } else {
        if (cvr1 == '1' && v.length == 11) {
            searchMobilePhoneGuiSuArea(v);
        } else {
            if (v.length == 11) {
                areaCode = v.substring(0, 4);
            }
            cityName = parent.GetCityName(areaCode);
            SetCookie("PhoneCityName", cityName);
        }
    }
    cityName = getCookie("PhoneCityName");
    SetCookie("AccessTelNum", v);
    var AccessNum = getCookie("AccessNum");

    var RelayNum = '无'; //取到中继号码

    var row = parent.GetTel(RelayNum);

    var AccessUnitName = "";
    var AccessUnitNum = "";
    var AccessKeyNum = "";
    var AccessNum = "";

    if (row) {
        AccessNum = row["ServicePhone"] || '';
        AccessUnitName = row["UnitName"] || '';
        AccessUnitNum = row["UnitNum"] || '';
        AccessKeyNum = row["KeyNum"] || '';
    }

    if (parent.AccessUnitNum != '平台') {

        AccessNum = parent._ServicePhone;
        AccessUnitNum = parent.AccountUnitNum;
        AccessUnitName = parent.AccountUnitName;
        RelayNum = parent._RelayNum;
    }

    SetCookie("AccessNum", AccessNum);
    SetCookie("AccessUnitNum", AccessUnitNum);
    SetCookie("AccessUnitName", AccessUnitName);
    SetCookie("AccessRelayNum", RelayNum);
    SetCookie("AccessKeyNum", AccessKeyNum);

    var LastTelPhone = " 来电号码：【" + v
                        + "】 号码归属: 【" + cityName + "】 中继号码: 【" + RelayNum
                        + "】 400号码: 【" + AccessNum + "】 公司名称: 【" + AccessUnitName + "】";

    SetCookie("LastTelPhone", LastTelPhone);

    parent.$("#pTitle").html(LastTelPhone);
}


//查询手机归属地   
function searchMobilePhoneGuiSuArea(mobileNo) {
    $.ajax({
        url: "../Service/AjaxPhone.ashx?Mobile=" + mobileNo,
        dataType: "JSON",
        type: "POST",
        async: false,
        success: function (r) {
            if (r) {
                if (r.result["area"]) {
                    var cityName = parent.GetCityName(r.result["area"]);
                    
                    SetCookie("PhoneCityName", cityName);
                }
            }
        }
    });
}


function link_url() {
    var turl = '';
    var tt = '';
    //$('#mP').panel({ title: tt, content: CF(turl), closable: false });
    mswindow.window('close');
}

function Ge(columns, optbar, Alignment) {
    if (Alignment == 0) {
        //操作按扭在左边
        if (columns.length > 0) {
            if (columns[0].field == 'ck') {
                for (var i = 0; i < optbar.length; i++) {
                    columns.splice(i + 1, 0, optbar[i]);
                }
            } else {
                for (var i = optbar.length - 1; i >= 0; i--) {
                    columns.unshift(optbar[i]);
                }
            }
        }
    }
    if (Alignment == 1) {
        for (var i = 0; i < optbar.length; i++) {
            columns.push(optbar[i]);
        }
    }
    return columns;
}