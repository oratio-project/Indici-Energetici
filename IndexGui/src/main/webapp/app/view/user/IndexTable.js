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

Ext.define('Index.view.user.IndexTable', {
	extend : 'Ext.grid.Panel',
	alias : 'widget.indextable',
	// id:'indextable',
	bodyPadding : 5,
	scroll: false,
	sortableColumns : false,
	enableColumnHide : false,
	columns : [ {
		text : i18n.t('common.value'),
		dataIndex : 'value',
		flex : 1
	}, {
		text : i18n.t('common.unit'),
		dataIndex : 'unit',
		flex : 1
	}, {
		text : i18n.t('common.formula'),
		dataIndex : 'formula',
		flex : 1
	},{
		text : i18n.t('common.arguments'),
		dataIndex : 'arguments',
		flex : 1
	}],

	setIndexSnapshotDatetime : function(datetime) {
		this.setTitle(Ext.String.format(i18n.t('common.snapshotindextitle'),
				datetime));
	},
	setId : function(id){
		this.id = 'indextable_'+id;
	}

});
