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

Ext.require('Ext.container.Viewport');

//global variable for start and end datetime interval for user graphs
var startInterval = default_start;
var endInterval = default_end;

// init i18next for language support
i18n.init();

Ext.application({
	name : 'Index',
	appFolder : 'app',
	autoCreateViewport : true,
	controllers : [ 'MainController', 'AdminController', 'UserController', 'FormDateController', 'FormCreateSnapshotController', 'IndexTableController', 'IndexNavigationTreeController' ]
});
