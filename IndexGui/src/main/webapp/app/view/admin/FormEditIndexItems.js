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

Ext.define('Index.view.admin.FormEditIndexItems',{
	extend : 'Ext.form.Panel',
	alias : 'widget.formeditindex',
	id : 'formeditindexitems',
	autoScroll : true,
	bodyPadding : 5,
	width : 600,
	padding : 5,
	title : 'Edit index item',
	layout : {
		type : 'table',
		columns: 2
	},
	items : [  {
		xtype:'form',
		id:'edit-index_form',
		width: '100%',
		border : false,
		layout : {
			type : 'table',
			columns:2
		},
		items: [{
			name: 'id',
			fieldLabel : i18n.t('common.id'),
			colspan: 2,
			bodyPadding : 5,
			id: 'edit-index-id',
			xtype:'displayfield'
		},{
			name: 'name',
			fieldLabel : i18n.t('common.name'),
			colspan: 2,
			bodyPadding : 5,
			id: 'edit-index-name',
			xtype:'textfield'
		},
		{
			name: 'description',
			fieldLabel : i18n.t('common.description'),
			colspan: 2,
			bodyPadding : 5,
			id: 'edit-index-description',
			xtype:'textareafield',
			grow : true
		},
		{
			name: 'formula',
			fieldLabel : i18n.t('common.formula'),
			colspan:1,
			bodyPadding : 5,
			id: 'edit-index-formula',
			xtype:'textareafield'
		},{
			xtype : 'button',
			id : 'edit-add-balance-code',
			margin: '0 0 0 5',
			text : i18n.t('form.add_balance_code'),
			colspan : 1
		},{
			name: 'unit',
			fieldLabel : i18n.t('common.unit'),
			colspan: 2,
			bodyPadding : 5,
			id: 'edit-index-unit',
			xtype:'textfield'
		},{
			xtype : 'combobox',
			name : 'category',
			id : 'edit-index-category',
			fieldLabel : i18n.t('common.category'),
			colspan: 2,
			displayField: 'name',
		    valueField: 'id',
		}]
	},{
		xtype:'menuseparator',
		colspan : 2,
		width:'100%'
	},{
		xtype : 'form',
		id: 'edit-range_form',
		border : false,
		width:'100%',
		layout : {
			type : 'table',
			columns:2
		},
		items : [{
			xtype:'text',
			text: 'Range 1',
			colspan : 2
		},{
			fieldLabel : i18n.t('common.color'),
			name : 'range1-color',
			id : 'edit-range1-color',
			colspan: 2,
			bodyPadding : 5,
			store : [i18n.t('color.red'),i18n.t('color.yellow'),i18n.t('color.green')],
			xtype:'combobox'
		},{
			fieldLabel : i18n.t('common.superior'),
			name : 'range1-superior',
			id : 'edit-range1-superior',
			colspan: 2,
			bodyPadding : 5,
			xtype:'numberfield'
		},{
			fieldLabel : i18n.t('common.inferior'),
			name : 'range1-inferior',
			colspan: 2,
			bodyPadding : 5,
			id: 'edit-range1-inferior',
			xtype:'numberfield'
		},{
			xtype:'menuseparator',
			colspan : 2
		},{
			xtype:'text',
			text: 'Range 2',
			colspan : 2
		},{
			fieldLabel : i18n.t('common.color'),
			name : 'range2-color',
			colspan: 2,
			bodyPadding : 5,
			id: 'edit-range2-color',
			store : [i18n.t('color.red'),i18n.t('color.yellow'),i18n.t('color.green')],
			xtype:'combobox'
		},{
			fieldLabel : i18n.t('common.superior'),
			name : 'range2-superior',
			colspan: 2,
			bodyPadding : 5,
			id: 'edit-range2-superior',
			xtype:'numberfield'
		},{
			fieldLabel : i18n.t('common.inferior'),
			name : 'range2-inferior',
			colspan: 2,
			bodyPadding : 5,
			id: 'edit-range2-inferior',
			xtype:'numberfield'
		},{
			xtype:'menuseparator',
			colspan : 2
		},{
			xtype:'text',
			text: 'Range 3',
			colspan : 2
		},{
			fieldLabel : i18n.t('common.color'),
			name : 'range3-color',
			colspan: 2,
			bodyPadding : 5,
			id: 'edit-range3-color',
			store : [i18n.t('color.red'),i18n.t('color.yellow'),i18n.t('color.green')],
			xtype:'combobox'
		},{
			fieldLabel : i18n.t('common.superior'),
			name : 'range3-superior',
			colspan: 2,
			bodyPadding : 5,
			id: 'edit-range3-superior',
			xtype:'numberfield'
		},{
			fieldLabel : i18n.t('common.inferior'),
			name : 'range3-inferior',
			colspan: 2,
			bodyPadding : 5,
			id: 'edit-range3-inferior',
			xtype:'numberfield'
		}]
	} 
	               
	],
	buttons : [{
		text : i18n.t('form.close'),
		id: 'edit-close',
		handler : function(button){
			button.up().up().setVisible(false);
		}
	},{
		text : i18n.t('form.update'),
		id: 'index-update'
	}]
});
