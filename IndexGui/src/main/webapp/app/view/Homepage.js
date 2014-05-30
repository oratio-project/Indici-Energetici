/*******************************************************************************
* Oratio4Energy(R) Copyright
* Copyright (c) 2014 by Proxima Centauri srl (Italy)
* 
* Proxima Centauri s.r.l.
* based in: Via Michelangelo 10 
* I - 10126 Torino - Italy
* Email: proxima@proxima-centauri.it
* Web: www.proxima-centauri.it  
* 
* This file is part of Oratio4Energy.
* 
* Oratio4Energy is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* Oratio4Energy is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with Oratio4Energy.  If not, see http://www.gnu.org/licenses.
*******************************************************************************/

Ext.define('Index.view.Homepage', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.homepage',
	margins : '5 0 0 5',
	autoScroll : true,
	layout: {
	    type: 'vbox',
	    align : 'stretch',
	    pack  : 'start',
	},
	defaults : {
		bodyPadding: 10
	},
	items: [{
		title : i18n.t('homepage.data_analysis'),
		html : '<p>'+i18n.t('homepage.data_analysis_content')+'</p>',
		padding : 5,
		margins : 15,
		height:100
	},{
		title : i18n.t('homepage.credits'),
		html : '<p>'+i18n.t('homepage.credits_content')+'</p>'+
				'<ul>' +
					'<li> <a href="http://areeweb.polito.it/ricerca/tebe/" target="_blank"> http://areeweb.polito.it/ricerca/tebe/</a> ' +
						'<br><img src="img/logo_tebe.png" alt="tebe"/>'+
					'</li>' +
					'<li> <a href="http://elite.polito.it/ " target="_blank"> http://elite.polito.it/</a>'+
						'<br><img src="img/logo_elite.png" alt="elite"/>'+
					'</li>' +
				'</ul>',
		padding : 5,
		margin : 15,
		height: 350
	},{
		title : i18n.t('homepage.calculation'),
		html : '<p>'+i18n.t('homepage.calculation_content')+'</p>',
		padding : 5,
		margins : 15,
		height:100
	}]
});
