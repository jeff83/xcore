/* <debug> */
Ext.Loader.setConfig({
	enabled: true,
	paths: {
		'xcore': 'app',
		'Ext.ux': 'resources/extjs-gpl/4.2.2/ux'
	}
});
/* </debug> */

Ext.define('xcore.App', {
	extend: 'Deft.mvc.Application',
	requires: [ 'overrides.AbstractMixedCollection', 'xcore.ux.window.Notification', 'xcore.view.Viewport', 'xcore.store.Roles' ],

	init: function() {
		Ext.fly('circularG').destroy();
		Ext.setGlyphFontFamily('custom');
		Ext.tip.QuickTipManager.init();

		var chartdatapoller = new Ext.direct.PollingProvider({
			id: 'chartdatapoller',
			type: 'polling',
			interval: 2000,
			url: POLLING_URLS.chartdata
		});
		var heartbeat = new Ext.direct.PollingProvider({
			type: 'polling',
			interval: 5 * 60 * 1000, // 5 minutes
			url: POLLING_URLS.heartbeat
		});
		Ext.direct.Manager.addProvider(REMOTING_API, chartdatapoller, heartbeat);
		Ext.direct.Manager.getProvider('chartdatapoller').disconnect();

		if (Ext.view.AbstractView) {
			Ext.view.AbstractView.prototype.loadingText = i18n.loading;
		}

		this.setupGlobalErrorHandler();

		Ext.direct.Manager.on('event', function(e) {
			if (e.code && e.code === 'parse') {
				window.location.reload();
			}
		});

		Ext.direct.Manager.on('exception', function(e) {
			if (e.message === 'accessdenied') {
				xcore.ux.window.Notification.error(i18n.error, i18n.error_accessdenied);
			} else {
				xcore.ux.window.Notification.error(i18n.error, e.message);
			}
		});

		Deft.Injector.configure({
			messageBus: 'Ext.util.Observable',
			rolesStore: {
				className: 'xcore.store.Roles',
				eager: true
			}
		});

		Ext.create('xcore.view.Viewport');
	},

	setupGlobalErrorHandler: function() {
		var existingFn = window.onerror;
		if (typeof existingFn === 'function') {
			window.onerror = Ext.Function.createSequence(existingFn, this.globalErrorHandler);
		} else {
			window.onerror = this.globalErrorHandler;
		}
	},

	globalErrorHandler: function(msg, url, line) {
		var message = msg + "-->" + url + "::" + line;
		logService.error(message);
	}
});

Ext.onReady(function() {
	Ext.create('xcore.App');
});