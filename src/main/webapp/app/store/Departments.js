Ext.define('xcore.store.Departments', {
	extend: 'Ext.data.Store',
	model: 'xcore.model.Department',
	autoLoad: false,
	remoteSort: true,
	remoteFilter: true,
	pageSize: 30,
	autoSync: false,
	sorters: [ {
		property: 'code',
		direction: 'ASC'
	} ]
});