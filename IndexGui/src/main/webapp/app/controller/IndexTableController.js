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

Ext.define('Index.controller.IndexTableController',{
	extend : 'Ext.app.Controller',
	views : ['user.IndexTable'],
	init : function(){
	},
	onIndexTableRendered : function(){
	},
	onItemClick : function(dv, record, item, index, e){
		var name =record.get('name');
		var desc = record.get('description');
		console.log(desc);
		var up_container_right = Ext.getCmp("up_container_right");
		Ext.create('Ext.form.field.Display',{
			id: 'index_name',
			fieldLabel: i18n.t('common.name'),
		});
		
		Ext.create('Ext.form.field.Display',{
			id: 'index_description',
			fieldLabel: i18n.t('common.description'),
			width: 400
		});
		
		up_container_right.removeAll();
		var textfield = Ext.getCmp('index_name').setValue(name);
		var description = Ext.getCmp('index_description').setValue(desc);
	}
});
