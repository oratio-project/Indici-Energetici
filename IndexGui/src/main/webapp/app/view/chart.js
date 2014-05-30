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

Ext.define('Index.view.chart', {
	extend : 'Ext.chart.Chart',
	width : 100,
	height : 280,
	legend : {
		position : 'right'
	},
	setElementId : function(chartid) {
		this.id = chartid;
	},

	getElementId : function() {
		return this.id;
	},
	setMinimum : function(m){
		//loop items
		for(var i = 0 ; i < this.axes.items.length; i++){
			if(this.axes.items[i].title == 'Date'){
				this.axes.items[i].minimum = Ext.Date.parse(m,'Y-m-d\\TH:i:sO');
				console.log(this.axes.items[i].minimum);
			}

		}

	},
	setMaximum : function(m){
		//loop items
		for(var i = 0 ; i < this.axes.items.length; i++){
			if(this.axes.items[i].title == 'Date'){
				this.axes.items[i].maximum = Ext.Date.parse(m, 'Y-m-d\\TH:i:sO') ;
				console.log(this.axes.items[i].maximum);
			}

		}
	}
});
