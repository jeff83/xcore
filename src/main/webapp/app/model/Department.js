Ext.define("xcore.model.Department",
{
  extend : "Ext.data.Model",
  fields : [ {
    name : "code",
    type : "string"
  }, {
    name : "name",
    type : "string"
  }, {
    name : "id",
    type : "int",
    useNull : true
  } ],
  proxy : {
    type : "direct",
    api : {
      read : "departmentService.read",
      create : "departmentService.create",
      update : "departmentService.update",
      destroy : "departmentService.destroy"
    },
    reader : {
      root : "records"
    }
  }
});