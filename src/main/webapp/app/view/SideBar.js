Ext.define('xcore.view.SideBar', {
	extend: 'Ext.panel.Panel',
	requires: [ 'xcore.view.poll.PollChart', 'xcore.view.user.List', 'xcore.view.accesslog.List', 'xcore.view.logevent.List', 'xcore.view.config.Edit' ],
	title: i18n.navigation,
	collapsible: true,
	layout: 'fit',
	minWidth: 100,
	maxWidth: 200,

	initComponent: function() {
		var me = this;
		me.items = [ {
			xtype: 'treepanel',
			itemId: 'menuTree',
			border: 0,
			store: Ext.create('xcore.store.Navigation'),
			rootVisible: false,
			animate: false
		} ];

		me.callParent(arguments);
	}
});