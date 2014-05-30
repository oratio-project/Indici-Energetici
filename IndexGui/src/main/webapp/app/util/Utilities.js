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

Ext.define('Index.util.Utilities', {
     singleton: true,
     serverAddress: configServerAddress,
   //functions to handle save on remote server
 	onSaveSuccess : function (record, operation, storename, viewid, button){
 		Ext.Msg.alert(i18n.t('success.title'), i18n.t('success.successfully_operation'));
		//refresh grid
		var view = Ext.getCmp(viewid);
		console.log(view);
		var store = Ext.create(storename);
		view.bindStore(store);
		//close form
		button.up().up().setVisible(false);
 	},
 	onSaveError : function(){
 		Ext.Msg.alert(i18n.t('error.title'), i18n.t('error.unsuccessfully_operation'));
 	},
 	deleteRecord : function(grid,rec){
 		var store = grid.getStore();
 		store.remove(rec);
 		store.sync();
 	}
});
