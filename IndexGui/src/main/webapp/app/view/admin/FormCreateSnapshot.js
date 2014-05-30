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

Ext.define('Index.view.admin.FormCreateSnapshot', {
	extend : 'Ext.form.Panel',
	alias : 'widget.formcreatesnapshot',
	id : 'formcreatesnapshot',
	bodyPadding : 5,
	width : 500,
	layout : {
		type : 'table',
		columns: 2
	},
	items : [{
		title: i18n.t('form.fromdate'),
		colspan: 1,
		bodyPadding : 5,
		id: 'from_date',
		xtype:'datepicker'
		
	},{
		title: i18n.t('form.todate'),
		colspan : 1,
		bodyPadding : 5,
		id : 'to_date',
		xtype:'datepicker'
		
	},{
		xtype : 'combobox',
		id: 'cadency',
		store : Ext.create('Index.store.Cadencies'),
		fieldLabel : i18n.t('form.cadency'),
		displayField: 'cadency',
	    valueField: 'cadency',
	    allowBlank: false,
		editable : false,
		colspan : 2,
		padding : '10 0 5 0'
	}],

	buttons : [{
		text : i18n.t('form.create'),
		id: 'create'
	}]
});
