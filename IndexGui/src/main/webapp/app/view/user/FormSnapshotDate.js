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

Ext.define('Index.view.user.FormSnapshotDate', {
	extend : 'Ext.form.Panel',
	alias : 'widget.formsnapshotdate',
	width: '100%',
	layout : 'column',
	items : [ {
		xtype : 'datepicker',
		id : 'snapshots-datepicker',
		columnWidth: 0.70,
		height : 200,
		autoScroll : true,
		border : false,
	}, {
		xtype : 'timepicker',
		id : 'snapshots-timepicker',
		minValue : Ext.Date.parse('00:00:00', 'G:i'),
		maxValue : Ext.Date.parse('23:00:00', 'G:i'),
		format : 'G:i',
		height : 200,
		autoScroll : true,
		border : false,
		columnWidth: 0.30,
		increment : 60 //time step - number of minutes
	}],
	buttons : [{
		text: i18n.t('form.show'),
		id: 'snapshots-button'
	}]
});
