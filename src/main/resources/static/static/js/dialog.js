var dialogText = {
	title : '温馨提示',
	content : '',
	confirmeBtnText : '确定',
	confirmeBtnClass : 'pop-tipcomfirme',
	width : 500
};

window.pop = window.pop || {
	version : "beta 0.1"
};
pop.ui = pop.ui || {};

(function() {
	function popalert(option) {
		var m = this;
		m.option = {
			title : dialogText.title,
			content : dialogText.content,
			cancelShow : false,
			hideCloseBtn : false,
			waiting : false,
			confirmeBtnText : dialogText.confirmeBtnText,
			confirmeBtnClass : dialogText.confirmeBtnClass,
			cancelBtnText : dialogText.cancelBtnText,
			width : dialogText.width,
			link : 'javascript:void(0);',
			css : '',
			
			beforeCallback : function() {
			},
			callback : function() {
			},
			closeCallBack : function() {
			}
		};
		$.extend(m.option, option);
	}
	popalert.prototype.init = function() {
		var m = this;
		m.popup();
	}

	popalert.prototype.popup = function(opt) {
		var m = this, html;
		opt && typeof opt === 'object' && $.extend(m.option, opt);
		typeof opt === 'string' && (m.option.content = opt);
		html = m.option.waiting ? '<div class="pop-alert"><div class="pop-wrap" style="width:'
				+ m.option.width
				+ 'px;margin-left:'
				+ (-m.option.width / 2)
				+ 'px"><div class="pop-title"><h2><i class="basic-ico revise-icon"></i>'
				+ m.option.title
				+ '</h2></div><div class="pop-content"><p class="pop-tips">'
				+ m.option.content
				+ '</p></div></div><div class="pop-layer"></div></div>'
				: '<div class="pop-alert"><div class="pop-wrap" style="width:'
						+ m.option.width
						+ 'px;margin-left:'
						+ (-m.option.width / 2)
						+ 'px"><div class="pop-title"><h2><i class="basic-ico revise-icon"></i>'
						+ m.option.title
						+ '</h2><a class="pop-close" href="#" title="关闭"></a></div><div class="pop-content">'
						+ m.option.content
						+ '<p class="pop-comfirme"><a href="'
						+ m.option.link
						+ '" class="'
						+ m.option.confirmeBtnClass
						+ '">'
						+ m.option.confirmeBtnText
						+ '</a><a href="#" class="pop-tipcancel">'
						+ m.option.cancelBtnText
						+ '</a></p></div></div><div class="pop-layer"></div></div>';
		$('body').append(html).end().find('.pop-alert .pop-layer').height(
				$(document).height());
		m.popcss();
		m.option.cancelShow
				&& $('.pop-alert .pop-tipcancel')
						.css('display', 'inline-block');
		m.option.hideCloseBtn && $('.pop-alert .pop-close').hide();
		m.regEvent();
		m.option.beforeCallback();
	};
	popalert.prototype.popcss = function() {
		var m = this, css = '<style type="text/css">' + m.option.css
				+ '</style>';
		$('.pop-alert').append(css);
		m.position();
	}
	popalert.prototype.position = function() {
		var w = $(window), m = $('.pop-alert .pop-wrap');
		m.css({
			top : w.scrollTop() + w.height() / 2 - m.height() / 2
		});
	}
	popalert.prototype.close = function() {
		$('.pop-alert').remove();
	}
	popalert.prototype.regEvent = function() {
		var m = this;
		$('.pop-alert .pop-wrap .pop-close').unbind().click(function(event) {
			event.preventDefault();
			m.close();
			m.option.closeCallBack();
		});
		$('.pop-alert .pop-wrap .pop-tipcomfirme').unbind().click(
				function(event) {
					$(this).attr('href') == 'javascript:void(0);'
							&& event.preventDefault();
					
					m.option.callback();
					m.close();
				});
		if (m.option.cancelShow) {
			$('.pop-alert .pop-wrap .pop-tipcancel').unbind().click(
					function(event) {
						event.preventDefault();
						m.close();
						m.option.closeCallBack();
					});
		}
		;
	}
	pop.ui.alert = popalert;
	pop.ui.dialogTip = new popalert({});
	pop.ui.dialogConfirme = new popalert({
		cancelShow : true
	});
	pop.ui.dialogWaiting = new popalert({
		waiting : true
	});
	pop.ui.dialogClose = new popalert({
		close : true
	});
})();
