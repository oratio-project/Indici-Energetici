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

Ext.define('Index.view.admin.IndexItemsList', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.indexitemslist',
	id:'indextablelist',
	sortableColumns : true,
	enableColumnHide : false,
	padding : 5,
	columns : [ {
		text : i18n.t('common.name'),
		dataIndex : 'name',
		flex : 2
	},{
		text : i18n.t('common.description'),
		dataIndex : 'description',
		flex : 3
	},{
		xtype:'actioncolumn',
		text: i18n.t('form.edit'),
		flex : 1,
		items : [{
			icon : 'img/edit.png',
			tooltip: i18n.t('form.edit'),
			margin : 5,
			handler : function (grid, rowIndex, colIndex){
				this.fireEvent('index_edit', grid, rowIndex,colIndex);
			}
		}]
	},{
		xtype:'actioncolumn',
		text: i18n.t('form.delete'),
		flex : 1,
		items : [{
			icon : 'img/delete.png',
			tooltip: i18n.t('form.delete'),
			margin : 5,
			handler : function (grid, rowIndex, colIndex){
				this.fireEvent('index_delete', grid, rowIndex, colIndex);
			}
		}]
	}]

});
