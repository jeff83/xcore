Ext.define('xcore.view.user.List', {
	extend: 'Ext.grid.Panel',
	requires: [ 'xcore.controller.User', 'xcore.ux.form.field.FilterField', 'xcore.store.Users' ],
	controller: 'xcore.controller.User',
	
	title: i18n.user_users,
	closable: true,
	border: true,

	initComponent: function() {

		var me = this;

		me.store = Ext.create('xcore.store.Users');

		me.columns = [
        {
			xtype: 'actioncolumn',
			width: 30,
			items: [ {
				icon: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAQAAAC1+jfqAAAAK0lEQVR4AWMgBBhXyRFQsPI/xQoyCCgg7EgX2jkSYQWZAOFN2jtSjsKQBAD0NQ+N4ZAsdgAAAABJRU5ErkJggg=='
			} ]
		}, {
			text: i18n.user_username,
			dataIndex: 'userName',
			flex: 1
		}, {
			text: i18n.user_firstname,
			dataIndex: 'firstName',
			flex: 1
		}, {
			text: i18n.user_lastname,
			dataIndex: 'name',
			flex: 1
		}, {
			text: i18n.user_email,
			dataIndex: 'email',
			flex: 1
		}, {
			text: 'Role',
			dataIndex: 'role',
            flex: 1
		}, {
			text: i18n.user_enabled,
			dataIndex: 'enabled',
			width: 85,
			renderer: function(value) {
				if (value === true) {
					return i18n.yes;
				}
				return i18n.no;
			}
		} ];

		me.dockedItems = [
            { // //表格的第一部分，包括查询条件和工具栏
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
                        name: 'userName',
                        itemId: 'userNameTextField222',
                        fieldLabel: i18n.user_username
                    }, {
                        name: 'firstName',
                        fieldLabel: i18n.user_firstname
                    }, {
                        name: 'name',
                        fieldLabel: i18n.user_lastname
                    }, {
                        name: 'email',
                        fieldLabel: i18n.user_email,
                        vtype: 'email'
                    }, {
                        xtype: 'combobox',
                        fieldLabel: i18n.user_language,
                        name: 'locale',
                        store: Ext.create('Ext.data.ArrayStore', {
                            fields: [ 'code', 'language' ],
                            data: [ [ 'de', i18n.user_language_german ], [ 'en', i18n.user_language_english ] ]
                        }),
                        valueField: 'code',
                        displayField: 'language',
                        queryMode: 'local',
                        emptyText: i18n.user_selectlanguage,
                        forceSelection: true
                    }, {
                        fieldLabel: i18n.user_enabled,
                        name: 'enabled',
                        xtype: 'checkboxfield',
                        inputValue: 'true',
                        uncheckedValue: 'false'
                    }],
                    rbar: [ {
                        xtype: 'button',
                        itemId: 'searchButton',
                        text: "查询",
                        action: 'search',
                        glyph: 0xe80d,
                        formBind: true
                    }, {
                        text: "重置",
                        handler: function() {
                            this.up('form').getForm().reset();
                        },
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
                    }, '->', {
                        itemId: 'filterField',
                        fieldLabel: i18n.filter,
                        labelWidth: 40,
                        xtype: 'filterfield'
                    }, "",{
                        text: i18n.create,
                        itemId: 'createButton2',
                        glyph: 0xe807
                    } ,

                        '-', // same as {xtype: 'tbseparator'} to create Ext.toolbar.Separator
                        'text 1', // same as {xtype: 'tbtext', text: 'text1'} to create Ext.toolbar.TextItem
                        { xtype: 'tbspacer' },// same as ' ' to create Ext.toolbar.Spacer
                        'text 2',
                        { xtype: 'tbspacer', width: 50 }, // add a 50px space
                        'text 3'
                    ]
                }

            ]

        },
        {  //表格的第二部分，分页工具条
			xtype: 'pagingtoolbar',
			dock: 'bottom',
			store: me.store
		} ];

		me.callParent(arguments);
	}

});