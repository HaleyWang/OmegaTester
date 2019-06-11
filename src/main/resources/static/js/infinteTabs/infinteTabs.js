function tabs_unlimited() {
	$('.tabs-unlimited').append(function() {
		let html = ''
		html += '<li class="__left-tabunlimited">&laquo;</li>'
		html += '<li class="__right-tabunlimited">&raquo;</li>'
		return html
	})

	$('.tabs-unlimited').each(function() {
		let r = $(this),
			o = r.width(),
			f = 0,
			l = 0

		r
			.find('> li')
			.not('.__left-tabunlimited, .__right-tabunlimited')
			.each(function() {
				f += $(this).width()
			})

		r
			.find('.__right-tabunlimited')
			.click(function() {
			        f = 0;
					r
            			.find('> li')
            			.not('.__left-tabunlimited, .__right-tabunlimited')
            			.each(function() {
            				f += $(this).width()
            			});

            						o = r.width();



				if(l < f-o+60) {
					l+=60
					r
						.find('li')
						.eq(0)
						.animate({
							'margin-left' : -l
						})
				}
			})

		r
			.find('.__left-tabunlimited')
			.click(function() {

			    f = 0;
                r
                    .find('> li')
                    .not('.__left-tabunlimited, .__right-tabunlimited')
                    .each(function() {
                        f += $(this).width()
                    });
                                						o = r.width();



				if(l > 0) {
					l-=80
					r
						.find('li')
						.eq(0)
						.animate({
							'margin-left' : -l
						})
				}
			})

	})



}