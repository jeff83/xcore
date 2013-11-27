Ext.define('xcore.store.AccessLogs', {
	extend: 'Ext.data.Store',
	model: 'xcore.model.AccessLog',
	autoLoad: false,
	remoteSort: true,
	remoteFilter: true,
	pageSize: 30,
	sorters: [ {
		property: 'logIn',
		direction: 'DESC'
	} ]
});