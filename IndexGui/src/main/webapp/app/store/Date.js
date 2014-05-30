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

//store per le date
//è uguale in tutti i casi, cambiano solo i parametri dell'url
Ext.define('Index.store.Date', {
	extend : 'Ext.data.Store',

	model : 'Index.model.Date',
	autoLoad : true,
	field : [{
		name : 'datepart',
		type : 'int'
	}],
	proxy : {
		type : 'rest',
		url : Index.util.Utilities.serverAddress + 'indexsnapdates/year',
		reader : {
			type : 'json',
		}
	},
	sorters : [{
		property : 'datepart',
		direction : 'ASC'
	}],
	//metodo per aggiungere i parametri all'url
	setDateParameter : function(base, year, month, day, hour){
		if(base=='year'){
			this.getProxy().url = Index.util.Utilities.serverAddress + "indexsnapdates/year";
		}
		else if(base=='month'){
			this.getProxy().url = Index.util.Utilities.serverAddress + "indexsnapdates/"+base+"?year="+year;
		}
		else if(base=='day'){
			this.getProxy().url = Index.util.Utilities.serverAddress + "indexsnapdates/"+base+"?year="+year+"&month="+month;
		}
		else if(base=='hour'){
			this.getProxy().url = Index.util.Utilities.serverAddress + "indexsnapdates/"+base+"?year="+year+"&month="+month+"&day="+day;
		}
	},
});

