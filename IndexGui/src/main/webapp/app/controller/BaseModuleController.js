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

Ext.define('Index.controller.BaseModuleController', {
	extend : 'Ext.app.Controller',
	categoryId : -1,
	categoryName : "",
	mainView : "",

	// Menu navigation store
	stores : [ 'Categories', 'Date'],

	refs : [ {
		ref : 'contentPanel',
		selector : 'contentPanel'
	} ],

	start : function() {
		console.log(this.id + " controller started !! (category " + this.categoryId + ")");

		// get category name
		var index = this.getCategoriesStore().find('id', this.categoryId);

		if (index != -1) {
			this.categoryName = this.getCategoriesStore().getAt(index).data.name;
		}

		contentPanel = this.getContentPanel();

		contentPanel.removeAll(true);
		
	},

	onBalanceSnapshotsLoad : function() {

	},

	onIndexSnapshotsLoad : function() {

	},
});
