Ext.define('xcore.view.Viewport', {
	extend: 'Ext.Viewport',
	controller: 'xcore.controller.Viewport',
	requires: [ 'Ext.ux.TabReorderer', 'Ext.ux.TabCloseMenu', 'xcore.view.Header', 'xcore.view.SideBar', 'xcore.store.Navigation' ],

	style: {
		backgroundColor: '#F8F8F8'
	},

	layout: {
		type: 'border',
		padding: 5
	},

	defaults: {
		split: true
	},

	initComponent: function() {
		var me = this;

		var tabCloseMenu = Ext.create('Ext.ux.TabCloseMenu');
		tabCloseMenu.closeTabText = i18n.tabclosemenu_close;
		tabCloseMenu.closeOthersTabsText = i18n.tabclosemenu_closeother;
		tabCloseMenu.closeAllTabsText = i18n.tabclosemenu_closeall;

		me.items = [ Ext.create('xcore.view.Header', {
			region: 'north',
			split: false
		}), {
			region: 'center',
			xtype: 'tabpanel',
			itemId: 'tabPanel',
			plugins: [ Ext.create('Ext.ux.TabReorderer'), tabCloseMenu ],
			plain: true
		}, Ext.create('xcore.view.SideBar', {
			region: 'west',
			width: 180
		}) ];

		me.callParent(arguments);
	}

});