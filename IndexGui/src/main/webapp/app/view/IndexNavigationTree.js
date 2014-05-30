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

Ext.define('Index.view.IndexNavigationTree',{
	extend : 'Ext.grid.Panel',
	alias : 'widget.treenavigation',
	title : '',
	margins : '5 0 0 5',
	width : 250,
	collapsible : true, // make collapsible
	resizable : true,
	hideHeaders : true,
	layout : 'fit',
	columns: [{ 
		text:i18n.t('common.name') , 
		dataIndex: 'name', 
		width: 200 
		},{
			xtype:'actioncolumn',
			width: 100,
			items : [{
				icon : 'img/plus.gif',
				tooltip: i18n.t('form.details'),
				handler : function (grid, rowIndex, colIndex){
					//fire event
					this.fireEvent('details',grid, rowIndex, colIndex );
				}
			}]
		}],
	setCategoryName: function(categoryname){
		if(categoryname==""){
			this.setTitle(i18n.t('user.title'));
		}
		else{
			this.setTitle(i18n.t('user.title') + ' - ' + categoryname);
		}
		
	}
});
