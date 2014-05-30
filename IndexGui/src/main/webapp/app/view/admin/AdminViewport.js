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

Ext.define('Index.view.admin.AdminViewport', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.adminviewport',
	//requires : ['Index.view.admin.IndexItemsList'],
	initComponent : function() {
		this.items = [{
			xtype : 'panel',
			region: 'center',
			layout : 'border',
			items : [{
				// content panel
				xtype : 'contentPanel',
				title: i18n.t('admin.panel_title'),
				id : 'adminContentPanel',
				region : 'center',
				
				items : [{
					xtype : 'panel',
					id : 'button_panel',
					width:'100%',
					border : false
				},{
					xtype : 'panel',
					border : false,
					layout : {
						type:'hbox',
						align : 'stretch',
						pack : 'start'
					},
					items : [{
						xtype : 'panel',
						id : 'left_panel',
						flex : 1,
						width: '100%',
						autoHeight: true,
						maxHeight: 650,
						layout : 'fit',
						border : false
					},{
						xtype : 'panel',
						id : 'right_panel',
						flex : 1,
						border : false
					}]
				}]
			}]
		}],

		this.callParent();
	}
});
