package com.cmbc.entity;

import ch.ralscha.extdirectspring.generator.Model;
import ch.rasc.edsutil.entity.AbstractPersistable;
import com.cmbc.codegenerator.annotation.EntityGen;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@EntityGen(label = "部门")
@Entity
@Table(name = "Department")
@Model(value = "xcore.model.Department", readMethod = "departmentService.read", createMethod = "departmentService.create", updateMethod = "departmentService.update", destroyMethod = "departmentService.destroy", paging = true)
public class Department extends AbstractPersistable {
    @NotEmpty(message = "{user_missing_username}")
    @Size(max = 100)
    @Column(unique = true)
    private String code;

    @NotEmpty(message = "{user_missing_username}")
    @Size(max = 100)
    @Column()
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
