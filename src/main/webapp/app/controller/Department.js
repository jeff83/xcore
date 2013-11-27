Ext.define('xcore.controller.Department', {
	extend: 'xcore.controller.CrudBase',
	requires: [ 'xcore.view.department.Edit' ],

	control: {
		exportButton: true
	},

	destroyConfirmMsg: function(record) {
		return record.get('name') + ' ' + i18n.reallyDestroy;
	},

	destroyFailureCallback: function() {
		xcore.ux.window.Notification.error(i18n.error, i18n.user_lastAdminUserError);
	},

	editWindowClass: 'xcore.view.department.Edit',

	createModel: function() {
		return Ext.create('xcore.model.Department');
	},

	buildContextMenuItems: function(record) {
		var me = this;
		var items = this.callParent(arguments);

		items.push({
			xtype: 'menuseparator'
		});
		items.push({
			text: i18n.user_switchto,
			handler: Ext.bind(me.switchTo, me, [ record ])
		});

		return items;
	},

	onFilterField: function(field, newValue) {
		this.callParent(arguments);

		if (newValue) {
			this.getExportButton().setParams({
				filter: newValue
			});
		} else {
			this.getExportButton().setParams();
		}
	},

	switchTo: function(record) {
		if (record) {
			securityService.switchUser(record.data.id, function(ok) {
				if (ok) {
					History.pushState({}, i18n.app_title, "?");
					window.location.reload();
				}
			}, this);
		}
	}

});