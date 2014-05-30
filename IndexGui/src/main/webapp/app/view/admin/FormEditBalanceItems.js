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

Ext.define('Index.view.admin.FormEditBalanceItems',{
	extend : 'Ext.form.Panel',
	alias : 'widget.formeditbalanceitems',
	id : 'formeditbalanceitems',
	bodyPadding : 5,
	width : 600,
//	hidden : true,
	padding : 5,
	title : 'Edit balance item',
	layout : {
		type : 'table',
		columns: 2
	},
	items : [{
		fieldLabel : i18n.t('common.code'),
		colspan: 2,
		bodyPadding : 5,
		id: 'balance-code',
		xtype:'displayfield'
	},
	{
		fieldLabel : i18n.t('common.var_name'),
		colspan: 2,
		bodyPadding : 5,
		id: 'balance-name',
		xtype:'textfield'
	},
	{
		fieldLabel : i18n.t('common.description'),
		colspan: 2,
		bodyPadding : 5,
		id: 'balance-description',
		xtype:'textareafield',
		grow: true
	},{
		fieldLabel : i18n.t('common.unit'),
		colspan: 2,
		bodyPadding : 5,
		id: 'balance-unit',
		xtype:'textfield'
	}],
	buttons : [{
		text : i18n.t('form.close'),
		id: 'edit-balance-close',
		handler : function(button){
			button.up().up().setVisible(false);
		}
	},{
		text : i18n.t('form.update'),
		id: 'balance-update'
	}]
});
