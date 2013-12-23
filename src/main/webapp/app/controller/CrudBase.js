Ext.define('xcore.controller.CrudBase', {
	extend: 'Deft.mvc.ViewController',
	control: { //使用什么方式，选择中界面组件，类型还是id,  实现采用Ext.ComponentQueryView，也可以是"'useredit button[action=save]':
		view: {
			removed: 'onRemoved',
			itemdblclick: 'onItemDblClick',
			itemcontextmenu: 'onItemContextMenu'
		},
		actionColumn: {
			selector: 'actioncolumn',
			listeners: {
				click: 'onActionColumnClick'
			}
		},
		createButton: {
			click: 'onCreateButtonClick'
		},
		filterField: {
			filter: 'onFilterField'
		},
        searchButton:{
            click: 'onSearchButtonClick'
        }

	},

	constructor: function() {
        //将control属性的所有值，merge入父对象的control属性中。
		if (!Ext.Object.equals(this.control, this.superclass.control)) {
			var toBeDeleted = [];
			var i;

			Ext.Object.each(this.control, function(key, value, myself) {
				if (!value) {
					toBeDeleted.push(key);
				}
			});

			Ext.merge(this.control, this.superclass.control);

			for (i = 0; i < toBeDeleted.length; i++) {
				delete this.control[toBeDeleted[i]];
			}
		}

		return this.callParent(arguments);
	},

	init: function() {
        //getView如何获得view对象,view 创建controller对象，同时传入了view的引用。
		var store = this.getView().getStore();
		store.clearFilter(true);
		store.load();
	},

	destroy: function() {
		if (this.actionMenu) {
			this.actionMenu.destroy();
		}
		return this.callParent(arguments);
	},

	onItemContextMenu: function(view, record, item, index, e, eOpts) {
		e.stopEvent();
		this.showContextMenu(record, e.getXY());
	},

    //当操作列被点击时的处理
	onActionColumnClick: function(grid, rowIndex, colIndex, item, e, record, row) {
		this.showContextMenu(record, null, row);
	},

	showContextMenu: function(record, xy, item) {
		var me = this;
		var items = me.buildContextMenuItems(record);

		if (this.actionMenu) {
			this.actionMenu.destroy();
		}

		this.actionMenu = Ext.create('Ext.menu.Menu', {
			items: items,
			border: true
		});

		if (xy) {
			this.actionMenu.showAt(xy);
		} else {
			this.actionMenu.showBy(item);
		}
	},

    onSearchButtonClick:function(button){
        var me = this;
        var form = button.up('form'); //查找父层级中的window组件
        var values = form.getValues();
        var store = this.getView().getStore();
        if (values) {
            store.clearFilter(true);
            store.filter(
                [   {
                        property: 'userName',
                        value   : values.userName
                    },
                    {
                        property: 'firstName',
                        value   : values.firstName
                    },
                    {
                        property: 'name',
                        value   : values.name
                    }
                ]);
        } else {
            store.clearFilter();
        }


    },

	onRemoved: function() {
		History.pushState({}, i18n.app_title, "?");
	},
    //修改
	onItemDblClick: function(grid, record) {
		this.editObject(record);
	},
    //新建
	onCreateButtonClick: function() {
		this.editObject();
	},

	editObject: function(record) {
		this.getView().getStore().rejectChanges();

		var editWindow = Ext.create(this.editWindowClass);

		var form = editWindow.down('form');
		if (record) {
			form.loadRecord(record); //从一个model中加载form中的数据，保存时，也保存到这个record对象上
		} else {
			form.loadRecord(this.createModel());
		}

		form.isValid();

		this.setFocus(editWindow);

		var saveButton = editWindow.down('#editFormSaveButton');
		if (this.isReadonly(record)) {
			Ext.suspendLayouts();
			form.getForm().getFields().each(function(field) {
				field.setReadOnly(true);
			});
			saveButton.setVisible(false);
			Ext.resumeLayouts();
		} else {
			saveButton.addListener('click', this.onEditFormSaveButtonClick, this);
		}

	},

	destroyObject: function(record) {
		var me = this;
		var store = me.getView().getStore();

		Ext.Msg.confirm(i18n.attention, this.destroyConfirmMsg(record), function(buttonId, text, opt) {
			if (buttonId === 'yes') {
				var record = me.getView().getSelectionModel().getSelection()[0];
				store.remove(record);
				store.sync({
					success: function() {
						xcore.ux.window.Notification.info(i18n.successful, me.successfulDestroyMsg);
					},
					failure: function(records, operation) {
						store.rejectChanges();
						me.destroyFailureCallback();
					}
				});
			}
		});
	},

	onFilterField: function(field, newValue) {
		var store = this.getView().getStore();
		if (newValue) {
			store.clearFilter(true);
			store.filter('filter', newValue);
		} else {
			store.clearFilter();
		}
	},

	onEditFormSaveButtonClick: function(button) {
		var me = this;
		var win = button.up('window'); //查找父层级中的window组件
		var form = win.down('form');  //查询window组件下的form对象
		var store = this.getView().getStore();

        //Persists the values in this form into the passed Ext.data.Model  将数据更新到原来加载的model对象上。
        // form.loadRecord(record);
		form.updateRecord();
		var record = form.getRecord();
        //判断数据model是否被更改了
		if (!record.dirty) {
			win.close();
			return;
		}
        //在数据库中没有存储
		if (record.phantom) {
			store.rejectChanges(); //拒绝掉对store中原有数据的所有本地修改，以便增加新的修改，并与后台同步。
			store.add(record); //此时有没有http请求呢
		}
        //同步store,发生相应的后台操作。这种处理，不用重新全量分页查一次列表数据。
		store.sync({
			success: function(records, operation) {
				xcore.ux.window.Notification.info(i18n.successful, me.successfulSaveMsg);
				win.close();
			},
			failure: function(records, operation) {
				store.rejectChanges();
			}
		});
	},

	setFocus: function(editWindow) {
		var query;
		if (this.focusFieldName) {
			query = 'field[name=' + this.focusFieldName + ']';
		} else {
			query = 'field';
		}

		var field = editWindow.down(query);
		if (field) {
			field.focus();
		}
	},

	successfulDestroyMsg: i18n.defaultSuccessfulDestroyMsg,
	successfulSaveMsg: i18n.defaultSuccessfulSaveMsg,

	destroyConfirmMsg: Ext.emptyFn,
	destroyFailureCallback: Ext.emptyFn,

	focusFieldName: null,

	isReadonly: function(record) {
		return false;
	},

	createModel: Ext.emptyFn,

    //构建列表的右键，默认包括三个菜单项
	buildContextMenuItems: function(record) {

		return [ {
			text: i18n.edit,
			hidden: this.isReadonly(record),
			glyph: 0xe803,
			handler: Ext.bind(this.editObject, this, [ record ])// Ext.bind 构建一个函数的闭包
		}, {
			text: i18n.show,
			hidden: !this.isReadonly(record),// 非只读下不展示
			glyph: 0xe817,
			handler: Ext.bind(this.editObject, this, [ record ])
		}, {
			text: i18n.destroy,
			hidden: this.isReadonly(record),
			glyph: 0xe806,
			handler: Ext.bind(this.destroyObject, this, [ record ])
		} ];

	},

	editWindowClass: null
});