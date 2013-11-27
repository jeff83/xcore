Ext.define('xcore.view.department.Edit', {
	extend: 'Ext.window.Window',
	title: i18n.user,
	layout: 'fit',
	autoShow: true,
	resizable: true,
	constrain: true,
	width: 500,
	modal: true,
	glyph: 0xe803,

	requires: [ 'Ext.ux.form.MultiSelect' ],

	initComponent: function() {
		var me = this;

		me.items = [ {
			xtype: 'form',
			padding: 5,
			bodyPadding: 10,

			defaultType: 'textfield',
			defaults: {
				anchor: '100%'
			},

			fieldDefaults: {
				msgTarget: 'side'
			},

			items: [ {
				name: 'code',
				itemId: 'userNameTextField',
				fieldLabel: '组织编号',
				allowBlank: false
			}, {
				name: 'name',
				fieldLabel: '名称',
				allowBlank: false
			}],

			buttons: [ {
				xtype: 'button',
				itemId: 'editFormSaveButton',
				text: i18n.save,
				action: 'save',
				glyph: 0xe80d,
				formBind: true
			}, {
				text: i18n.cancel,
				scope: me,
				handler: me.close,
				glyph: 0xe80e
			} ]
		} ];

		me.callParent(arguments);
	}
});