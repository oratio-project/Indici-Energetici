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

Ext.define('Index.controller.UserController', {
	extend : 'Index.controller.BaseModuleController',
	requires : [ 'Index.view.user.FormSnapshotDate','Index.view.user.app.PortalPanel' ],
	stores : [ 'BalanceSnapshots', 'IndexSnapshots', 'IndexSnapshotsItem', 'IndexItems'],
	refs : [ {
		ref : 'contentPanel',
		selector : 'contentPanel'
	} ],

	init : function() {
		this.listen({
			// We are using Controller event domain here
			controller : {
				// This selector matches any originating Controller
				'*' : {
					set_date : 'loadStores'
				}
			}
		});
		this.control({
			'actioncolumn' : {
				details : this.onViewDetails
			},
			
		});
	this.global_datetime = null;
	
	
	
	},

	loadStores : function(encoded_dates) {
		console.log('Set date event ' + encoded_dates);
		
		this.indexstore = Ext.create('Index.store.IndexSnapshotsItem');
		this.balancestore = Ext.create('Index.store.BalanceSnapshotsItem');
		
		
		this.indexstore.setCategoryAndDateInterval(this.categoryId, encoded_dates);
		this.balancestore.setDateInterval(encoded_dates);

		this.indexstore.load();
		this.balancestore.load();

	},

	start : function() {
		//reset
		startInterval = 'default_start';
		endInterval = 'default_end';
		
		// call superclass method
		this.superclass.start.call(this);

		// load balance snapshot and index snapshot
		// load BalanceSnapshot
		this.getBalanceSnapshotsStore().on("load", this.onBalanceSnapshotsLoad, this);
		this.getBalanceSnapshotsStore().load();

		// load IndexSnapshots
		this.getIndexSnapshotsStore().on("load", this.onIndexSnapshotsLoad, this);
		this.getIndexSnapshotsStore().load();

		var treepanel = Ext.getCmp('tree_panel');
		treepanel.setCategoryName(this.categoryName);

		var up_container = Ext.create('Ext.panel.Panel', {
			xtype : 'panel',
			id : 'up_container',
			height : 300,
			layout : {
				type : 'hbox',
				pack : 'start',
				align : 'stretch'
			},
			items : [ {
				xtype : 'panel',
				border : false ,
				id : 'up_container_left',
				padding : '0 2 0 0',
				flex : 1,
				layout : {
					type : 'hbox',
					pack : 'center',
					align : 'stretch'
				},
				autoScroll : true,
				defaults : {
					margin : 10
				},
				items : [ {
					xtype : 'formsnapshotdate'
				} ]
			}, {
				xtype : 'panel',
				border : false ,
				id : 'up_container_right',
				padding : '0 0 0 2',
				autoScroll : true,
				title : Ext.String.format(i18n.t('common.dateinterval'), 'none'),
				flex : 1,
				layout : {
					type : 'hbox',
					align : 'stretch',
					pack : 'center',
				}
			} ]

		});
		

		var bottom_container = Ext.create('Index.view.user.app.PortalPanel', {
			id : 'bottom_container',
			flex : 3,
			autoScroll : true,
			items : [{
				id:'col-1'
			}]
		});
		
		contentPanel.add(up_container);
		contentPanel.add(bottom_container);
		
		// create form for dates
		var form = Ext.create('Index.view.user.FormDate');
		var up_container_right = Ext.getCmp("up_container_right");

		up_container_right.add(form);

		// get time picker and set min and max hour

		var selected_category = this.categoryId;
		var indices = Ext.create('Index.store.IndexItems');
		// just set the category to the store
		indices.setCategory(selected_category);
		indices.load();

		// create tree navigation index
		var tree_navigation_index = Ext.getCmp('tree_panel');
		tree_navigation_index.reconfigure(indices);

		// handle click on show value
		var button = Ext.getCmp('snapshots-button');
		// button.center();
		button.on('click', this.onClickShow, this);
	},

	onIndexSnapshotsLoad : function() {
		
		// get date picker and set min and max date
		var datepicker = Ext.getCmp('snapshots-datepicker');

		console.log('setted max date');
		console.log(this.getIndexSnapshotsStore().getAt(0));
		datepicker.setValue(this.getIndexSnapshotsStore().getAt(0).data.datetime);
		datepicker.setMaxDate(this.getIndexSnapshotsStore().getAt(0).data.datetime);
		datepicker.setMinDate(this.getIndexSnapshotsStore().getAt(this.getIndexSnapshotsStore().getCount() - 1).data.datetime);
		//set disabled date
		var enabledDates = new Array();
		for(var i = 0 ; i < this.getIndexSnapshotsStore().getCount() ; i++){
			var date_tmp = this.getIndexSnapshotsStore().getAt(i).data.datetime;
			
			if(enabledDates.indexOf(Ext.Date.format(date_tmp, 'm/d/Y')) == -1){
				enabledDates.push(Ext.Date.format(date_tmp, 'm/d/Y'));
			}
		} 

		//regular expression to invert vector
		datepicker.setDisabledDates(["^(?!"+enabledDates.join("|")+").*$"]);

		// get last index snapdate datetime
		var datetime = this.getIndexSnapshotsStore().getAt(0);
		this.global_datetime = datetime.data.datetime;

		var panel = Ext.getCmp('up_container_left');
		panel.setTitle(Ext.String.format(i18n.t('common.snapshotindextitle'), this.global_datetime));
		
	},
	onClickShow : function() {
		// get datepicker value
		var date = Ext.getCmp('snapshots-datepicker').getValue();

		var time = Ext.getCmp('snapshots-timepicker').getSelectedNodes();

		// check time selection
		if (time.length == 0) {
			Ext.Msg.alert(i18n.t('error.title'), i18n.t('error.time_selection'));

		} else {
			time = time[0].textContent;
			console.log(time);
			// split hour and minute
			var split = time.split(':');

			var split2 = split[1].split(' ');

			// construct date time
			var hour = split[0];
			var minutes = split2[0];

			// get day, month, year, timezone
			var day = date.getDate();
			var month = date.getMonth();
			var year = date.getFullYear();

			var datetime = new Date(year, month, day, hour, minutes);
			console.log(datetime);
			
			this.updateCurrentTables(datetime);
			
		}

	},
	onViewDetails : function (grid, rowIndex, colIndex){
		var rec = grid.getStore().getAt(rowIndex);
		var bottom_container = Ext.getCmp('col-1');
		//var col1 = Ext.getCmp('col-1');
		
		//check if index details exists
		var view = bottom_container.getComponent('details_'+rec.get('id'));
		if(view == null){
			//create index details
			var index_details = Ext.create('Index.view.user.IndexDetails');
			index_details.setIndexName(rec.get('name'));
			index_details.setDetailsId(rec.get('id'));
			
			//description
			var desc = Ext.create('Ext.form.field.Display', {
				fieldLabel : i18n.t('common.description'),
				value : rec.get('description'),
				width : '100%'
			});
			index_details.add(desc);
			
			//add table
			var table = Ext.create('Index.view.user.IndexTable', {
				height : 100,
				width : '100%',
			});
			
			table.setId(rec.get('id'));
			
			index_details.add(table);
			
			var button = Ext.create('Ext.Button', {
				text : i18n.t('chart.show'),
				id : 'createchart_' + rec.get('id'),
				width : 100
			});
			
			var action_panel = Ext.create('Ext.panel.Panel',{
				width : 100,
				layout: {
			        type: 'hbox',       // Arrange child items vertically
			        pack: 'start',
			        align: 'stretch',   	
			        padding: 5
			    }
			});
			
			var button_report = Ext.create('Ext.Button', {
				text : i18n.t('form.report'),
				id : 'report_' + rec.get('id'),
				flex:1
			});
			
			var button_compare = Ext.create('Ext.Button', {
				text : i18n.t('form.comparereport'),
				id : 'compare_' + rec.get('id'),
				flex:1
			});
			
			button_report.on('click', this.viewReport, this);
			button_compare.on('click', this.compareReport, this);
			
			button.on('click', this.createChart, this);
			
			action_panel.add(button_report);
			action_panel.add(button_compare);
			index_details.add(action_panel);
		
//			index_details.add(button_report);
//			index_details.add(button_compare);
			
			index_details.add(button);
			
			//fill table
			this.fillTableData(table, rec.get('id'));
			//col1.add(index_details);
			bottom_container.add(index_details);
			
			
		}
	},
	viewReport : function(button){
		var url;
		var id = button.getId().split('_')[1];
		// check if dates are set or not
		if (startInterval == default_start && endInterval == default_end) {
			url = configServerAddress + "indexreport?id=" + id;
		}
		else{
			url = configServerAddress + "indexreport?id=" + id + "&start=" + encodeURIComponent(startInterval) + "&end=" + encodeURIComponent(endInterval);
			
		}
		window.location=url;
	},
	compareReport : function(button){
		//need the completed index item store
		//get index store
		var myindexstore = this.getIndexItemsStore();
		console.log(myindexstore);
		
		var id = button.getId().split('_')[1];
		
		
		//show window with indexes
		var compare_report_window = Ext.create('Ext.window.Window', {
		    title: i18n.t('common.compare_report_window'),
		    height: 400,
		    width: 300,
		    modal : true,
		    autoScroll:true,
		    items: {  // Let's put an empty grid in just to illustrate fit layout
		        xtype: 'grid',
		        border: false,
		        id:'index-id-grid',
		        store : myindexstore,
		        columns : [{
		    		text : i18n.t('common.code'),
		    		dataIndex : 'id',
		    		flex : 1
		    	},{
		    		text : i18n.t('common.name'),
		    		dataIndex : 'name',
		    		flex : 1
		    	}]
		    },
		    buttons : [{
		    	text : i18n.t('form.compare'),
		    	id: 'id-compare',
		    	handler : function(button){
		    		//get selected grid
		    		var grid = Ext.getCmp('index-id-grid');
		    		console.log(grid);
		    		var current_selection = grid.getSelectionModel().getSelection()[0].get('id');
		    		console.log(current_selection);
		    		//check if dates are set or not
		    		var url;
		    		if (startInterval == default_start && endInterval == default_end) {
		    			url = configServerAddress + "compareindexreport?id=" + id + "&compare_id=" + current_selection;
		    		}
		    		else{
		    			//get selected dates
		    			url = configServerAddress + "compareindexreport?id=" + id + "&compare_id=" + current_selection + "&start=" + encodeURIComponent(startInterval) + "&end=" + encodeURIComponent(endInterval);
		    			
		    		}
		    		window.location=url;
		    		
		    		button.up().up().close();
		    	}
		    }]
		}).show();
		
//		var url;
//		var id = button.getId().split('_')[1];
//		// check if dates are set or not
//		if (startInterval == default_start && endInterval == default_end) {
//			url = configServerAddress + "indexreport?id=" + id;
//		}
//		else{
//			//get selected dates
//			alert(startInterval);
//			alert(endInterval);
//			url = configServerAddress + "indexreport?id=" + id + "&start=" + encodeURIComponent(startInterval) + "&end=" + encodeURIComponent(endInterval);
//			
//		}
//		window.location=url;
	},
	updateCurrentTables: function(datetime){
		console.log('update current table');
		//get all id of current visible details
		var bottom_container_col = Ext.getCmp('col-1');
		var tables = bottom_container_col.query("*[id^=indextable_]");
		
		if(tables.length==0){
			Ext.Msg.alert(i18n.t('error.title'), i18n.t('error.no_index_visualization'));
		}
		else{
			
			//update datetime global variable
			this.global_datetime = datetime;
			
			var store = this.getIndexSnapshotsItemStore();
			store.clearFilter(true);
			store.setCategoryAndDatetime(this.categoryId, this.global_datetime);
			//per ognuno, devo ricalcolare lo snapstore
			
			for(var i = 0 ; i < tables.length; ++i){
				var table = tables[i];
				var id = tables[i].id.split('_')[1];
				store.load({
					callback : function(records, operation, success){
						//maybe no snapshot are present						
						if(records.length>0){
							var panel = Ext.getCmp('up_container_left');
							panel.setTitle(Ext.String.format(i18n.t('common.snapshotindextitle'), datetime));
							
							//query by id
							store.filter('id', id);
							
							//sono sicura che sia nella posizione 0 perhcè il filtro per id mi restituisce per forza un solo elemento
							var item = store.getAt(0);
							
							var snap_store = Ext.create('Index.store.Snap', {
								data : item.snap().getAt(0),
							});
							
							//bind store to table
							table.bindStore(snap_store);
						}
						else{
							Ext.Msg.alert(i18n.t('error.title'), i18n.t('error.missingdata'));
						}				
						
					}
				});
			}
		}

	},
	fillTableData : function(table, id){
		var store = this.getIndexSnapshotsItemStore();
		store.setCategoryAndDatetime(this.categoryId, this.global_datetime);
		
		store.load({
			callback : function(){
				store.clearFilter(true);
				
				//query by id
				store.filter('id', id);
				
				//sono sicura che sia nella posizione 0 perhcè il filtro per id mi restituisce per forza un solo elemento
				var item = store.getAt(0);
				
				var snap_store = Ext.create('Index.store.Snap', {
					data : item.snap().getAt(0),
				});
			
				//bind store to table
				table.bindStore(snap_store);
			}
		});
	},
	createChart : function(button) {
		// check if dates are setted or not
		if (startInterval == default_start && endInterval == default_end) {
			Ext.Msg.alert(i18n.t('error.title'), i18n.t('error.missingdate'));
		}
		else {
			var index_id = button.getId().split('_')[1];
			index_value = this.indexstore.query('id', index_id);
			
			var index_name = index_value.getAt(0).get('name');

			index_value.getAt(0).snap().sort('datetime', 'ASC');
			snap_data = index_value.getAt(0).snap();

			// the data to set in the store
			var data = [];

			var fields_array = [ 'datetime', 'unit' ];
			
			
			// first copy the values data
			for (var i = 0; i < snap_data.getCount(); ++i) {

				var snap_item = snap_data.getAt(i);
				
				single_item = {
					datetime : snap_item.get('datetime'),
					unit : snap_item.get('unit')
				};
				single_item[index_name] = snap_item.get('value');
				
				// select snapshot
				var balance_snap = this.balancestore.query('datetime', snap_item.get('datetime'));

				// work on arguments
				for (arg_index in snap_item.get('arguments')) {
					var arg = snap_item.get('arguments')[arg_index];
					balance_snap.each(function(item) {
						
						var code = item.get('code');
						
						if (code == arg) {
							arg_value = item.get('value');
							var unit = item.get('unit');
//							console.log("find arguments " + arg + " and value " + arg_value);
							single_item[arg] = parseFloat(arg_value);
							single_item[arg+'_unit'] = unit;
							fields_array.push(arg+'_unit');
						}
					});

				}
				data.push(single_item);
			}

			// create series, and fields, and axes
			var axes_array = [];
			
			var primary_axes_fields = [];
			var secondary_axes_fields = [];
			
			var series_array = [];

			// create series for balance items
			{
				// get first elements of item
				first_entry = data[0];
				
				for (key in first_entry) {
					if (key == 'datetime' || key == 'unit' || key.match("unit$"))
						//ignore
						continue;
					
					//is it an index or an argument?
					
					//provo a filtrare l'indexstore?
					
					var its_index = this.indexstore.query('name', key).getAt(0);
					
					if(typeof its_index ==='undefined'){
						
						// get index series
						var index_series = {
							type : 'line',
							axis : 'left',
							xField : 'datetime',
							yField :key,
							tips : {
								trackMouse : true,
								width : 200,
								height : 80,
								renderer : function(storeItem, item) {
									
									var date = storeItem.get('datetime');
									this.setTitle("<p>" + Ext.Date.format(date, 'd-m-y H:i') + "</p>" + "<p>" + item.series.yField + " : " + storeItem.get(item.series.yField) + " " + storeItem.get(item.series.yField+'_unit')+ "</p>");
								}
							},
								
						};
			
						
						series_array.push(index_series);
						
						//construct field array of primary axes
						primary_axes_fields.push(key);
						
					}
					else{
						// get index series
						var index_series = {
							type : 'line',
							axis : 'right',
							xField : 'datetime',
							yField : key,
							tips : {
								trackMouse : true,
								width : 250,
								height : 80,
								renderer : function(storeItem, item) {
									var date = storeItem.get('datetime');
									this.setTitle("<p>" + Ext.Date.format(date, 'd-m-y H:i') + "</p>" + "<p> " + item.series.yField + ": " + storeItem.get(item.series.yField) + " " +storeItem.get('unit') + " </p>");
								}
							}
						};
						series_array.push(index_series);
						
						secondary_axes_fields.push(key);
					}
					// push the key
					fields_array.push(key);
					
				}

			}

			// create store
			var store = new Ext.data.JsonStore({
				// store configs
				storeId : 'myStore',
				// alternatively, a Ext.data.Model name can be given (see
				// Ext.data.Store for an example)
				fields : fields_array,
				data : data
			});
			
			var datetime_axes = {
					title : 'Date',
					type : 'Time',
					position : 'bottom',
					fields : 'datetime',
					dateFormat : 'd-m-y H:i',	
					constrain : true,
					label: {
	                    rotate: {
	                        degrees: -45
	                    }
					},
					//step : [Ext.Date.HOUR, 1]
			};
			
			
			axes_array.push(datetime_axes);
			
			//index axes
			var index_axes = {
					title : 'Index Value',
					type : 'Numeric',
					position : 'right',
					fields : secondary_axes_fields,
			};
			axes_array.push(index_axes);
			
			//index axes
			var components_axes = {
					title : 'Components Value',
					type : 'Numeric',
					position : 'left',
					fields : primary_axes_fields
			};
			axes_array.push(components_axes);
			
			
			var chart = Ext.create('Index.view.chart', {
				store : store,
				series : series_array,
				axes : axes_array,
			});
			
			//set max and min
			chart.setMinimum(startInterval);
			chart.setMaximum(endInterval);

			// set chart id
			var indexdetails = Ext.getCmp('col-1').getComponent('details_' + index_id);
			indexdetails.remove('chart_' + index_id);
			indexdetails.remove('hidechart_' + index_id);
			chart.setElementId('chart_' + index_id);
			indexdetails.setHeight(500);
			indexdetails.add(chart);
			
			//create button hide chart
			var hide_chart = Ext.create('Ext.Button', {
				text : i18n.t('chart.hide'),
				id : 'hidechart_' + index_id,
				width : 100,
				handler : function(b){
					b.up().setHeight(200);
					b.up().remove('chart_' + index_id);
					b.up().remove(b);
				}
			});
			
			indexdetails.add(hide_chart);			
			
		}
	},
	
	createTooltip :function (name, value){
		return "<p> " + name + " : " + value + " </p>";
	}

});
