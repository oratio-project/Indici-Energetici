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

Ext.define('Index.store.BalanceSnapshots', {
	extend : 'Ext.data.Store',

	model : 'Index.model.Snapshot',
	proxy : {
		type : 'rest',
		idParam : 'datetime',
		url : Index.util.Utilities.serverAddress + 'balancesnapdates',
		reader : {
			type : 'json'
		},
		listeners : {
			// error handling
			exception : function(proxy, response, operation) {
				Ext.create('Index.lib.ErrorUtil').handle(proxy, response, operation);
			}
		}
	},
	sorters : [ {
		property : 'datetime',
		direction : 'DESC'
	} ],
});
