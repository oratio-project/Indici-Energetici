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

Ext.define('Index.controller.MainController', {
	extend : 'Ext.app.Controller',
	requires : 'Index.util.Utilities',
	// Menu navigation store
	stores : [ 'Menu', 'Categories', 'BalanceItems', 'BalanceSnapshots', 'IndexSnapshots'],

	refs : [ {
		ref : 'contentPanel',
		selector : 'contentPanel'
	} ],

	init : function() {
		this.control({
			'menu' : {
				selectionchange : 'onNavSelectionChange'
			},
		});

		// connect to load event of categories
		this.getCategoriesStore().on("load", this.categorieLoad, this);
	},

	onNavSelectionChange : function(selModel, records) {

		// get the menu item class
		var record = records[0], functionController = record.get('id');
		
		console.log(functionController);
		if(functionController != 'AdminController'){
			// split the app request
			functionSplit = functionController.split("/");

			var controller = this.getController(functionSplit[0]);

			// set category to undefined -1
			controller.categoryId = -1;

			// load the category if present
			if (functionSplit.length > 1) {
				controller.categoryId = functionSplit[1];
			}
			
			//get card panel
			var cardPanel = Ext.getCmp('card_panel');
			
			//solo se è diverso da quello attuale
			if(cardPanel.getLayout().getActiveItem().getItemId() != functionSplit[0] ){
				cardPanel.getLayout().setActiveItem(functionSplit[0]);
			}

			// Remember to call the init method manually
			controller.start();
		}
	},

	categorieLoad : function(records, successful, eOpts) {
		console.log('Load categories completed - update menu');
		console.log(records);
		// update the list of menu item based on category id
		var menuStore = this.getMenuStore();

		
		//TO DO : load possible action from a store
		//find child
		var adminNode = menuStore.getRootNode().findChild('text', i18n.t('menu.admin'));
		
		var userNode = menuStore.getRootNode().findChild('text', i18n.t('menu.user'));
		
		records.each(function(record) {
			userNode.appendChild({
				id : userNode.data.id + "/" + record.data.id,
				text : record.data.name,
				leaf : true
			});
		});
		
	},
});
