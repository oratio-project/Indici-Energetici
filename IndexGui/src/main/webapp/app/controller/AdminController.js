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

Ext.define('Index.controller.AdminController', {
	extend : 'Index.controller.BaseModuleController',
	mainView : 'Index.view.admin.AdminViewport',
	view : ['Index.view.admin.FormCreateCategories', 'Index.view.admin.FormCreateBalanceItems', 'Index.view.admin.FormCreateIndexItems'],
	stores : ['Categories'],
	requires : ['Index.util.Utilities'],
	//setup listener for action columns
	init: function() {
	    this.control({
	        'actioncolumn':{
	            index_edit: this.onIndexItemEdit,
	            index_delete : this.onIndexItemDelete,
	            balance_edit: this.onBalanceItemEdit,
	            balance_delete : this.onBalanceItemDelete,
	            category_edit : this.onCategoryItemEdit,
	            category_delete : this.onCategoryItemDelete
	        },
	        'button[id="category-create"]' : {
	        	click : this.onCreateCategory
	        },
	        'button[id="category-update"]' : {
	        	click : this.onUpdateCategory
	        },
	        'button[id="balance-create"]' : {
	        	click : this.onCreateBalanceItem
	        },
	        'button[id="balance-update"]' : {
	        	click : this.onUpdateBalanceItem
	        },
	        'button[id="index-create"]' : {
	        	click : this.onCreateIndexItem
	        },
	        'button[id="index-update"]' : {
	        	click : this.onUpdateIndexItem
	        },
	        'button[id="add-balance-code"]' : {
	        	click : this.onAddBalanceCode
	        },
	        'button[id="edit-add-balance-code"]' : {
	        	click : this.onAddBalanceCode
	        },
	        'button[id="add-range"]':{
	        	click : this.onAddRange
	        },
	        'button[id="delete-range"]':{
	        	click : this.onDeleteRange
	        }
	    });
	    //global variable
	    range_array = new Array(); 
	    count = 0;
	},

	start : function() {
		this.superclass.start.call(this);
		var button_panel = Ext.getCmp('button_panel');
		var left_panel = Ext.getCmp('left_panel');
		var right_panel = Ext.getCmp('right_panel');
		
		//check submenu selected
		if(this.categoryId==-1){
			button_panel.removeAll(false);
			left_panel.removeAll(false);
			right_panel.removeAll(false);
		}
		else{
			button_panel.removeAll(false);
			left_panel.removeAll(false);
			right_panel.removeAll(false);
			
			var current_view=null;
			
			//if create snapshot
			if(this.categoryId=='FormCreateSnapshot') {
				current_view = Ext.create('Index.view.admin.'+this.categoryId);
				//set the first value for the combobox
				var combobox = Ext.getCmp('cadency');
				combobox.select(combobox.getStore().getAt(0));
				
				//top_panel.add(current_view);
				left_panel.add(current_view);
			}
			else {
				var add_element_button = Ext.create('Ext.Button', {
				    text: i18n.t('admin.add'),
				    icon : 'img/new.png',
				    id: 'add',
				    padding: 5,
				    margin : 5
				});
				
				//hidden by default
				var form_view = Ext.create('Index.view.admin.FormCreate'+this.categoryId);
				
				current_view = Ext.create('Index.view.admin.'+this.categoryId+"List");
				//get the correct store
				var store = Ext.create('Index.store.'+this.categoryId);
				
				current_view.bindStore(store);
						
				
				button_panel.add(add_element_button);
				right_panel.add(form_view);
				
				//handle click on add, edit and delete
				add_element_button.on('click', this.addElement, this);
				
				left_panel.add(current_view);
			}
			
		}

	},
	
	addElement : function(){
		//hide possible modification view
		var edit_form = Ext.getCmp(Ext.util.Format.lowercase('FormEdit'+this.categoryId));
		console.log(Ext.util.Format.lowercase('FormEdit'+this.categoryId));
		console.log(edit_form);
		
		if(edit_form!=null){
			console.log('removing edit form');
			var right_panel = Ext.getCmp('right_panel');
			right_panel.remove(edit_form);
		}
		
		//get form view and set visibility
		var view_id = Ext.util.Format.lowercase('FormCreate'+this.categoryId);
		var view_el = Ext.getCmp(view_id);
		console.log(view_el);
		view_el.setVisible(true);
		
		//fill cateogry combobox
		
		var category_combobox = Ext.getCmp('add-index-category');
		if(category_combobox!=null){
			var category_store = this.getCategoriesStore();
			category_combobox.bindStore(category_store);
		}
		
	},
	onIndexItemEdit : function (grid, rowIndex, colIndex){
		
		var right_panel = Ext.getCmp('right_panel');
		
		var selected_record = grid.getStore().getAt(rowIndex);
		
		grid.getSelectionModel().select(selected_record);
		
		//remove previous edit view
		var editing_view = Ext.getCmp('formeditindexitems');
		
		//remove add view 
		if(editing_view!=null){
			right_panel.remove(editing_view);
		}
		
		var add_view = Ext.getCmp('formcreateindexitems');
		if(add_view.isVisible()==true){
			add_view.setVisible(false);
		}
		
		//create view for editing
		editing_view = Ext.create('Index.view.admin.FormEditIndexItems');
		editing_view.setTitle('Edit index item ' + selected_record.get('name'));
		
		console.log(selected_record);
		
		//set values of form
		Ext.getCmp('edit-index-id').setValue(selected_record.get('id'));
		Ext.getCmp('edit-index-name').setValue(selected_record.get('name'));
		Ext.getCmp('edit-index-description').setValue(selected_record.get('description'));
		Ext.getCmp('edit-index-formula').setValue(selected_record.get('formula'));
		Ext.getCmp('edit-index-unit').setValue(selected_record.get('unit'));
		
		Ext.getCmp('edit-index-category').bindStore(this.getCategoriesStore());
		Ext.getCmp('edit-index-category').setValue(selected_record.get('category').id);
		
		
		//get range object
		var range_array = selected_record.get('range');
		
		for(var i = 0 ; i < range_array.length; ++i){
			var j = i + 1;
			Ext.getCmp('edit-range'+j+'-color').setValue(range_array[i].color);
			Ext.getCmp('edit-range'+j+'-superior').setValue(range_array[i].superior);
			Ext.getCmp('edit-range'+j+'-inferior').setValue(range_array[i].inferior);
		}
		
		//add view to content panel	
		right_panel.add(editing_view);
		
	},
	onUpdateIndexItem : function(button){
		console.log('update index item');
		
		var index_form = button.up('panel').down('form[id="edit-index_form"]').getForm();
		var index_form_values = index_form.getValues();
		
		//merge category with index form values
		index_form_values['category'] = {
			id : index_form.findField('edit-index-category').getValue(),
		};
		
		index_form_values['id'] = index_form.findField('edit-index-id').getValue();
		
		var range_form = button.up('panel').down('form[id="edit-range_form"]').getForm();
		
		//build json obj for range
		
		var range1_json = this.buildJsonRangeObj(range_form.findField('edit-range1-color').getValue(), 
													range_form.findField('edit-range1-superior').getValue(), 
													range_form.findField('edit-range1-inferior').getValue());

		var range2_json = this.buildJsonRangeObj(range_form.findField('edit-range2-color').getValue(), 
													range_form.findField('edit-range2-superior').getValue(), 
													range_form.findField('edit-range2-inferior').getValue());

		var range3_json = this.buildJsonRangeObj(range_form.findField('edit-range3-color').getValue(), 
													range_form.findField('edit-range3-superior').getValue(), 
													range_form.findField('edit-range3-inferior').getValue());

		
		var indexitem_put = Ext.create('Index.model.put.IndexItemPut');
		
		
		if(range1_json!=null || range2_json!=null || range3_json!=null){
			console.log('non ho messo tutto a null');
			index_form_values['range'] = [];
			
			if(range1_json!=null){
				index_form_values['range'].push(range1_json);
			}
			if(range2_json!=null){
				index_form_values['range'].push(range2_json);
			}
			if(range3_json!=null){
				index_form_values['range'].push(range3_json);
			}
		}
		else{
			delete indexitem_put.data.range;
		}
		
		
		indexitem_put.set(index_form_values);
		
		
		indexitem_put.save({
			success : function ( record, operation ){
				Ext.Msg.alert(i18n.t('success.title'), i18n.t('success.successfully_operation'));
				//refresh grid
				var view = Ext.getCmp('indextablelist');
				console.log(view);
				view.bindStore(Ext.create('Index.store.IndexItems'));
				//close the form
				button.up().up().setVisible(false);
			},
			failure : function ( record, operation ){
				
				Ext.Msg.alert(i18n.t('error.title'), i18n.t('error.unsuccessfully_operation'));
			}
		});
	},
	onBalanceItemEdit : function (grid, rowIndex, colIndex){
		console.log('edit clicked');
		console.log(grid.getStore().getAt(rowIndex).get('code'));
		
		var selected_record = grid.getStore().getAt(rowIndex);
		grid.getSelectionModel().select(selected_record);
		
		right_panel = Ext.getCmp('right_panel');
		
		//remove previous edit view
		var editing_view = Ext.getCmp('formeditbalanceitems');
		if(editing_view!=null){
			right_panel.remove(editing_view);
		}
		var add_view = Ext.getCmp('formcreatebalanceitems');
		if(add_view.isVisible()==true){
			add_view.setVisible(false);
		}
		
		//create view for editing
		editing_view = Ext.create('Index.view.admin.FormEditBalanceItems');
		editing_view.setTitle('Edit balance item ' + selected_record.get('code'));

		//set values of form
		Ext.getCmp('balance-code').setValue(selected_record.get('code'));
		Ext.getCmp('balance-name').setValue(selected_record.get('name'));
		Ext.getCmp('balance-description').setValue(selected_record.get('description'));
		Ext.getCmp('balance-unit').setValue(selected_record.get('unit'));
		
		right_panel.add(editing_view);
	},
	onIndexItemDelete : function(grid, rowIndex, colIndex){
		
		//delete modify element form
		var modify_form = Ext.getCmp('formeditindexitems');
		if(modify_form!=null){
			var right_panel = Ext.getCmp('right_panel');
			right_panel.remove(modify_form);
		}
		
		var rec = grid.getStore().getAt(rowIndex);
		//are you sure?
		Ext.MessageBox.confirm('Delete ' + rec.get('name'), Ext.String.format(i18n.t('common.confirmation_message'),
				rec.get('name')), function(btn){
			   if(btn === 'yes'){	 
			       Index.util.Utilities.deleteRecord(grid,rec);
			   }
			 });
	},
	onCreateBalanceItem : function (button){
		console.log('balance create');
		var form = button.up('form').getForm();
		
		//get all variable
		
		var balance_code = form.findField('code').getValue();		
		var balance_name = form.findField('name').getValue();
		var balance_description = form.findField('description').getValue();
		var balance_unit = form.findField('unit').getValue();
		
		//check values
		if(balance_code==''|| balance_description==''||balance_name==''||balance_unit==''){
			//error message
			Ext.Msg.alert(i18n.t('error.title'), i18n.t('error.missing_form_data'));
		}
		else if (Ext.isNumeric(balance_code.charAt(0))==true){
			Ext.Msg.alert(i18n.t('error.title'), i18n.t('error.code_with_number'));
		}
		else{
			//go on with balance creation
			var balance_post = Ext.create('Index.model.post.BalanceItemPost', {
				code : balance_code,
				name : balance_name,
				description : balance_description,
				unit : balance_unit
			});
			console.log('balance post');
			
			console.log(balance_post);
			
			var myMask = new Ext.LoadMask(Ext.getCmp('formcreatebalanceitems'), {msg:i18n.t('common.wait')});
			myMask.show();
			
			balance_post.save({
				success : function ( record, operation ) {
					var storename = 'Index.store.BalanceItems';
					var viewid = 'balanceitemslist';
					Index.util.Utilities.onSaveSuccess(record, operation, storename, viewid, button);
					myMask.hide();
				},
				failure : function ( record, operation ) {
					Index.util.Utilities.onSaveError();
				}
			});
			
		}
	},
	onBalanceItemDelete : function (grid, rowIndex, colIndex){
		//delete modify element form
		var modify_form = Ext.getCmp('formeditbalanceitems');
		if(modify_form!=null){
			var right_panel = Ext.getCmp('right_panel');
			right_panel.remove(modify_form);
		}
		
		var rec = grid.getStore().getAt(rowIndex);
		//are you sure?
		Ext.MessageBox.confirm('Delete ' + rec.get('code'), Ext.String.format(i18n.t('common.confirmation_message'),
				rec.get('code')), function(btn){
			   if(btn === 'yes'){
				   Index.util.Utilities.deleteRecord(grid,rec);
			   }
			 });
	},
	onUpdateBalanceItem : function (button){
		console.log('balance item update');
		//get the form
		var form = button.up('form').getForm();
		
		var balance_code = form.findField('balance-code').getValue();
		console.log('balance code ' + balance_code);
		var balance_name = form.findField('balance-name').getValue();
		var balance_description = form.findField('balance-description').getValue();
		var balance_unit = form.findField('balance-unit').getValue();
		
		//create balance item put
		var balance_put = Ext.create('Index.model.put.BalanceItemPut');
		
		console.log(balance_put);
		
		//set other value
		balance_put.set({
			'code' : balance_code,
			'name' : balance_name,
			'description' : balance_description,
			'unit' : balance_unit
		});
		
		//save modification
		balance_put.save({
			success : function ( record , operation ){
				Ext.Msg.alert(i18n.t('success.title'), i18n.t('success.successfully_operation'));
				//refresh grid
				var view = Ext.getCmp('balanceitemslist');
				console.log(view);
				view.bindStore(Ext.create('Index.store.BalanceItems'));
				//close form
				button.up().up().setVisible(false);
			},
			failure : function (record, operation){
				Index.util.Utilities.onSaveError();
			}
		});
	},
	onCategoryItemEdit : function (grid, rowIndex, colIndex){
		var selected_record = grid.getStore().getAt(rowIndex);
		grid.getSelectionModel().select(selected_record);
		right_panel = Ext.getCmp('right_panel');
		
		//remove previous edit view
		var editing_view = Ext.getCmp('formeditcategories');
		if(editing_view!=null){
			right_panel.remove(editing_view);
		}
		
		var add_view = Ext.getCmp('formcreatecategories');
		console.log('add view');
		console.log(add_view);
		
		if(add_view.isVisible()==true){
			add_view.setVisible(false);
		}
		
		//create view for editing
		editing_view = Ext.create('Index.view.admin.FormEditCategoryItems');
		editing_view.setTitle('Edit category item ' + selected_record.get('name'));
		Ext.getCmp('category-id').setValue(selected_record.get('id'));
		Ext.getCmp('category-name').setValue(selected_record.get('name'));
		right_panel.add(editing_view);
		
	},
	onCreateCategory: function(button){
		console.log('create category');
		var category_name = button.up('form').getForm().findField('name').getValue();
		if(category_name==null||category_name==''){
			//error message
			Ext.Msg.alert(i18n.t('error.title'), i18n.t('error.missing_category_name'));
		}
		else{
			//go on with category creation
			var category_post = Ext.create('Index.model.post.CategoryPost',{name:category_name});
			console.log(category_post);
			
			var myMask = new Ext.LoadMask(Ext.getCmp('formcreatecategories'), {msg:i18n.t('common.wait')});
			myMask.show();
			category_post.save({
				success : function(record, operation){
					var viewid = 'categorieslist';
					var storename = 'Index.store.Categories';
					Index.util.Utilities.onSaveSuccess(record, operation, storename, viewid, button);
					myMask.hide();
				},
				failure : function (record, operation){
					Index.util.Utilities.onSaveError();
				}
			});
		}
	},
	onCategoryItemDelete: function(grid, rowIndex, colIndex){
		//delete modify element form
		var modify_form = Ext.getCmp('formeditcategories');
		if(modify_form!=null){
			var right_panel = Ext.getCmp('right_panel');
			right_panel.remove(modify_form);
		}
		var rec = grid.getStore().getAt(rowIndex);
		Ext.MessageBox.confirm('Delete ' + rec.get('name'), Ext.String.format(i18n.t('common.confirmation_message'),
				rec.get('name')), function(btn){
			   if(btn === 'yes'){
				   Index.util.Utilities.deleteRecord(grid,rec);
			   }
			 });
	},
	onUpdateCategory: function(button){
		console.log('update category');
		var category_name = button.up('form').getForm().findField('category-name').getValue();
		var category_id = button.up('form').getForm().findField('category-id').getValue();
		
		
		//create category put object
		var category_put = Ext.create('Index.model.put.CategoryPut');
		console.log(category_put);
		//set the request to the category with specified id
		category_put.setId(category_id);
		console.log(category_put);
		//set new name
		category_put.set('name', category_name);
		category_put.save({
			success : function (record , operation) {
				console.log(record);
				console.log(operation);
				Ext.Msg.alert(i18n.t('success.title'), i18n.t('success.successfully_operation'));
				//refresh grid
				var view = Ext.getCmp('categorieslist');
				console.log(view);
				view.bindStore(Ext.create('Index.store.Categories'));
				//close form
				button.up().up().setVisible(false);
			},
			failure : function (record , operation) {
				Ext.Msg.alert(i18n.t('error.title'), i18n.t('error.unsuccessfully_operation'));
			}
		});
		
	},
	onCreateIndexItem : function ( button ){
		console.log('create index item');
		
		var index_form = button.up('panel').down('form[id="index_form"]').getForm();
		var index_form_values = index_form.getValues();
		
		//merge category with index form values
		index_form_values['category'] = {
			id : index_form.findField('add-index-category').getValue(),
		};
		
		
		var range_form = button.up('panel').down('form[id="range_form"]').getForm();
		
		
		var range1_json = this.buildJsonRangeObj(range_form.findField('range1-color').getValue(), 
													range_form.findField('range1-superior').getValue(), 
													range_form.findField('range1-inferior').getValue());
		
		var range2_json = this.buildJsonRangeObj(range_form.findField('range2-color').getValue(), 
				range_form.findField('range2-superior').getValue(), 
				range_form.findField('range2-inferior').getValue());
		
		var range3_json = this.buildJsonRangeObj(range_form.findField('range3-color').getValue(), 
				range_form.findField('range3-superior').getValue(), 
				range_form.findField('range3-inferior').getValue());
		
		if(range1_json!=null || range2_json!=null || range3_json!=null){
			index_form_values['range'] = [];
			if(range1_json!=null){
				index_form_values['range'].push(range1_json);
			}
			if(range2_json!=null){
				index_form_values['range'].push(range2_json);
			}
			if(range3_json!=null){
				index_form_values['range'].push(range3_json);
			}
		}
		
		var indexitem_post = Ext.create('Index.model.post.IndexItemPost', index_form_values);
		
		indexitem_post.save({
			success : function ( record, operation ){
				var storename = 'Index.store.IndexItems';
				var viewid = 'indextablelist';
				Index.util.Utilities.onSaveSuccess(record, operation, storename, viewid, button);
			},
			failure : function ( record, operation ){
				Index.util.Utilities.onSaveError();
			}
		});
		
	},
	buildJsonRangeObj : function (color, superior, inferior){
		var json = null;
		
		if(color!=null){
			json = {
				'color': color	
			};
			//deve esserci per forza uno dei due
			if(superior!=null || inferior!=null){
				if(superior!=null){
					json['superior'] = superior;
				}
				if(inferior!=null){
					json['inferior'] = inferior;
				}
			}
			else{
				//perchè non può esserci solo il colore
				json = null;
			}
			
		}
		return json;

	},
	onAddBalanceCode : function(button){
		var formula_textfield_id='';
		
		if(button.id == 'add-balance-code'){
			formula_textfield_id = 'index-formula';
		}
		else{
			formula_textfield_id = 'edit-formula';
		}
		
		var balance_store = Ext.create('Index.store.BalanceItems');
		balance_store.load({
			callback : function () {
				console.log(balance_store);
				//open new window with code and name list
				var balance_window = Ext.create('Ext.window.Window', {
				    title: i18n.t('common.select_code'),
				    height: 400,
				    width: 300,
				    modal : true,
				    autoScroll:true,
				    items: {  // Let's put an empty grid in just to illustrate fit layout
				        xtype: 'grid',
				        border: false,
				        id:'balance-code-grid',
				        store : balance_store,
				        columns : [{
				    		text : i18n.t('common.code'),
				    		dataIndex : 'code',
				    		flex : 1
				    	},{
				    		text : i18n.t('common.name'),
				    		dataIndex : 'name',
				    		flex : 1
				    	}]
				    },
				    buttons : [{
				    	text : i18n.t('form.insert'),
				    	id: 'code-insert',
				    	handler : function(button){
				    		//get selected grid
				    		var grid = Ext.getCmp('balance-code-grid');
				    		console.log(grid);
				    		var current_selection = grid.getSelectionModel().getSelection()[0].get('code');
				    		console.log(current_selection);
				    		//add to formula text
				    		var formula_text = Ext.getCmp('index-formula').getValue();
				    		formula_text+=current_selection;
				    		Ext.getCmp(formula_textfield_id).setValue(formula_text);
				    		
				    		button.up().up().close();
				    	}
				    }]
				}).show();
			}
		});
		
	},

});
