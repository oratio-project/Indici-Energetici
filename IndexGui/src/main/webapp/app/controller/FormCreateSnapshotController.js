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

Ext.define('Index.controller.FormCreateSnapshotController',{
	extend : 'Ext.app.Controller',
	//stores : ['IndexSnapshots'],
	views : ['admin.FormCreateSnapshot'],
	
	init : function(){
		this.control({
			'formcreatesnapshot':{
				render : this.onRenderForm
			},
            'button[id="create"]': {
            	click : this.onCreateClick
            }
		});
	},
	
	start : function () {
		
	},
	onRenderForm : function(){

	},
	onCreateClick : function (button){
		var cadency = button.up('form').getForm().findField('cadency').getValue();
		
		
		var from_date = Ext.getCmp('from_date').getValue();
		//formt date with timezone
		from_date = Ext.Date.format(from_date, 'Y-m-d\\TH:i:sO');
		console.log(from_date);
		
		var to_date = Ext.getCmp('to_date').getValue();
		to_date = Ext.Date.format(to_date, 'Y-m-d\\TH:i:sO');
		console.log(to_date);
		
		//prima chiamata su balance snapshots (crea intervalli temporali con una certa cadenza)
		//seconda chiamata su index (calcola gli indici per quegli intervalli temporali appena definiti)
	
		//create a new istance
		var balance_snap = Ext.create('Index.model.post.BalanceSnapshotPost', {start: from_date, end: to_date, cadency: cadency});
		var index_snap = Ext.create('Index.model.post.IndexSnapshotPost', {start: from_date, end: to_date});
		var myMask = new Ext.LoadMask(Ext.getCmp('formcreatesnapshot'), {msg:i18n.t('common.wait')});
		myMask.show();
		balance_snap.save({
			success : function(record, operation){
				console.log(record);
				console.log('success ');
				index_snap.save({
					success : function (record, operation){
						myMask.hide();
						
						Ext.Msg.alert(i18n.t('success.title'), i18n.t('success.successfully_operation'));
					
					},
					failure : function (record, operation){
						myMask.hide();
						
						Ext.Msg.alert(i18n.t('error.title'), i18n.t('error.unsuccessfully_operation'));
					}
				});
			},
			failure : function(record, operation){
				myMask.hide();
				
				Ext.Msg.alert(i18n.t('error.title'), i18n.t('error.unsuccessfully_operation'));
				
			}
		});
	}
});
