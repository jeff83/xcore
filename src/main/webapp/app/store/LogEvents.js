Ext.define('xcore.store.LogEvents', {
	extend: 'Ext.data.Store',
	model: 'xcore.model.LogEvent',
	autoLoad: false,
	remoteSort: true,
	remoteFilter: true,
	pageSize: 30,
	sorters: [ {
		property: 'eventDate',
		direction: 'DESC'
	} ]
});