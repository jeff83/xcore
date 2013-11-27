Ext.define('xcore.view.user.List', {
	extend: 'Ext.grid.Panel',
	requires: [ 'xcore.controller.Department', 'xcore.ux.form.field.FilterField', 'xcore.store.Departments' ],
	controller: 'xcore.controller.Department',
	
	title: '部门管理',
	closable: true,
	border: true,

	initComponent: function() {

		var me = this;

		me.store = Ext.create('xcore.store.Departments');

		me.columns = [ {
			xtype: 'actioncolumn',
			width: 30,
			items: [ {
				icon: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAQAAAC1+jfqAAAAK0lEQVR4AWMgBBhXyRFQsPI/xQoyCCgg7EgX2jkSYQWZAOFN2jtSjsKQBAD0NQ+N4ZAsdgAAAABJRU5ErkJggg=='
			} ]
		}, {
			text: '组织编号',
			dataIndex: 'code',
			flex: 1
		}, {
			text: '名称',
			dataIndex: 'name',
			flex: 1
		}];

		me.dockedItems = [{
            xtype:'panel',
            dock: 'top',
            width:'100%',
            layout: {
                type: 'vbox',
                align:'stretch'
            },
            items:[
                {
                    xtype: 'form',
                    padding: 5,
                    bodyPadding: 10,
                    layout: {
                        type: 'table',
                        columns: 4
                    },
                    defaultType: 'textfield',
                    defaults: {
                        anchor: '100%'
                    },
                    fieldDefaults: {
                        msgTarget: 'side'
                    },

                    items: [ {
                        name: 'code',
                        itemId: 'userNameTextField222',
                        fieldLabel: '组织编号',
                        allowBlank: false
                    }, {
                        name: 'name',
                        fieldLabel: '名称',
                        allowBlank: false
                    }],
                    rbar: [ {
                        xtype: 'button',
                        itemId: 'editFormSaveButton222',
                        text: '查询',
                        action: 'save',
                        glyph: 0xe80d,
                        formBind: true
                    }, {
                        text: '重置',
                        scope: me,
                        handler: me.close,
                        glyph: 0xe80e
                    } ]
                },
                {
                    xtype: 'toolbar',
                    items: [ {
                        text: i18n.create,
                        itemId: 'createButton',
                        glyph: 0xe807
                    }, '-', {
                        text: i18n.excelexport,
                        itemId: 'exportButton',
                        glyph: 0xe813,
                        href: 'usersExport.xlsx',
                        hrefTarget: '_self'
                    }
                    ]
                }

            ]

        }, {
			xtype: 'pagingtoolbar',
			dock: 'bottom',
			store: me.store
		} ];

		me.callParent(arguments);
	}

});