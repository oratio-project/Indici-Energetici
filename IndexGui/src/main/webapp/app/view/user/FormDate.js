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

Ext.define('Index.view.user.FormDate', {
	extend : 'Ext.form.Panel',
	alias : 'widget.formdate',
	width : '100%',
	layout : 'column',
	height : 250,
	margin : 10,
	padding : '0 0 0 5',
	items : [{
		title: i18n.t('form.fromdate'),
		columnWidth: 0.50,
		defaultType : 'combobox',
		height: 200,
		border:false,
		defaults : {
			padding : 5
		},
		items : [{
			fieldLabel : i18n.t('form.year'),
			id: 'from_anno',
			displayField: 'datepart',
		    valueField: 'datepart',
		    allowBlank: false,
			editable: false
		}, {
			fieldLabel : i18n.t('form.month'),
			id: 'from_mese',
			displayField: 'datepart',
		    valueField: 'datepart',
		    allowBlank: false,
			editable : false
		}, {
			fieldLabel : i18n.t('form.day'),
			id : 'from_giorno',
			displayField: 'datepart',
		    valueField: 'datepart',
		    allowBlank: false,
			editable : false
		}, {
			fieldLabel : i18n.t('form.hour'),
			id : 'from_ora',
			displayField: 'datepart',
		    valueField: 'datepart',
		    allowBlank: false,
			editable : false
		}]
	},{
		title: i18n.t('form.todate'),
		columnWidth: 0.50,
		defaultType : 'combobox',
		height: 200,
		border : false,
		defaults : {
			padding : 5
		},
		items : [{
			fieldLabel : i18n.t('form.year'),
			id : 'to_anno',
			displayField: 'datepart',
		    valueField: 'datepart',
		    allowBlank: false,
			editable: false
		}, {
			fieldLabel : i18n.t('form.month'),
			id : 'to_mese',
			displayField: 'datepart',
		    valueField: 'datepart',
		    allowBlank: false,
			editable : false
		}, {
			fieldLabel : i18n.t('form.day'),
			id : 'to_giorno',
			displayField: 'datepart',
		    valueField: 'datepart',
		    allowBlank: false,
			editable : false
		},{
			fieldLabel : i18n.t('form.hour'),
			id : 'to_ora',
			displayField: 'datepart',
		    valueField: 'datepart',
		    allowBlank: false,
			editable : false
		}]
	}],

	buttons : [{
		text: i18n.t('form.reset'),
		id: 'reset'
	},{
		text : i18n.t('form.setdate'),
		id: 'setdateinterval',
		formBind: true, //only enabled once the form is valid
	    disabled: true
	}]
});
