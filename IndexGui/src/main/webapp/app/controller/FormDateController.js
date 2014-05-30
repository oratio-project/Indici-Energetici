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

Ext.define('Index.controller.FormDateController',{
	extend : 'Ext.app.Controller',
	stores : ['IndexSnapshotsItem'],
	views : ['user.FormDate'],
	
	init : function(){
		console.log('Form controller is running');
		this.control({
			'formdate': {
                render: this.onFormRendered
            },
            'button[id="reset"]':{
            	click : this.onReset
            },
            'button[id="setdateinterval"]' : {
            	click : this.onSetDateInterval
            }
		});
	},
	start : function (){
		
	},
	onFormRendered : function(){
		var anni = Ext.create('Index.store.Date');
		//populate anno comboboxes
		var combobox_fromanno=Ext.getCmp("from_anno");
		var combobox_toanno=Ext.getCmp("to_anno");
		
		combobox_fromanno.bindStore(anni);
		combobox_toanno.bindStore(anni);
		
		combobox_fromanno.on('select', this.onSelect, this);
		combobox_toanno.on('select', this.onSelect, this);
		
	},
	onReset : function(button){
		button.up('form').getForm().reset();
		//reset date interval
		startInterval = default_start;
		endInterval = default_end;
		//delete chart
		var charts = Ext.ComponentQuery.query('[id^=chart_]');
		
		for(var i = 0; i<charts.length; i++){
			var split = (charts[i].id).split('_');
			var index_details = Ext.ComponentQuery.query('[id=details_'+split[1]+']');
			//remove chart
			index_details[0].remove(charts[i]);
			index_details[0].remove('hidechart_'+split[1]);
			//set new size
			index_details[0].setHeight(200);
		}
		//change panel title
		Ext.getCmp('up_container_right').setTitle(Ext.String.format(i18n.t('common.dateinterval'), 'none'));
		
	},
	onSetDateInterval : function(button){
		
		//se ci sono dei grafici, li elimino
		//delete chart
		var charts = Ext.ComponentQuery.query('[id^=chart_]');
		if(charts.length>0){
			for(var i = 0; i<charts.length; i++){
				var split = (charts[i].id).split('_');
				var index_details = Ext.ComponentQuery.query('[id=details_'+split[1]+']');
				//remove chart
				index_details[0].remove(charts[i]);
				index_details[0].remove('hidechart_'+split[1]);
				//set new size
				index_details[0].setHeight(200);
			}
		}
		
		//memorize two dates
		var form = button.up('form').getForm();
		
		//funzione per costruire la data a cui passo i nomi dei parametri da cerca e il form
		startInterval = this.getFormattedDate(form, 'from_anno', 'from_mese', 'from_giorno', 'from_ora');
		endInterval = this.getFormattedDate(form, 'to_anno', 'to_mese', 'to_giorno', 'to_ora');
		
		//encode date
		//encode dates
		var dates = {
				start : startInterval,
				end : endInterval
		};
		
		var encoded_dates = Ext.Object.toQueryString(dates);
		
		this.fireEvent('set_date', encoded_dates);
		
		//change panel title
		Ext.getCmp('up_container_right').setTitle(Ext.String.format(i18n.t('common.dateinterval'), startInterval+" "+endInterval));
		
		
	},
	onSelect : function(combobox){
		//once selected, is it year, month or day?
		//need to check the combo id
		var id = combobox.getId();
		
		if(id=="to_anno"){
			var selected_year = combobox.getValue();
			
			//get correct store based on combobox value
			var mesi = Ext.create('Index.store.Date');
			//set month as base and selected year
			mesi.setDateParameter('month', selected_year);
			
			//check if a combo already exist
			var month_combobox = Ext.getCmp("to_mese");
			this.populateCombobox(month_combobox, mesi);
		}
		
		if(id=="to_mese"){
			var selected_month = combobox.getValue();
			var anno = Ext.getCmp("to_anno").getValue();
			//get correct store based on combobox value
			var giorni = Ext.create('Index.store.Date');
			giorni.setDateParameter('day', anno, selected_month);
			
			//check if a combo already exist
			var day_combobox = Ext.getCmp("to_giorno");
			this.populateCombobox(day_combobox, giorni);
		}
		if(id=="to_giorno"){
			var selected_day = combobox.getValue();
			var anno = Ext.getCmp("to_anno").getValue();
			var mese = Ext.getCmp("to_mese").getValue();
			//get correct store based on combobox value
			var ore = Ext.create('Index.store.Date');
			ore.setDateParameter('hour', anno, mese, selected_day);
			
			//check if a combo already exist
			var hour_combobox = Ext.getCmp("to_ora");
			this.populateCombobox(hour_combobox, ore);
		}
		if(id=="from_anno"){
			var selected_year = combobox.getValue();
			
			//get correct store based on combobox value
			var mesi = Ext.create('Index.store.Date');
			mesi.setDateParameter('month', selected_year);
			//check if a combo already exist
			var month_combobox = Ext.getCmp("from_mese");
			this.populateCombobox(month_combobox, mesi);
		}
		
		if(id=="from_mese"){
			var selected_month = combobox.getValue();
			var anno = Ext.getCmp("from_anno").getValue();
			//get correct store based on combobox value
			var giorni = Ext.create('Index.store.Date');
			giorni.setDateParameter('day', anno, selected_month);
			//check if a combo already exist
			var day_combobox = Ext.getCmp("from_giorno");
			this.populateCombobox(day_combobox, giorni);
		}
		if(id=="from_giorno"){
			var selected_day = combobox.getValue();
			var anno = Ext.getCmp("from_anno").getValue();
			var mese = Ext.getCmp("from_mese").getValue();
			//get correct store based on combobox value
			var ore = Ext.create('Index.store.Date');
			ore.setDateParameter('hour', anno, mese, selected_day);
			//check if a combo already exist
			var hour_combobox = Ext.getCmp("from_ora");
			this.populateCombobox(hour_combobox, ore);
		}
	},
	populateCombobox : function (combobox, store) {
		combobox.clearValue();
		combobox.bindStore(store);
		combobox.on('select', this.onSelect, this);
	},
	getFormattedDate : function (form, a, m, g, o){
		var anno = form.findField(a).getValue();
		var mese = form.findField(m).getValue();
		var giorno = form.findField(g).getValue();
		var ora = form.findField(o).getValue();
		
		var date = new Date(anno+'/'+mese+'/'+giorno+' '+ora+':00:00');
		var formatted_date = Ext.Date.format(date, 'Y-m-d\\TH:i:sO');
		return formatted_date;
	}
});
